package com.charterProject.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.charterProject.dao.CustomerDao;
import com.charterProject.dao.TransactionDao;
import com.charterProject.model.Customer;
import com.charterProject.model.Transaction;
import com.charterProject.model.to.RewardsPointTo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RewardsService {

	@Value("${transaction_duration_to_consider}")
	private Integer transactionDuration;
	
	@Autowired
	private TransactionDao transactionDao;
	
	@Autowired
	private CustomerDao customerDao;
	
	@Value("${points_per_100}")
	private Integer pointPer100;
	
	@Value("${points_per_50}")
	private Integer pointPer50;
	
	public RewardsPointTo findTotalRewardsByCustomer(Integer customerId) {
		List<Long> pointsByMonth = new ArrayList<>();
		int noOfMonths = transactionDuration;
		while(noOfMonths != 0) {
			List<Transaction> transactionList = transactionDao.findAllByTransactionByCustomerIdByTransactionDurationInMonth(
					customerId, noOfMonths, ++noOfMonths);
			Long point = new RewardsPointCalculator().calculate(transactionList);
			pointsByMonth.add(point);
		}
		Customer customer = customerDao.findCustomerById(customerId);
		return computeRewardsTo(customer, pointsByMonth);
	}
	
	public List<RewardsPointTo> findAllRewards() {
		List<Customer> customerList = customerDao.findAllCustomers();
		MultiValueMap<Customer, Long> customerPointMapByMonth = new LinkedMultiValueMap<>();
		for(Customer customer: customerList) {
			int noOfMonths = transactionDuration;
			while(noOfMonths != 0) {
				List<Transaction> transactionList = transactionDao.findAllByTransactionByCustomerIdByTransactionDurationInMonth(
						customer.getId(), noOfMonths, ++noOfMonths);
				Long point = new RewardsPointCalculator().calculate(transactionList);
				customerPointMapByMonth.add(customer, point);
			}
		}
		return computeRewardsTo(customerPointMapByMonth);
	}
	
	private List<RewardsPointTo> computeRewardsTo(MultiValueMap<Customer, Long> customerPointMapByMonth) {
		List<RewardsPointTo> result = new ArrayList<RewardsPointTo>();
		for(Customer customer : customerPointMapByMonth.keySet()) {
			List<Long> rewardsByMonth = customerPointMapByMonth.get(customer);
			result.add(computeRewardsTo(customer, rewardsByMonth));
		}
		return result;
	}
	
	private RewardsPointTo computeRewardsTo(Customer customer, List<Long> rewardsByMonth) {
		if(customer == null || rewardsByMonth == null || rewardsByMonth.isEmpty())
			return null;
		return new RewardsPointTo(
					customer.getName(), rewardsByMonth, rewardsByMonth.stream().reduce(0l, Long:: sum));
	}

	private class RewardsPointCalculator {
		
		public Long calculate(List<Transaction> transactions) {
			return transactions.stream().map(trx -> calculatePointForTransaction(trx)).reduce(0l, Long:: sum);
		}

		private Long calculatePointForTransaction(Transaction trx) {
			log.info("Got Transaction Amount = {}", trx.getTransactionAmount());
			Long trxValue = Math.round(trx.getTransactionAmount());
			Long totalTrxInHundreds = (trxValue)/100;
			Long remainingValue = trxValue%100;
			Long totalRemainingTrxInFifty = remainingValue/50;
			log.info("Got totalin100 ={}, totalin50 ={}", totalTrxInHundreds, totalRemainingTrxInFifty );
			return points(totalTrxInHundreds, totalRemainingTrxInFifty);
		}

		private Long points(Long totalTrxInHundreds, Long totalRemainingTrxInFifty) {
			return (totalTrxInHundreds * pointPer100) + (totalRemainingTrxInFifty*pointPer50);
		}
	}
}
