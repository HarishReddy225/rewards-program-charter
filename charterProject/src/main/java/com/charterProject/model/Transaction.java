package com.charterProject.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Transaction {

	private Long id;
	private Integer customerId;
	private Date transactionDate;
	private Double transactionAmount;
}
