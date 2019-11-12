package com.cg.ibs.accountmanagement.dao;

import java.math.BigInteger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.cg.ibs.accountmanagement.bean.Customer;
import com.cg.ibs.accountmanagement.util.DBUtil;

@Repository("customerDao")
public class CustomerDaoImpl implements CustomerDao {
	
	private static Logger logger = Logger.getLogger(CustomerDaoImpl.class);
	
	private EntityManager entityManager;
	
	public CustomerDaoImpl() {
		entityManager= DBUtil.getEntityManger();
	}

	public Customer getCustomerByUCI(BigInteger UCI) {
		TypedQuery<Customer> query = entityManager.createNamedQuery("CB_BY_UCI",Customer.class);
		query.setParameter("UCI", UCI);
		Customer customerBean = query.getSingleResult();
		logger.info(" Customer details returned using UCI");
		return customerBean;
	}

	public Customer getCustomerByUserId(String userId) {
		Customer customerBean=null;
		TypedQuery<Customer> query = entityManager.createNamedQuery("CB_BY_UID", Customer.class);
		query.setParameter("userId", userId);
		try {
		customerBean = query.getSingleResult();}
		catch(NoResultException e){	
		}
		logger.info(" Customer details returned using userId");
		return customerBean;
	}


}
