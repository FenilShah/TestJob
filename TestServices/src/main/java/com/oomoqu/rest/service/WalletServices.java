package com.oomoqu.rest.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.oomoqu.rest.dao.UserDao;
import com.oomoqu.rest.dao.WalletDao;
import com.oomoqu.rest.dao.WalletUserDao;
import com.oomoqu.rest.model.User;
import com.oomoqu.rest.model.Wallet;
import com.oomoqu.rest.model.WalletUser;
import com.oomoqu.rest.util.CommonUtility;

@Component
public class WalletServices {
	private static Logger logger = Logger.getLogger(WalletServices.class);
	
	@Autowired
	WalletDao walletDao;

	@Autowired
	CommonUtility commonUtility;
	
	@Autowired
	WalletUserService walletUserService;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	WalletUserDao walletUserDao;
	
	public Wallet saveWallet(Wallet wallet){
		logger.debug(" WalletServices -> Inside saveOrUpdate Wallet");
		
		if(wallet == null){
			wallet = new Wallet();
		}
		wallet.setEtheriumAddressId("etherstaticaddress1");
			
		wallet.setDeviceId(wallet.getDeviceId());
		wallet.setCreatedDate(CommonUtility.getCurrentDate());
		wallet.setModifiedDate(CommonUtility.getCurrentDate());
		wallet.setLastSyncTime(CommonUtility.getCurrentDate());
		wallet.setLastSyncStatus(true);
		walletDao.saveWallet(wallet);
		return wallet;
		
	}
	
	public String deleteWallet(String walletId){
		logger.debug(" WalletServices -> Inside delete Wallet");
		if(StringUtils.isEmpty(walletId)){
			return commonUtility.invalidField();
		} else {
			Wallet wallet = new Wallet();
			wallet.setWalletId(walletId);
			walletDao.deleteWallet(wallet);
			return commonUtility.removeObject();
		}
	}
	public String findWalletById(String walletId){
		logger.debug(" WalletServices -> find Wallet By Id");
		Wallet wallet = walletDao.findWalletById(walletId);
		return commonUtility.prepareObjectToJson(wallet);
	}
	
	public String findWalletIdByEmailId(String emailId){
		Wallet wallet = this.findWalletByEmailId(emailId);
		if(wallet != null)
			return wallet.getWalletId();
		else
			return null;
	}
	
	public Wallet findWalletByEmailId(String emailId){
		WalletUser walletUser = null;
		User user = userDao.findByEmailId(emailId);
		
		if(user!=null){
			walletUser = walletUserDao.findWalletUserByUserId(user.getUserId());
		}
		if(walletUser != null){
			return walletDao.findWalletById(walletUser.getWalletId());
		} else {
			return null;
		}
	}
	
	public String getWideWalletId(){
	
		Wallet wallet = null;
		WalletUser walletUser = null;
		User user = userDao.findByEmailId("brandregistrar@oomoqu.com");
		
		if(user != null){
			walletUser = walletUserDao.findWalletUserByUserId(user.getUserId());
			
			if(walletUser == null){
				wallet = new Wallet();
				walletDao.saveWallet(wallet);
				
				walletUser = new WalletUser();
				walletUser.setUserId(user.getUserId());
				walletUser.setWalletId(wallet.getWalletId());
				
				walletUserDao.saveWalletUser(walletUser);
				
				return wallet.getWalletId();
			}
			return walletUser.getWalletId();
		}
		return null;
	}
	
}
