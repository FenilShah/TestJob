package com.oomoqu.rest.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@Document(collection = "BatchDetail")
public class BatchDetail {
	public String batchDetailId;
	public String batchId;
	public String couponId;
	public String status;
	public Coupon coupon;
	
	@Id
	public String getBatchDetailId() {
		return batchDetailId;
	}
	public void setBatchDetailId(String batchDetailId) {
		this.batchDetailId = batchDetailId;
	}
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public String getCouponId() {
		return couponId;
	}
	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}
	
	@Transient
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Transient
	public Coupon getCoupon() {
		return coupon;
	}
	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}
	
	
	
}
