package com.cg.ibs.accountmanagement.ui;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.cg.ibs.accountmanagement.bean.Account;
import com.cg.ibs.accountmanagement.bean.AccountStatus;
import com.cg.ibs.accountmanagement.bean.AccountType;
import com.cg.ibs.accountmanagement.bean.Customer;
import com.cg.ibs.accountmanagement.bean.ServiceProvider;
import com.cg.ibs.accountmanagement.bean.TransactionBean;
import com.cg.ibs.accountmanagement.bean.TransactionType;
import com.cg.ibs.accountmanagement.exception.IBSException;
import com.cg.ibs.accountmanagement.service.AccountService;
import com.cg.ibs.accountmanagement.service.BankService;
import com.cg.ibs.accountmanagement.service.CustomerService;
import com.cg.ibs.accountmanagement.service.TransactionService;

@Component("accountUi")
public class AccountUi {
	Scanner scanner = new Scanner(System.in);
	int input = 3;
	boolean flag5;
	private static DecimalFormat df = new DecimalFormat("#,###.00");
	private DateTimeFormatter customFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy 'at' hh:mm a");
	LocalDateTime myDateObj = LocalDateTime.now();
	
	static ApplicationContext appContext= new ClassPathXmlApplicationContext("applicationcontext.xml");
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	BankService bankService;
	
	@Autowired
	TransactionService transactionService;

	public void func() {

		while (input == 3) {
			System.out.println("***********Login Page************");
			System.out.println("Select who you are :");
			System.out.println("1.Customer \t 2.Banker");
			while (!scanner.hasNextInt()) {
				System.out.println("Enter a valid number!");
				scanner.next();
				scanner.nextLine();
			}
			input = scanner.nextInt();
			CustomerMenu custMenu = null;
			BankMenu bankMenu = null;

			if (input == 1) {
				String userId = null;
				String password = null;
				boolean bool1;
				Account account = new Account();
				List<TransactionBean> txns;
				int length;
				Customer customerBean = new Customer();
				do {
					bool1 = false;
					try {
						System.out.println("Enter UserId");
						userId = scanner.next();
						System.out.println("Enter Password");
						password = scanner.next();
						// try {
						if (customerService.validateCustomer(userId, password)) {
							customerBean = customerService.customerByUserId(userId);
							List<Account> accountsList = accountService.getAccounts(customerBean.getUci());
							System.out.println("\n" + "--------SUCCESSFULLY LOGGED IN!-------- \n");

							while (custMenu != CustomerMenu.QUIT) {
								System.out.println("Options available: ");
								for (CustomerMenu menu : CustomerMenu.values()) {
									System.out.println((menu.ordinal() + 1) + "." + menu.toString().replace("_", " "));
								}
								
								for(Account accounts:accountsList) {
								txns = transactionService
										.getMiniStmt(accounts.getAccNo());
								length = txns.size();
								if (length == 0) {
									txns.add(transactionService.openingAccountTransaction(accounts));
								}
								}
								
								while (!scanner.hasNextInt()) {
									System.out.println("Enter a valid number!");
									scanner.next();
									scanner.nextLine();
								}
								int ordinal = (scanner.nextInt() - 1);
								if (ordinal >= 0 && ordinal < CustomerMenu.values().length) {
									char check;
									custMenu = CustomerMenu.values()[ordinal];
									switch (custMenu) {
									case CHECK_BALANCE:
										do {
											accountsList = accountService.getAccounts(customerBean.getUci());
											account = selectAccount(accountsList);
											System.out.println(
													"Balance in INR: " + accountService.getBalance(account.getAccNo())
															.setScale(2, RoundingMode.HALF_UP) + "\n");
											System.out.println(
													"Do you want to continue in CheckBalance? Press Y/y to continue and anything else to go back to main menu");
											check = scanner.next().charAt(0);
										} while (check == 'Y' || check == 'y');
										// System.out.println("Do you want to go to main menu?");
										// scanner.next();
										break;
									case TRANSFER_FUNDS:
										do {
											accountsList = accountService.getAccounts(customerBean.getUci());
											transferFunds(account, accountsList);
											System.out.println(
													"Do you want to continue in transfer funds? Press Y/y to continue and anything else to go back to main menu");
											check = scanner.next().charAt(0);
										} while (check == 'y' || check == 'Y');
										break;
									case GENERATE_MINI_STATEMENT:
										do {
											accountsList = accountService.getAccounts(customerBean.getUci());
											account = selectAccount(accountsList);
											txns = transactionService
													.getMiniStmt(account.getAccNo());
											List<TransactionBean> txns_sub;
											length = txns.size();
											if (length == 0) {
												txns.add(transactionService.openingAccountTransaction(account));
											}
												System.out.println(
														"-------------------------------------------------------------------------------------------------------------------------------------");
												System.out.printf("%10s %25s %20s %35s %30s", "SERIAL NO",
														"TRANSACTION TYPE", "TRANSACTION DATE", "TRANSACTION AMOUNT",
														"TRANSACTION MODE");
												System.out.println();
												System.out.println(
														"-------------------------------------------------------------------------------------------------------------------------------------");
												if (length <= 10) {
													txns_sub = txns;
												} else {
													txns_sub = txns.subList(0, 10);
												}
												for (TransactionBean a : txns_sub) {
													BigDecimal amount = a.getTransactionAmount();
													amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
													if (a.getTransactionType().equals(TransactionType.CREDIT)) {
														System.out.format("%10s %15s %35s %25s %30s",
																(txns_sub.indexOf(a) + 1), a.getTransactionType(),
																customFormat.format(a.getTransactionDate()),
																"+" + amount, a.getTransactionMode());
														System.out.println();
													} else {
														System.out.format("%10s %15s %35s %25s %30s",
																(txns_sub.indexOf(a) + 1), a.getTransactionType(),
																customFormat.format(a.getTransactionDate()),
																"-" + amount, a.getTransactionMode());
														System.out.println();

													}
												}
												System.out.println(
														"-------------------------------------------------------------------------------------------------------------------------------------");
											
											System.out.println(
													"Do you want to continue to view mini statements?Press Y/y to continue and anything else to go back to main menu");
											check = scanner.next().charAt(0);
										} while (check == 'y' || check == 'Y');
										break;
									case GENERATE_PERIODIC_STATEMENT:
										do {
											accountsList = accountService.getAccounts(customerBean.getUci());
											boolean flag;
											LocalDateTime startDate1 = null;
											LocalDateTime endDate1 = null;
											account = selectAccount(accountsList);
											do {
												flag = false;
												try {
													DateTimeFormatter formatter = DateTimeFormatter
															.ofPattern("dd/MM/yyyy-HH:mm:ss");
													System.out.println("Enter start date in dd/MM/YYYY format");
													String startDate = scanner.next();

													startDate = startDate + "-00:00:00";
													// scanner.nextLine();
													// System.out.println(startDate);

													startDate1 = LocalDateTime.parse(startDate, formatter);
												} catch (Exception excp) {
													System.out.println("invalid start date");
													flag = true;
												}
											} while (flag);
											do {
												flag = false;
												try {
													DateTimeFormatter formatter = DateTimeFormatter
															.ofPattern("dd/MM/yyyy-HH:mm:ss");
													System.out.println("Enter end date in dd/MM/yyyy format");
													String endDate = scanner.next();
													endDate = endDate + "-23:59:59";
													endDate1 = LocalDateTime.parse(endDate, formatter);
												} catch (Exception excp) {
													System.out.println("invalid end date");
													flag = true;
												}
											} while (flag);
											List<TransactionBean> txns1;
											try {
												txns1 = transactionService.getPeriodicStmt(startDate1, endDate1,
														account.getAccNo());
												int length1 = txns1.size();
												if (length1 == 0) {
													System.out
															.println("No transactions are done during this period!!!");
												} else {

													System.out.println(
															"-------------------------------------------------------------------------------------------------------------------------------------");
													System.out.printf("%10s %25s %20s %25s %30s", "TRANSACTION ID",
															"TRANSACTION TYPE", "TRANSACTION DATE",
															"TRANSACTION AMOUNT", "TRANSACTION MODE");
													System.out.println();
													System.out.println(
															"-------------------------------------------------------------------------------------------------------------------------------------");
													for (TransactionBean a : txns1) {
														BigDecimal amount = a.getTransactionAmount();
														amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
														if (a.getTransactionType().equals(TransactionType.CREDIT)) {
															System.out.format("%10s %15s %35s %25s %30s",
																	(txns1.indexOf(a) + 1), a.getTransactionType(),
																	customFormat.format(a.getTransactionDate()),
																	"+" + amount, a.getTransactionMode());
															System.out.println();
														} else {
															System.out.format("%10s %15s %35s %25s %30s",
																	(txns1.indexOf(a) + 1), a.getTransactionType(),
																	customFormat.format(a.getTransactionDate()),
																	"-" + amount, a.getTransactionMode());
															System.out.println();

														}
													}
													System.out.println(
															"-------------------------------------------------------------------------------------------------------------------------------------");
												}
											} catch (Exception excp) {
												System.out.println(excp.getMessage());
											}
											System.out.println(
													"Do you want to continue to view PeriodicStatements? Press Y/y to continue and anything else to go back to main menu");
											check = scanner.next().charAt(0);
										} while (check == 'y' || check == 'Y');
										break;
									case OPEN_ACCOUNT:
										do {
										Boolean result = createAccount(accountsList, customerBean);
//										if (!result) {
//											System.out.println(
//													"Savings Account selected is closed, cannot perform any further transaction!!");
//										}
										System.out.println(
												"Do you want to continue furthur? Press Y/y to continue and anything else to go back to main menu");
										check = scanner.next().charAt(0);
										}while(check=='y'||check=='Y');
										break;
									case VIEW_ACCOUNT:
										do {
										accountsList = accountService.getAccounts(customerBean.getUci());
										account = selectAccount(accountsList);
										if (account.getAccType().equals(AccountType.FIXED_DEPOSIT)) {
											System.out.println(
													"-------------------------------------------------------------------------------------------------------------------------------------------------------------");
											System.out.printf("%10s %25s %20s %25s %30s %35s", "FD ACCOUNT NUMBER",
													"FD-TENURE", "FD-INVESTMEMT AMT", "MATURITY AMOUNT", "ACCOUNT TYPE",
													"ACCOUNT STATUS");
											System.out.println();
											System.out.println(
													"------------------------------------------------------------------------------------------------------------------------------------------------------------");
											double roundOffAmount = Math
													.round(((account.getMaturityAmt()).doubleValue()) * 100.0) / 100.0;
											System.out.format("%10s %25s %20s %25s %30s %35s", printAccountNumber(account.getAccNo()),
													account.getTenure(), account.getOpenBalance(), roundOffAmount,
													account.getAccType(), account.getAccStatus());
											System.out.println();

											System.out.println(
													"-----------------------------------------------------------------------------------------------------------------------------------------------------------");
										} else if (account.getAccType().equals(AccountType.RECURRING_DEPOSIT)) {
											System.out.println(
													"-----------------------------------------------------------------------------------------------------------------------------------------------------------");
											System.out.printf("%10s %25s %20s %25s %30s %35s", "RD ACCOUNT NUMBER",
													"RD-TENURE", "RD-INVESTMEMT AMT", "MATURITY AMOUNT", "ACCOUNT TYPE",
													"ACCOUNT STATUS");
											System.out.println();
											System.out.println(
													"-----------------------------------------------------------------------------------------------------------------------------------------------------------");
											double roundOffAmount = Math
													.round(((account.getMaturityAmt()).doubleValue()) * 100.0) / 100.0;
											System.out.format("%10s %25s %20s %25s %30s %35s",printAccountNumber(account.getAccNo()),
													account.getTenure(), account.getOpenBalance(), roundOffAmount,
													account.getAccType(), account.getAccStatus());
											System.out.println();
											System.out.println(
													"-----------------------------------------------------------------------------------------------------------------------------------------------------------");
										} else {
											System.out.println(
													"---------------------------------------------------------------------------------------------------");
											System.out.printf("%10s %25s %20s %25s", "ACCOUNT NUMBER", "BALANCE",
													"ACCOUNT TYPE", "ACCOUNT STATUS");
											System.out.println();
											System.out.println(
													"---------------------------------------------------------------------------------------------------");
											System.out.format("%10s %25s %20s %25s", printAccountNumber(account.getAccNo()),
													account.getBalance(), account.getAccType(), account.getAccStatus());
											System.out.println();
											System.out.println(
													"---------------------------------------------------------------------------------------------------");

										}
										System.out.println(
												"Do you want to continue furthur? Press Y/y to continue and anything else to go back to main menu");
										check = scanner.next().charAt(0);
										}while(check=='y'||check=='Y');
										break;
									case CLOSE_ACCOUNT:
										do {
										accountsList = accountService.getAccounts(customerBean.getUci());
										account = selectInvestmentAccount(accountsList);
										if(account.getAccStatus().equals(AccountStatus.CLOSED))
											break;
										System.out.println(
												"Select the Account where Maturity Amount is to be Credited:  ");
										Account closingAccount = selectAccountSaving(accountsList);
										if (closingAccount.getAccStatus().equals(AccountStatus.ACTIVE)) {
											closeAccount(account, closingAccount);

										} else {
											System.out.println(
													"Savings Account selected is closed, cannot perform any further transaction!!");
										}
										System.out.println(
												"Do you want to continue furthur? Press Y/y to continue and anything else to go back to main menu");
										check = scanner.next().charAt(0);
										}while(check=='y'||check=='Y');
										break;
									case PAY_UTILITY_BILLS:
										char c;
										do {
											try {
												ServiceProvider serviceProvider1 = new ServiceProvider();
												List<ServiceProvider> providers = new ArrayList<ServiceProvider>();
												providers = transactionService.getServiceProviders();
												if(providers.isEmpty()) {
													System.out.println("Bill Payment Portal is down! Returning to Main Menu!");
													break;
												}
												int inc = 1;
												System.out.println(
														"-----------------------------------------------------------------------------------------");
												System.out.printf("%10s %15s %20s", "SERIAL NUMBER", "COMPANY NAME",
														"CATEGORY");
												System.out.println();
												System.out.println(
														"-----------------------------------------------------------------------------------------");
												for (ServiceProvider serviceProvider : providers) {
													System.out.format("%10s %15s %20s", inc,
															serviceProvider.getNameOfCompany(),
															serviceProvider.getCategory());
													System.out.println();
													inc++;
												}
												System.out.println(
														"------------------------------------------------------------------------------------------");
												while (!scanner.hasNextInt()) {
													System.out.println("Enter a valid number!");
													scanner.next();
													scanner.nextLine();
												}
												int choice = scanner.nextInt();
												while (choice > providers.size() || choice <= 0) {
													while (!scanner.hasNextInt()) {
														System.out.println("Enter a valid number!");
														scanner.next();
														scanner.nextLine();
													}
													choice = scanner.nextInt();
												}
												serviceProvider1 = providers.get(choice - 1);
												account = selectAccountSaving(accountsList);
												if (account.getAccStatus().equals(AccountStatus.ACTIVE)) {
													System.out.println("Enter amount to be transferred");
													while (!scanner.hasNextBigDecimal()) {
														System.out.println("Enter a valid number!");
														scanner.next();
														scanner.nextLine();
													}
													BigDecimal amount = scanner.nextBigDecimal();
													System.out.println("Enter transaction password");
													String transacPass = scanner.next();
													checkTransactionPassword(transacPass, account.getTrans_Pwd());
													transactionService.payUtilityBill(account.getAccNo(),
															serviceProvider1.getAccountNumber(), transacPass, amount);
													System.out.println("Payment done successfully");
												} else {
													System.out.println(
															"Savings Account selected is closed, cannot perform any further transaction!!");
												}
											} catch (Exception excp) {
												System.out.println(excp.getMessage());
											}

											System.out.println("Do you want to continue?Y/N");
											c = scanner.next().charAt(0);

										} while (c == 'Y' || c == 'y');
										break;
									case QUIT:
										System.out.println("Successfully logged out");
										break;
									}

								} else {
									System.out.println("Invalid option");
								}
							}

						}
//						} catch (IBSException e) {
//							System.out.println(e.getMessage());
//							bool1 = true;
//						}
					} catch (Exception excp) {
						// System.out.println("Invalid userId/password ");
						System.out.println(excp.getMessage());
//						excp.printStackTrace();
						bool1 = true;
					}
				} while (bool1);

			} else if (input == 2) {

				String bankerId;
				String password;
				boolean flag1;
				do {
					flag1 = false;
					try {
						System.out.println("Enter BankerId");
						bankerId = scanner.next();
						System.out.println("Enter Password");
						password = scanner.next();
						if (bankService.validateBanker(bankerId, password)) {
							while (bankMenu != BankMenu.QUIT) {
								System.out.println("Options available: ");
								for (BankMenu menu : BankMenu.values()) {
									System.out.println((menu.ordinal() + 1) + "." + menu.toString().replace("_", " "));
								}
								System.out.println("ENTER YOUR CHOICE");
								while (!scanner.hasNextInt()) {
									System.out.println("Enter a valid number!");
									scanner.next();
									scanner.nextLine();
								}
								int ordinal = (scanner.nextInt() - 1);
								if (ordinal >= 0 && ordinal <= BankMenu.values().length) {
									bankMenu = BankMenu.values()[ordinal];
									switch (bankMenu) {
									case REQUEST_PERIODIC_STATEMENT:
										char c;
										do {
										 c='n';
										boolean flag;
										LocalDateTime startDate1 = null;
										LocalDateTime endDate1 = null;
										do {
											flag = false;
											try {
												DateTimeFormatter formatter = DateTimeFormatter
														.ofPattern("dd/MM/yyyy-HH:mm:ss");
												System.out.println("Enter start date in dd/MM/yyyy format");
												String startDate = scanner.next();
												startDate=startDate+ "-00:00:00";
												startDate1 = LocalDateTime.parse(startDate, formatter);
											} catch (Exception excp) {
												System.out.println("invalid start date");
												flag = true;
											}
										} while (flag);
										do {
											flag = false;
											try {
												DateTimeFormatter formatter = DateTimeFormatter
														.ofPattern("dd/MM/yyyy-HH:mm:ss");
												System.out.println("Enter end date in dd/MM/yyyy format");
												String endDate = scanner.next();
												endDate=endDate+ "-23:59:59";
												endDate1 = LocalDateTime.parse(endDate, formatter);
											} catch (Exception excp) {
												System.out.println("invalid end date");
												flag = true;
											}
										} while (flag);
                                        
										if(endDate1.compareTo(startDate1)>0 && startDate1.compareTo(LocalDateTime.now())<0 && Period.between(startDate1.toLocalDate(), endDate1.toLocalDate()).toTotalMonths() <= 6)
										{
										List<TransactionBean> txns1 = null;
										do {
											flag = false;
											try {
												System.out.println("Enter account number");
												BigInteger accNo = scanner.nextBigInteger();

												txns1 = bankService.periodicTransactions(startDate1, endDate1, accNo);

											} catch (Exception excp) {
												//System.out.println("Enter correct input");
												System.out.println(excp.getMessage());
												flag = true;
												scanner.next();
											}
										} while (flag);
										int length1 = txns1.size();
										if (length1 == 0) {
											System.out.println("No transactions are done in this period!!!");
										} else {
											System.out.println(
													"-------------------------------------------------------------------------------------------------------------------------------------");
											System.out.printf("%10s %25s %20s %25s %30s", "TRANSACTION ID",
													"TRANSACTION TYPE", "TRANSACTION DATE",
													"TRANSACTION AMOUNT", "TRANSACTION MODE");
											System.out.println();
											System.out.println(
													"-------------------------------------------------------------------------------------------------------------------------------------");
											for (TransactionBean a : txns1) {
												BigDecimal amount = a.getTransactionAmount();
												amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
												if (a.getTransactionType().equals(TransactionType.CREDIT)) {
													System.out.format("%10s %15s %35s %25s %30s",
															(txns1.indexOf(a) + 1), a.getTransactionType(),
															customFormat.format(a.getTransactionDate()),
															"+" + amount, a.getTransactionMode());
													System.out.println();
												} else {
													System.out.format("%10s %15s %35s %25s %30s",
															(txns1.indexOf(a) + 1), a.getTransactionType(),
															customFormat.format(a.getTransactionDate()),
															"-" + amount, a.getTransactionMode());
													System.out.println();

												}
											}
											System.out.println(
													"-------------------------------------------------------------------------------------------------------------------------------------");
							}
									}
										else {
											System.out.println("Invalid Period");
										}
										System.out.println("do you want to continue?y/Y");
										c=scanner.next().charAt(0);
										}while(c=='y'||c=='Y');
										break;
									case FUNDS_DEPOSIT_ENTRY:
										BigDecimal amt = new BigDecimal(0);
										boolean flag3 = false;
//										BigDecimal balance = new BigDecimal(0);
										do {
											flag3 = false;
											try {
												System.out.println("Enter the account number");
												BigInteger accNo = scanner.nextBigInteger();
												System.out.println("Enter the amount you want to deposit");
												amt = scanner.nextBigDecimal();
												bankService.fundsDeposit(accNo, amt);
												System.out.println("Amount Deposited Successfully");
											} catch (InputMismatchException excp) {
												flag3 = true;
												scanner.next();
											} catch (Exception excp) {
												System.out.println(excp.getMessage());
												flag3 = true;
											}
										} while (flag3);
										break;
									case QUIT:
										System.out.println("Successfully logged out");
										break;
									}
								} else {
									System.out.println("Invalid option");
								}
							}
						}
					} catch (IBSException excp) {
						System.out.println(excp.getMessage());
						flag1 = true;
					}
				} while (flag1);
			} else {
				System.out.println("enter proper input");
			}
			do {
				flag5 = false;
				try {
					System.out.println("Press 3 to Continue. Press any other number to EXIT!");
					input = scanner.nextInt();
				} catch (InputMismatchException excp) {
					System.out.println("enter proper Numerical Input");
					flag5 = true;
					scanner.next();
				}
			} while (flag5);
		}
	}

	public static void main(String args[]) {
		AccountUi accountUi= appContext.getBean("accountUi",AccountUi.class);
		accountUi.func();
	}

	public Account selectAccount(List<Account> accounts) {
		System.out.println("Select an account from the following");
		Account account = new Account();
		String accString;
//		Scanner scanner = new Scanner(System.in);
		int choice = 0;
		for (int i = 0; i < accounts.size(); i++) {
			account = accounts.get(i);
			accString= printAccountNumber(account.getAccNo());
			System.out.println((i + 1) + "  Account Number: " + accString + "\t");
		}
		System.out.println("\n Enter your choice");

		while (!scanner.hasNextInt()) {
			System.out.println("Enter a valid number!");
			scanner.next();
			scanner.nextLine();
		}

		choice = scanner.nextInt();

		while (choice > accounts.size() || choice <= 0) {
			System.out.println("Enter a valid number!");
			while (!scanner.hasNextInt()) {
				System.out.println("Enter a valid number!");
				scanner.next();
				scanner.nextLine();
			}
			choice = scanner.nextInt();
		}

		account = accounts.get(choice - 1);
//		scanner.close();
		return account;
	}

	public Account selectAccountSaving(List<Account> accounts) {

		Account account = new Account();
		List<Account> savingsList = new ArrayList<Account>();
//		Scanner scanner = new Scanner(System.in);
		int choice = 0;
		String accString;
		for (Account accountBean : accounts) {
			if (accountBean.getAccType().equals(AccountType.SAVINGS)) {
				savingsList.add(accountBean);
			}
		}
		System.out.println("Select an account from the following");
		for (int i = 0; i < savingsList.size(); i++) {
			account = savingsList.get(i);
			accString= printAccountNumber(account.getAccNo());
			System.out.println((i + 1) + "  Account Number: " + accString + "\t Account Status: "
					+ account.getAccStatus());
		}
		System.out.println("\n Enter your choice");
		while (!scanner.hasNextInt()) {
			System.out.println("Enter a valid number!");
			scanner.next();
			scanner.nextLine();
		}

		choice = scanner.nextInt();

		while (choice > savingsList.size() || choice <= 0) {
			System.out.println("Enter a valid number!");
			while (!scanner.hasNextInt()) {
				System.out.println("Enter a valid number!");
				scanner.next();
				scanner.nextLine();
			}
			choice = scanner.nextInt();
		}

		account = savingsList.get(choice - 1);
//		scanner.close();
		return account;
	}

	public Account selectInvestmentAccount(List<Account> accounts) {

		Account account = new Account();
		List<Account> investmentList = new ArrayList<Account>();
//		Scanner scanner = new Scanner(System.in);
		int choice = 0;
		String accString;
		for (Account accountBean : accounts) {
			if (accountBean.getAccStatus().equals(AccountStatus.ACTIVE)) {
				if (accountBean.getAccType().equals(AccountType.FIXED_DEPOSIT)
						|| accountBean.getAccType().equals(AccountType.RECURRING_DEPOSIT)) {
					investmentList.add(accountBean);
				}
			}
		}
		if (investmentList.isEmpty()) {
			System.out.println("No Fixed Deposit/Recurring Deposit Account exists!");
			account.setAccStatus(AccountStatus.CLOSED);
		} else {
			System.out.println("Select an account you want to close: ");
			for (int i = 0; i < investmentList.size(); i++) {
				account = investmentList.get(i);
				accString= printAccountNumber(account.getAccNo());
				System.out.println((i + 1) + "  Account Number: " + accString + " Account Type: "
						+ account.getAccType());
			}
			System.out.println("\n Enter your choice");
			while (!scanner.hasNextInt()) {
				System.out.println("Enter a valid number!");
				scanner.next();
				scanner.nextLine();
			}

			choice = scanner.nextInt();

			while (choice > investmentList.size() || choice <= 0) {
				System.out.println("Enter a valid number!");
				while (!scanner.hasNextInt()) {
					System.out.println("Enter a valid number!");
					scanner.next();
					scanner.nextLine();
				}
				choice = scanner.nextInt();
			}

			account = investmentList.get(choice - 1);
		}
//		scanner.close();
		return account;
	}

	public Boolean createAccount(List<Account> accountsList, Customer customerBean) {
		Boolean result = false;
		Double InvestAmt=0.0;
		Account account;
		BigInteger accountNum;
		String tranPwd, accountTransacPwd;
		int transInput;
		System.out.println("Select an account you want to open:");
		for (AccountMenu menu : AccountMenu.values()) {
			System.out.println((menu.ordinal() + 1) + "." + menu.toString().replace("_", " "));
		}

		while (!scanner.hasNextInt()) {
			System.out.println("Enter a valid number!");
			scanner.next();
			scanner.nextLine();
		}

		int ordinal = scanner.nextInt();

		while (ordinal > 2 || ordinal <= 0) {
			System.out.println("Enter a valid number!");
			while (!scanner.hasNextInt()) {
				System.out.println("Enter a valid number!");
				scanner.next();
				scanner.nextLine();
			}
			ordinal = scanner.nextInt();
		}

		Account createdAccount = new Account();

		switch (ordinal) {
		case 1:
			account = selectAccountSaving(accountsList);
			if (account.getAccStatus().equals(AccountStatus.ACTIVE)) {
				accountNum = account.getAccNo();
				try {
					System.out.println("Enter FDInvestment Amount");
					while (!scanner.hasNextDouble()) {
						System.out.println("Enter a valid amount!");
						scanner.next();
						scanner.nextLine();
					}

					InvestAmt = scanner.nextDouble();
					
					while(((InvestAmt>account.getBalance().doubleValue()) || InvestAmt <=0.0)) {
						System.out.println("Enter a valid amount!");
						while (!scanner.hasNextDouble()) {
							System.out.println("Enter a valid amount!");
							scanner.next();
							scanner.nextLine();
						}

						InvestAmt = scanner.nextDouble();

					}

					System.out.println("Enter FD Tenure in years(max 10 years)");
					while (!scanner.hasNextDouble()) {
						System.out.println("Enter a valid Tenure!");
						scanner.next();
						scanner.nextLine();
					}

					double fdTenure = scanner.nextDouble();
					while(fdTenure>10 || fdTenure<=0) {
						System.out.println("Enter FD Tenure in years(max 10 years)");
						fdTenure = scanner.nextDouble();
					}

					
					createdAccount = accountService.addAccount(customerBean, accountNum, AccountType.FIXED_DEPOSIT,
							InvestAmt, fdTenure);
					System.out.println("FD Successfully created with total amount after tenure:"
							+ df.format(createdAccount.getMaturityAmt()));
					result = true;
				} catch (IBSException excp) {
					System.out.println(excp.getMessage());
				} catch (InputMismatchException mismatchException) {
					System.out.println(" Enter Proper Input");
					scanner.next();
				}
			}
			break;
		case 2:
			account = selectAccountSaving(accountsList);
			accountTransacPwd= account.getTrans_Pwd();
			if (account.getAccStatus().equals(AccountStatus.ACTIVE)) {
				accountNum = account.getAccNo();
				try {
					System.out.println("Enter RD Ivestment Amount");

					while (!scanner.hasNextDouble()) {
						System.out.println("Enter a valid amount!");
						scanner.next();
						scanner.nextLine();
					}

					InvestAmt = scanner.nextDouble();

					while(((InvestAmt>account.getBalance().doubleValue()) || InvestAmt <=0.0)) {
						System.out.println("Enter a valid amount!");
						while (!scanner.hasNextDouble()) {
							System.out.println("Enter a valid amount!");
							scanner.next();
							scanner.nextLine();
						}

						InvestAmt = scanner.nextDouble();

					}

					System.out.println("Enter RD Tenure in years max(10 years)");

					while (!scanner.hasNextDouble()) {
						System.out.println("Enter a valid Tenure!");
						scanner.next();
						scanner.nextLine();
					}

					double rdTenure = scanner.nextDouble();
					
					while(rdTenure>10 || rdTenure<=0) {
						System.out.println("Enter RD Tenure in years max(10 years)");
						while (!scanner.hasNextDouble()) {
							System.out.println("Enter a valid Tenure!");
							scanner.next();
							scanner.nextLine();
						}
						rdTenure = scanner.nextDouble();
					}
					
					System.out.println("Enter transaction password");

					tranPwd = scanner.next();

					transInput = checkTransactionPassword(tranPwd, accountTransacPwd);

					while (transInput == 2) {
						System.out.println("Enter transaction password");

						tranPwd = scanner.next();

						transInput = checkTransactionPassword(tranPwd, accountTransacPwd);

					}

					
					createdAccount = accountService.addAccount(customerBean, account.getAccNo(),
							AccountType.RECURRING_DEPOSIT, InvestAmt, rdTenure);
					System.out.println("RD Successfully created with total amount after tenure:"
							+ df.format(createdAccount.getMaturityAmt()));
					result = true;
				} catch (IBSException excp) {
					System.out.println(excp.getMessage());
				} catch (InputMismatchException excp) {
					System.out.println(excp.getMessage());
				}
			}
			break;

		}
		return result;
	}

	public void closeAccount(Account closingAccountBean, Account creditAccountBean) {
		char ch='n';
		AccountType temp = closingAccountBean.getAccType();
		LocalDate today = LocalDate.now();
		LocalDate creationDate = closingAccountBean.getAccCreationDate();
		long noOfDaysBetween = ChronoUnit.DAYS.between(creationDate, today);
		double years=noOfDaysBetween/365.0;
		double tenure=closingAccountBean.getTenure();
		double invAmt=closingAccountBean.getOpenBalance().doubleValue();
		if(tenure>years) {
			System.out.println("Penality Amount will be deducted as tenure is not completed yet.(Penalty= Rs " +(0.01* invAmt) + ") Do you want to continue? Y/y?");
			ch=scanner.next().charAt(0);
		}
		if(ch=='y'||ch=='Y') {
		//Penalty to be added
		try {
			
			if (temp.equals(AccountType.FIXED_DEPOSIT)) {
				
				creditAccountBean = accountService.closeAccount(closingAccountBean.getAccNo(),
						creditAccountBean.getAccNo());
				
				System.out.println("The FD account is closed. Balance has been credited to Account: "
						+ printAccountNumber(creditAccountBean.getAccNo()) + ", Updated Balance of Saving Account: "
						+ creditAccountBean.getBalance().setScale(2, RoundingMode.HALF_UP));
			} else {
				creditAccountBean = accountService.closeAccount(closingAccountBean.getAccNo(),
						creditAccountBean.getAccNo());
				
				System.out.println("The RD account is closed. Balance has been credited to Account: "
						+ printAccountNumber(creditAccountBean.getAccNo()) + ", Updated Balance of Saving Account: "
						+ creditAccountBean.getBalance().setScale(2, RoundingMode.HALF_UP));
			}
		} catch (Exception excp) {
			System.out.println(excp.getMessage());
			//excp.printStackTrace();
		}
		}
		else
		{
			System.out.println("Account is not closed");
		}
	}

	public void transferFunds(Account account, List<Account> accountsList) {
		account = selectAccountSaving(accountsList);
		int transInput = 0;
		String otherBank;
		BigInteger recipientAccNo = null;
		BigInteger transferAccount= account.getAccNo();
		String accountTransacPwd= account.getTrans_Pwd();
		BigDecimal amt = null;
		BigDecimal bal;
		int choice = 0;
		try {
			bal=accountService.getBalance(account.getAccNo());
			System.out.println("Select the Bank of Recipient");
			System.out.println("1. IBS Bank");
			System.out.println("2. Other Bank");
			System.out.println("\n" + "Enter your choice");

			while (!scanner.hasNextInt()) {
				System.out.println("Enter a valid number!");
				scanner.next();
				scanner.nextLine();
			}

			choice = scanner.nextInt();

			while ((choice > 2 || choice <= 0)) {
				System.out.println("Enter a valid number!");
				while (!scanner.hasNextInt()) {
					System.out.println("Enter a valid number!");
					scanner.next();
					scanner.nextLine();
				}

				choice = scanner.nextInt();

			}

			if (choice == 1) {
				System.out.println("enter recipient account no.");
				while (!scanner.hasNextBigInteger()) {
					System.out.println("Enter a valid number!");
					scanner.next();
					scanner.nextLine();
				}
				recipientAccNo = scanner.nextBigInteger();

				while (!(recipientAccNo.toString().length() <= 11) || recipientAccNo.equals(transferAccount) || !(accountService.checkAccountExists(recipientAccNo))) {
					System.out.println("Enter a valid Account Number!");
					while (!scanner.hasNextBigInteger()) {
						System.out.println("Enter a valid number!");
						scanner.next();
						scanner.nextLine();
					}
					recipientAccNo = scanner.nextBigInteger();

				}	
				
				
				System.out.println("enter amount to be transferred in Rs.");

				while (!scanner.hasNextBigDecimal()) {
					System.out.println("Enter a valid amount");
					scanner.next();
					scanner.nextLine();
				}
				amt = scanner.nextBigDecimal();

				while (amt.compareTo(new BigDecimal(0)) <= 0 || amt.compareTo(bal) > 0) {
					System.out.println("Enter proper amount again");
					amt = scanner.nextBigDecimal();
				}
				
				System.out.println("Enter transaction password");

				String tranPwd = scanner.next();

				transInput = checkTransactionPassword(tranPwd, accountTransacPwd);

				while (transInput == 2) {
					System.out.println("Enter transaction password");

					tranPwd = scanner.next();

					transInput = checkTransactionPassword(tranPwd, accountTransacPwd);

				}

				if (transInput == 1) {
					BigDecimal balance = transactionService.TransferFunds(transferAccount, amt, tranPwd,
							recipientAccNo);

					//transactionService.fundsDeposit(recipientAccNo, amt, account.getAccNo());

					System.out.println("Amount Transferred Successfully. Remaining balance in INR: " + balance);
				} else
					System.out.println("\nExiting to Main Menu! \n");
			} 
			else {
				System.out.println("Enter Bank Name: ");
//				while (!scanner.hasNext()) {
//					System.out.println("Enter a valid bank name!");
//					scanner.next();
//					scanner.nextLine();
//				}
				otherBank= scanner.next();
				scanner.nextLine();
				
				System.out.println("enter recipient account no.");

				while (!scanner.hasNextBigInteger()) {
					System.out.println("Enter a valid number!");
					scanner.next();
					scanner.nextLine();
				}
				recipientAccNo = scanner.nextBigInteger();

				while (!(recipientAccNo.toString().length() <= 11) || recipientAccNo.equals(transferAccount)) {

					while (!scanner.hasNextBigInteger()) {
						System.out.println("Enter a valid Account Number!");
						System.out.println("Enter a valid number!");
						scanner.next();
						scanner.nextLine();
					}
					recipientAccNo = scanner.nextBigInteger();

				}

				System.out.println("enter amount to be transferred");

				while (!scanner.hasNextBigDecimal()) {
					System.out.println("Enter a valid amount");
					scanner.next();
					scanner.nextLine();
				}
				amt = scanner.nextBigDecimal();

				while (amt.compareTo(new BigDecimal(0)) <= 0 || amt.compareTo(bal) > 0) {
					System.out.println("Enter proper amount again");
					amt = scanner.nextBigDecimal();
				}

				System.out.println("Enter transaction password");

				String tranPwd = scanner.next();

				transInput = checkTransactionPassword(tranPwd, accountTransacPwd);

				while (transInput == 2) {
					System.out.println("Enter transaction password");

					tranPwd = scanner.next();
					
					transInput = checkTransactionPassword(tranPwd, accountTransacPwd);

				}
				// Add a wrong password functionality
				if (transInput == 1) {
					BigDecimal balance = transactionService.TransferFundsOtherBank(transferAccount, amt, tranPwd,
							recipientAccNo, otherBank);

//		transactionService.fundsDeposit(recipientAccNo, amt, account.getAccNo());

					System.out.println("Amount Transferred. Remaining balance in INR: " + balance);

				} else
					System.out.println("\nExiting to Main Menu! \n");
				;
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public int checkTransactionPassword(String tranPwd, String accountTransacPwd) {
		int result = 0;
		String choice;
		if (tranPwd.equals(accountTransacPwd)) {
			result = 1;
		} else {
			System.out.println("Wrong Transaction Password! \n");
			System.out.println("To enter Password again, Press y. Press anything else to exit to Main Menu ");
			choice = scanner.next();
			if (choice.equals("y") || choice.equals("Y")) {
				result = 2;
			} else
				result = 3;
		}
		return result;
	}
	
	public String printAccountNumber(BigInteger AccountNo) {
		String AccountString= AccountNo.toString();
		while(AccountString.length()<11) {
			AccountString= "0"+AccountString;
		}
		return AccountString;
	}
}
