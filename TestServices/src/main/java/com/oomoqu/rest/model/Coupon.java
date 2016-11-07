package com.oomoqu.rest.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="coupon")
public class Coupon {

	private String couponId;
	private String couponHashKey;
	private Date createdDate;
	private Date modifiedDate;
	private String promotionId;
	private String walletId;
	private String barcodeString;
	private Date issuedDate;
	private Date usedDate;
	private Date reedemedDate;
	private Date sharedDate;
	private Date paymentRequestedDate;
	private Date paidDate;
	private Double reedemedAmount;
	private String serialNo;
	private Date validatedDate;
	private Date approvedDate;
	private Date rejectedDate;
	private String addressId;
	private String type;
	
	@Id
	public String getCouponId() {
		return couponId;
	}
	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public String getCouponHashKey() {
		return couponHashKey;
	}
	public void setCouponHashKey(String couponHashKey) {
		this.couponHashKey = couponHashKey;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	public String getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}
	
	public String getWalletId() {
		return walletId;
	}
	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}
	public String getBarcodeString() {
		return barcodeString;
	}
	public void setBarcodeString(String barcodeString) {
		this.barcodeString = barcodeString;
	}
	
	public Date getIssuedDate() {
		return issuedDate;
	}
	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}

	public Date getUsedDate() {
		return usedDate;
	}
	public void setUsedDate(Date usedDate) {
		this.usedDate = usedDate;
	}
	public Date getReedemedDate() {
		return reedemedDate;
	}
	public void setReedemedDate(Date reedemedDate) {
		this.reedemedDate = reedemedDate;
	}
	public Date getSharedDate() {
		return sharedDate;
	}
	public void setSharedDate(Date sharedDate) {
		this.sharedDate = sharedDate;
	}
	
	public Double getReedemedAmount() {
		return reedemedAmount;
	}
	public void setReedemedAmount(Double reedemedAmount) {
		this.reedemedAmount = reedemedAmount;
	}
	
	public Date getPaymentRequestedDate() {
		return paymentRequestedDate;
	}
	public void setPaymentRequestedDate(Date paymentRequestedDate) {
		this.paymentRequestedDate = paymentRequestedDate;
	}
	
	public Date getPaidDate() {
		return paidDate;
	}
	public void setPaidDate(Date paidDate) {
		this.paidDate = paidDate;
	}
	
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	
	public Date getValidatedDate() {
		return validatedDate;
	}
	public void setValidatedDate(Date validatedDate) {
		this.validatedDate = validatedDate;
	}
	
	public Date getApprovedDate() {
		return approvedDate;
	}
	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}
	
	public Date getRejectedDate() {
		return rejectedDate;
	}
	public void setRejectedDate(Date rejectedDate) {
		this.rejectedDate = rejectedDate;
	}
	
	public String getAddressId() {
		return addressId;
	}
	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
