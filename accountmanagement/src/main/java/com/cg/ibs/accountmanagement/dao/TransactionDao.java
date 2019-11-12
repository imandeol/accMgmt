package com.cg.ibs.accountmanagement.dao;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import com.cg.ibs.accountmanagement.bean.ServiceProvider;
import com.cg.ibs.accountmanagement.bean.TransactionBean;

public interface TransactionDao {
	public void addNewTransaction(TransactionBean transBean);
	public void updatetransaction(TransactionBean transBean);
	public List<TransactionBean> getPeriodicTransactions(LocalDateTime startDate, LocalDateTime endDate,BigInteger accNumber);
	public List<TransactionBean> getMiniTransactions(BigInteger accNumber);
	public List<ServiceProvider> getServiceProviders();
}
