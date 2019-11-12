package com.cg.ibs.accountmanagement.service;

import com.cg.ibs.accountmanagement.bean.Customer;
import com.cg.ibs.accountmanagement.exception.IBSException;

public interface CustomerService {
	public boolean validateCustomer(String userId, String password) throws IBSException;
	public Customer customerByUserId(String userId) ;

}
