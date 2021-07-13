package com.charterProject.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.charterProject.model.Customer;
import com.charterProject.model.Transaction;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class TransactionDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Value("${query_for_transactions}")
	private String queryForTransactions;
	
	public List<Transaction> findAllByTransactionByCustomerIdByTransactionDurationInMonth(Integer customerId, int month1, int month2) {
		List<Transaction> transactions = null;
		try {
			transactions =jdbcTemplate.query(queryForTransactions, new Object[] {customerId, month1, month2}, 
				(rs, rowNum)  ->{
					return new Transaction(rs.getLong("id"), rs.getInt("customer_id"), rs.getDate("transaction_date"),
							rs.getDouble("transaction_amount"));
				});
		} catch (EmptyResultDataAccessException e) {
			log.error("No data found in Transactions table");
			return null;
		}
		return transactions;
	}

}
