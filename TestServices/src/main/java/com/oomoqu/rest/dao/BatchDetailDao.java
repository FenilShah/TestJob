package com.oomoqu.rest.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.oomoqu.rest.model.BatchDetail;

@Service
public class BatchDetailDao {
	@Autowired
	MongoOperations mongoOperations;
	
	public void save(BatchDetail batchDetail){
		mongoOperations.save(batchDetail);
	}
	
	public List<BatchDetail> findByBatchId(String batchId){
		return mongoOperations.find(new Query(Criteria.where("batchId").is(batchId)), BatchDetail.class);
	}
}
