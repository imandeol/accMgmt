package com.cg.ibs.accountmanagement.bean;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Customers")
@NamedQueries({ 
	@NamedQuery(name = "CB_BY_UID", query = "Select c from Customer c where c.userId= :userId "),
	@NamedQuery(name="CB_BY_UCI",query = "Select c from Customer c where c.uci= :UCI")
})
public class Customer implements Serializable {

	@Id
	@Column(name = "UCI", nullable = false, length = 16)
	private BigInteger uci;
	@Column(name = "USER_ID", nullable = false, length = 15)
	private String userId;
	@Column(name = "PASSWORD", nullable = false, length = 15)
	private String password;
	@Column(name = "FIRST_NAME", nullable = false, length = 20)
	private String firstName;
	@Column(name = "LAST_NAME", nullable = true, length = 20)
	private String lastname;
	@Column(name = "FATHER_NAME", nullable = false, length = 40)
	private String fatherName;
	@Column(name = "MOTHER_NAME", nullable = false, length = 40)
	private String motherName;
	@Column(name = "DOB", nullable = false)
	private LocalDate dateofBirth;
	@Column(name = "EMAIL_ID", nullable = false, length = 35)
	private String emailId;
	@Column(name = "GENDER", nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private Gender gender;
	@Column(name = "MOBILE_NUMBER", nullable = false, length = 10)
	private String mobileNumber;
	@Column(name = "ALTERNATE_MOBILE_NUMBER", nullable = false, length = 10)
	private String alternateMobileNumber;
	@Column(name = "AADHAR_NUMBER", nullable = false, length = 12)
	private String aadharNumber;
	@Column(name = "PAN_NUMBER", nullable = false, length = 10)
	private String panNumber;
	@Column(name = "APPLICANT_ID", nullable = false, length = 5)
	private long applicantId;
	@Column(name = "login_count", length = 2)
	private int login = 0;
//	@OneToOne
//	private AddressBean address;

	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
	private Set<AccountHolding> accountHoldings;

	public Customer() {
		super();
	}

	public BigInteger getUci() {
		return uci;
	}

	public void setUci(BigInteger uci) {
		this.uci = uci;
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getMotherName() {
		return motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	public LocalDate getDateofBirth() {
		return dateofBirth;
	}

	public void setDateofBirth(LocalDate dateofBirth) {
		this.dateofBirth = dateofBirth;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getAlternateMobileNumber() {
		return alternateMobileNumber;
	}

	public void setAlternateMobileNumber(String alternateMobileNumber) {
		this.alternateMobileNumber = alternateMobileNumber;
	}

	public String getAadharNumber() {
		return aadharNumber;
	}

	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public long getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(long applicantId) {
		this.applicantId = applicantId;
	}

	public int getLogin() {
		return login;
	}

	public void setLogin(int login) {
		this.login = login;
	}

//	public AddressBean getAddress() {
//		return address;
//	}
//
//	public void setAddress(AddressBean address) {
//		this.address = address;
//	}

	public Set<AccountHolding> getAccountHoldings() {
		return accountHoldings;
	}

	public void setAccountHoldings(Set<AccountHolding> accountHoldings) {
		this.accountHoldings = accountHoldings;
	}

}