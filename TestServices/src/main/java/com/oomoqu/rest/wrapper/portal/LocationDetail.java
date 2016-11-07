package com.oomoqu.rest.wrapper.portal;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.oomoqu.rest.model.Address;
import com.oomoqu.rest.model.BatchMaster;
import com.oomoqu.rest.model.Location;

@JsonInclude(Include.NON_NULL)
public class LocationDetail extends Location{
	private Set<BrandDetail> brandDetail;
	
	public Set<BrandDetail> getBrandDetail() {
		return brandDetail;
	}
	public void setBrandDetail(Set<BrandDetail> brandDetail) {
		this.brandDetail = brandDetail;
	}
	
	private List<BrandDetail> brandDetails;
	public List<BrandDetail> getBrandDetails() {
		return brandDetails;
	}
	public void setBrandDetails(List<BrandDetail> brandDetails) {
		this.brandDetails = brandDetails;
	}
	
	private List<BatchMaster> batchMasters;
	public List<BatchMaster> getBatchMasters() {
		return batchMasters;
	}
	public void setBatchMasters(List<BatchMaster> batchMasters) {
		this.batchMasters = batchMasters;
	}
	
	private Address address;
	public Address getAddress() {
		return address;
	}
	
	public void setAddress(Address address) {
		this.address = address;
	}
	
	public boolean equals(Object o) {
		return (o instanceof Location) && (((Location) o).getLocationId()).equals(this.getLocationId());
	}
	 
	public int hashCode() {
	   return this.getLocationId().hashCode();
	}
	
}
