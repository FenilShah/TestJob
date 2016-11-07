package com.oomoqu.rest.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.oomoqu.rest.model.PromotionCoin;

@Service
public class PromotionCoinDao {
	private static Logger logger = Logger.getLogger(PromotionCoinDao.class);
	
	@Autowired
	MongoOperations mongoOperations;
	
	public void save(PromotionCoin promotionCoin){
		logger.info("In PromotionCoinDao --> save");
		mongoOperations.save(promotionCoin);
	}
	
	public PromotionCoin findById(String promotionCoinId){
		logger.info("In PromotionCoinDao --> findByBrandId");
		return mongoOperations.findOne(new Query(Criteria.where("promotionCoinId").is(promotionCoinId)), PromotionCoin.class);
	}
	
	public List<PromotionCoin> findAll(){
		logger.info("In PromotionCoinDao --> findAll");
		return mongoOperations.findAll(PromotionCoin.class);
	}
}
