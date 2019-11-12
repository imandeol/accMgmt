package com.cg.ibs.accountmanagement.bean;

import java.math.BigInteger;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "service_providers")
@NamedQuery(name = "GET_SP_LIST" ,query = "SELECT s FROM ServiceProvider s")
public class ServiceProvider {
	@Column(name = "CATEGORY", nullable = false, length = 30)
	private String category;
	@Column(name = "COMPANY_NAME ", nullable = false, length = 30)
	private String nameOfCompany;
	@Column(name = "GST_IN", nullable = false, length = 16)
	private String gstin;
	@Column(name = "PAN", nullable = false, length = 10)
	private String panNumber;
	@Column(name = "ACCOUNT_NUMBER", nullable = false, length = 16)
	private BigInteger accountNumber;
	@Column(name = "BANK_NAME", nullable = false, length = 30)
	private String bankName;
	@Column(name = "COMPANY_ADDRESS", nullable = false, length = 200)
	private String companyAddress;
	@Column(name = "MOBILE_NUMBER", nullable = false, length = 10)
	private BigInteger mobileNumber;
	@Id
	private String userId;
	@Column(name = "PASSWORD", nullable = false, length = 200)
	private String password;
	@Column(name = "SPI")
	private BigInteger spi = BigInteger.valueOf(-1);
	@Column(name = "STATUS", length = 30)
	private String status = "Pending";
	@Column(name = "REQUEST_DATE", nullable = false)
	private LocalDateTime requestDate;

	public ServiceProvider() {
		super();
	}

	public String getNameOfCompany() {
		return nameOfCompany;
	}

	public void setNameOfCompany(String nameOfCompany) {
		this.nameOfCompany = nameOfCompany;
	}

	public String getGstin() {
		return gstin;
	}

	public void setGstin(String gstin) {
		this.gstin = gstin;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public BigInteger getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(BigInteger accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public BigInteger getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(BigInteger mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public BigInteger getSpi() {
		return spi;
	}

	public void setSpi(BigInteger spi) {
		this.spi = spi;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(LocalDateTime requestDate) {
		this.requestDate = requestDate;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "ServiceProvider [category=" + category + ", nameOfCompany=" + nameOfCompany + ", gstin=" + gstin
				+ ", panNumber=" + panNumber + ", accountNumber=" + accountNumber + ", bankName=" + bankName
				+ ", companyAddress=" + companyAddress + ", mobileNumber=" + mobileNumber + ", userId=" + userId
				+ ", password=" + password + ", spi=" + spi + ", status=" + status + ", requestDate=" + requestDate
				+ "]";
	}

}
