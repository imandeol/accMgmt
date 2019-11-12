package com.cg.ibs.accountmanagement.dao;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.cg.ibs.accountmanagement.bean.ServiceProvider;
import com.cg.ibs.accountmanagement.bean.TransactionBean;
import com.cg.ibs.accountmanagement.util.DBUtil;

@Repository("transactionDao")
public class TransactionDaoImpl implements TransactionDao {

	private static Logger logger = Logger.getLogger(TransactionDaoImpl.class);

	private EntityManager entityManager;
	
	public TransactionDaoImpl() {
		entityManager= DBUtil.getEntityManger();
	}
	

	@Override
	public void addNewTransaction(TransactionBean tranBean) {
		entityManager.persist(tranBean);
	}
	

	@Override
	public List<TransactionBean> getPeriodicTransactions(LocalDateTime startDate, LocalDateTime endDate,
			BigInteger accNo) {
//		Account accountBean = entityManager.find(Account.class, accNo);
		List<TransactionBean> tranList = null;
		TypedQuery<TransactionBean> query = entityManager.createNamedQuery("GET_PERIODIC", TransactionBean.class);
		query.setParameter("accNo", accNo);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		tranList = query.getResultList();
		logger.info(" Periodic statement returned");
		return tranList;
	}

	@Override
	public List<TransactionBean> getMiniTransactions(BigInteger accNo) {
//		Account accountBean = entityManager.find(Account.class, accNo);
		List<TransactionBean> tranList = null;
		TypedQuery<TransactionBean> query = entityManager.createNamedQuery("GET_MINI", TransactionBean.class);
		query.setParameter("accNo", accNo);
		tranList = query.getResultList();
		logger.info(" Mini statement returned");
		return tranList;
	}

	@Override
	public List<ServiceProvider> getServiceProviders() {
		List<ServiceProvider> spList = null;
		TypedQuery<ServiceProvider> query = entityManager.createNamedQuery("GET_SP_LIST", ServiceProvider.class);
		spList = query.getResultList();
		logger.info(" List of service providers returned");
		return spList;
	}

	@Override
	public void updatetransaction(TransactionBean transBean) {
		// TODO Auto-generated method stub
		entityManager.merge(transBean);
		
	}

}
