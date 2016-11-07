package com.oomoqu.rest.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "brandusers")
public class BrandUsers {
	private String brandUserId;
	private String brandId;
	private String userId;
	
	@Id
	public String getBrandUserId() {
		return brandUserId;
	}
	public void setBrandUserId(String brandUserId) {
		this.brandUserId = brandUserId;
	}
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
}
