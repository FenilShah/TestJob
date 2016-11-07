package com.oomoqu.rest.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="promotion")
public class Promotion {
	//private static final String[] status = {"Pending","Approved","Active","Ended","Expired"};
	
	private String promotionId;
	private String name;
	private String description;
	private Date startDate;
	private Date endDate;
	private Date modifiedDate;
	private String ownerId;
	private String modifiedByUserId;
	private String brandId;
	
	private Integer totalCoupons; //Status : Allocated
	private String barcodeFormat; 
	private Integer availableCoupons; //Status : Issued (noOfCoupon - availableCoupons)
	private Boolean approved;
	private Integer createdCoupons; //Status : Created
	private String status;
	private String portalPromotionId;
	private String termsAndConditions;
	private String imageUrl;
	private String barcodeString;
	private String promotionNo;
	private String promotionCoinId;
	
	@Id
	public String getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	
	public Integer getTotalCoupons() {
		return totalCoupons;
	}
	public void setTotalCoupons(Integer totalCoupons) {
		this.totalCoupons = totalCoupons;
	}
	
	public String getBarcodeFormat() {
		return barcodeFormat;
	}
	public void setBarcodeFormat(String barcodeFormat) {
		this.barcodeFormat = barcodeFormat;
	}
	
	public Integer getAvailableCoupons() {
		return availableCoupons;
	}
	public void setAvailableCoupons(Integer availableCoupons) {
		this.availableCoupons = availableCoupons;
	}
	
	public Boolean getApproved() {
		return approved;
	}
	public void setApproved(Boolean approved) {
		this.approved = approved;
	}
	
	public Integer getCreatedCoupons() {
		return createdCoupons;
	}
	public void setCreatedCoupons(Integer createdCoupons) {
		this.createdCoupons = createdCoupons;
	}
	
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	
	public String getModifiedByUserId() {
		return modifiedByUserId;
	}
	public void setModifiedByUserId(String modifiedByUserId) {
		this.modifiedByUserId = modifiedByUserId;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getPortalPromotionId() {
		return portalPromotionId;
	}
	public void setPortalPromotionId(String portalPromotionId) {
		this.portalPromotionId = portalPromotionId;
	}
	
	public String getTermsAndConditions() {
		return termsAndConditions;
	}
	public void setTermsAndConditions(String termsAndConditions) {
		this.termsAndConditions = termsAndConditions;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public String getBarcodeString() {
		return barcodeString;
	}
	public void setBarcodeString(String barcodeString) {
		this.barcodeString = barcodeString;
	}
	
	public String getPromotionNo() {
		return promotionNo;
	}
	public void setPromotionNo(String promotionNo) {
		this.promotionNo = promotionNo;
	}
	
	public String getPromotionCoinId() {
		return promotionCoinId;
	}
	public void setPromotionCoinId(String promotionCoinId) {
		this.promotionCoinId = promotionCoinId;
	}
	
	
}
