package com.oomoqu.rest.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oomoqu.rest.dao.CouponDao;
import com.oomoqu.rest.model.BarcodeField;
import com.oomoqu.rest.model.Coupon;
import com.oomoqu.rest.model.Promotion;


@Component
public class BarcodeFieldService {
	private static Logger logger = Logger.getLogger(BarcodeFieldService.class);
	
	@Autowired
	CouponDao couponDao;
	
	public BarcodeField decodeBarcodeString(String barcodeString){
		logger.debug("BarcodeApplicationFieldServiceService -- > Inside decode barcode string");
		BarcodeField baf = new BarcodeField(barcodeString);
		return baf;
	}
	
	public void generateAndUpdateCouponBarcodeString(Promotion promotion, Coupon coupon){
		String couponBarcodeString = this.generateCouponBarcodeStrings(promotion, coupon);
		coupon.setBarcodeString(couponBarcodeString);
		couponDao.createCoupon(coupon);
	}
	
	public String generateCouponBarcodeStrings(Promotion promotion, Coupon coupon){
		String couponBarcodeString = null;
		BarcodeField barcodeField = this.decomposeBarcodeFieldByBarcodeString(promotion.getBarcodeString());
		if(barcodeField != null){
			barcodeField.setSerialNumberValue(coupon.getSerialNo());
			couponBarcodeString = barcodeField.composeBarcodeApplicationfieldToString(barcodeField, coupon.getSerialNo());
		}
		
		return couponBarcodeString;
	}
	
	public String generateCouponBarcodeStrings(Promotion promotion, Coupon coupon, String saveValue){
		String couponBarcodeString = null;
		BarcodeField barcodeField = this.decomposeBarcodeFieldByBarcodeString(promotion.getBarcodeString());
		if(barcodeField != null){
			barcodeField.setSaveValueVLI(String.valueOf(saveValue.length()));
			barcodeField.setSaveValueValue(saveValue);
			couponBarcodeString = barcodeField.composeBarcodeApplicationfieldToString(barcodeField, coupon.getSerialNo());
		}
		
		return couponBarcodeString;
	}
	
	public String setOffercodeAndComposeBarcodeString(String promotionNumber, String promotionBarcodeString){
		BarcodeField barcodeField = this.decomposeBarcodeFieldByBarcodeString(promotionBarcodeString);
		
		barcodeField.setOfferCodeValue(String.format("%06d", Integer.parseInt(promotionNumber)));
		
		return barcodeField.composeBarcodeApplicationfieldToString(barcodeField, "");
		
	}
	
	public BarcodeField decomposeBarcodeFieldByBarcodeString(String barcodeString){
		return new BarcodeField(barcodeString);
	}
	
	public double convertRedeemAmount(int saveValue){
		return ((double)saveValue/100);
	}
	
	public String getSerialNumberFromBarcodeString(String couponBarcodeString){
		String serialNumber = null;
		BarcodeField barcodeField = new BarcodeField(couponBarcodeString);
		if(barcodeField != null){
			serialNumber = barcodeField.getSerialNumberValue();
		}
		return serialNumber;
		
	}
	
	public boolean validateBarcodeString(String barcodeString){
		if("8110".equals(barcodeString.substring(0,4))) {
			return true;
		} else {
			return false;
		}
	}
	
}
