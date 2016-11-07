package com.oomoqu.rest.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.oomoqu.rest.model.ScannedPaperCoupon;

@Service
public class ScannedPaperCouponDao {
	private static Logger logger = Logger.getLogger(ScannedPaperCoupon.class);
	
	@Autowired
	MongoOperations mongoOperations;
	
	public void ScannedPaperCoupon(ScannedPaperCoupon invalidPaperCoupon){
		logger.debug("Inside ScannedPaperCouponDao -> save invalid paper coupon");
		if(!invalidPaperCoupon.getStatus()){
			ScannedPaperCoupon scannedPaperCoupon1 = this.findByBarcodeStringAndUser(invalidPaperCoupon.getBarcodeString(),invalidPaperCoupon.getScannedByUser());
			if(scannedPaperCoupon1!=null)
				invalidPaperCoupon.setPaperCouponId(scannedPaperCoupon1.getPaperCouponId());
		}
		mongoOperations.save(invalidPaperCoupon);
	}
	
	public ScannedPaperCoupon findByBarcodeStringAndUser(String barcodeString, String emailId){
		logger.debug("Inside ScannedPaperCouponDao -> find by barcode string");
		Query query = new Query(Criteria.where("barcodeString").is(barcodeString).and("scannedByUser").is(emailId));
		return mongoOperations.findOne(query, ScannedPaperCoupon.class);
	}
	
}
