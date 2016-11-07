package com.oomoqu.rest.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="location")
public class Location {
	private String locationId;
	// private String portalRetailerId;
	private String retailerId;
	private String name;
	private String portalLocationId;
	private String addressId;
	private Boolean isDeleted;
	
	@Id
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPortalLocationId() {
		return portalLocationId;
	}
	public void setPortalLocationId(String portalLocationId) {
		this.portalLocationId = portalLocationId;
	}
	/*public String getPortalRetailerId() {
		return portalRetailerId;
	}
	public void setPortalRetailerId(String portalRetailerId) {
		this.portalRetailerId = portalRetailerId;
	}*/
	public String getRetailerId() {
		return retailerId;
	}
	public void setRetailerId(String retailerId) {
		this.retailerId = retailerId;
	}
	public String getAddressId() {
		return addressId;
	}
	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}
	public Boolean getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
}
