package com.cg.ibs.accountmanagement.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import com.cg.ibs.accountmanagement.exception.IBSException;
import com.cg.ibs.accountmanagement.service.AccountServiceImpl;

class AccountServiceImplTest {
	
//	@Test
//	public void chekTransferFundsPositive() {
//		BigInteger integer = new BigInteger("1234567890123456");
//		try {
//			BigDecimal actualCurrentBalance = customerService.transferFunds("saif123", "1111", new BigDecimal(2000),
//					integer);
//			assertEquals(20000.00, actualCurrentBalance.doubleValue(),
//					"This method should return the remaining balance after transfer");
//		} catch (IBSCustExceptionImpl e) {
//			fail("bow bow!");
//		}
//	}

	AccountServiceImpl accountServiceImpl = new AccountServiceImpl();
	@Test
	public void chekGetBalancePositive() {
		BigInteger integer = new BigInteger("1234567890123456");
		try {
			assertNotNull(accountServiceImpl.getBalance(integer));
		} catch (IBSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void chekGetBalanceNeutral() {
		BigInteger integer = new BigInteger("1234567890123456");
		try {
			assertNotNull(accountServiceImpl.getBalance(integer));
		} catch (IBSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void chekAddAccount() {
		
	}

	@Test
	public void chekCloseAcccountPositive() {
		BigInteger integer = new BigInteger("1234567890123456");
		try {
			assertEquals(100000, accountServiceImpl.closeAccount(integer, integer).getMaturityAmt().doubleValue());
		} catch (IBSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void chekCloseAcccountNegative() {
		BigInteger integer = new BigInteger("1234567890123456");
		try {
			assertEquals(100000, accountServiceImpl.closeAccount(integer, integer).getMaturityAmt().doubleValue());
		} catch (IBSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}





//@Test
//public void chekValidateCustomer() {
//	assertThrows(IBSCustExceptionImpl.class, () -> {
//		customerService.validateCustomer("saif13", "saif");
//	});
//}