package com.oomoqu.rest.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="retailer")
public class Retailer {
	
	private String retailerId;
	private String name;
	private String logoUrl;
	private Date createdDate; 
	private Date modifiedDate;
	private String createdByUserId;	
	private String portalRetailerId;	
	private String portalCompanyId;
	private Boolean isDeleted;
	
	@Id
	public String getRetailerId() {
		return retailerId;
	}
	public void setRetailerId(String retailerId) {
		this.retailerId = retailerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
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
	public String getCreatedByUserId() {
		return createdByUserId;
	}
	public void setCreatedByUserId(String createdByUserId) {
		this.createdByUserId = createdByUserId;
	}
	public String getPortalRetailerId() {
		return portalRetailerId;
	}
	public void setPortalRetailerId(String portalRetailerId) {
		this.portalRetailerId = portalRetailerId;
	}
	public String getPortalCompanyId() {
		return portalCompanyId;
	}
	public void setPortalCompanyId(String portalCompanyId) {
		this.portalCompanyId = portalCompanyId;
	}
	public Boolean getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
}
