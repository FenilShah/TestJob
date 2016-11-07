package com.oomoqu.rest.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Document(collection = "walletusers")
public class WalletUser {

	private String id;
	private String walletId;
	private String userId;
	
	@Id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getWalletId() {
		return walletId;
	}
	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
