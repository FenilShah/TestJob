package com.oomoqu.rest.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "scannedPaperCoupon")
public class ScannedPaperCoupon {

	private String paperCouponId;
	private String barcodeString;
	private String scannedByUser;
	private Date scannedDate;
	private boolean status;
	
	@Id
	public String getPaperCouponId() {
		return paperCouponId;
	}
	public void setPaperCouponId(String paperCouponId) {
		this.paperCouponId = paperCouponId;
	}
	public String getBarcodeString() {
		return barcodeString;
	}
	public void setBarcodeString(String barcodeString) {
		this.barcodeString = barcodeString;
	}
	public String getScannedByUser() {
		return scannedByUser;
	}
	public void setScannedByUser(String scannedByUser) {
		this.scannedByUser = scannedByUser;
	}
	public Date getScannedDate() {
		return scannedDate;
	}
	public void setScannedDate(Date scannedDate) {
		this.scannedDate = scannedDate;
	}
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
}
