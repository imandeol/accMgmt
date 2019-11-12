package com.cg.ibs.accountmanagement.dao;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.hibernate.query.criteria.internal.expression.function.AggregationFunction.MAX;
import org.springframework.stereotype.Repository;

import com.cg.ibs.accountmanagement.bean.Account;
import com.cg.ibs.accountmanagement.exception.IBSException;
import com.cg.ibs.accountmanagement.exception.IBSExceptionInt;
import com.cg.ibs.accountmanagement.util.DBUtil;

@Repository("accountDao")
public class AccountDaoImpl implements AccountDao {
	
	private static Logger logger = Logger.getLogger(AccountDaoImpl.class);
	
	private EntityManager entityManager;
	
	public AccountDaoImpl() {
		entityManager= DBUtil.getEntityManger();
	}
	
	@Override
	public void update(Account accountBean) throws IBSException {
		if (accountBean != null) {
			entityManager.merge(accountBean);
		} else {
			logger.error(IBSExceptionInt.INVALID_ACC_NO);
			throw new IBSException(IBSExceptionInt.INVALID_ACC_NO);
		}
	}

	@Override
	public Account getAccountByAccNo(BigInteger accNumber) throws IBSException {
		Account accountBean = new Account();
		if (accountBean != null) {
			accountBean = entityManager.find(Account.class, accNumber);
		} else {
			logger.error(IBSExceptionInt.INVALID_ACC_NO);
			throw new IBSException(IBSExceptionInt.INVALID_ACC_NO);
		}
		
		return accountBean;
	}

	@Override
	public void addAccount(Account account) {
		entityManager.persist(account);
	}

	@Override
	public List<Account> getAccountsByUCI(BigInteger UCI) {
		TypedQuery<Account> query = entityManager.createNamedQuery("ACC_BY_UCI", Account.class);
		query.setParameter("UCI", UCI);
		List<Account> list = (List<Account>) query.getResultList();
		logger.info(" Accounts list returned");
		return list;
	}
	
	@Override
	public boolean checkAccountExists(BigInteger accNo) {
		boolean result= false;
		Account account= entityManager.find(Account.class, accNo);
		if(account!=null) {
			result= true;
		}
		return result;
	}
	
//	@Override
//	public BigInteger getAccNo() {
//		BigInteger accNumber= null;
//		try {
//		TypedQuery<BigInteger> query= entityManager.createNamedQuery("MAX_ACC_NUMBER", BigInteger.class);
//			accNumber= query.getSingleResult();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return accNumber.add(BigInteger.valueOf(1));
//		
//	}
}
