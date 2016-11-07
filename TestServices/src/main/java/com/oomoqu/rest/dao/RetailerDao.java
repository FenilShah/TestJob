package com.oomoqu.rest.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.oomoqu.rest.model.Retailer;

@Service
public class RetailerDao {
	private static Logger logger = Logger.getLogger(RetailerDao.class);
	
	private static final String NAME = "name";
	
	@Autowired
	MongoOperations mongoOperations;
	
	public void save(Retailer retailer){
		logger.debug("RetailerDao -> Inside Save Retailer");
		Retailer existRetailer = findByPortalRetailerId(retailer.getPortalRetailerId());
		if(existRetailer != null){
			retailer.setRetailerId(existRetailer.getRetailerId());
		}
		mongoOperations.save(retailer);
	}
	
	public Retailer findByPortalRetailerId(String portalRetailerId){
		logger.debug("RetailerDao -> Inside Find Retailer by portalRetailerId ");
		Query query = new Query(Criteria.where("portalRetailerId").is(portalRetailerId));
		Retailer retailer =	mongoOperations.findOne(query, Retailer.class);
		return retailer;
	}

	public List<Retailer> findAllRetailer(){
		logger.debug("RetailerDao -> Inside find all retailers ");
		return mongoOperations.find(new Query(getCriteria()), Retailer.class);
	}
	
	public Retailer findRetailerNameById(Retailer retailer){
		logger.debug("RetailerDao -> Inside find Retailer Name by Id");
		Query validateRetailer;
		if(retailer.getPortalRetailerId() != null)
			validateRetailer = new Query(Criteria.where(NAME).is(retailer.getName()).and("portalRetailerId").ne(retailer.getPortalRetailerId()));
		else
			validateRetailer = new Query(Criteria.where(NAME).is(retailer.getName()));
		return mongoOperations.findOne(validateRetailer, Retailer.class);
	}
	
	/*public void remove(String portalRetailerId){
		logger.debug("RetailerDao -> Inside remove Retailer");
		Retailer removeRetailer = findByPortalRetailerId(portalRetailerId);
		mongoOperations.remove(removeRetailer);
	}*/
	
	public Retailer remove(String retailerName){
		logger.debug("RetailerDao -> Inside remove Retailer");
		Query query = new Query(getCriteria().and(NAME).is(retailerName));
		
		Update update = new Update();
		update.set("isDeleted", true);
		
		return mongoOperations.findAndModify(query, update, Retailer.class);
		
	}
	
	public Retailer findByName(String retailerName){
		logger.debug("RetailerDao -> Inside find By name " + retailerName );
		return mongoOperations.findOne(new Query(getCriteria().and(NAME).is(retailerName)), Retailer.class) ;
	}
	
	public Retailer findById(String retailerId){
		logger.debug("RetailerDao -> Inside find By id " + retailerId );
		return mongoOperations.findOne(new Query(getCriteria().and("retailerId").is(retailerId)), Retailer.class) ;
	}
	
	private Criteria getCriteria(){
		return Criteria.where("isDeleted").ne(true);
	}
	
}
