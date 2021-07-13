package com.charterProject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.charterProject.model.to.RewardsPointTo;
import com.charterProject.service.RewardsService;

@RestController
public class RewardsReportController {
	
	@Autowired
	private RewardsService rewardsService;
	
	@GetMapping("/getAllRewards")
	public ResponseEntity getAllrewardsInLast3Months() {
		List<RewardsPointTo> result =  rewardsService.findAllRewards();
		if(result == null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No Records Found");
		} else {
			return ResponseEntity.ok(result);
		}
	}
	
	@GetMapping("/getRewardsByCustomerId/{customerId}")
	public ResponseEntity getRewardsForCustomerInLast3Months(@PathVariable("customerId") Integer customerId) {
		RewardsPointTo result = rewardsService.findTotalRewardsByCustomer(customerId);
		if(result == null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No Records Found");
		} else {
			return ResponseEntity.ok(result);
		}
	}

}
