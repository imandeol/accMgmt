package com.cg.ibs.accountmanagement.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityTransaction;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.ibs.accountmanagement.bean.Account;
import com.cg.ibs.accountmanagement.bean.AccountHolding;
import com.cg.ibs.accountmanagement.bean.AccountHoldingType;
import com.cg.ibs.accountmanagement.bean.AccountStatus;
import com.cg.ibs.accountmanagement.bean.AccountType;
import com.cg.ibs.accountmanagement.bean.Customer;
import com.cg.ibs.accountmanagement.bean.TransactionBean;
import com.cg.ibs.accountmanagement.bean.TransactionMode;
import com.cg.ibs.accountmanagement.bean.TransactionType;
import com.cg.ibs.accountmanagement.dao.AccountDao;
import com.cg.ibs.accountmanagement.dao.TransactionDao;
import com.cg.ibs.accountmanagement.exception.IBSException;
import com.cg.ibs.accountmanagement.exception.IBSExceptionInt;
import com.cg.ibs.accountmanagement.util.DBUtil;

@Service("accountService")
public class AccountServiceImpl implements AccountService {
	
	private static Logger logger = Logger.getLogger(AccountServiceImpl.class);

	EntityTransaction entityTransaction;
	
//	public AccountServiceImpl() {
//		entityTransaction = DBUtil.getTransaction();
//	}


	@Autowired
	private AccountDao accountDao;
	
	@Autowired
	private TransactionDao transactionDao;


	@Override
	public BigDecimal getBalance(BigInteger accNo) throws IBSException {
		Account accountBean = accountDao.getAccountByAccNo(accNo);
		logger.info(" Balance displayed");
		return accountBean.getBalance();
	}

	@Override
	public Account addAccount(Customer customerBean,BigInteger accNo, AccountType accType, double amt, double tenure) throws IBSException {
		entityTransaction= DBUtil.getTransaction();
		double maturity_amt = 0;
//		BigInteger accountNumber;
		Account accountBean=null;
		Account accountBean2 = accountDao.getAccountByAccNo(accNo);
		if(amt>0) {
		AccountHolding accountHoldingBean = new AccountHolding();
		
		accountBean = new Account();
//		accountNumber= accountDao.getAccNo();
//		accountBean.setAccNo(accountNumber);
		accountBean.setAccCreationDate(LocalDate.now());
		accountBean.setAccStatus(AccountStatus.ACTIVE);
		accountBean.setAccType(accType);
		accountBean.setOpenBalance(BigDecimal.valueOf(amt));
		accountBean.setBalance(BigDecimal.valueOf(amt));
		accountBean.setTenure(tenure);
		accountBean.setTrans_Pwd("1111");
		
		if (accType.equals(AccountType.FIXED_DEPOSIT)) {
			maturity_amt = createFD(tenure, amt);
			if(accountBean2.getBalance().compareTo(BigDecimal.valueOf(amt))<0) {
				logger.error(IBSExceptionInt.BALANCE_ERROR_MESSAGE);
				throw new IBSException(IBSExceptionInt.BALANCE_ERROR_MESSAGE);
			}
		} else if (accType.equals(AccountType.RECURRING_DEPOSIT)) {
			maturity_amt = createRD(tenure, amt);
			if(accountBean2.getBalance().compareTo(BigDecimal.valueOf(amt))<0) {
				logger.error(IBSExceptionInt.BALANCE_ERROR_MESSAGE);
				throw new IBSException(IBSExceptionInt.BALANCE_ERROR_MESSAGE);
			}
		} else {
			logger.error(IBSExceptionInt.INVALID_ACC_TYPE);
			throw new IBSException(IBSExceptionInt.INVALID_ACC_TYPE);
		}
		
		accountBean.setMaturityAmt(new BigDecimal(maturity_amt));
		accountBean.setTransaction(new HashSet<TransactionBean>());
		accountBean.setAccountHoldings(new HashSet<AccountHolding>());
		
		accountHoldingBean.setAccount(accountBean);
		accountHoldingBean.setCustomer(customerBean);
		accountHoldingBean.setType(AccountHoldingType.INDIVIDUAL);
		accountBean.getAccountHoldings().add(accountHoldingBean);
		customerBean.getAccountHoldings().add(accountHoldingBean);

		if(!entityTransaction.isActive()) {
			entityTransaction.begin();
			}
			
		accountDao.addAccount(accountBean);

		TransactionBean tranBean1 = new TransactionBean();
		TransactionBean tranBean2 = new TransactionBean();
		
		tranBean1.setAccount(accountBean);
		tranBean1.setTransactionAmount(BigDecimal.valueOf(amt));
		tranBean1.setTransactionDate(LocalDateTime.now());
		tranBean1.setTransactionDescription("account creation of" + accType.toString());
		tranBean1.setTransactionMode(TransactionMode.ONLINE);
		tranBean1.setTransactionType(TransactionType.CREDIT);
		//tranBean1.setReferenceId(tranBean2.getReferenceId());
		
		tranBean2.setAccount(accountBean2);
		tranBean2.setTransactionAmount(BigDecimal.valueOf(amt));
		tranBean2.setTransactionDate(LocalDateTime.now());
		tranBean2.setTransactionDescription("account creation of" + accType.toString());
		tranBean2.setTransactionMode(TransactionMode.ONLINE);
		tranBean2.setTransactionType(TransactionType.DEBIT);
		//tranBean2.setReferenceId(tranBean1.getReferenceId());

		transactionDao.addNewTransaction(tranBean1);
		transactionDao.addNewTransaction(tranBean2);
		tranBean1.setReferenceId(String.valueOf(tranBean2.getTransactionId()));
		tranBean2.setReferenceId(String.valueOf(tranBean1.getTransactionId()));
		transactionDao.updatetransaction(tranBean2);
		transactionDao.updatetransaction(tranBean1);
		accountBean2.setBalance(accountBean2.getBalance().subtract(new BigDecimal(amt)));
		accountDao.update(accountBean2);
		
		accountBean.getTransaction().add(tranBean1);
		accountBean2.getTransaction().add(tranBean2);
		
		entityTransaction.commit();
		logger.info(" Account added");
		}
		else
		{
			throw new IBSException(IBSExceptionInt.BALANCE_ERROR_MESSAGE);
		}
		return accountBean;
	}

	@Override
	public Account closeAccount(BigInteger accNo,BigInteger creditAccNo) throws IBSException {
		entityTransaction= DBUtil.getTransaction();
		double maturity_amt;
		Account accountBean = accountDao.getAccountByAccNo(accNo);
		Account creditAccount = accountDao.getAccountByAccNo(creditAccNo);
		accountBean.setAccStatus(AccountStatus.CLOSED);
		if(accountBean.getAccType().equals(AccountType.FIXED_DEPOSIT))
		{
			maturity_amt= deleteFD(accountBean);
		}
		else if(accountBean.getAccType().equals(AccountType.RECURRING_DEPOSIT))
		{
			maturity_amt= deleteRD(accountBean);
		}
		else
		{
			logger.error(IBSExceptionInt.INVALID_ACC_TYPE);
			throw new IBSException(IBSExceptionInt.INVALID_ACC_TYPE);	
		}
		creditAccount.setBalance(creditAccount.getBalance().add(new BigDecimal(maturity_amt)));
		TransactionBean tranBean1 = new TransactionBean();
		TransactionBean tranBean2 = new TransactionBean();
		
		tranBean1.setAccount(accountBean);
		tranBean1.setTransactionAmount(new BigDecimal(maturity_amt));
		tranBean1.setTransactionDate(LocalDateTime.now());
		tranBean1.setTransactionDescription("account Closing of" + accountBean.getAccType().toString());
		tranBean1.setTransactionMode(TransactionMode.ONLINE);
		tranBean1.setTransactionType(TransactionType.DEBIT);
		//tranBean1.setReferenceId(tranBean2.getReferenceId());
		
		tranBean2.setAccount(creditAccount);
		tranBean2.setTransactionAmount(new BigDecimal(maturity_amt));
		tranBean2.setTransactionDate(LocalDateTime.now());
		tranBean2.setTransactionDescription("account Closing of " +accountBean.getAccType().toString());
		tranBean2.setTransactionMode(TransactionMode.ONLINE);
		tranBean2.setTransactionType(TransactionType.CREDIT);
		//tranBean2.setReferenceId(tranBean1.getReferenceId());
		
		if(!entityTransaction.isActive()) {
			entityTransaction.begin();
			}
			
		
		transactionDao.addNewTransaction(tranBean1);
		transactionDao.addNewTransaction(tranBean2);
		tranBean1.setReferenceId(String.valueOf(tranBean2.getTransactionId()));
		tranBean2.setReferenceId(String.valueOf(tranBean1.getTransactionId()));
		transactionDao.updatetransaction(tranBean2);
		transactionDao.updatetransaction(tranBean1);
		accountBean.setBalance(BigDecimal.valueOf(0.0));
		accountDao.update(accountBean);
		accountDao.update(creditAccount);
		accountBean.getTransaction().add(tranBean1);
		creditAccount.getTransaction().add(tranBean2);
		entityTransaction.commit();
		logger.info(" Account closed and credited account details returned");
		return creditAccount;
	}


	@Override
	public Account viewAccount(BigInteger accNo) throws IBSException {
		logger.info(" Account details displayed");
		return accountDao.getAccountByAccNo(accNo);
	}

	public double createFD(double fdTenure, double fdInvestAmt) {
		LocalDateTime creationDate = LocalDateTime.now();
		double maturityAmt = 0;
		double rate = 7;
		double ratePercent = (1 + (rate / 400));
		ratePercent = Math.pow(ratePercent, (4 * fdTenure));
		maturityAmt = fdInvestAmt * (ratePercent);
		logger.info(" Fd created");
		return maturityAmt;
	}


	public double createRD(double rdTenure, double rdInvestAmt) {
		LocalDateTime creationDate = LocalDateTime.now();
		rdTenure = rdTenure * 12;
		double rate = 7;
		double maturityAmt = 0;
		maturityAmt = ((rdInvestAmt * (rdTenure * (rdTenure + 1)) * rate) / (2400.0)) + (rdInvestAmt * rdTenure);
		logger.info(" Rd created");
		return maturityAmt;
	}

	public double deleteFD(Account accountBean) {
		LocalDate today = LocalDate.now();
		double balanceAmt = 0;
		BigDecimal finalAmount;
		LocalDate creationDate = accountBean.getAccCreationDate();
//		Duration period = Duration.between(creationDate, today);
		long noOfDaysBetween = ChronoUnit.DAYS.between(creationDate, today);
		int totalMonths = (int) (noOfDaysBetween / 30);
		totalMonths = totalMonths / 3;
		double principal = accountBean.getBalance().doubleValue();
		double rate = 6;
		if (totalMonths < 3) {
			balanceAmt = (principal + (principal * (totalMonths * (int) rate) / 1200))- (0.01 * principal);
		} else {
			double ratePercent = (1 + (rate / totalMonths) / 100);
			ratePercent = Math.pow(ratePercent, totalMonths);
			balanceAmt = principal * (ratePercent);
		}
		logger.info(" Fd account deleted");
		return balanceAmt;
	}

	public double deleteRD(Account accountBean) {
		LocalDate today = LocalDate.now();
		double balanceAmt = 0;
		LocalDate creationDate = accountBean.getAccCreationDate();
		long noOfDaysBetween = ChronoUnit.DAYS.between(creationDate, today);
		int months = (int) (noOfDaysBetween / 30);
		double rate = 7;
		double investAmt = accountBean.getOpenBalance().doubleValue();
		double tenure = accountBean.getTenure();
		if (months == 0) {
			balanceAmt = investAmt - (0.01 * investAmt);
		} else if (tenure > months) {
			rate = 6;
			balanceAmt = ((investAmt * (months * (months + 1)) * rate) / (2400.0)) + (investAmt * months);
		} else {
			balanceAmt = ((investAmt * (tenure * (tenure + 1)) * rate) / (2400.0)) + (investAmt * months);
		}
		logger.info(" Rd account deleted");
		return balanceAmt;
	}

	@Override
	public List<Account> getAccounts(BigInteger UCI) {
		logger.info(" All accounts displayed");
		return accountDao.getAccountsByUCI(UCI);
	}
	 
	@Override
	public boolean checkAccountExists(BigInteger accNo)
	{
		return accountDao.checkAccountExists(accNo);
	}
}
