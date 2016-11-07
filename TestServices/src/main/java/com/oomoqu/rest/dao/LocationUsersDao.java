package com.oomoqu.rest.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import com.oomoqu.rest.model.LocationUsers;

@Service
public class LocationUsersDao {
	private static Logger logger = Logger.getLogger(LocationUsersDao.class);
	
	@Autowired
	MongoOperations mongoOperations;
	
	public void saveLocationUsers(LocationUsers locationUsers){
		logger.debug("LocationUsersDao -> Inside save LocationUsers");
		Query searchLocationQuery = new Query(Criteria.where("locationId").is(locationUsers.getLocationId()).and("userId").is(locationUsers.getUserId()));
		LocationUsers findLocationUsers = mongoOperations.findOne(searchLocationQuery, LocationUsers.class);
		if(findLocationUsers == null){
			mongoOperations.save(locationUsers);
		}	
	}
	
	public void deleteLocationUser(String userId){
		logger.debug("LocationUsersDao -> Inside delete LocationUsers");
		Query locationUserQuery = new Query(Criteria.where("userId").is(userId));
		LocationUsers removeLocationUsers = mongoOperations.findOne(locationUserQuery, LocationUsers.class);
		if(removeLocationUsers != null)
			mongoOperations.remove(removeLocationUsers);
	}
	
	public LocationUsers findLocationUsersByUserId(String userId){
		logger.debug("LocationUsersDao -> Inside find Location Users By UserId");
		Query query = new Query(Criteria.where("userId").is(userId));
		return mongoOperations.findOne(query, LocationUsers.class);
	}
	
	public List<LocationUsers> findLocationUsersByLocationId(String locationId){
		logger.debug("LocationUsersDao -> Inside find location user by locationId");
		Query query = new Query(Criteria.where("locationId").is(locationId));
		return mongoOperations.find(query, LocationUsers.class);
	}
	
}
