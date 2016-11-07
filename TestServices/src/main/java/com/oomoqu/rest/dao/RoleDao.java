package com.oomoqu.rest.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.oomoqu.rest.model.Role;

@Service
public class RoleDao {
	private static Logger logger = Logger.getLogger(RoleDao.class);
	
	public static final String NAME = "name";
	
	@Autowired
	MongoOperations mongoOperations;
	
	public Role findById(String id){
		logger.debug("RoleDao -> Inside find By Id method");
		Query searchOUserQuery = new Query(Criteria.where("_id").is(id));
		return mongoOperations.findOne(searchOUserQuery, Role.class);
		
	}
	
	public Role findRoleByName(String name){
		logger.debug("RoleDao -> Inside find Role By Name");
		Query searchOUserQuery = new Query(Criteria.where(NAME).is(name));
		return mongoOperations.findOne(searchOUserQuery, Role.class);
		
	}
	
	public void saveRole(Role role){
		mongoOperations.save(role);
	}
}
