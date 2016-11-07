package com.oomoqu.rest.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="retailerusers")
public class RetailerUsers {

	private String retailerUserId;
	private String retailerId;
	private String userId;
	
	@Id
	public String getRetailerUserId() {
		return retailerUserId;
	}
	public void setRetailerUserId(String retailerUserId) {
		this.retailerUserId = retailerUserId;
	}
	public String getRetailerId() {
		return retailerId;
	}
	public void setRetailerId(String retailerId) {
		this.retailerId = retailerId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

}
