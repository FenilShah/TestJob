package com.oomoqu.rest.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.oomoqu.rest.dao.PromotionCoinDao;
import com.oomoqu.rest.dao.PromotionDao;
import com.oomoqu.rest.dao.SequenceDao;
import com.oomoqu.rest.exception.CustomException;
import com.oomoqu.rest.model.Promotion;
import com.oomoqu.rest.model.PromotionCoin;
import com.oomoqu.rest.model.Response;
import com.oomoqu.rest.model.ResponseStatusCode;
import com.oomoqu.rest.model.User;
import com.oomoqu.rest.util.CommonUtility;
import com.oomoqu.rest.wrapper.portal.PromotionDetail;

@Service
public class PromotionCoinService {
	
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private PromotionCoinDao promotionCoinDao;
	
	@Autowired
	private SequenceDao sequenceDao;
	
	@Autowired
	private PromotionDao promotionDao;
	
	@Autowired
	private BarcodeFieldService barcodeFieldService;
	
	public Response savePromotionCoinWhenBrandCreated(String brandName) throws CustomException{
		PromotionCoin promotionCoin = new PromotionCoin();
		promotionCoin.setTotalCoins(0);
		promotionCoin.setDistributionRule("{'new':{'coins':25}}");
		promotionCoin.setRedeemRule("{ condition: <=, coins : 50}");
		
		PromotionDetail promotionDetail = new PromotionDetail();
		promotionDetail.setPromotionCoin(promotionCoin);
		
		return this.createOrSavePromotion(brandName, promotionDetail);
	}
	
	public Response createOrSavePromotion(String brandName, PromotionDetail promotionDetail) throws CustomException{
		String brandId = brandService.getBrandIdByBrandName(brandName);
		if(StringUtils.isEmpty(brandId)){
			throw new CustomException("Brand does not exist.");
		}
		
		User user =CommonUtility.getUser();
		
		Promotion existingPromotion = promotionDao.findByBrandAndPromotionCoin(brandId);
		PromotionCoin promotionCoin = promotionDetail.getPromotionCoin();
		
		
		if(existingPromotion != null){
			PromotionCoin existingPromotionCoin = promotionCoinDao.findById(existingPromotion.getPromotionCoinId());
			
			//existingPromotion.setBarcodeString(promotionDetail.getBarcodeString());
			existingPromotion.setModifiedDate(CommonUtility.getCurrentDate());
			existingPromotion.setModifiedByUserId(user.getEmail());
			existingPromotion.setImageUrl(promotionDetail.getImageUrl()!=null?promotionDetail.getImageUrl():existingPromotion.getImageUrl());
			existingPromotion.setDescription(promotionDetail.getDescription());
			existingPromotion.setTermsAndConditions(promotionDetail.getTermsAndConditions());
			promotionDao.savePromotion(existingPromotion);
			
			existingPromotionCoin.setDistributionRule(promotionCoin.getDistributionRule());
			existingPromotionCoin.setRedeemRule(promotionCoin.getRedeemRule());
			//existingPromotionCoin.setTotalCoins(promotionCoin.getTotalCoins());
			
			promotionCoinDao.save(existingPromotionCoin);
		}else{
			promotionCoinDao.save(promotionCoin);
			
			Promotion promotion = new Promotion();
			
			promotion.setStartDate(CommonUtility.getCurrentDate());
			
			Calendar cal = Calendar.getInstance();
			cal.set(2099, Calendar.DECEMBER, 31); //Year, month and day of month
			
			promotion.setEndDate(cal.getTime());
			
			promotion.setOwnerId(user.getEmail());
			//promotionCoin.setStatus("Pending");
			Long seqLong = sequenceDao.getNextSequence("PROMO");
			promotion.setPromotionNo(seqLong.toString());
			promotion.setBrandId(brandId);
			promotion.setName("Branded Coins");
			promotion.setModifiedDate(CommonUtility.getCurrentDate());
			promotion.setModifiedByUserId(user.getEmail());
			promotion.setPromotionCoinId(promotionCoin.getPromotionCoinId());
			
			/**
			 *  @Author Mahendra Panchal
				Application Identifiers : 8110
				GS1 COmpany Prefix VI : 0
				GS1 Company Prefix : 654010
				Offer Code : 000000 // Replace with Promotion Unique Number
				Save Value VI : 5
				Save Value : 00000 // Replace with Enter Coin(value) by Wallet user
				Primary Purchase Requirement VI : 1
				Primary Purchase Requirement : 1 
				Primary Purchase Requirement Code : 2
				Primary Purchase Family Code : 000
			*/
			String promotionBarcodeString = barcodeFieldService.setOffercodeAndComposeBarcodeString(promotion.getPromotionNo(), "81100654010000000500000112000");
			promotion.setBarcodeString(promotionBarcodeString);
			
			promotionDao.savePromotion(promotion);
		}
		return new Response(ResponseStatusCode.saved, promotionDetail);
	}
	
	public PromotionDetail getPromotionCoinByBrand(String brandName) throws CustomException{
		String brandId = brandService.getBrandIdByBrandName(brandName);
		if(StringUtils.isEmpty(brandId)){
			throw new CustomException("Brand does not exist.");
		}
		
		PromotionDetail promotionDetail = promotionDao.findDetailsByBrandAndPromotionCoin(brandId);
		PromotionCoin promotionCoin = promotionCoinDao.findById(promotionDetail.getPromotionCoinId());
		promotionDetail.setPromotionCoin(promotionCoin);
		return promotionDetail;
	}
	
	public PromotionCoin findPromotionCoin(String brandId) throws CustomException{
		Promotion promotion = promotionDao.findByBrandAndPromotionCoin(brandId);
		return promotionCoinDao.findById(promotion.getPromotionCoinId());
		
	}
}
