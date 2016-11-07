package com.oomoqu.rest.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "locationusers")
public class LocationUsers {
	private String locationUserId;
	private String locationId;
	private String userId;
	
	@Id
	public String getLocationUserId() {
		return locationUserId;
	}
	public void setLocationUserId(String locationUserId) {
		this.locationUserId = locationUserId;
	}
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
