package com.oomoqu.rest.wrapper.portal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.oomoqu.rest.model.Coupon;

@JsonInclude(Include.NON_NULL)
public class CouponDetail extends Coupon{
	
	public Boolean validated = false;
	
	public CouponDetail(){
		
	}
	
	public CouponDetail(Coupon coupon){
		this.setSerialNo(coupon.getSerialNo());
		this.setReedemedAmount(coupon.getReedemedAmount());
		this.setBarcodeString(coupon.getBarcodeString());
		if(coupon.getValidatedDate() != null) {
			this.setValidated(true);
		}
	}

	public Boolean getValidated() {
		return validated;
	}

	public void setValidated(Boolean validated) {
		this.validated = validated;
	}
	
	
}
