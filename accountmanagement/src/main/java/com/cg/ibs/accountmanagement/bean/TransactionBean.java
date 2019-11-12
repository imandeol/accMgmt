package com.cg.ibs.accountmanagement.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "Transaction")
@NamedQueries({
	@NamedQuery(name = "GET_MINI",query= "select t from TransactionBean t where t.account.accNo= :accNo order by t.transactionDate desc"),
	@NamedQuery(name = "GET_PERIODIC",query= "select t from TransactionBean t where t.account.accNo= :accNo AND t.transactionDate BETWEEN :startDate AND :endDate order by t.transactionDate desc")
})
public class TransactionBean implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "TRANS_ID", nullable = false, length = 10)
	private int transactionId;

	@Column(name = "TRANS_TYPE", nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private TransactionType transactionType;
	
	@Column(name = "TRANS_DATE_TIME", nullable = false, length = 20)
	private LocalDateTime transactionDate;
	
	@Column(name = "AMOUNT", nullable = false, length = 10)
	private BigDecimal transactionAmount;
	
	@Column(name = "TRANS_MODE", nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private TransactionMode transactionMode;
	
	@Column(name = "TRANS_DESC", nullable = false, length = 40)
	private String transactionDescription;
	
	@Column(name = "REFERENCE_ID",length = 60)
	private String referenceId;
	
	@ManyToOne
	private Account account;
 
	public TransactionBean(int transactionId, TransactionType transactionType, LocalDateTime transactionDate,
			BigDecimal transactionAmount, TransactionMode transactionMode, String transactionDescription,
			String referenceId, Account account) {
		super();
		this.transactionId = transactionId;
		this.transactionType = transactionType;
		this.transactionDate = transactionDate;
		this.transactionAmount = transactionAmount;
		this.transactionMode = transactionMode;
		this.transactionDescription = transactionDescription;
		this.referenceId = referenceId;
		this.account = account;
	}

	public TransactionBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public LocalDateTime getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}

	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public TransactionMode getTransactionMode() {
		return transactionMode;
	}

	public void setTransactionMode(TransactionMode transactionMode) {
		this.transactionMode = transactionMode;
	}
	
	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getTransactionDescription() {
		return transactionDescription;
	}

	public void setTransactionDescription(String transactionDescription) {
		this.transactionDescription = transactionDescription;
	}
	

}