package com.oomoqu.rest.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.oomoqu.rest.model.UserRoles;

@Service
public class UserRolesDao {
	private static Logger logger = Logger.getLogger(UserRolesDao.class);
	
	@Autowired
	MongoOperations mongoOperations;
	
	public void saveUserRoles(List<UserRoles> userRoles){
		logger.debug("UserRolesDao -> Inside save UserRoles");
		mongoOperations.insertAll(userRoles);
	}
	
	public List<UserRoles> findUserRoles(String userId){
		logger.debug("UserRolesDao -> Inside save find UserRoles");
		Query searchOUserQuery = new Query(Criteria.where("userId").is(userId));
		return mongoOperations.find(searchOUserQuery, UserRoles.class);
	}
	
	public UserRoles findUserByRole(String userId, String roleId){
		logger.debug("UserRolesDao -> Inside save find UserRoles");
		Query searchOUserQuery = new Query(Criteria.where("userId").is(userId).and("roleId").is(roleId));
		return mongoOperations.findOne(searchOUserQuery, UserRoles.class);
	}
}
