package com.oomoqu.rest.job;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oomoqu.rest.dao.PromotionDao;
import com.oomoqu.rest.exception.CustomException;
import com.oomoqu.rest.model.Promotion;
import com.oomoqu.rest.service.CouponService;
import com.oomoqu.rest.util.CommonUtility;

@Component
public class PromotionStatusServices {
	private static final String[] status = {"Pending","Approved","Active","Ended","Expired","Rejected"};
	
	@Autowired
	PromotionDao promotionDao;
	
	@Autowired
	CouponService couponService;
	
	public void checkStatus() throws CustomException{
		List<Promotion> promotions = promotionDao.findAllPromotions();
		for(Promotion promotion : promotions){
			checkStatusImpl(promotion);
			
		}
	}
	
	public void checkStatusOnTheFly(Promotion promotion) throws CustomException{
		//Promotion promotionNew = promotionDao.findPromotioanBypromotionId(promotion.getPromotionId());
		checkStatusImpl(promotion);
	}
	
	public void checkStatusImpl(Promotion promotion) throws CustomException{
		
			//System.out.println(promotion.getName());
			int index = checkPromotionStatus(promotion);
			System.out.println("Promotion name " + promotion.getName() + " status " + status[index]);
			
			if(!status[index].equals(promotion.getStatus())){
				promotion.setStatus(status[index]);
				promotionDao.savePromotion(promotion);
				
				if(status[2].equals(status[index])){
					if(promotion.getTotalCoupons() != 0){
						couponService.generateCouponForPromotion(promotion);
					}
				}
			}	
		
		
		//promotionDao.insertAll(promotions);
	}
	
	public int checkPromotionStatus(Promotion promotion){
		Date date = new Date();
		
		if(promotion.getApproved() == null){
			promotion.setApproved(false);
		}
		
		if(date.before(promotion.getStartDate()) 
				&& CommonUtility.compareDateBeforeAndEquals(date, promotion.getEndDate())){
			if(promotion.getApproved() == null){
				return 0;
			}else if(promotion.getApproved()){
				return 1;
			}else{
				return 5;
			}	
		}
		else if(CommonUtility.compareDateAfterAndEquals(date, promotion.getStartDate()) 
				&& CommonUtility.compareDateBeforeAndEquals(date, promotion.getEndDate()) 
				&& promotion.getApproved()){
			return 2;
		}else if(date.after(promotion.getEndDate()) 
				&& promotion.getApproved()){
			return 3;
		}else{
			return 4;
		}
	}
}
