package com.oomoqu.rest.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oomoqu.rest.dao.ScannedPaperCouponDao;
import com.oomoqu.rest.model.ScannedPaperCoupon;

@Component
public class ScannedPaperCouponService {
	private static Logger logger = Logger.getLogger(ScannedPaperCouponService.class);
	
	@Autowired
	ScannedPaperCouponDao invalidPaperCouponDao;
	
	public void ScannedPaperCoupon(ScannedPaperCoupon invalidPaperCoupon){
		logger.debug("InvalidPaperCouponService -> Inside save Invalid Paper coupon");
		invalidPaperCouponDao.ScannedPaperCoupon(invalidPaperCoupon);
	}
}
