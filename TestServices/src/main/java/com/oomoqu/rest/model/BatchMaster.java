package com.oomoqu.rest.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "BatchMaster")
public class BatchMaster {
	private String batchId;
	private Long batchNo;
	private Date createdDate;
	private Double requestedAmount;
	private Double paidAmount;
	private String status;
	private String locationId;
	private String promotionId;
	private List<BatchDetail> batchDetails;
	
	@Id
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	
	public Long getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(Long batchNo) {
		this.batchNo = batchNo;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public Double getRequestedAmount() {
		return requestedAmount;
	}
	public void setRequestedAmount(Double requestedAmount) {
		this.requestedAmount = requestedAmount;
	}
	
	public Double getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	
	public String getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}
	
	@Transient
	public List<BatchDetail> getBatchDetails() {
		return batchDetails;
	}
	public void setBatchDetails(List<BatchDetail> batchDetails) {
		this.batchDetails = batchDetails;
	}
	
}
