package com.oomoqu.rest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oomoqu.rest.dao.CoinTransactionDao;
import com.oomoqu.rest.model.CoinTransaction;
import com.oomoqu.rest.util.CommonUtility;

@Service
public class CoinTransactionService {
	
	@Autowired
	CoinTransactionDao coinTransactionDao;
	
	public void transferCoin(String senderWalletId, String recieverWalletId, String transactionType, Integer amount){
		CoinTransaction coinTransaction = new CoinTransaction();
		coinTransaction.setTransactionDate(CommonUtility.getCurrentDate());
		coinTransaction.setRecieverWalletId(recieverWalletId);
		coinTransaction.setSenderWalletId(senderWalletId);
		coinTransaction.setTransactionType(transactionType);
		coinTransaction.setAmount(amount);
		coinTransactionDao.save(coinTransaction);
	}
	
	public List<CoinTransaction> findByRecieverWallet(String recieverWalletId){
		return coinTransactionDao.findByRecieverWalletId(recieverWalletId);
	}
}
