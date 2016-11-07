package com.oomoqu.rest.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "transaction")
public class Transaction {
	private String transactionId;
	private Date transactionDate;
	private String couponId;
	private String senderWalletId;
	private String locationId;
	private String addressId;
	private String recieverWalletId;
	private String transactionType;
	
	
	@Id
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
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
	
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public String getAddressId() {
		return addressId;
	}
	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
}
