//package com.cg.ibs.accountmanagement.bean;
//import java.io.Serializable;
//import java.math.BigInteger;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.EnumType;
//import javax.persistence.Enumerated;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//
//import com.cg.ibs.rm.ui.Status;
//import com.cg.ibs.rm.ui.Type;
//
//@Entity
//@Table(name = "Beneficiary_table")
//public class Beneficiary implements Serializable {
///**
// *
// */
//private static final long serialVersionUID = 1597299572298697066L;
//@Id
//@Column(name = "Account_number")
//private BigInteger accountNumber;
//@Column(name = "Account_name")
//private String accountName;
//@Column(name = "Ifsc_Code")
//private String ifscCode;
//@Column(name = "Bank_name")
//private String bankName;
//@Column(name = "Type_of_account")
//@Enumerated(EnumType.STRING)
//private Type type;
//@Column(name = "Status")
//@Enumerated(EnumType.STRING)
//private Status status;
//@Column(name = "Remarks")
//private String adminRemarks=" ";
//@ManyToOne
//@JoinColumn(name = "UCI")
//private CustomerBean customer;
//
//public Beneficiary() {
// super();
//}
//
//public Beneficiary(BigInteger accountNumber, String accountName, String ifscCode, String bankName, Type type,
//  Status status, String adminRemarks, CustomerBean customer) {
// super();
// this.accountNumber = accountNumber;
// this.accountName = accountName;
// this.ifscCode = ifscCode;
// this.bankName = bankName;
// this.type = type;
// this.status = status;
// this.adminRemarks = adminRemarks;
// this.customer = customer;
//}
//
//@Override
//public String toString() {
// return "Beneficiary [accountNumber=" + accountNumber + ", accountName=" + accountName + ", ifscCode=" + ifscCode
//   + ", bankName=" + bankName + ", type=" + type + ", status=" + status + "]";
//}
//
//public Type getType() {
// return type;
//}
//
//public void setType(Type type) {
// this.type = type;
//}
//
//public BigInteger getAccountNumber() {
// return accountNumber;
//}
//
//public void setAccountNumber(BigInteger accountNumber) {
// this.accountNumber = accountNumber;
//}
//
//public String getAdminRemarks() {
// return adminRemarks;
//}
//
//public void setAdminRemarks(String adminRemarks) {
// this.adminRemarks = adminRemarks;
//}
//
//public CustomerBean getCustomer() {
// return customer;
//}
//
//public void setCustomer(CustomerBean customer) {
// this.customer = customer;
//}
//
//public String getAccountName() {
// return accountName;
//}
//
//public void setAccountName(String accountName) {
// this.accountName = accountName;
//}
//
//public String getIfscCode() {
// return ifscCode;
//}
//
//public void setIfscCode(String ifscCode) {
// this.ifscCode = ifscCode;
//}
//
//public String getBankName() {
// return bankName;
//}
//
//public void setBankName(String bankName) {
// this.bankName = bankName;
//}
//
//public Status getStatus() {
// return status;
//}
//
//public void setStatus(Status status) {
// this.status = status;
//}
//
//}