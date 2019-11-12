package com.cg.ibs.accountmanagement.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import com.cg.ibs.accountmanagement.bean.TransactionBean;
import com.cg.ibs.accountmanagement.exception.IBSException;

public interface BankService {
	public boolean validateBanker(String bankId,String password) throws IBSException;
	public void fundsDeposit(BigInteger accNo,BigDecimal amt) throws IBSException;
	public List<TransactionBean> periodicTransactions(LocalDateTime startDate,LocalDateTime endDate,BigInteger accNo) throws IBSException;

}
