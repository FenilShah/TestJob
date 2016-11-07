package com.oomoqu.rest.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.oomoqu.rest.model.Sequence;

@Service
public class SequenceDao {
	private static Logger logger = Logger.getLogger(SequenceDao.class);
	
	@Autowired
	MongoOperations mongoOperations;
	
	public Long getNextSequence(String item){
		logger.info("Inside get sequence no for item " + item);
		
		Query query = new Query(Criteria.where("item").is(item));
		Sequence sequence = mongoOperations.findOne(query, Sequence.class);
		
		Long result;
		if(sequence == null){
			sequence = new Sequence();
			sequence.setItem(item);
			if("PROMO".equals(item)){
				result = 10000L;
				sequence.setCounter(result);
			}else{
				result = 1000000000L;
				sequence.setCounter(result);
			}
		}else{
			result = sequence.getCounter()+1;
			sequence.setCounter(result);
		}
		
		mongoOperations.save(sequence);
		
		return result;
	}
}
