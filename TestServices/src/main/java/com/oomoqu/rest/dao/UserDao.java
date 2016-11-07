package com.oomoqu.rest.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.oomoqu.rest.model.SharedCoupon;
import com.oomoqu.rest.model.User;


@Service
public class UserDao {
	private static Logger logger = Logger.getLogger(UserDao.class);
	
	@Autowired
	MongoOperations mongoOperations;
	
	public void save(User ouser){
		logger.debug("UserDao -> Inside save OomoqueUser");
		mongoOperations.save(ouser);
	};
	
	public void removeUser(User ouser){
		logger.debug("UserDao -> Inside delete OomoqueUser");
		mongoOperations.remove(ouser);
	};
	
	public User deleteUser(String email){
		logger.debug("BrandDao -> Inside delete Brand");
		Query query = new Query(getCriteria().and("email").is(email));
		
		Update update = new Update();
		update.set("isDeleted", true);
		
		return mongoOperations.findAndModify(query, update, User.class);
	}
	
	public User findById(String userId){
		logger.debug("UserDao -> Inside find OomoqueUser By Id");
		return this.findOne(Criteria.where("userId").is(userId));
	};
	
	public boolean validateUser(User user){
		logger.debug("UserDao -> Inside validating a user");
		Criteria criteria;

		if(StringUtils.isEmpty(user.getUserId())){
			criteria = getCriteria().and("email").is(user.getEmail());
		}else{
			criteria = getCriteria().and("email").is(user.getEmail()).and("userId").ne(user.getUserId());
		}
		
		Query searchOUserQuery = new Query(criteria);
		return !mongoOperations.exists(searchOUserQuery, User.class);
	};
	
	public User verifyUser(String userName, String password){
		return this.findOne(getCriteria().and("email").is(userName).and("password").is(password));
	}
	
	public User findByEmailId(String userName){
		return this.findOne(getCriteria().and("email").is(userName));
	}
	
	public User findUserByPortalId(String portalUserId){
		return this.findOne(getCriteria().and("portalUserId").is(portalUserId));
	}
	
	public User findUserByLoginMethod(SharedCoupon sharedCoupon){
		return this.findOne(getCriteria().and("loginType").is(sharedCoupon.getLoginType()).and("loginId").is(sharedCoupon.getLoginId()));
	}
	
	public List<User> findAllUser(){
		return mongoOperations.find(new Query(getCriteria()), User.class);
	}
	
	public Criteria getCriteria(){
		return Criteria.where("isDeleted").ne(true);
	}
	
	public User findOne(Criteria criteria){
		return mongoOperations.findOne(new Query(criteria), User.class);
	}
}
