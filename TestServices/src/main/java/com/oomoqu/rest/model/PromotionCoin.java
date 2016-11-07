package com.oomoqu.rest.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="promotioncoins")
public class PromotionCoin {
	private String promotionCoinId;
	
	private Integer totalCoins = 0; 
	
	private String distributionRule;
	private String redeemRule;
	
	@Id
	public String getPromotionCoinId() {
		return promotionCoinId;
	}

	public void setPromotionCoinId(String promotionCoinId) {
		this.promotionCoinId = promotionCoinId;
	}

	public Integer getTotalCoins() {
		return totalCoins;
	}

	public void setTotalCoins(Integer totalCoins) {
		this.totalCoins = totalCoins;
	}

	public String getDistributionRule() {
		return distributionRule;
	}

	public void setDistributionRule(String distributionRule) {
		this.distributionRule = distributionRule;
	}

	public String getRedeemRule() {
		return redeemRule;
	}

	public void setRedeemRule(String redeemRule) {
		this.redeemRule = redeemRule;
	}

}
