package com.oomoqu.rest.wrapper.mobile;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.oomoqu.rest.model.Brand;

@JsonInclude(Include.NON_NULL)
public class BrandDetail extends Brand implements Comparable<BrandDetail>{
	private List<CouponDetail> couponDetails;
	
	public BrandDetail(){
		
	}
	
	public BrandDetail(Brand brand){
		this.setBrandId(brand.getBrandId());
		this.setLogoUrl(brand.getLogoUrl());
		this.setName(brand.getName());
	}
	
	public List<CouponDetail> getCouponDetails() {
		return couponDetails;
	}

	public void setCouponDetails(List<CouponDetail> couponDetails) {
		this.couponDetails = couponDetails;
	}
	

	@Override
	public int compareTo(BrandDetail o) {
		// TODO Auto-generated method stub
		return this.getName().compareTo(o.getName()); 
	}
	
	

}
