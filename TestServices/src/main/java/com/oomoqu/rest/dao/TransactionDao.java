package com.oomoqu.rest.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.oomoqu.rest.model.Transaction;

@Service
public class TransactionDao {
	private static Logger logger = Logger.getLogger(TransactionDao.class);
	@Autowired
	MongoOperations mongoOperations;
	
	public void saveAllTransaction(List<Transaction> transactions){
		logger.debug("TransactionDao -> Inside save AllTransaction");
		mongoOperations.insertAll(transactions);
	}
	
	public void save(Transaction transaction){
		logger.debug("TransactionDao -> Inside save Transaction");
		mongoOperations.save(transaction);
	}
	public List<Transaction> findTransactionsByLocationId(String locationId){
		logger.debug("TransactionDao -> Inside find Transactions By LocationId");
		Query searchTransactions = new Query(Criteria.where("locationId").is(locationId));
		return mongoOperations.find(searchTransactions, Transaction.class);
	}
	public List<Transaction> findTransactionsByLocationIds(List<String> locationIds){
		logger.debug("TransactionDao -> Inside find Transactions By LocationIds");
		Query query = new Query();
		query.addCriteria(Criteria.where("locationId").in(locationIds));
		return mongoOperations.find(query, Transaction.class);
	}
	
	public List<Transaction> findTransactionByWalletAndType(String walletId, String transactionType){
		logger.debug("TransactionDao -> Inside findTransactionByWalletAndType");
		Query query = new Query();
		query.addCriteria(Criteria.where("senderWalletId").is(walletId).and("transactionType").is(transactionType));
		return mongoOperations.find(query, Transaction.class);
	}
}
