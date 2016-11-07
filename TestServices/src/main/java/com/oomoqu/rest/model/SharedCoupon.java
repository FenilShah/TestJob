package com.oomoqu.rest.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "SharedCoupons")
public class SharedCoupon {
	private String sharedCouponId;
	private String couponId;
	private String senderWalletId;
	private String receiverWalletId;
	private String loginType;
	private String loginId; 
	private Date sharedDate;
	private Boolean isAssigned;
	private Date assignedDate;
	
	@Id
	public String getSharedCouponId() {
		return sharedCouponId;
	}
	public void setSharedCouponId(String sharedCouponId) {
		this.sharedCouponId = sharedCouponId;
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
	
	public String getReceiverWalletId() {
		return receiverWalletId;
	}
	public void setReceiverWalletId(String receiverWalletId) {
		this.receiverWalletId = receiverWalletId;
	}
	
	public String getLoginType() {
		return loginType;
	}
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
	
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	
	public Date getSharedDate() {
		return sharedDate;
	}
	public void setSharedDate(Date sharedDate) {
		this.sharedDate = sharedDate;
	}
	
	public Boolean getIsAssigned() {
		return isAssigned;
	}
	public void setIsAssigned(Boolean isAssigned) {
		this.isAssigned = isAssigned;
	}
	
	public Date getAssignedDate() {
		return assignedDate;
	}
	public void setAssignedDate(Date assignedDate) {
		this.assignedDate = assignedDate;
	}
	
	public boolean equals(Object o) {
	      return (o instanceof SharedCoupon) && (((SharedCoupon) o).getSharedCouponId()).equals(this.getSharedCouponId());
	}

	public int hashCode() {
	      return sharedCouponId.hashCode();
	}
}
