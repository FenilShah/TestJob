package com.oomoqu.rest.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oomoqu.rest.dao.WalletUserDao;
import com.oomoqu.rest.exception.CustomException;
import com.oomoqu.rest.model.User;
import com.oomoqu.rest.model.WalletUser;
import com.oomoqu.rest.util.CommonUtility;


@Component
public class WalletUserService {
	private static Logger logger = Logger.getLogger(WalletUserService.class);
	
	@Autowired
	WalletUserDao walletUserDao;
	
	public void saveWalletUser(String userId,String walletId){
		logger.debug("WalletUserService -> Inside save Wallet User");
		WalletUser walletUser = new WalletUser();
		walletUser.setUserId(userId);
		walletUser.setWalletId(walletId);
		walletUserDao.saveWalletUser(walletUser);
	}
	
	public WalletUser findLoggedInWalletUser(){
		logger.debug("WalletUserService -> find Logged In Wallet User");
		User user = CommonUtility.getUser();
		return walletUserDao.findWalletUserByUserId(user.getUserId());
	}
	
	public String findWalletIdByUser(String userId) throws CustomException{
		WalletUser walletUser = walletUserDao.findWalletUserByUserId(userId);
		if(walletUser == null){
			throw new CustomException("User does not have a wallet.");
		}
		return walletUser.getWalletId();
	}
	
}
