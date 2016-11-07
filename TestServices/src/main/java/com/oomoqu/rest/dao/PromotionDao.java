package com.oomoqu.rest.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.oomoqu.rest.model.Promotion;
import com.oomoqu.rest.util.CommonUtility;
import com.oomoqu.rest.wrapper.portal.PromotionDetail;


@Service
public class PromotionDao {
	private static Logger logger = Logger.getLogger(PromotionDao.class);
	
	private static final String NAME = "name";
	private static final String STATUS = "status";
	
	@Autowired
	MongoOperations mongoOperations;
	
	public void savePromotion(Promotion promotion){
		logger.debug("PromotionDao -> Inside save Promotion");
		mongoOperations.save(promotion);
	}
	
	public void remove(Promotion promotion){
		logger.debug("PromotionDao -> Inside delete Promotion");
		mongoOperations.remove(promotion);
	}
	
	public Promotion delete(String brandId, String promotionName){
		logger.debug("BrandDao -> Inside delete Brand");
		Query query = new Query(getCriteria().and("name").is(promotionName).and("brandId").is(brandId));
		
		Update update = new Update();
		update.set("isDeleted", true);
		
		return mongoOperations.findAndModify(query, update, Promotion.class);
	}
	
	public boolean validatePromotion(Promotion promotion){
		logger.debug("PromotionDao -> Inside validate Promotion");
		Query searchPromotionQuery;
		if(StringUtils.isEmpty(promotion.getPromotionId())){
			searchPromotionQuery = new Query(getCriteria().and(NAME).is(promotion.getName()).and("brandId").is(promotion.getBrandId()));
		} else {
			searchPromotionQuery = new Query(getCriteria().and(NAME).is(promotion.getName()).and("brandId").is(promotion.getBrandId()).and("promotionId").ne(promotion.getPromotionId()));	
		}
		return !mongoOperations.exists(searchPromotionQuery, Promotion.class);
	}
	
	public Promotion findPromotioanBypromotionId(String promotionId){
		logger.debug("PromotionDao -> Inside find Promotioan By promotionId");
		return mongoOperations.findOne(new Query(Criteria.where("promotionId").is(promotionId)), Promotion.class);
	}
	
	public Promotion findByBrandAndPromotionCoin(String brandId){
		logger.debug("PromotionDao -> Inside find Promotioan By brandId");
		return mongoOperations.findOne(new Query(Criteria.where("brandId").is(brandId).and("promotionCoinId").exists(true)), Promotion.class);
	}
	
	public PromotionDetail findDetailsByBrandAndPromotionCoin(String brandId){
		logger.debug("PromotionDao -> Inside find Promotioan By brandId");
		return mongoOperations.findOne(new Query(Criteria.where("brandId").is(brandId).and("promotionCoinId").exists(true)), PromotionDetail.class);
	}
	
	public Promotion findByPromotionCoinId(String promotionCoinId){
		logger.debug("PromotionDao -> Inside find Promotioan By brandId");
		return mongoOperations.findOne(new Query(Criteria.where("promotionCoinId").is(promotionCoinId)), Promotion.class);
	}
	
	public Promotion findPromotionByNameAndBrand(String brandId, String promotionName){
		logger.debug("PromotionDao -> Inside find Promotioan By promotion name and brand");
		Query query = new Query(getCriteria().and("brandId").is(brandId).and("name").is(promotionName));
		return mongoOperations.findOne(query, Promotion.class);
	}
	
	public List<Promotion> getPromotionListByBrandId(String brandId, Integer start, Integer limit){
		logger.debug("PromotionDao -> Inside get Promotion List By BrandId " + brandId );
		Query searchQueryByBrandId = getPromotionListQuery(brandId);
		if(start != null) {
			searchQueryByBrandId.skip(start);
		}
		if(limit != null) {
			searchQueryByBrandId.limit(limit);
		}
		searchQueryByBrandId.with(new Sort(Sort.Direction.DESC, "modifiedDate"));
		List<Promotion> promotionList = mongoOperations.find(searchQueryByBrandId, Promotion.class);
		return promotionList;
	}
	
	public int numberOfCount(String brandId){
		logger.debug("PromotionDao -> Inside numberOfCount");
		Query searchBrandByBrandId = getPromotionListQuery(brandId);
		List<Promotion> promotionList = mongoOperations.find(searchBrandByBrandId, Promotion.class);
		return promotionList.size();
	}
	
	public Query getPromotionListQuery(String brandId){
		logger.debug("PromotionDao -> Inside get Promotion List Query");
		if(CommonUtility.findExactRole("WalletUser") || CommonUtility.findExactRole("Registration")){
			return new Query(getCriteria().and("brandId").is(brandId)
					.and("availableCoupons").ne(0)
					.and(STATUS).is("Active")
					.and("promotionCoinId").exists(false));
		}else{
			if(CommonUtility.findExactRole("BrandAdmin") || CommonUtility.findExactRole("Admin")){
				return new Query(getCriteria().and("brandId").is(brandId).and("promotionCoinId").exists(false));
			} else if (CommonUtility.findExactRole("AccountManager")) {
				return new Query(getCriteria().and("brandId").is(brandId));
			} else {
				return new Query(getCriteria().and("brandId").is(brandId).and("ownerId").is(CommonUtility.getUser().getUserId()).and("promotionCoinId").exists(false));
			}
		}
	}
	
	public Promotion updatePromotion(Promotion promotion){
		logger.debug("PromotionDao -> Inside update Promotion");
		
		Promotion promotionNew  = this.findPromotioanBypromotionId(promotion.getPromotionId());
		
		promotionNew.setApproved(promotion.getApproved());
		if(promotion.getApproved()){
			promotionNew.setStatus("Approved");
		}else{
			promotionNew.setStatus("Rejected");
		}
		
		this.savePromotion(promotionNew);
		
		return promotionNew;
	}
	
	public List<Promotion> findAllPromotions(){
		logger.debug("PromotionDao -> Inside find All Promotions");
		return mongoOperations.findAll(Promotion.class);
	}
	
	public void insertAll(List<Promotion> promotions){
		logger.debug("PromotionDao -> Inside insertAll");
		mongoOperations.insertAll(promotions);
	}
	
	public PromotionDetail findPromotionByPromotionPortalId(String promotionId){
		logger.debug("PromotionDao -> Inside find Promotion By PromotionPortalId -- " + promotionId);
		Query query = new Query();
		query.addCriteria(getCriteria().and("promotionId").is(promotionId));
		return mongoOperations.findOne(query, PromotionDetail.class);
	}
	
	public List<PromotionDetail> findPromotionReportsByBrandId(String brandId){
		logger.debug("PromotionDao -> Inside find Promotion Reports By BrandId");
		Query query = getPromotionListQuery(brandId);		
		return mongoOperations.find(query, PromotionDetail.class);
	}
	
	public Promotion findByPortalPromotionId(String portalPromotionId){
		logger.debug("PromotionDao -> Inside find findByPortalPromotionId -- " + portalPromotionId);
		Query query = new Query();
		query.addCriteria(getCriteria().and("portalPromotionId").is(portalPromotionId));
		return mongoOperations.findOne(query, Promotion.class);
	}
	
	public Promotion findByNumber(String promotionNumber){
		logger.debug("PromotionDao -> Inside find By Promotion No -- " + promotionNumber );
		Query query = new Query();
		query.addCriteria(Criteria.where("promotionNo").is(promotionNumber));
		return mongoOperations.findOne(query, Promotion.class);
	}
	
	private Criteria getCriteria(){
		return Criteria.where("isDeleted").ne(true);
	}
}
