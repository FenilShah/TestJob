package com.oomoqu.rest.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.oomoqu.rest.model.Wallet;

@Service
public class WalletDao {
	private static Logger logger = Logger.getLogger(WalletDao.class);
	
	@Autowired
	MongoOperations mongoOperations;
	
	public void saveWallet(Wallet wallet){
		logger.debug("WalletDao -> Inside save Wallet");
		mongoOperations.save(wallet);
	};
	
	public void deleteWallet(Wallet wallet){
		logger.debug("WalletDao -> Inside delete Wallet");
		mongoOperations.remove(wallet);
	};
	
	public Wallet findWalletById(String walletId){
		logger.debug("WalletDao -> Inside find Wallet By Id");
		Query searchWalletQuery = new Query(Criteria.where("_id").is(walletId));
		Wallet wallet = mongoOperations.findOne(searchWalletQuery, Wallet.class);
		return wallet;
	};
	
	public List<Wallet> findAllWallet(){
		logger.debug("WalletDao -> Inside find All Wallet");
		return mongoOperations.findAll(Wallet.class);
	}
}
