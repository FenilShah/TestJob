package com.oomoqu.rest.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.oomoqu.rest.model.SharedCoupon;
import com.oomoqu.rest.model.User;

@Service
public class SharedCouponDao {
	private static Logger logger = Logger.getLogger(SharedCouponDao.class);
	
	@Autowired
	MongoOperations mongoOperations;
	
	public void save(SharedCoupon sharedCoupon) throws Exception{
		logger.debug("SharedCouponDao -> Inside save");
		mongoOperations.save(sharedCoupon);
	}
	
	public List<SharedCoupon> findSharedCouponsByUser(User user){
		logger.debug("SharedCouponDao -> Inside find SharedCoupons By User");
		Criteria criteria = Criteria.where("loginType").is(user.getLoginType()).and("loginId").is(user.getLoginId());
		Query searchOUserQuery = new Query(criteria);
		return mongoOperations.find(searchOUserQuery, SharedCoupon.class);
	}
}
