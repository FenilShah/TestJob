package com.oomoqu.rest.wrapper.portal;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.oomoqu.rest.model.Location;
import com.oomoqu.rest.model.Retailer;

@JsonInclude(Include.NON_NULL)
public class RetailerDetail extends Retailer{

	public RetailerDetail(Retailer retailer){
		this.setRetailerId(retailer.getRetailerId());
		this.setName(retailer.getName());
		this.setCreatedByUserId(retailer.getCreatedByUserId());
		this.setCreatedDate(retailer.getCreatedDate());
		this.setIsDeleted(retailer.getIsDeleted());
		this.setLogoUrl(retailer.getLogoUrl());
		this.setModifiedDate(retailer.getModifiedDate());
		this.setPortalCompanyId(retailer.getPortalCompanyId());
	}
	private List<Location> locations;
	
	public List<Location> getLocations() {
		return locations;
	}
	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}
	
	private List<LocationDetail> locationDetails;

	public List<LocationDetail> getLocationDetails() {
		return locationDetails;
	}
	public void setLocationDetails(List<LocationDetail> locationDetails) {
		this.locationDetails = locationDetails;
	}
	
}
