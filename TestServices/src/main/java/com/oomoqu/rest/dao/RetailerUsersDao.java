package com.oomoqu.rest.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import com.oomoqu.rest.model.RetailerUsers;

@Service
public class RetailerUsersDao {
	private static Logger logger = Logger.getLogger(RetailerUsers.class);
	
	@Autowired
	MongoOperations mongoOperations;
	
	public void save(RetailerUsers retailerUsers){
		logger.debug("RetailerUsersDao -> Inside Save Retailer Users");
		Query searchRetailerQuery = new Query(Criteria.where("retailerId").is(retailerUsers.getRetailerId()).and("userId").is(retailerUsers.getUserId()));
		RetailerUsers retailerUsers1 = mongoOperations.findOne(searchRetailerQuery, RetailerUsers.class);
		if(retailerUsers1 == null)
			mongoOperations.save(retailerUsers);
	}
	
	public void delete(String userId){
		logger.debug("RetailerUsersDao -> Inside delete RetailerUser");
		Query retailerUserQuery = new Query(Criteria.where("userId").is(userId));
		RetailerUsers retailerUsers = mongoOperations.findOne(retailerUserQuery, RetailerUsers.class);
		if(retailerUsers!=null)
			mongoOperations.remove(retailerUsers);
	}
	
	public List<RetailerUsers> findRetailerUsersByRetailerId(String retailerId){
		logger.debug("RetailerUsersDao -> Inside find Retailer Users By retailerId");
		Query query = new Query(Criteria.where("retailerId").is(retailerId));
		return mongoOperations.find(query,RetailerUsers.class);
	}

	public RetailerUsers findRetailerUserByUserId(String userId){
		logger.debug("RetailerUsersDao -> Inside find Retailer Users By UserId");
		Query query = new Query(Criteria.where("userId").is(userId));
		return mongoOperations.findOne(query,RetailerUsers.class);
	}
	
}
