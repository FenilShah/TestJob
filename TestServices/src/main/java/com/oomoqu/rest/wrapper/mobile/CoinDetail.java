package com.oomoqu.rest.wrapper.mobile;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.oomoqu.rest.model.Brand;
import com.oomoqu.rest.model.Coin;
import com.oomoqu.rest.model.PromotionCoin;

@JsonInclude(Include.NON_NULL)
public class CoinDetail extends Coin{

	private String redeemRule;
	private String brandId;
	private String logoUrl;
	private String name;
	
	public CoinDetail(Coin coin, PromotionCoin promotionCoin, Brand brand){
		this.setAvailableCoins(coin.getAvailableCoins());
		this.setPromotionCoinId(coin.getPromotionCoinId());
		this.setRedeemRule(promotionCoin.getRedeemRule());
		this.setBrandId(brand.getBrandId());
		this.setLogoUrl(brand.getLogoUrl());
		this.setName(brand.getName());
	}

	public String getRedeemRule() {
		return redeemRule;
	}

	public void setRedeemRule(String redeemRule) {
		this.redeemRule = redeemRule;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
