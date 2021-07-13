package com.charterProject.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.charterProject.model.Customer;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class CustomerDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private String query = "select * from customer where id=?";
	
	private String queryForAllCustomers = "select * from customer";
	
	public Customer findCustomerById(Integer id) {
		try {
			return jdbcTemplate.queryForObject(query, new Object[] {id}, (rs, rowNum)  ->{
				return new Customer(rs.getInt("id"), rs.getString("name"));
			});
		} catch (EmptyResultDataAccessException e) {
			log.error("No data found in Customer table for customerId: {}", id);
			return null;
		}
	}
	
	public List<Customer> findAllCustomers() {
		try {
			return jdbcTemplate.query(queryForAllCustomers,  (rs, rowNum)  ->{
				return new Customer(rs.getInt("id"), rs.getString("name"));
			});
		} catch (EmptyResultDataAccessException e) {
			log.error("No data found in Customer table");
			return null;
		}
			
	}

}
