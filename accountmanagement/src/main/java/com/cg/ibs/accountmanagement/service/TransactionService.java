package com.cg.ibs.accountmanagement.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import com.cg.ibs.accountmanagement.bean.Account;
import com.cg.ibs.accountmanagement.bean.ServiceProvider;
import com.cg.ibs.accountmanagement.bean.TransactionBean;
import com.cg.ibs.accountmanagement.exception.IBSException;

public interface TransactionService {
	public List<TransactionBean> getMiniStmt(BigInteger accNo);
	public List<TransactionBean> getPeriodicStmt(LocalDateTime startDate, LocalDateTime endDate,BigInteger accNo) throws IBSException;
	public BigDecimal TransferFunds(BigInteger accNo, BigDecimal amt, String tranPwd, BigInteger recipientNo) throws IBSException;
	public List<ServiceProvider> getServiceProviders();
	public BigDecimal payUtilityBill(BigInteger accNo, BigInteger recipientNo, String transacPass, BigDecimal amount) throws IBSException;
	public int fundsDeposit(BigInteger accNo, BigDecimal amt, BigInteger transferAccount,int transId) throws IBSException;
	public BigDecimal TransferFundsOtherBank(BigInteger accNo, BigDecimal amt, String tranPwd, BigInteger recipientNo, String otherBank) throws IBSException;
	public TransactionBean openingAccountTransaction(Account account) throws IBSException;
}
