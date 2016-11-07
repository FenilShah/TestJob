package com.oomoqu.rest.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.oomoqu.rest.model.WalletUser;


@Service
public class WalletUserDao {
	private static Logger logger = Logger.getLogger(WalletUserDao.class);
	
	@Autowired
	MongoOperations mongoOperations;
	
	public void saveWalletUser(WalletUser walletUser){
		logger.debug("WalletUserDao -> Inside save WalletUser");
		mongoOperations.save(walletUser);
	}
	
	public WalletUser findWalletUserByUserId(String userId){
		logger.debug("WalletUserDao -> Inside find WalletUser By UserId");
		Criteria criteria = Criteria.where("userId").is(userId);
		Query searchOUserQuery = new Query(criteria);
		return mongoOperations.findOne(searchOUserQuery, WalletUser.class);
	}
	
	public void removeWalletUser(WalletUser walletUer){
		logger.debug("WalletUserDao -> Inside remove WalletUser");
		mongoOperations.remove(walletUer);
	}
	
}
