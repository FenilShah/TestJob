package com.oomoqu.rest.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.oomoqu.rest.model.BrandUsers;

@Service
public class BrandUsersDao {
	private static Logger logger = Logger.getLogger(BrandUsersDao.class);
	
	@Autowired
	MongoOperations mongoOperations;
	
	public void saveBrandUsers(BrandUsers brandUsers){
		logger.debug("BrandUsersDao -> Inside Save Brand Users");
		Query searchBrandQuery = new Query(Criteria.where("brandId").is(brandUsers.getBrandId()).and("userId").is(brandUsers.getUserId()));
		BrandUsers brandUsersFound = mongoOperations.findOne(searchBrandQuery, BrandUsers.class);
		if(brandUsersFound == null){
			mongoOperations.save(brandUsers);
		}	
	}
	
	public BrandUsers findBrandUsersByUserId(String userId){
		logger.debug("BrandUsersDao -> Inside find Brand Users By UserId");
		Query query = new Query(Criteria.where("userId").is(userId));
		return mongoOperations.findOne(query, BrandUsers.class);
	}
	
	public void deleteBrandUser(String userId){
		logger.debug("BrandUsersDao -> Inside delete BrandUser");
		Query brandUserQuery = new Query(Criteria.where("userId").is(userId));
		BrandUsers removeBrandUsers = mongoOperations.findOne(brandUserQuery, BrandUsers.class);
		if(removeBrandUsers != null)
			mongoOperations.remove(removeBrandUsers);
	
	}
	
	public List<BrandUsers> findBrandUsersByBrandId(String brandId){
		logger.debug("BrandUsersDao -> Inside find Brand Users By UserId");
		Query query = new Query(Criteria.where("brandId").is(brandId));
		return mongoOperations.find(query, BrandUsers.class);
	}
}
