package com.cg.ibs.accountmanagement.dao;

import java.math.BigInteger;
import java.util.List;

import com.cg.ibs.accountmanagement.bean.Account;
import com.cg.ibs.accountmanagement.exception.IBSException;

public interface AccountDao {
	public void update(Account accountBean) throws IBSException;
	public void addAccount(Account account);
	public List<Account> getAccountsByUCI(BigInteger UCI);
	public Account getAccountByAccNo(BigInteger accNumber) throws IBSException;
	public boolean checkAccountExists(BigInteger accNo);
	public BigInteger getAccNo();
}
