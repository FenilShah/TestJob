package com.oomoqu.rest.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="coins")
public class Coin {
	private String coinId;
	private String promotionCoinId;
	private String contractAddress;
	private String walletId;
	private Integer availableCoins = 0;
	private Date createdDate;
	private Date modifiedDate;
	
	@Id
	public String getCoinId() {
		return coinId;
	}
	public void setCoinId(String coinId) {
		this.coinId = coinId;
	}
	
	public String getPromotionCoinId() {
		return promotionCoinId;
	}
	public void setPromotionCoinId(String promotionCoinId) {
		this.promotionCoinId = promotionCoinId;
	}
	
	public String getContractAddress() {
		return contractAddress;
	}
	public void setContractAddress(String contractAddress) {
		this.contractAddress = contractAddress;
	}
	
	public String getWalletId() {
		return walletId;
	}
	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}
	
	public Integer getAvailableCoins() {
		return availableCoins;
	}
	public void setAvailableCoins(Integer availableCoins) {
		this.availableCoins = availableCoins;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	
	
}
