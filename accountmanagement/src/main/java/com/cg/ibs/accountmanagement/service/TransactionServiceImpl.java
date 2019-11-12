package com.cg.ibs.accountmanagement.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.persistence.EntityTransaction;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.ibs.accountmanagement.bean.Account;
import com.cg.ibs.accountmanagement.bean.AccountStatus;
import com.cg.ibs.accountmanagement.bean.ServiceProvider;
import com.cg.ibs.accountmanagement.bean.TransactionBean;
import com.cg.ibs.accountmanagement.bean.TransactionMode;
import com.cg.ibs.accountmanagement.bean.TransactionType;
import com.cg.ibs.accountmanagement.dao.AccountDao;
import com.cg.ibs.accountmanagement.dao.TransactionDao;
import com.cg.ibs.accountmanagement.exception.IBSException;
import com.cg.ibs.accountmanagement.exception.IBSExceptionInt;
import com.cg.ibs.accountmanagement.util.DBUtil;
@Service("transactionService")
public class TransactionServiceImpl implements TransactionService {

	private static Logger logger = Logger.getLogger(TransactionServiceImpl.class);

	EntityTransaction entityTransaction;	
	
	@Autowired
	private TransactionDao transactionDao;
	
	@Autowired
	private AccountDao accountDao;

	@Override
	public List<TransactionBean> getMiniStmt(BigInteger accNo) {
		List<TransactionBean> transList = null;
		transList = transactionDao.getMiniTransactions(accNo);
		logger.info(" Mini statement fetched");
		return transList;
	}

	@Override
	public List<TransactionBean> getPeriodicStmt(LocalDateTime startDate, LocalDateTime endDate, BigInteger accNo)
			throws IBSException {
//		entityTransaction=DBUtil.getTransaction();
		List<TransactionBean> transList = null;
		LocalDate startDate1 = startDate.toLocalDate();
		LocalDate endDate1 = endDate.toLocalDate();

//		Period period= Period.between(startDate1, endDate1);
//		int months = ((int) period.getDays()) / 30;
		long noOfDaysBetween = ChronoUnit.DAYS.between(startDate1, endDate1);
		int months = (int) (noOfDaysBetween / 30);
		//System.out.println(noOfDaysBetween);
		if (startDate.compareTo(endDate) < 0 && months <= 6 && startDate.compareTo(LocalDateTime.now()) < 0) {
			transList = transactionDao.getPeriodicTransactions(startDate, endDate, accNo);
			logger.info(" Periodic statement fetched");
		} else {
			throw new IBSException(IBSExceptionInt.INVALID_PERIOD);
		}
		return transList;
	}

	@Override
	public BigDecimal TransferFunds(BigInteger accNo, BigDecimal amt, String tranPwd, BigInteger recipientNo)
			throws IBSException {
		entityTransaction= DBUtil.getTransaction();
		LocalDateTime todayDate = LocalDateTime.now();
		Account accountBean = accountDao.getAccountByAccNo(accNo);
		BigDecimal currentBal = accountBean.getBalance();
		int refId;
		if (accountBean.getAccStatus().equals(AccountStatus.CLOSED)) {
			logger.error(IBSExceptionInt.ACCOUNT_CLOSED);
			throw new IBSException(IBSExceptionInt.ACCOUNT_CLOSED);
		} else if (currentBal.compareTo(amt) < 0 || amt.compareTo(new BigDecimal(0)) <= 0) {
			logger.error(IBSExceptionInt.BALANCE_ERROR_MESSAGE);
			throw new IBSException(IBSExceptionInt.BALANCE_ERROR_MESSAGE);
		} else {
			if (tranPwd.equals(accountBean.getTrans_Pwd())) {
				accountBean.setBalance(accountBean.getBalance().subtract(amt));
				TransactionBean t1 = new TransactionBean();
				t1.setAccount(accountBean);
				t1.setTransactionAmount(amt);
				t1.setTransactionDate(todayDate);
				t1.setTransactionDescription("Online transfer to " + recipientNo.toString());
				t1.setTransactionMode(TransactionMode.ONLINE);
				t1.setTransactionType(TransactionType.DEBIT);
				//t1.setReferenceId("Transfer to "+recipientNo.toString());
				if(!entityTransaction.isActive()) {
				entityTransaction.begin();
				}
				transactionDao.addNewTransaction(t1);
				refId= fundsDeposit(recipientNo, amt, accNo, t1.getTransactionId());
				t1.setReferenceId(String.valueOf(refId));
				
				accountDao.update(accountBean);
				try {
				entityTransaction.commit();}
				catch(Exception excp)
				{
					logger.error("tranfer failed",excp);
				}
			} else {
				logger.error(IBSExceptionInt.INVALID_TRANS_PASSW);
				throw new IBSException(IBSExceptionInt.INVALID_TRANS_PASSW);
			}
		}
		logger.info(" Funds transferred succesfully");
		return accountBean.getBalance();
	}

	
	public BigDecimal payBill(BigInteger accNo, BigDecimal amt, String tranPwd, BigInteger recipientNo)
			throws IBSException {
		entityTransaction= DBUtil.getTransaction();
		LocalDateTime todayDate = LocalDateTime.now();
		Account accountBean = accountDao.getAccountByAccNo(accNo);
		BigDecimal currentBal = accountBean.getBalance();
//		int refId;
		if (accountBean.getAccStatus().equals(AccountStatus.CLOSED)) {
			logger.error(IBSExceptionInt.ACCOUNT_CLOSED);
			throw new IBSException(IBSExceptionInt.ACCOUNT_CLOSED);
		} else if (currentBal.compareTo(amt) < 0 || amt.compareTo(new BigDecimal(0)) <= 0) {
			logger.error(IBSExceptionInt.BALANCE_ERROR_MESSAGE);
			throw new IBSException(IBSExceptionInt.BALANCE_ERROR_MESSAGE);
		} else {
			if (tranPwd.equals(accountBean.getTrans_Pwd())) {
				accountBean.setBalance(accountBean.getBalance().subtract(amt));
				TransactionBean t1 = new TransactionBean();
				t1.setAccount(accountBean);
				t1.setTransactionAmount(amt);
				t1.setTransactionDate(todayDate);
				t1.setTransactionDescription("Online transfer to " + recipientNo.toString());
				t1.setTransactionMode(TransactionMode.ONLINE);
				t1.setTransactionType(TransactionType.DEBIT);
				t1.setReferenceId("Pay utility bills");
				if(!entityTransaction.isActive()) {
					entityTransaction.begin();
					}
					
				
				transactionDao.addNewTransaction(t1);	
				accountDao.update(accountBean);
				try {
				entityTransaction.commit();}
				catch(Exception excp)
				{
					logger.error("tranfer failed",excp);
				}
			} else {
				logger.error(IBSExceptionInt.INVALID_TRANS_PASSW);
				throw new IBSException(IBSExceptionInt.INVALID_TRANS_PASSW);
			}
		}
		logger.info(" Funds transferred succesfully");
		return accountBean.getBalance();
	}
	
	
	@Override
	public int fundsDeposit(BigInteger accNo, BigDecimal amt, BigInteger transferAccount,int transId) throws IBSException {
		entityTransaction= DBUtil.getTransaction();
		Account accountBean = accountDao.getAccountByAccNo(accNo);
		TransactionBean tranBean = new TransactionBean();
		if (accountBean != null && accountBean.getAccStatus().equals(AccountStatus.ACTIVE)) {
			accountBean.setBalance(accountBean.getBalance().add(amt));
			tranBean.setAccount(accountBean);
			tranBean.setTransactionAmount(amt);
			tranBean.setTransactionDate(LocalDateTime.now());
			tranBean.setTransactionType(TransactionType.CREDIT);
			tranBean.setTransactionMode(TransactionMode.ONLINE);
			tranBean.setTransactionDescription("Online Transfer from" + transferAccount );
			tranBean.setReferenceId(String.valueOf(transId));

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
		return tranBean.getTransactionId();
	}


	@Override
	public BigDecimal payUtilityBill(BigInteger accNo, BigInteger recipientNo, String transacPass, BigDecimal amount)
			throws IBSException {
		BigDecimal balance = payBill(accNo, amount, transacPass, recipientNo);
		logger.info(" Utility bill paid");
		return balance;
	}

	@Override
	public List<ServiceProvider> getServiceProviders() {
		List<ServiceProvider> spList = transactionDao.getServiceProviders();
		logger.info(" Service providers list displayed");
		return spList;
	}
	
	@Override
	public BigDecimal TransferFundsOtherBank(BigInteger accNo, BigDecimal amt, String tranPwd, BigInteger recipientNo, String otherBank)
			throws IBSException {
		entityTransaction= DBUtil.getTransaction();
		LocalDateTime todayDate = LocalDateTime.now();
		Account accountBean = accountDao.getAccountByAccNo(accNo);
		BigDecimal currentBal = accountBean.getBalance();
		if (accountBean.getAccStatus().equals(AccountStatus.CLOSED)) {
			logger.error(IBSExceptionInt.ACCOUNT_CLOSED);
			throw new IBSException(IBSExceptionInt.ACCOUNT_CLOSED);
		} else if (currentBal.compareTo(amt) < 0 || amt.compareTo(new BigDecimal(0)) <= 0) {
			logger.error(IBSExceptionInt.BALANCE_ERROR_MESSAGE);
			throw new IBSException(IBSExceptionInt.BALANCE_ERROR_MESSAGE);
		} else {
			if (tranPwd.equals(accountBean.getTrans_Pwd())) {
				accountBean.setBalance(accountBean.getBalance().subtract(amt));
				TransactionBean t1 = new TransactionBean();
				t1.setAccount(accountBean);
				t1.setTransactionAmount(amt);
				t1.setTransactionDate(todayDate);
				t1.setTransactionDescription("Online transfer to " + recipientNo.toString());
				t1.setTransactionMode(TransactionMode.ONLINE);
				t1.setTransactionType(TransactionType.DEBIT);
				t1.setReferenceId("#"+otherBank+"/"+recipientNo.toString()+"/"+todayDate.toString());
				if(!entityTransaction.isActive()) {
				entityTransaction.begin();
				}
				transactionDao.addNewTransaction(t1);
				accountDao.update(accountBean);
				try {
				entityTransaction.commit();}
				catch(Exception excp)
				{
					logger.error("tranfer failed",excp);
				}
				
			} else {
				logger.error(IBSExceptionInt.INVALID_TRANS_PASSW);
				throw new IBSException(IBSExceptionInt.INVALID_TRANS_PASSW);
			}
		}
		logger.info(" Funds transferred succesfully");
		return accountBean.getBalance();
		}

}
