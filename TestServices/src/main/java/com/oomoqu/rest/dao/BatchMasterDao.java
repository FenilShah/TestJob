package com.oomoqu.rest.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.oomoqu.rest.model.BatchMaster;

@Service
public class BatchMasterDao {
	@Autowired
	MongoOperations mongoOperations;
	
	public void save(BatchMaster batchMaster){
		mongoOperations.save(batchMaster);
	}
	
	public BatchMaster findById(String batchId){
		return mongoOperations.findOne(new Query(Criteria.where("batchId").is(batchId)), BatchMaster.class);
	}
	
	public List<BatchMaster> findByLocationId(String locationId){
		return mongoOperations.find(new Query(Criteria.where("locationId").is(locationId)), BatchMaster.class);
	}
	
	public BatchMaster findByBatchNo(Long batchNo){
		return mongoOperations.findOne(new Query(Criteria.where("batchNo").is(batchNo)), BatchMaster.class);
	}
	
	public List<BatchMaster> findByPromotionId(String promotionId){
		return mongoOperations.find(new Query(Criteria.where("promotionId").is(promotionId)), BatchMaster.class);
	}
	
	public List<BatchMaster> findByPromotionAndStatus(String promotionId, boolean status){
		Criteria criteria = Criteria.where("promotionId").is(promotionId);
		if(status){
			criteria.and("status").is("Pending");
		}else{
			criteria.and("status").ne("Pending");
		}
		return mongoOperations.find(new Query(criteria), BatchMaster.class);
	}
}
