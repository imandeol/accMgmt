package com.cg.ibs.accountmanagement.dao;

import java.math.BigInteger;

import com.cg.ibs.accountmanagement.bean.Customer;

public interface CustomerDao {
	public Customer getCustomerByUCI(BigInteger UCI);
	public Customer getCustomerByUserId(String userId);
}
