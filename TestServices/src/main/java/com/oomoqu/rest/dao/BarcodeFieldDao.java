package com.oomoqu.rest.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.oomoqu.rest.model.BarcodeField;
import com.oomoqu.rest.model.Promotion;

@Service
public class BarcodeFieldDao {
	private static Logger logger = Logger.getLogger(BarcodeFieldDao.class);
	
	
	@Autowired
	MongoOperations mongoOperations;
	
	public void saveBarcodeApplicationField(BarcodeField baf){
		logger.debug("BarcodeApplicationFieldDao -> Inside Save Barcode ApplicationField");
		mongoOperations.save(baf);
	}
	
	public Promotion findPromotioanBypromotionId(String promotionId){
		logger.debug("BarcodeApplicationFieldDao -> Inside find Promotioan By promotionId");
		Query searchPromotionOne = new Query(Criteria.where("promotionId").is(promotionId));
		Promotion sPromotion = mongoOperations.findOne(searchPromotionOne, Promotion.class);
		return sPromotion;
	}
	
	public BarcodeField findBarcodeApplicationFieldBypromotionId(String promotionId){
		logger.debug("BarcodeApplicationFieldDao -> Inside find BarcodeApplication Field By promotionId");
		Query searchBarcodeApplicationFieldOne = new Query(Criteria.where("promotionId").is(promotionId));
		BarcodeField barcodeApplicationField = mongoOperations.findOne(searchBarcodeApplicationFieldOne, BarcodeField.class);
		return barcodeApplicationField;
	}
	
}
