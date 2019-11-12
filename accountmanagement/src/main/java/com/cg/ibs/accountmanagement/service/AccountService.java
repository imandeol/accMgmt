
package com.cg.ibs.accountmanagement.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import com.cg.ibs.accountmanagement.bean.Account;
import com.cg.ibs.accountmanagement.bean.AccountType;
import com.cg.ibs.accountmanagement.bean.Customer;
import com.cg.ibs.accountmanagement.exception.IBSException;

public interface AccountService {
	public List<Account> getAccounts(BigInteger UCI);

	public BigDecimal getBalance(BigInteger accNo) throws IBSException;

	public Account viewAccount(BigInteger accNo) throws IBSException;

	public Account closeAccount(BigInteger accNo,BigInteger creditAccNo) throws IBSException;

	Account addAccount(Customer customerBean, BigInteger accNo, AccountType accType, double amt, double tenure)
			throws IBSException;
	public boolean checkAccountExists(BigInteger accNo);
	
}
