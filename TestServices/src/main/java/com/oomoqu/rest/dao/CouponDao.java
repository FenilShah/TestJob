package com.oomoqu.rest.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.oomoqu.rest.model.Coupon;
import com.oomoqu.rest.util.CommonUtility;

@Service
public class CouponDao {
	private static Logger logger = Logger.getLogger(CouponDao.class);
	
	@Autowired
	MongoOperations mongoOperations;
	
	public void createCoupon(Coupon coupon){
		logger.debug("CouponDao -> Inside create Coupon");
		mongoOperations.save(coupon);
	}
	
	public void createBulkCoupon(List<Coupon> coupons){
		logger.debug("CouponDao -> Inside create BulkCoupon");
		mongoOperations.insertAll(coupons);
	}
	
	public Coupon findById(String id){
		logger.debug("CouponDao -> Inside find By Id");
		Criteria criteria = Criteria.where("couponId").is(id);
		Query searchOUserQuery = new Query(criteria);
		return mongoOperations.findOne(searchOUserQuery, Coupon.class);
	}
	
	public void updateCouponStatus(List<String> couponIds){
		logger.debug("CouponDao -> Inside update CouponStatus");
		Query query = new Query();
		query.addCriteria(Criteria.where("couponId").in(couponIds));
		Update update = new Update();
		update.set("reedemedDate", CommonUtility.getCurrentDate());
		mongoOperations.updateMulti(query, update, Coupon.class);
	}
	
	public long getReedemedCouponAmountByPromotionId(String promotionId){
		logger.debug("CouponDao -> Inside get Reedemed Coupon Amount By PromotionId");
		Query query = new Query();
		query.addCriteria(Criteria.where("promotionId").is(promotionId)
				.and("reedemedDate").exists(true));
		return mongoOperations.count(query, Coupon.class);
	}
	
	public List<Coupon> findCouponsByPromotionId(String promotionId){
		logger.debug("CouponDao -> Inside find Coupons By PromotionId");
		Query query = new Query();
		query.addCriteria(Criteria.where("promotionId").is(promotionId));
		return mongoOperations.find(query, Coupon.class);
	}
	
	public Coupon findBySerialNumber(String serialNumber){
		Query query = new Query(Criteria.where("serialNo").is(serialNumber));
		return mongoOperations.findOne(query, Coupon.class);
	}
	
	public List<Coupon> findReedemedCoupons(String walletId, Date lastSyncDate){
		logger.debug("CouponDao -> Inside find Reedemed Coupons");
		Query query = new Query();
		query.addCriteria(getCouponCriteria().and("walletId").is(walletId)
				.and("reedemedDate").gt(lastSyncDate));
		return mongoOperations.find(query, Coupon.class);
	}
	
	public List<Coupon> findIssuedCoupons(String walletId){
		logger.debug("CouponDao -> Inside find Issued Coupons " + walletId);
		Query query = new Query();
		Criteria criteria = findIssuedCouponsCriteria(walletId);
		
		query.addCriteria(criteria);
		return mongoOperations.find(query, Coupon.class);
	}
	
	public List<Coupon> findCouponsByWalletWithoutPaymentRequestedDate(String walletId){
		return this.find(Criteria.where("walletId").is(walletId).and("paymentRequestedDate").exists(false));
	}
	
	public List<Coupon> findCouponsByWalletWithoutUsedDate(String walletId){
		return this.find(getCouponCriteria().and("walletId").is(walletId).and("usedDate").exists(false));
	}
	
	public List<Coupon> findCouponsByWalletWithUsedDate(String walletId){
		return this.find(getCouponCriteria().and("walletId").is(walletId).and("usedDate").exists(true));
	}
	
	public List<Coupon> findCouponsByWallets(List<String> walletIds){
		return this.find(getCouponCriteria().and("walletId").in(walletIds));
	}
	
	public Coupon findCouponByWalletAndPromotion(String walletId, String promotionId){
		return this.findOne(getCouponCriteria().and("walletId").is(walletId).and("promotionId").is(promotionId));
	}
	
	public Coupon findAvailableCoupon(String promotionId){
		return this.findOne(getCouponCriteria().and("promotionId").is(promotionId).and("issuedDate").exists(false));
	}
	
	public List<Coupon> findIssuedCoupons(String walletId, List<String> promotionIds){
		logger.debug("CouponDao -> Inside find Issued Coupons");
		Query query = new Query();
		Criteria criteria = findIssuedCouponsCriteria(walletId);
		
		criteria.and("promotionId").in(promotionIds);
		
		query.addCriteria(criteria);
		return mongoOperations.find(query, Coupon.class);
	}
	
	public Criteria findIssuedCouponsCriteria(String walletId){
		logger.debug("CouponDao -> Inside find Issued Coupons Criteria");
		return getCouponCriteria().and("walletId").is(walletId)
				.and("reedemedDate").exists(false)
				.and("usedDate").exists(true);
	}
	
	public List<Coupon> findCouponsByCouponIds(List<String> couponIds){
		logger.debug("CouponDao -> Inside find Coupons By CouponIds");
		Query query = new Query();
		query.addCriteria(Criteria.where("couponId").in(couponIds));
		return mongoOperations.find(query, Coupon.class);
	}
	
	public void updateCouponPaymentStatus(List<String> couponIds){
		logger.debug("CouponDao -> Inside update CouponPayment Status");
		Query query = new Query();
		query.addCriteria(Criteria.where("couponId").in(couponIds));
		Update update = new Update();
		update.set("paymentRequestedDate", CommonUtility.getCurrentDate());
		mongoOperations.updateMulti(query, update, Coupon.class);
	}
	
	public Coupon findIssuedCoupon(String walletId, String promotionId){
		logger.debug("CouponDao -> Inside find Issued coupon to Wallet");
		Query query = new Query();
		query.addCriteria(getCouponCriteria().and("walletId").is(walletId).and("promotionId").is(promotionId).and("usedDate").exists(false));
		return mongoOperations.findOne(query, Coupon.class);
	}
	
	private List<Coupon> find(Criteria criteria){
		return mongoOperations.find(new Query(criteria), Coupon.class);
	}
	
	private Coupon findOne(Criteria criteria){
		return mongoOperations.findOne(new Query(criteria), Coupon.class);
	}
	
	private Criteria getCouponCriteria(){
		return Criteria.where("type").ne("coin");
	} 
}
