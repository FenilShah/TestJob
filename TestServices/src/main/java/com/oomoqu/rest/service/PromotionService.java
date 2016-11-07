package com.oomoqu.rest.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.oomoqu.rest.dao.BrandDao;
import com.oomoqu.rest.dao.CouponDao;
import com.oomoqu.rest.dao.PromotionDao;
import com.oomoqu.rest.dao.SequenceDao;
import com.oomoqu.rest.exception.CustomException;
import com.oomoqu.rest.job.PromotionStatusServices;
import com.oomoqu.rest.model.Coupon;
import com.oomoqu.rest.model.PaginationUtil;
import com.oomoqu.rest.model.Promotion;
import com.oomoqu.rest.model.Response;
import com.oomoqu.rest.model.ResponseStatusCode;
import com.oomoqu.rest.model.User;
import com.oomoqu.rest.model.WalletUser;
import com.oomoqu.rest.util.CommonUtility;
import com.oomoqu.rest.wrapper.portal.PromotionDetail;


@Component
public class PromotionService {
	private static Logger logger = Logger.getLogger(PromotionService.class);
	
	@Autowired
	PromotionDao promotionDao;
	
	@Autowired
	CouponDao couponDao;
	
	@Autowired
	CommonUtility commonUtility;
	
	@Autowired
	CouponService couponService;
	
	@Autowired
	BrandDao brandDao;
	
	@Autowired
	BrandUserServices brandUserServices;
	
	@Autowired
	WalletUserService walletUserService;
	
	@Autowired
	PromotionStatusServices promotionStatusServices;
	
	@Autowired
	BrandService brandService;
	
	@Autowired
	SequenceDao sequenceDao;
	
	@Autowired
	BarcodeFieldService barcodeFieldService;
	
	public Response createOrSavePromotion(Promotion promotion, String brandName){
		logger.debug("PromotionService -> Inside createOrSavePromotion");
		
			try {
				String brandId = brandService.getBrandIdByBrandName(brandName);
				if(StringUtils.isEmpty(brandId)){
					throw new CustomException("Brand does not exist.");
				}
				promotion.setBrandId(brandId);
				
				if(validatePromotion(promotion)){
					promotion.setModifiedDate(CommonUtility.getCurrentDate());
					promotion.setStartDate(promotion.getStartDate());
					promotion.setEndDate(promotion.getEndDate());
					
					User user =CommonUtility.getUser();
					promotion.setModifiedByUserId(user.getEmail());
					
					if(StringUtils.isEmpty(promotion.getPromotionId())){
						promotion.setAvailableCoupons(promotion.getTotalCoupons());
						promotion.setOwnerId(user.getEmail());
						
						Long seqLong = sequenceDao.getNextSequence("PROMO");
						promotion.setPromotionNo(seqLong.toString());
						// set promotion no as offer code
						promotion.setBarcodeString(barcodeFieldService.setOffercodeAndComposeBarcodeString(promotion.getPromotionNo(), promotion.getBarcodeString()));
					}else{
						Promotion promotionOld = promotionDao.findPromotioanBypromotionId(promotion.getPromotionId());
						promotion.setPromotionNo(promotionOld.getPromotionNo());
					}
					
					promotion.setStatus("Pending");
					
					promotionDao.savePromotion(promotion);
					return new Response(ResponseStatusCode.saved, promotion);
				}else{
					throw new CustomException("Duplicate promotion name.");
				}
			} catch (CustomException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				return CommonUtility.caughtError(e.getMessage());
			}
	}
	
	public Response removePromotion(String brandName, String promotionName){
		logger.debug("PromotionService -> Inside remove Promotion");
		String brandId = brandService.getBrandIdByBrandName(brandName);
		Promotion promotion = promotionDao.delete(brandId, promotionName);
		if(promotion != null){
			return new Response(ResponseStatusCode.deleted);
		}else{
			return new Response(ResponseStatusCode.failed, "Record does not found.");
		}
	}
	
	private boolean validatePromotion(Promotion promotion) throws CustomException{
		logger.debug("PromotionService -> Inside validate Promotion");
		if(StringUtils.isEmpty(promotion.getName())){
			throw new CustomException("Please provide promotion name.");
		}else if(!StringUtils.isEmpty(promotion.getStatus()) && !"Pending".equals(promotion.getStatus())){
			throw new CustomException("Only promotion with pending status can be edited.");
		}
		
		return promotionDao.validatePromotion(promotion);
	}
	
	public String retrievePromotionList(String brandName, Integer start, Integer limit){
		logger.debug("PromotionService -> Inside retrieve Promotion List for a brand " + brandName);
		
		String brandId = brandService.getBrandIdByBrandName(brandName);
		
		List<Promotion> listPromotion = promotionDao.getPromotionListByBrandId(brandId, start, limit);
		int count = promotionDao.numberOfCount(brandId);
		//List<Object> objectList = new ArrayList<Object>(listPromotion);
		//PaginationUtil paginationUtil = new PaginationUtil();
		//paginationUtil.setData(objectList);
		//paginationUtil.setCount(count);
		Response response = new Response();
		response.setData(listPromotion);
		response.append("count", String.valueOf(count));
		return response.toString();
	}
	
	public PaginationUtil retrieveIssuedPromotionList(String brandId, Integer start, Integer limit){
		logger.debug("PromotionService -> Inside retrieve Issued Promotion List");
		WalletUser walletUser = walletUserService.findLoggedInWalletUser();
		List<Promotion> promotions = promotionDao.getPromotionListByBrandId(brandId, start, limit);
		List<String> promotionIds = new ArrayList<String>();
		for(Promotion promotion : promotions){
			promotionIds.add(promotion.getPromotionId());
		}
		List<PromotionDetail> promotionReports = couponService.getIssuedCoupons(walletUser.getWalletId(), promotionIds);
		PaginationUtil paginationUtil = new PaginationUtil();
		List<Object> objectList = new ArrayList<Object>(promotionReports);
		paginationUtil.setData(objectList);
		paginationUtil.setCount(promotionReports.size());
		return paginationUtil;
	}
	
	public Promotion findPromotionByNameAndBrand(String brandName, String promotionName){
		logger.debug("PromotionService -> Inside find Promotion By promotionId");
		String brandId = brandService.getBrandIdByBrandName(brandName);
		return promotionDao.findPromotionByNameAndBrand(brandId, promotionName);
	}
	
	public List<PromotionDetail> getPromotionSummaryReport(String brandName){
		logger.debug("PromotionService -> Inside get Promotion Summary Report");
		String brandId = brandService.getBrandIdByBrandName(brandName);
		List<Promotion> listPromotion = promotionDao.getPromotionListByBrandId(brandId, null, null);
		List<PromotionDetail> promotionReports = new ArrayList<PromotionDetail>();
		for(Promotion promotion : listPromotion){
			promotionReports.add(setPromotionReportData(promotion));
		}
		return promotionReports;
	}
	
	public PromotionDetail getPromotionCouponSummary(String brandName, String promotionName){
		logger.debug("PromotionService -> Inside get get Promotion Coupon Summary");
		String brandId = brandService.getBrandIdByBrandName(brandName);
		Promotion promotion = promotionDao.findPromotionByNameAndBrand(brandId, promotionName);
		if(promotion != null){
			return setPromotionReportData(promotion);
		}else{
			return null;
		}
	}
	
	public PromotionDetail setPromotionReportData(Promotion promotion){
		logger.debug("PromotionService -> Inside set Promotion Report Data");
		PromotionDetail promotionReport = new PromotionDetail(promotion);
		promotionReport.setReedemedCoupons((int)couponDao.getReedemedCouponAmountByPromotionId(promotion.getPromotionId()));
		if(promotion.getApproved() == null)
			promotion.setApproved(true);
		return promotionReport;
	}
	
	public Response updatePromotion(Promotion promotion){
		try{
			Promotion updatedPromotion = promotionDao.updatePromotion(promotion);
			promotionStatusServices.checkStatusOnTheFly(updatedPromotion);
			if(updatedPromotion!=null){
				return new Response(ResponseStatusCode.updated);
			}else{
				return new Response(ResponseStatusCode.failed);
			}
		}catch(CustomException e){
			return CommonUtility.caughtError(e.getMessage());
		}
	}
	
	public Set<Promotion> findPromotionsByCoupons(List<Coupon> coupons){
		Set<Promotion> promotions = new HashSet<Promotion>();
		for(Coupon coupon: coupons){
			Promotion promotion = promotionDao.findPromotioanBypromotionId(coupon.getPromotionId());
			if("Active".equals(promotion.getStatus())){
				promotions.add(promotion);
			}
		}
		return promotions;
	}
	
	public PromotionDetail getAllCoupons(String brandName, String promotionName){
		logger.debug("CouponService -- > Inside getAllCoupons");
		Promotion promotion = this.findPromotionByNameAndBrand(brandName, promotionName);
		if(promotion != null){
			PromotionDetail promotionReport = promotionDao.findPromotionByPromotionPortalId(promotion.getPromotionId());
			List<Coupon> coupons = couponDao.findCouponsByPromotionId(promotionReport.getPromotionId());
			promotionReport.setCoupons(coupons);
			return promotionReport;
		}else{
			return null;
		}
	}
	
	public List<PromotionDetail> findCouponsByAllPromotions(String brandName){
		logger.debug("CouponService -- > Inside find Coupons By All Promotions");
		String brandId = brandService.getBrandIdByBrandName(brandName);
		if(!StringUtils.isEmpty(brandId)){
			List<PromotionDetail> listPromotion = promotionDao.findPromotionReportsByBrandId(brandId);
			for(PromotionDetail promotionReport : listPromotion){
				List<Coupon> coupons = couponDao.findCouponsByPromotionId(promotionReport.getPromotionId());
				promotionReport.setCoupons(coupons);
			}
			return listPromotion;
		}else{
			return null;
		}
	}
}
