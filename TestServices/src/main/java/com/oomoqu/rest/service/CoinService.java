package com.oomoqu.rest.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.oomoqu.rest.dao.BrandDao;
import com.oomoqu.rest.dao.CoinDao;
import com.oomoqu.rest.dao.PromotionCoinDao;
import com.oomoqu.rest.dao.PromotionDao;
import com.oomoqu.rest.dao.RoleDao;
import com.oomoqu.rest.dao.UserDao;
import com.oomoqu.rest.dao.UserRolesDao;
import com.oomoqu.rest.dao.WalletDao;
import com.oomoqu.rest.dao.WalletUserDao;
import com.oomoqu.rest.exception.CustomException;
import com.oomoqu.rest.model.Brand;
import com.oomoqu.rest.model.Coin;
import com.oomoqu.rest.model.Coupon;
import com.oomoqu.rest.model.Promotion;
import com.oomoqu.rest.model.PromotionCoin;
import com.oomoqu.rest.model.Response;
import com.oomoqu.rest.model.ResponseStatusCode;
import com.oomoqu.rest.model.Role;
import com.oomoqu.rest.model.User;
import com.oomoqu.rest.model.UserRoles;
import com.oomoqu.rest.model.Wallet;
import com.oomoqu.rest.model.WalletUser;
import com.oomoqu.rest.util.CommonUtility;
import com.oomoqu.rest.util.Properties;
import com.oomoqu.rest.wrapper.mobile.CoinDetail;

@Service
public class CoinService {
	private static Logger logger = Logger.getLogger(CoinService.class);
	
	@Autowired
	private CoinDao coinDao;

	@Autowired
	private BrandService brandService;
	
	@Autowired
	private PromotionCoinDao promotionCoinDao;
	
	@Autowired
	private BrandUserServices brandUserServices;
	
	@Autowired
	private CoinTransactionService coinTransactionService;
	
	@Autowired
	private WalletServices walletServices;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private UserRolesDao userRolesDao;
	
	@Autowired
	private WalletUserDao walletUserDao;
	
	@Autowired
	private WalletDao walletDao;
	
	@Autowired
	private BrandDao brandDao;
	
	@Autowired
	private PromotionDao promotionDao;
	
	@Autowired
	private PromotionCoinService promotionCoinService;
	
	public Response addBalance(String brandName, Integer amount) throws CustomException{
		logger.info("Coin service --> addBalance");
		
		String brandId = brandService.getBrandIdByBrandName(brandName);
		if(StringUtils.isEmpty(brandId)){
			throw new CustomException("Brand does not exist.");
		}
		
		PromotionCoin promotionCoin = promotionCoinService.findPromotionCoin(brandId);
		
		if(promotionCoin == null){
			throw new CustomException("No promotion found for coin for brand " + brandName);
		}
		
		String distributionRule = promotionCoin.getDistributionRule();
		if(StringUtils.isEmpty(distributionRule)){
			throw new CustomException("Please add distribution rule in promotion coin before adding balance");
		}
		
		Coin coin = coinDao.findByPromotionCoinId(promotionCoin.getPromotionCoinId());

		String walletId = brandUserServices.findWalletIdByBrandUsers(brandId);
		if(StringUtils.isEmpty(walletId)){
			throw new CustomException("Wallet does not exist for brand.");
		}
		
		
		if(coin == null){
			coin = new Coin();
			coin.setCreatedDate(CommonUtility.getCurrentDate());
			coin.setPromotionCoinId(promotionCoin.getPromotionCoinId());
			coin.setWalletId(walletId);
		}
		
		coin.setModifiedDate(CommonUtility.getCurrentDate());
		coin.setAvailableCoins(coin.getAvailableCoins() + amount);
		
		coinDao.save(coin);
	
		coinTransactionService.transferCoin(null, walletId, "DEPOSIT", amount);
		
		promotionCoin.setTotalCoins(promotionCoin.getTotalCoins() + amount);
		promotionCoinDao.save(promotionCoin);
		
		//this.distributeCoins(coin, distributionRule);
		
		return new Response(ResponseStatusCode.updated, coin);
	}
	
	public void distributeCoins(Coin coin, String distributionRule) throws CustomException{
		int coinAmount;
		
		try{
			JSONObject jsonObject = new JSONObject(distributionRule);
			JSONObject jsonObject2 = new JSONObject(jsonObject.get("new").toString().replaceAll("\"", "'"));
			coinAmount = Integer.parseInt(jsonObject2.get("coins").toString());
		}catch(JSONException e){
			throw new CustomException("Invalid distribution rule.");
		}
		
		List<User> users = userDao.findAllUser();
		Role role = roleDao.findRoleByName(Properties.walletUserRole);
		
		for(User user : users){
			if(coin.getAvailableCoins() < coinAmount){
				break;
			}
			
			UserRoles userRole = userRolesDao.findUserByRole(user.getUserId(), role.getRoleId());
			
			if(userRole != null){
				WalletUser walletUser = walletUserDao.findWalletUserByUserId(userRole.getUserId());
				Wallet wallet = walletDao.findWalletById(walletUser.getWalletId());
				if(wallet != null){
					this.sendCoins(coin, wallet, coinAmount);
				}
			}
		}
		
	}
	
	public void distributeCoins(User user, Wallet wallet) throws CustomException{
		List<PromotionCoin> promotionCoins = promotionCoinDao.findAll();
		
		for(PromotionCoin promotionCoin : promotionCoins){
			int coinAmount;
			
			try{
				JSONObject jsonObject = new JSONObject(promotionCoin.getDistributionRule());
				JSONObject jsonObject2 = new JSONObject(jsonObject.get("new").toString().replaceAll("\"", "'"));
				coinAmount = Integer.parseInt(jsonObject2.get("coins").toString());
			}catch(JSONException e){
				throw new CustomException("Invalid distribution rule.");
			}
			
			Coin coin = coinDao.findByPromotionCoinId(promotionCoin.getPromotionCoinId());
			
			if(coin == null || coin.getAvailableCoins() < coinAmount){
				continue;
			}
				
			this.sendCoins(coin, wallet, coinAmount);
			
		}
	}
	
	public void sendCoins(String senderEmail, String receiverEmail, Integer amount) throws CustomException{
		Wallet senderWallet = walletServices.findWalletByEmailId(senderEmail);
		
		Wallet receiverWallet = walletServices.findWalletByEmailId(receiverEmail);
		
		if(senderWallet == null || receiverWallet == null){
			throw new CustomException("Either sender or receiver does not have a wallet.");
		}
		
		//Coin senderCoins = coinDao.findByWalletId(senderWallet.getWalletId());
		
		/*if(senderCoins == null || senderCoins.getAvailableCoins() < amount){
			throw new CustomException("Sender has insufficient balance.");
		}*/
		
		//this.sendCoins(senderCoins, receiverWallet, amount);
	}
	
	public void deductCoins(Coupon coupon){
		String walletId = coupon.getWalletId();
		String promotionId = coupon.getPromotionId();
		Double amount = coupon.getReedemedAmount();
		Promotion promotion = promotionDao.findPromotioanBypromotionId(promotionId);
		Coin coin = coinDao.findByWalletAndPromotionCoinId(walletId, promotion.getPromotionCoinId());
		
		coin.setAvailableCoins(coin.getAvailableCoins() - amount.intValue());
		
		coinDao.save(coin);
		
		coinTransactionService.transferCoin(walletId, null, "WITHDRAW", amount.intValue());

	}
	
	public void sendCoins(Coin senderCoins, Wallet receiverWallet, Integer amount) throws CustomException{
		
		Coin receiverCoins = coinDao.findByWalletAndPromotionCoinId(receiverWallet.getWalletId(),senderCoins.getPromotionCoinId());
		
		if(receiverCoins == null){
			receiverCoins = new Coin();
			receiverCoins.setCreatedDate(CommonUtility.getCurrentDate());
			receiverCoins.setPromotionCoinId(senderCoins.getPromotionCoinId());
			receiverCoins.setWalletId(receiverWallet.getWalletId());
		}
		
		receiverCoins.setModifiedDate(CommonUtility.getCurrentDate());
		receiverCoins.setAvailableCoins(receiverCoins.getAvailableCoins() + amount);
		
		senderCoins.setAvailableCoins(senderCoins.getAvailableCoins() - amount);
		
		coinDao.save(senderCoins);
		coinDao.save(receiverCoins);
		
		coinTransactionService.transferCoin(senderCoins.getWalletId(), receiverCoins.getWalletId(), "TRANSFER", amount);

	}
	
	public Object getBalance(String emailId) throws CustomException{
		Wallet wallet = walletServices.findWalletByEmailId(emailId);
		
		if(wallet == null){
			throw new CustomException("User does not have a wallet.");
		}
		
		List<Coin> coins = coinDao.findByWalletId(wallet.getWalletId());
		
		List<CoinDetail> coinDetails = new ArrayList<CoinDetail>();
		
		for(Coin coin : coins){
			PromotionCoin promotionCoin = promotionCoinDao.findById(coin.getPromotionCoinId());
			 
			if(promotionCoin != null){
				Promotion promotion = promotionDao.findByPromotionCoinId(promotionCoin.getPromotionCoinId());
				Brand brand = brandDao.findById(promotion.getBrandId());
				if(brand != null){
					CoinDetail coinDetail = new CoinDetail(coin, promotionCoin, brand);
					coinDetails.add(coinDetail);
				}
			}	
		}
		
		return coinDetails;
	}
	
	public Object getHistory(String emailId) throws CustomException{
		Wallet wallet = walletServices.findWalletByEmailId(emailId);
		
		if(wallet == null){
			throw new CustomException("User does not have a wallet.");
		}
		
		return coinTransactionService.findByRecieverWallet(wallet.getWalletId());
	}
}
