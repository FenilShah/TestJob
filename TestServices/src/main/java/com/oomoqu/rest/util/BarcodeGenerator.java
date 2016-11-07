package com.oomoqu.rest.util;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.idautomation.databar.DataBar;
import com.idautomation.databar.encoder.barCodeEncoder;
import com.oomoqu.rest.dao.CouponDao;
import com.oomoqu.rest.exception.CustomException;
import com.oomoqu.rest.model.BarcodeField;
import com.oomoqu.rest.model.Coupon;
import com.oomoqu.rest.service.BarcodeFieldService;

@Component
public class BarcodeGenerator {
	Logger logger = Logger.getLogger(BarcodeGenerator.class);
	
	@Autowired
	EncodeImagesToByte encodeImagesToByte;
	
	@Autowired
	BarcodeFieldService barcodeApplicationFieldService;
	
	@Autowired
	CouponDao couponDao;
	/**
	public void generateBarcode(List<Coupon> coupons,String barcodeString) throws CustomException{
		logger.debug("Inside Barcode generatore -> Generating coupons barcode ");
		if(StringUtils.isEmpty(barcodeString)){
			throw new CustomException("Barcode string is require to generate coupons.");
		}
		for(Coupon coupon: coupons) {
			String result = createBarcodeForCoupon(coupon);
			coupon.setBarcodeString(result);
			couponDao.createCoupon(coupon);
		}
	}
	*/
	public String createBarcodeForCoupon(Coupon coupon){
		logger.debug("Inside Barcode generatore -> coupon barcode byte array ");
		
		DataBar db;
		db = new DataBar();
		
		/*if(coupon.getPromotionId() != null ){
			*//** pass bar code string, decode object *//*
			BarcodeField baf = new BarcodeField(barcodeString);
			
			*//** set couponId as serial number, again re-generate bar code string *//*
			db.code = baf.composeBarcodeApplicationfieldToString(baf, coupon.getCouponId());
		} else {
			db.code = coupon.getBarcodeString();
		}*/
		
		db.code = coupon.getBarcodeString();
		
		db.setSymbologyID(2);
		db.expandedStackedSegments = 8;
		db.backColor = java.awt.Color.white;
		
		String path = coupon.getCouponId() + ".jpeg";
		
		barCodeEncoder barcodeImageEncoder = new barCodeEncoder(db, "JPEG", path);
		
		logger.debug("Inside Barcode generatore -> Barcode Image generated : " + barcodeImageEncoder.result); 
		
		return encodeImagesToByte.encodeImageToString(path);
	}
}
