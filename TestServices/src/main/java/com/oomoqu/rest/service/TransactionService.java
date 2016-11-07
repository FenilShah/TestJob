package com.oomoqu.rest.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oomoqu.rest.dao.AddressDao;
import com.oomoqu.rest.dao.CouponDao;
import com.oomoqu.rest.dao.TransactionDao;
import com.oomoqu.rest.exception.CustomException;
import com.oomoqu.rest.model.Address;
import com.oomoqu.rest.model.Coupon;
import com.oomoqu.rest.model.Transaction;
import com.oomoqu.rest.util.CommonUtility;
import com.oomoqu.rest.wrapper.TransactionDetail;

@Component
public class TransactionService {
	private static Logger logger = Logger.getLogger(TransactionService.class);
	
	@Autowired
	TransactionDao transactionDao;
	
	@Autowired
	CouponDao couponDao;
	
	@Autowired
	AddressDao addressDao;
	
	@Autowired
	LocationService locationService;
	
	@Autowired
	WalletServices walletServices;
	
	@Autowired
	LocationUserService locationUserService;
	
	public void saveCouponTransaction(String emailId, List<TransactionDetail> transacationDetails) throws CustomException{
		logger.info(" TransactionService -> Inside save Transaction");
		
		for(TransactionDetail transactionDetail : transacationDetails){
			
			Transaction transaction = transactionDetail.getTransaction();
			
			Address address = transactionDetail.getAddress();
			
			Coupon coupon = couponDao.findById(transaction.getCouponId());
			
			if(coupon == null){
				throw new CustomException("Coupon not found.");
			}
				
			addressDao.save(address);

			
			coupon.setAddressId(address.getAddressId());
			coupon.setUsedDate(transaction.getTransactionDate());
			couponDao.createCoupon(coupon);
			
		}
		
	}
	
	public void transferCoupon(String couponId, String senderWalletId, String recieverWalletId, String transactionType){
		Transaction transaction = new Transaction();
		transaction.setCouponId(couponId);
		transaction.setRecieverWalletId(recieverWalletId);
		transaction.setSenderWalletId(senderWalletId);
		transaction.setTransactionDate(CommonUtility.getCurrentDate());
		transaction.setTransactionType(transactionType);
		transactionDao.save(transaction);
	}
	
	public List<Transaction> findTransactionByWalletAndType(String walletId, String transactionType){
		return transactionDao.findTransactionByWalletAndType(walletId, transactionType);
	}
}
