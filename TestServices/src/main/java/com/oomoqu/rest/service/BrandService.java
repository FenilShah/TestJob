package com.oomoqu.rest.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oomoqu.rest.dao.BrandDao;
import com.oomoqu.rest.dao.CouponDao;
import com.oomoqu.rest.exception.CustomException;
import com.oomoqu.rest.model.Brand;
import com.oomoqu.rest.model.Response;
import com.oomoqu.rest.model.ResponseStatusCode;
import com.oomoqu.rest.model.User;
import com.oomoqu.rest.util.CommonUtility;

@Component
public class BrandService {
	private static Logger logger = Logger.getLogger(BrandService.class);

	@Autowired
	BrandDao brandDao;
	
	@Autowired
	CouponDao couponDao;
	
	@Autowired
	CommonUtility commonUtility;
	
	@Autowired
	WalletUserService walletUserService;
	
	@Autowired
	PromotionService promotionService;
	
	@Autowired
	CouponService couponService;
	
	@Autowired
	private PromotionCoinService promotionCoinService;
	
	public Response createBrand(Brand brand) throws CustomException{
		logger.debug("BrandService -> Inside create Brand");
		boolean newBrand = false;
		
		String brandId = this.getBrandIdByBrandName(brand.getName());
		if(StringUtils.isBlank(brandId)){
			newBrand = true;
		}else{
			brand.setBrandId(brandId);
		}	
		
		if(validateBrand(brand)){
			brand.setCreatedDate(CommonUtility.getCurrentDate());

			User user = CommonUtility.getUser();
			brand.setCreatedByUserId(user.getUserId());

			brand.setModifiedDate(CommonUtility.getCurrentDate());
			
			brandDao.saveBrand(brand);
			
			if(newBrand){
				promotionCoinService.savePromotionCoinWhenBrandCreated(brand.getName());
			}
			
			return new Response(ResponseStatusCode.saved,brand);
		} else {
			throw new CustomException("Duplicate brandname.");
		}
	}

	private boolean validateBrand(Brand brand){
		logger.debug("BrandService -> Inside validate Brand");
		return brandDao.validateBrand(brand);
	}
	
	public Response deleteBrand(String brandName){
		logger.debug("BrandService -> Inside remove Brand" + brandName);
		Brand brand = brandDao.deleteBrand(brandName);
		if(brand != null){
			return new Response(ResponseStatusCode.deleted);
		}else{
			return new Response(ResponseStatusCode.failed,"Brand does not exist.");
		}
	}

	public String getBrandIdByBrandName(String brandName){
		Brand brand = getBrandByBrandName(brandName);
		return brand!=null?brand.getBrandId():null;
	}
	
	public Brand getBrandByBrandName(String brandName){
		return brandDao.findByName(brandName);
	}
	
	public List<Brand> findAllBrands(){
		return brandDao.findAllBrand();
	}
}
