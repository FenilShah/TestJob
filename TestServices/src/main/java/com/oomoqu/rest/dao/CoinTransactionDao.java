package com.oomoqu.rest.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.oomoqu.rest.model.CoinTransaction;

@Service
public class CoinTransactionDao {
	private static Logger logger = Logger.getLogger(CoinTransactionDao.class);
	
	@Autowired
	MongoOperations mongoOperations;
	
	public void save(CoinTransaction coinTransaction){
		logger.info("Inside CoinTransactionDao --> save.");
		mongoOperations.save(coinTransaction);
	}
	
	public List<CoinTransaction> findByRecieverWalletId(String recieverWalletId){
		logger.info("Inside CoinTransactionDao --> findByRecieverWalletId.");
		return mongoOperations.find(new Query(Criteria.where("recieverWalletId").is(recieverWalletId)), CoinTransaction.class);
	}
	
}
