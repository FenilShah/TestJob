package com.oomoqu.rest.wrapper.portal;

import java.util.List;

public class WalletCouponDetail {
	
	private String walletId;
	
	private List<CouponDetail> couponDetails;
	
	public String getWalletId() {
		return walletId;
	}
	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}
	
	public List<CouponDetail> getCouponDetails() {
		return couponDetails;
	}
	public void setCouponDetails(List<CouponDetail> couponDetails) {
		this.couponDetails = couponDetails;
	}
	
}
