package com.oomoqu.rest.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.oomoqu.rest.model.Coin;

@Service
public class CoinDao {
	private static Logger logger = Logger.getLogger(CoinDao.class);
	
	@Autowired
	MongoOperations mongoOperations;
	
	public void save(Coin coin){
		logger.info("Inside CouponDao --> save.");
		mongoOperations.save(coin);
	}
	
	public Coin findByPromotionCoinId(String promotionCoinId){
		logger.info("Inside CouponDao --> findByPromotionCoinId.");
		return mongoOperations.findOne(new Query(Criteria.where("promotionCoinId").is(promotionCoinId)), Coin.class);
	}
	
	public List<Coin> findByWalletId(String walletId){
		logger.info("Inside CouponDao --> walletId.");
		return mongoOperations.find(new Query(Criteria.where("walletId").is(walletId)), Coin.class);
	}
	
	public Coin findByWalletAndPromotionCoinId(String walletId, String promotionCoinId){
		logger.info("Inside CouponDao --> findByWalletAndPromotionCoinId.");
		return mongoOperations.findOne(new Query(Criteria.where("walletId").is(walletId).and("promotionCoinId").is(promotionCoinId)), Coin.class);
	}
}
