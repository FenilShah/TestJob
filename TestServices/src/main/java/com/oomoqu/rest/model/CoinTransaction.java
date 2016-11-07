package com.oomoqu.rest.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="cointransactions")
public class CoinTransaction {
	String coinTransactionId;
	private Date transactionDate;
	private String couponId;
	private String senderWalletId;
	private String recieverWalletId;
	private String transactionType;
	private Integer amount;
	
	@Id
	public String getCoinTransactionId() {
		return coinTransactionId;
	}
	public void setCoinTransactionId(String coinTransactionId) {
		this.coinTransactionId = coinTransactionId;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	
	public String getCouponId() {
		return couponId;
	}
	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}
	public String getSenderWalletId() {
		return senderWalletId;
	}
	public void setSenderWalletId(String senderWalletId) {
		this.senderWalletId = senderWalletId;
	}
	public String getRecieverWalletId() {
		return recieverWalletId;
	}
	public void setRecieverWalletId(String recieverWalletId) {
		this.recieverWalletId = recieverWalletId;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
	
}
