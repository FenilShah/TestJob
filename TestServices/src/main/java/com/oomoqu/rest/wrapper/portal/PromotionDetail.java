package com.oomoqu.rest.wrapper.portal;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.oomoqu.rest.model.BatchMaster;
import com.oomoqu.rest.model.Coupon;
import com.oomoqu.rest.model.Promotion;
import com.oomoqu.rest.model.PromotionCoin;

@JsonInclude(Include.NON_NULL)
public class PromotionDetail extends Promotion{
	
	private Integer reedemedCoupons;
	private Integer issuedCoupons;
	private Integer sharedCoupons;
	
	private List<Coupon> coupons;
	private List<BatchMaster> batchMasters;
	private List<LocationDetail> locationDetails;
	private List<RetailerDetail> retailerDetails;
	private PromotionCoin promotionCoin;
	
	public PromotionDetail(){
	}
	
	public PromotionDetail(Promotion promotion){
		this.clone(promotion);
	}
	
	
	public Integer getReedemedCoupons() {
		return reedemedCoupons;
	}

	public void setReedemedCoupons(Integer reedemedCoupons) {
		this.reedemedCoupons = reedemedCoupons;
	}

	public Integer getIssuedCoupons() {
		return issuedCoupons;
	}

	public void setIssuedCoupons(Integer issuedCoupons) {
		this.issuedCoupons = issuedCoupons;
	}

	public Integer getSharedCoupons() {
		return sharedCoupons;
	}

	public void setSharedCoupons(Integer sharedCoupons) {
		this.sharedCoupons = sharedCoupons;
	}

	public List<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<Coupon> coupons) {
		this.coupons = coupons;
	}

	
	public List<BatchMaster> getBatchMasters() {
		return batchMasters;
	}

	public void setBatchMasters(List<BatchMaster> batchMasters) {
		this.batchMasters = batchMasters;
	}
	
	public List<LocationDetail> getLocationDetails() {
		return locationDetails;
	}

	public void setLocationDetails(List<LocationDetail> locationDetails) {
		this.locationDetails = locationDetails;
	}

	public List<RetailerDetail> getRetailerDetails() {
		return retailerDetails;
	}

	public void setRetailerDetails(List<RetailerDetail> retailerDetails) {
		this.retailerDetails = retailerDetails;
	}
	
	public PromotionCoin getPromotionCoin() {
		return promotionCoin;
	}

	public void setPromotionCoin(PromotionCoin promotionCoin) {
		this.promotionCoin = promotionCoin;
	}

	public void clone(Promotion promotion){
		this.setPromotionId(promotion.getPromotionId());
		this.setName(promotion.getName());
		this.setDescription(promotion.getDescription());
		this.setStartDate(promotion.getStartDate());
		this.setEndDate(promotion.getEndDate());
		this.setTotalCoupons(checkNull(promotion.getTotalCoupons())); //Allocated coupons
		this.setCreatedCoupons(checkNull(promotion.getCreatedCoupons())); //Created coupons
		this.setIssuedCoupons(checkNull(promotion.getTotalCoupons())-checkNull(promotion.getAvailableCoupons()));
		this.setStatus(promotion.getStatus());
	}
	
	public Integer checkNull(Integer value){
		if(value == null){
			return 0;
		}else{
			return value;
		}
	}
	public boolean equals(Object o) {
	       return (o instanceof Promotion) && (((Promotion) o).getPromotionId()).equals(this.getPromotionId());
	 }

	 public int hashCode() {
	       return this.getPromotionId().hashCode();
	 }
}
