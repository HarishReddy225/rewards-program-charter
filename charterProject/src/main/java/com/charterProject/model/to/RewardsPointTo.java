package com.charterProject.model.to;

import java.util.ArrayList;
import java.util.List;

import com.charterProject.model.Customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RewardsPointTo {

	private String customerName;
	private List<Long> monthWiseRewardPoints = new ArrayList<Long>();
	private Long totalRewardsPoint;
}
