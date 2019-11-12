package com.cg.ibs.accountmanagement.service;

import javax.persistence.EntityTransaction;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.ibs.accountmanagement.bean.Customer;
import com.cg.ibs.accountmanagement.dao.CustomerDao;
import com.cg.ibs.accountmanagement.exception.IBSException;
import com.cg.ibs.accountmanagement.exception.IBSExceptionInt;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService {
	
	private static Logger logger = Logger.getLogger(CustomerServiceImpl.class);
	
	@Autowired
	private CustomerDao customerDao;
	
	EntityTransaction entityTransaction;
//	public CustomerServiceImpl() {
//		entityTransaction = DBUtil.getTransaction();
//	}


	@Override
	public boolean validateCustomer(String userId, String password) throws IBSException {
		boolean flag = false;
		Customer customerBean = customerDao.getCustomerByUserId(userId);
		if (customerBean != null) {
			if (customerBean.getPassword().equals(password)) {
				flag = true;
			} else {
				logger.error(IBSExceptionInt.WRONG_UID_AND_PASSWORD);
				throw new IBSException(IBSExceptionInt.WRONG_UID_AND_PASSWORD);
			}
		} else {
			logger.error(IBSExceptionInt.WRONG_UID_AND_PASSWORD);
			throw new IBSException(IBSExceptionInt.WRONG_UID_AND_PASSWORD);
		}
		logger.info("Validated customer returned");
		return flag;
	}

	@Override
	public Customer customerByUserId(String userId)  {
		return customerDao.getCustomerByUserId(userId);
	}
}
