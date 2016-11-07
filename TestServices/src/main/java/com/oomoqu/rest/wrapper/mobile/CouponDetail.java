package com.oomoqu.rest.wrapper.mobile;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.oomoqu.rest.model.Address;
import com.oomoqu.rest.model.Coupon;
import com.oomoqu.rest.model.Promotion;

@JsonInclude(Include.NON_NULL)
public class CouponDetail extends Coupon{
	private String promotionImageURL;
	private Address address;
	private String promotionName;
	private String promotionDescription;
	private Date promotionStartDate;
	private Date promotionEndDate;
	private String promotionTermsAndConditions;
	private String promotionStatus;
	private String promotionBarcodeString;

	public CouponDetail(Coupon coupon, Promotion promotion){
		this.setCouponId(coupon.getCouponId());
		this.setPromotionName(promotion.getName());
		this.setPromotionDescription(promotion.getDescription());
		this.setBarcodeString(coupon.getBarcodeString());
		this.setPromotionEndDate(promotion.getEndDate());
		this.setPromotionId(coupon.getPromotionId());
		this.setReedemedDate(coupon.getReedemedDate());
		this.setSharedDate(coupon.getSharedDate());
		this.setPromotionStartDate(promotion.getStartDate());
		this.setPromotionTermsAndConditions(promotion.getTermsAndConditions());
		this.setUsedDate(coupon.getUsedDate());
		this.setWalletId(coupon.getWalletId());
		this.setPromotionImageURL(promotion.getImageUrl());
		this.setPromotionStatus(promotion.getStatus());
		this.setPromotionBarcodeString(promotion.getBarcodeString());
		this.setReedemedAmount(coupon.getReedemedAmount());
	}
	
	public String getPromotionImageURL() {
		return promotionImageURL;
	}

	public void setPromotionImageURL(String promotionImageURL) {
		this.promotionImageURL = promotionImageURL;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}

	public String getPromotionDescription() {
		return promotionDescription;
	}

	public void setPromotionDescription(String promotionDescription) {
		this.promotionDescription = promotionDescription;
	}

	public Date getPromotionStartDate() {
		return promotionStartDate;
	}

	public void setPromotionStartDate(Date promotionStartDate) {
		this.promotionStartDate = promotionStartDate;
	}

	public Date getPromotionEndDate() {
		return promotionEndDate;
	}

	public void setPromotionEndDate(Date promotionEndDate) {
		this.promotionEndDate = promotionEndDate;
	}

	public String getPromotionTermsAndConditions() {
		return promotionTermsAndConditions;
	}

	public void setPromotionTermsAndConditions(String promotionTermsAndConditions) {
		this.promotionTermsAndConditions = promotionTermsAndConditions;
	}

	public String getPromotionStatus() {
		return promotionStatus;
	}

	public void setPromotionStatus(String promotionStatus) {
		this.promotionStatus = promotionStatus;
	}

	public String getPromotionBarcodeString() {
		return promotionBarcodeString;
	}

	public void setPromotionBarcodeString(String promotionBarcodeString) {
		this.promotionBarcodeString = promotionBarcodeString;
	}
	
	
}
