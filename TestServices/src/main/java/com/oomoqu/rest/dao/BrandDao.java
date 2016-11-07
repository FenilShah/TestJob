package com.oomoqu.rest.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.oomoqu.rest.model.Brand;
import com.oomoqu.rest.model.Promotion;
import com.oomoqu.rest.wrapper.portal.BrandDetail;

@Service
public class BrandDao {
	private static Logger logger = Logger.getLogger(BrandDao.class);
	
	
	private static final String NAME = "name";
	
	@Autowired
	MongoOperations mongoOperations;
	
	
	public void saveBrand(Brand brand){
		logger.debug("BrandDao -> Inside Save brand");
		mongoOperations.save(brand);
	}
	
	public Boolean validateBrand(Brand brand){
		logger.debug("BrandDao -> Inside find Brand Name By Id");
		Criteria criteria = getCriteria().and(NAME).is(brand.getName());
		if(brand.getBrandId() != null){
			  criteria.and("brandId").ne(brand.getBrandId());
		}
		return !mongoOperations.exists(new Query(criteria), Brand.class);
	}
	
	public Brand findById(String brandId){
		return mongoOperations.findOne(new Query(getCriteria().and("brandId").is(brandId)), Brand.class);
	}
	
	public Brand findByName(String brandName){
		logger.debug("BrandDao -> Inside find By name " + brandName);
		return mongoOperations.findOne(new Query(getCriteria().and(NAME).is(brandName)), Brand.class);
	}
	
	public List<Brand> findAllBrand(){
		logger.debug("BrandDao -> Inside find all brands ");
		return mongoOperations.find(new Query(getCriteria()), Brand.class);
	}
	
	public List<Brand> getBrandList(){
		logger.debug("BrandDao -> Inside get BrandList");
		List<Brand> brandList = (List<Brand>) mongoOperations.findAll(Brand.class);
		return brandList;
	}
	
	public Brand findByPortalBrandId(String portalBrandId){
		logger.debug("BrandDao -> Inside find By PortalBrandId " + portalBrandId);
		Query searchBrandQuery = new Query(Criteria.where("portalBrandId").is(portalBrandId));
		Brand brand = mongoOperations.findOne(searchBrandQuery, Brand.class);
		return brand;
	}
	
	public List<Brand> getBrandListWithActivePromotions(){
		logger.debug("BrandDao -> Inside getBrandList With ActivePromotions");
		Query query = getActivePromotionBrands();
		List<Brand> brandList = mongoOperations.find(query, Brand.class);
		return brandList;
	}
	
	public Query getActivePromotionBrands(){
		logger.debug("BrandDao -> Inside get ActivePromotion Brands");
		Query query = new Query(Criteria.where("status").is("Active"));
		query.fields().include("brandId");
		List<Promotion> brandList = mongoOperations.find(query, Promotion.class);
		
		List<String> brandIds = new ArrayList<String>();
		for(Promotion promotion : brandList){
			brandIds.add(promotion.getBrandId());
		}
		
		return new Query(Criteria.where("brandId").in(brandIds));
		
	}
	
	public Brand deleteBrand(String brandName){
		logger.debug("BrandDao -> Inside delete Brand");
		Query query = new Query(Criteria.where("name").is(brandName).and("isDeleted").ne(true));
		
		Update update = new Update();
		update.set("isDeleted", true);
		
		return mongoOperations.findAndModify(query, update, Brand.class);
	}
	
	public BrandDetail findBrandBybrandId(String brandId){
		return mongoOperations.findOne(new Query(Criteria.where("brandId").is(brandId)), BrandDetail.class);
	}
	
	public List<BrandDetail> findByIds(List<String> portalBrandIds){
		Query query = new Query(Criteria.where("portalBrandId").in(portalBrandIds));
		List<BrandDetail> brandList = mongoOperations.find(query, BrandDetail.class);
		return brandList;
	}
	
	public List<Brand> findBrandByIds(List<String> brandIds){
		Query query = new Query(Criteria.where("brandId").in(brandIds));
		List<Brand> brandList = mongoOperations.find(query, Brand.class);
		return brandList;
	}
	
	private Criteria getCriteria(){
		return Criteria.where("isDeleted").ne(true);
	}
	
}
