package com.cg.ibs.accountmanagement.exception;

public interface IBSExceptionInt {
	public static String WRONG_UID_AND_PASSWORD= "No account exists with this UserId and Password. Enter again";
	public static String INVALID_PASSWORD= "Please enter the right password";
	public static String INVALID_UCI= "No account exists with this UCI";
	public static String INVALID_ACC_NO= "No account exists with this Account Number";
	public static String INVALID_PERIOD= "Inappropriate period/dates entered";
	public static String BALANCE_ERROR_MESSAGE= "No sufficient balance/ Enter correct amount";
	public static String INVALID_TRANS_PASSW="Wrong Transaction password";
	public static String RD_MESSAGE= "there is no RDaccount with this acc.no";
	public static String RD_ACCOUNT_EXISTS= "RD account exist with this acc.no. Couldn't create new account";
	public static String FD_MESSAGE= "there is no FDaccount with this acc.no";
	public static String FD_ACCOUNT_EXISTS= "FD account exist with this acc.no. Couldn't create new account";
	public static String WRONG_BANKERID_AND_PASSWORD= "No account exists with this BankerId and Password. Enter again";
	public static String INVALID_BANK_PASSWORD= "Please enter the right password";
	public static String INVALID_ACC_TYPE = "Invalid Account Type";
	public static String ACCOUNT_CLOSED = "Account is closed, cannot perform any further transaction";
	
}
