package com.cg.ibs.accountmanagement.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

import javax.persistence.EntityTransaction;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.ibs.accountmanagement.bean.Account;
import com.cg.ibs.accountmanagement.bean.AccountStatus;
import com.cg.ibs.accountmanagement.bean.BankAdmin;
import com.cg.ibs.accountmanagement.bean.TransactionBean;
import com.cg.ibs.accountmanagement.bean.TransactionMode;
import com.cg.ibs.accountmanagement.bean.TransactionType;
import com.cg.ibs.accountmanagement.dao.AccountDao;
import com.cg.ibs.accountmanagement.dao.BankDao;
import com.cg.ibs.accountmanagement.dao.TransactionDao;
import com.cg.ibs.accountmanagement.exception.IBSException;
import com.cg.ibs.accountmanagement.exception.IBSExceptionInt;
import com.cg.ibs.accountmanagement.util.DBUtil;

@Service("bankService")
public class BankServiceImpl implements BankService {
	
	private static Logger logger = Logger.getLogger(BankServiceImpl.class);
	
	private EntityTransaction entityTransaction;
//	
//	public BankServiceImpl() {
//		entityTransaction = DBUtil.getTransaction();
//	}

	
	@Autowired
	private BankDao bankDao ;
	@Autowired
	private AccountDao accountDao;
	@Autowired
	TransactionDao transactionDao;

	@Override
	public boolean validateBanker(String bankId, String password) throws IBSException{
		boolean flag = false;
		BankAdmin bankBean = bankDao.getByAdminId(bankId);
		if (bankBean != null) {
			if (bankBean.getPassword().equals(password)) {
				flag = true;
			} else {
				logger.error(IBSExceptionInt.WRONG_BANKERID_AND_PASSWORD);
				throw new IBSException(IBSExceptionInt.WRONG_BANKERID_AND_PASSWORD);
			}
		} else {
			logger.error(IBSExceptionInt.WRONG_BANKERID_AND_PASSWORD);
			throw new IBSException(IBSExceptionInt.WRONG_BANKERID_AND_PASSWORD);
		}
		logger.info(" Validation returned");
		return flag;
	}

	@Override
	public void fundsDeposit(BigInteger accNo, BigDecimal amt) throws IBSException {
		entityTransaction= DBUtil.getTransaction();
		Account accountBean = accountDao.getAccountByAccNo(accNo);
		
		TransactionBean tranBean = new TransactionBean();
	
		if (accountBean != null && accountBean.getAccStatus().equals(AccountStatus.ACTIVE)) {
			System.out.println(accountBean.getAccNo());
			accountBean.setBalance(accountBean.getBalance().add(amt));
			tranBean.setAccount(accountBean);
			tranBean.setTransactionAmount(amt);
			tranBean.setTransactionDate(LocalDateTime.now());
			tranBean.setTransactionType(TransactionType.CREDIT);
			tranBean.setTransactionMode(TransactionMode.CASH);
			tranBean.setTransactionDescription("cash deposit");
			tranBean.setReferenceId("cash deposit");
			if(!entityTransaction.isActive()) {
				entityTransaction.begin();
				}
				
			accountDao.update(accountBean);
			transactionDao.addNewTransaction(tranBean);
			entityTransaction.commit();
			
		}
		else {
			logger.error(IBSExceptionInt.INVALID_ACC_NO);
			throw new IBSException(IBSExceptionInt.INVALID_ACC_NO);
		}
	}

	@Override
	public List<TransactionBean> periodicTransactions(LocalDateTime startDate, LocalDateTime endDate,
			BigInteger accNo) throws IBSException {
		List<TransactionBean> transList = null;
		if(accountDao.checkAccountExists(accNo)) {
		if (endDate.compareTo(startDate) >= 0
				&& Period.between(startDate.toLocalDate(), endDate.toLocalDate()).toTotalMonths() <= 6 && startDate.compareTo(LocalDateTime.now())<0) {
			transList = transactionDao.getPeriodicTransactions(startDate, endDate, accNo);
		} else {
			logger.error(IBSExceptionInt.INVALID_PERIOD);
			throw new IBSException(IBSExceptionInt.INVALID_PERIOD);
		}
		logger.info(" Periodic statement Returned");
		
	}else {
		throw new IBSException(IBSExceptionInt.INVALID_ACC_NO);
	}
		return transList;	
	}
}
