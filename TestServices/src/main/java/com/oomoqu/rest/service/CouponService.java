package com.oomoqu.rest.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.oomoqu.rest.dao.AddressDao;
import com.oomoqu.rest.dao.BrandDao;
import com.oomoqu.rest.dao.CoinDao;
import com.oomoqu.rest.dao.CouponDao;
import com.oomoqu.rest.dao.LocationDao;
import com.oomoqu.rest.dao.PromotionCoinDao;
import com.oomoqu.rest.dao.PromotionDao;
import com.oomoqu.rest.dao.RoleDao;
import com.oomoqu.rest.dao.SequenceDao;
import com.oomoqu.rest.dao.SharedCouponDao;
import com.oomoqu.rest.dao.TransactionDao;
import com.oomoqu.rest.dao.UserDao;
import com.oomoqu.rest.dao.UserRolesDao;
import com.oomoqu.rest.dao.WalletDao;
import com.oomoqu.rest.dao.WalletUserDao;
import com.oomoqu.rest.exception.CustomException;
import com.oomoqu.rest.model.Address;
import com.oomoqu.rest.model.BarcodeField;
import com.oomoqu.rest.model.Brand;
import com.oomoqu.rest.model.Coin;
import com.oomoqu.rest.model.Coupon;
import com.oomoqu.rest.model.ScannedPaperCoupon;
import com.oomoqu.rest.model.Location;
import com.oomoqu.rest.model.Promotion;
import com.oomoqu.rest.model.PromotionCoin;
import com.oomoqu.rest.model.Response;
import com.oomoqu.rest.model.ResponseStatusCode;
import com.oomoqu.rest.model.Retailer;
import com.oomoqu.rest.model.Role;
import com.oomoqu.rest.model.SharedCoupon;
import com.oomoqu.rest.model.Transaction;
import com.oomoqu.rest.model.User;
import com.oomoqu.rest.model.UserRoles;
import com.oomoqu.rest.model.Wallet;
import com.oomoqu.rest.model.WalletUser;
import com.oomoqu.rest.util.BarcodeGenerator;
import com.oomoqu.rest.util.CommonUtility;
import com.oomoqu.rest.util.Properties;
import com.oomoqu.rest.wrapper.mobile.BrandDetail;
import com.oomoqu.rest.wrapper.mobile.CouponDetail;
import com.oomoqu.rest.wrapper.portal.PromotionDetail;
import com.oomoqu.rest.wrapper.portal.WalletCouponDetail;

@Component
public class CouponService {
	private static Logger logger = Logger.getLogger(CouponService.class);
	
	@Autowired
	CouponDao couponDao;
	
	@Autowired
	CommonUtility commonUtility;
	
	@Autowired
	BarcodeGenerator barcodeGenerator;
	
	@Autowired
	PromotionDao promotionDao;
	
	@Autowired
	BrandUserServices brandUserServices;
	
	@Autowired
	SharedCouponDao sharedCouponDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	WalletUserDao walletUserDao;
	
	@Autowired
	BrandDao brandDao;

	@Autowired
	WalletDao walletDao;
	
	@Autowired
	WalletUserService walletUserService;
	
	@Autowired
	AddressDao addressDao;
	
	@Autowired
	TransactionDao transactionDao;
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	RoleDao roleDao;
	
	@Autowired
	UserRolesDao userRolesDao;
	
	@Autowired
	WalletServices walletServices;
	
	@Autowired
	BarcodeFieldService barcodeFieldService;
	
	@Autowired
	RetailerService retailerService;
	
	@Autowired
	RetailerUsersService retailerUsersService;
	
	@Autowired
	LocationDao locationDao;
	
	@Autowired
	LocationService locationService;
	
	@Autowired
	LocationUserService locationUserService;
	
	@Autowired
	SequenceDao sequenceDao;
	
	@Autowired
	private PromotionCoinService promotionCoinService;
	
	@Autowired
	private CoinDao coinDao;
	
	@Autowired
	private CoinService coinService;
	
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private PromotionCoinDao promotionCoinDao;
	
	@Autowired
	private PromotionService promotionService;
	
	@Autowired
	private ScannedPaperCouponService scannedPaperCouponService;
	
	public void generateCouponForPromotion(Promotion promotion) throws CustomException{
		logger.debug("CouponService -- > Inside Generate coupon for promotion");
			String walletId = brandUserServices.findWalletIdByBrandUsers(promotion.getBrandId());
			
			if(StringUtils.isEmpty(walletId)){
				throw new CustomException("Wallet not found for brand.");
			}
			
			if(StringUtils.isEmpty(promotion.getBarcodeString())){
				throw new CustomException("Barcode string is require to generate coupons.");
			}
			
			List<Coupon> coupons = new ArrayList<Coupon>();
			Coupon coupon;
			for(int i=0;i<promotion.getTotalCoupons();i++){
				coupon = new Coupon();
				coupon.setCouponHashKey("X-XX-XXX-XX-X-XXX-XX-X");
				coupon.setPromotionId(promotion.getPromotionId());
				coupon.setCreatedDate(CommonUtility.getCurrentDate());
				coupon.setModifiedDate(CommonUtility.getCurrentDate());
				coupon.setWalletId(walletId);
				Long nextNo = sequenceDao.getNextSequence(promotion.getPromotionId());
				String serialNo = promotion.getPromotionNo() + nextNo.toString();
				coupon.setSerialNo(serialNo);
				coupons.add(coupon);
			}

			couponDao.createBulkCoupon(coupons);
			
			
			for(Coupon coupon2 : coupons){
				barcodeFieldService.generateAndUpdateCouponBarcodeString(promotion, coupon2);
				
				transactionService.transferCoupon(coupon2.getCouponId(), null, walletId, "NEW");
			}
			
			promotion.setCreatedCoupons(coupons.size());
			promotionDao.savePromotion(promotion);
			
			issueCoupons(promotion,coupons);
	}
	
	public List<BrandDetail> getHistoryCoupons(String emailId){
		logger.debug("CouponService -- > Inside get History Coupons");
		
		Wallet wallet = walletServices.findWalletByEmailId(emailId);
		
		logger.debug("Wallet Id " + wallet.getWalletId());
		
		List<Transaction> transactions = transactionService.findTransactionByWalletAndType(wallet.getWalletId(), "Validated");
		
		List<String> couponIds = new ArrayList<String>();
		//Map<String, Address> addressMap  = new HashMap<String, Address>();
		
		for(Transaction transaction : transactions){
			couponIds.add(transaction.getCouponId());
			/*if(transaction.getAddressId() != null){
				Address address = addressDao.findById(transaction.getAddressId());
				addressMap.put(transaction.getCouponId(), address);
			}*/
		}
		
		transactions = transactionService.findTransactionByWalletAndType(wallet.getWalletId(), "SHARED");
		for(Transaction transaction : transactions){
			couponIds.add(transaction.getCouponId());
		}
		
		logger.debug("Coupons found " + couponIds.size());
		List<Coupon> coupons = couponDao.findCouponsByCouponIds(couponIds);
		
		List<Coupon> usedCoupons = couponDao.findCouponsByWalletWithUsedDate(wallet.getWalletId());
		coupons.addAll(usedCoupons);
		
		return getBrandDetailFromCoupons(coupons, true);
	}
	
	
	public Object getCouponsByWallet(String emailId){
		logger.debug("CouponService -- > Inside get Issued Coupons -- emailId " + emailId);
		
		List<Coupon> coupons = null;
		String walletId;
		
		try{
		
			User user = userDao.findByEmailId(emailId);

			if(user == null){
				throw new CustomException("User does not exist.");
			}
			
			List<UserRoles> userRoles = userRolesDao.findUserRoles(user.getUserId());
			
			if(CollectionUtils.isEmpty(userRoles)){
				throw new CustomException("Roles does not exist.");
			}
			
			for(UserRoles userRole : userRoles){
				Role role = roleDao.findById(userRole.getRoleId());
				if((role.getName()).equals("RetailerAdmin") || (role.getName()).equals("RetailerBranchAdmin") ){
					return retailerService.getLocationDetail(user.getUserId());
				}
			}
			
			walletId = walletUserService.findWalletIdByUser(user.getUserId()); 
			
			coupons = couponDao.findCouponsByWalletWithoutUsedDate(walletId);
			
			logger.debug("Coupons found " + coupons.size());
			
			return getBrandDetailFromCoupons(coupons, null);
		
		}catch(CustomException e){
			return CommonUtility.caughtError(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<BrandDetail> getBrandDetailFromCoupons(List<Coupon> coupons, Boolean withAddress){
		Promotion promotion;
		List<String> brandIds = new ArrayList<String>();
		
		Map<String, Object> couponListBrandWise = new HashMap<>();
		
		for(Coupon coupon : coupons){
			String promotionId = coupon.getPromotionId();
			promotion = promotionDao.findPromotioanBypromotionId(promotionId);
			
			String brandId = promotion.getBrandId();
			logger.debug("Brand Id " + brandId);
			brandIds.add(brandId);
			
			CouponDetail couponDetail = new CouponDetail(coupon, promotion);
			
			if(withAddress != null && withAddress){
				Address address = addressDao.findById(coupon.getAddressId());
				couponDetail.setAddress(address);
			}
			
			Object o = couponListBrandWise.get(brandId);
			if(o==null){
				List<CouponDetail> couponList = new ArrayList<CouponDetail>();
				couponList.add(couponDetail);
				couponListBrandWise.put(brandId, couponList);
			}else{
				List<CouponDetail> couponList = (List<CouponDetail>) o;
				couponList.add(couponDetail);
			}
		}
		
		List<Brand> brands = brandDao.findBrandByIds(brandIds);
		
		List<BrandDetail> brandDetails = new ArrayList<BrandDetail>();
 		BrandDetail brandDetail;
		for(Brand brand : brands){
			brandDetail = new BrandDetail(brand);
			brandDetail.setCouponDetails((List<CouponDetail>) couponListBrandWise.get(brand.getBrandId()));
			brandDetails.add(brandDetail);
		}
		
		Collections.sort(brandDetails);
		
		logger.debug("brands found " + brandDetails.size());
		return brandDetails;
	}
	
	public List<PromotionDetail> getIssuedCoupons(String walletId, List<String> promotionIds){
		logger.debug("CouponService -- > Inside get Issued Coupons");
		List<Coupon> coupons = couponDao.findIssuedCoupons(walletId,promotionIds);
		List<PromotionDetail> promotionReports = new ArrayList<PromotionDetail>();
		PromotionDetail promotionReport;
		for(Coupon coupon : coupons){
			String promotionId = coupon.getPromotionId();
			promotionReport = promotionDao.findPromotionByPromotionPortalId(promotionId);
			List<Coupon> couponList = new ArrayList<Coupon>();
			couponList.add(coupon);
			promotionReport.setCoupons(couponList);
			promotionReports.add(promotionReport);
		}
		return promotionReports;
	}
	
	public Coupon generateTempBarcode(Coupon coupon){
		logger.debug("CouponService -- > Inside generate TempBarcode");
		String byteArrayCoupon =  barcodeGenerator.createBarcodeForCoupon(coupon);
		Coupon cp = new Coupon();
		cp.setBarcodeString(byteArrayCoupon);
		return cp;
	}
	
	public void shareCoupon(String emailId, SharedCoupon sharedCoupon) throws Exception{
		logger.debug("CouponService -- > Inside shareCoupon");
		Wallet wallet = walletServices.findWalletByEmailId(emailId);
		sharedCoupon.setSharedDate(CommonUtility.getCurrentDate());
		sharedCoupon.setSenderWalletId(wallet.getWalletId());
		User user = userDao.findUserByLoginMethod(sharedCoupon);
		if(user != null){
			logger.info("User found " + user.getUserId());
			transferCoupon(user, sharedCoupon);
		}else{
			Coupon coupon = couponDao.findById(sharedCoupon.getCouponId());
			
			coupon.setWalletId(null);
			coupon.setSharedDate(CommonUtility.getCurrentDate());
			couponDao.createCoupon(coupon);
		}
		sharedCouponDao.save(sharedCoupon);
		transactionService.transferCoupon(sharedCoupon.getCouponId(), sharedCoupon.getSenderWalletId(), sharedCoupon.getReceiverWalletId(), "SHARED");
	}
	
	public void transferCoupon(User user, SharedCoupon sharedCoupon){
		logger.debug("CouponService -- > Inside transferCoupon");
		WalletUser walletUser = walletUserDao.findWalletUserByUserId(user.getUserId());
		sharedCoupon.setReceiverWalletId(walletUser.getWalletId());
		sharedCoupon.setIsAssigned(true);
		sharedCoupon.setAssignedDate(new Date());
		
		Coupon coupon = couponDao.findById(sharedCoupon.getCouponId());
		
		coupon.setWalletId(walletUser.getWalletId());
		coupon.setSharedDate(CommonUtility.getCurrentDate());
		couponDao.createCoupon(coupon);
		logger.info("Coupon transfered to user " + user.getUserId());
	}
	
	public void findSharedCoupons(User user){
		logger.debug("CouponService -- > Inside findSharedCoupons");
		List<SharedCoupon> sharedCoupons = sharedCouponDao.findSharedCouponsByUser(user);
		for(SharedCoupon sharedCoupon : sharedCoupons){
			transferCoupon(user, sharedCoupon);
		}
	}

	public int issueCoupons(Promotion promotion, List<Coupon> coupons){
		logger.info("Issuing coupons for promotion " + promotion.getPromotionId() + " promotion name "+ promotion.getName());
		
		List<User> users = userDao.findAllUser();
		Role role = roleDao.findRoleByName(Properties.walletUserRole);
		Iterator<Coupon> couponItr = coupons.iterator();
		int count=0;
		
		for(User user : users){
			UserRoles userRole = userRolesDao.findUserByRole(user.getUserId(), role.getRoleId());
			if(userRole != null){
				WalletUser walletUser = walletUserDao.findWalletUserByUserId(userRole.getUserId());
				Wallet wallet = walletDao.findWalletById(walletUser.getWalletId());
				if(wallet != null){
					if(couponItr.hasNext()){
						Coupon coupon = couponItr.next();
						
						transactionService.transferCoupon(coupon.getCouponId(), coupon.getWalletId(), wallet.getWalletId(), "ISSUE");
						
						coupon.setWalletId(wallet.getWalletId());
						coupon.setIssuedDate(CommonUtility.getCurrentDate());
						couponDao.createCoupon(coupon);
						logger.debug("Coupon issued to wallet " + wallet.getWalletId() + " and coupon id " + coupon.getCouponId());
						count++;
					}
				}
			}
 		}
				
		if(count>0){
			promotion.setAvailableCoupons(promotion.getAvailableCoupons()-count);
			promotionDao.savePromotion(promotion);
			logger.debug("Available promotion is " + promotion.getAvailableCoupons());
		}
		return count;
	}
	
	public void issueCoupons(User user, String walletId){
		logger.debug("Issuing coupon to user while registering");
		List<Brand> brands = brandDao.getBrandListWithActivePromotions();
		for(Brand brand : brands){
			logger.debug("Found active brand " + brand.getBrandId());
			List<Promotion> promotions = promotionDao.getPromotionListByBrandId(brand.getBrandId(), null, null);
			
			for(Promotion promotion : promotions){
				logger.debug("Found active promotion " + promotion.getPromotionId());
				Coupon coupon = couponDao.findAvailableCoupon(promotion.getPromotionId());
				if(coupon!=null){
					transactionService.transferCoupon(coupon.getCouponId(), coupon.getWalletId(), walletId, "ISSUE");
					
					coupon.setWalletId(walletId);
					coupon.setIssuedDate(CommonUtility.getCurrentDate());
					couponDao.createCoupon(coupon);
					
					promotion.setAvailableCoupons(promotion.getAvailableCoupons()-1);
					promotionDao.savePromotion(promotion);
					logger.debug("Coupon " + coupon.getCouponId() + " is assigned to walletId " + walletId);
				}	
			}
		}	
	}

	public void couponsForPayments(List<String> couponIds){
		logger.info("Inside coupon payments");
		couponDao.updateCouponPaymentStatus(couponIds);
	}
	
	public Coupon findBySerialNumber(String serialNumber){
		return couponDao.findBySerialNumber(serialNumber);
	}
	
	public Object validateCoupon(String retailerName, String locationName, WalletCouponDetail walletCouponDetail){
		logger.info("Inside validate Coupon");
		
		Retailer retailer = retailerService. retailerDao.findByName(retailerName);
		
		if(retailer==null){
			return new Response(ResponseStatusCode.failed, "Retailer does not exist");
		}
		
		List<com.oomoqu.rest.wrapper.portal.CouponDetail> couponsDetails1 = new ArrayList<com.oomoqu.rest.wrapper.portal.CouponDetail>();
		String userEmailId, walletId, userWalletId;
		
		Location location = locationService.getLocationByLocationName(retailerName, locationName);
		//locationId = locationService.findLocationByLocationId(retailerName, locationName);
		
		walletId = null;
		userEmailId = walletCouponDetail.getWalletId();
		userWalletId = walletServices.findWalletIdByEmailId(userEmailId);
		
		for(com.oomoqu.rest.wrapper.portal.CouponDetail couponDetail : walletCouponDetail.getCouponDetails()){
			
			if(couponDetail.getBarcodeString().length() < 25){
				return new Response(ResponseStatusCode.failed, "sorry, this is not GS1 Coupon Databar");
			}
			
			BarcodeField barcodeField = new BarcodeField(couponDetail.getBarcodeString());
			logger.info("Serial No : " + barcodeField.getSerialNumberValue());
			couponDetail.setSerialNo(barcodeField.getSerialNumberValue());
			
			Coupon coupon = this.findBySerialNumber(barcodeField.getSerialNumberValue());
			
			if(coupon == null){
				return new Response(ResponseStatusCode.failed, "No coupon available for this Barcode string");
			}
			
			if(coupon.getValidatedDate() != null){
				return new Response(ResponseStatusCode.failed, "Coupon already Used..!!");
			}
			
			if((coupon.getWalletId()).equals(userWalletId)){
				coupon.setValidatedDate(CommonUtility.getCurrentDate());
				
				couponDetail.setValidated(true);
					
					Transaction transaction = new Transaction();
					
					if(location != null){
						walletId = locationUserService.findWalletIdByLocationUsers(location.getLocationId());
						transaction.setLocationId(location.getLocationId());
						coupon.setAddressId(location.getAddressId());
					} 
					
					if(walletId == null){
						walletId = walletServices.getWideWalletId();
					}
					

					transaction.setRecieverWalletId(walletId);
					transaction.setSenderWalletId(userWalletId);
					transaction.setTransactionType("Validated");
					transaction.setCouponId(coupon.getCouponId());
					transaction.setTransactionDate(CommonUtility.getCurrentDate());
					transactionDao.save(transaction);
					
					if("coin".equals(coupon.getType())){
						coupon.setReedemedAmount(Double.parseDouble(barcodeField.getSaveValueValue()));
						coinService.deductCoins(coupon);
					}
					
					coupon.setWalletId(walletId);
			}
			
			couponDao.createCoupon(coupon);
			couponDetail.setBarcodeString(coupon.getBarcodeString());
			couponsDetails1.add(couponDetail);
		}
		
		walletCouponDetail.setCouponDetails(couponsDetails1);
		
		return walletCouponDetail;
	}
	
	public Object redeemCouponValue(WalletCouponDetail walletCouponDetail){
		String serialNumber = null;
		Coupon coupon = null;
		
		for(com.oomoqu.rest.wrapper.portal.CouponDetail couponDetail : walletCouponDetail.getCouponDetails()){
			
			if(couponDetail.validated){
				serialNumber = barcodeFieldService.getSerialNumberFromBarcodeString(couponDetail.getBarcodeString());
				coupon = this.findBySerialNumber(serialNumber);
				
				if(coupon == null)
					return new Response(ResponseStatusCode.failed, "Coupon reedemed failed, Invalid coupon found");
				
				coupon.setReedemedAmount(couponDetail.getReedemedAmount());
				coupon.setReedemedDate(CommonUtility.getCurrentDate());
				
				couponDao.createCoupon(coupon);
			}
			
		}
		
		return new Response(ResponseStatusCode.updated, "Redeemed successfully");
		
	}
	
	public Object createCouponForCoin(String brandName, String emailId, Integer amount) throws CustomException{
		String brandId = brandService.getBrandIdByBrandName(brandName);
		if(StringUtils.isEmpty(brandId)){
			throw new CustomException("Brand does not exist.");
		}
		
		Promotion promotion = promotionDao.findByBrandAndPromotionCoin(brandId);
		
		PromotionCoin promotionCoin = promotionCoinDao.findById(promotion.getPromotionCoinId());
		
		Wallet wallet = walletServices.findWalletByEmailId(emailId);
		
		Coin receiverCoins = coinDao.findByWalletAndPromotionCoinId(wallet.getWalletId(),promotionCoin.getPromotionCoinId());
		
		if(amount > receiverCoins.getAvailableCoins()){
			throw new CustomException("Insufficient balance");
		}
		
		Coupon coupon = new Coupon();
		coupon.setCouponHashKey("X-XX-XXX-XX-X-XXX-XX-X");
		coupon.setPromotionId(promotion.getPromotionId());
		coupon.setCreatedDate(CommonUtility.getCurrentDate());
		coupon.setModifiedDate(CommonUtility.getCurrentDate());
		
		
		
		coupon.setWalletId(wallet.getWalletId());
		String couponBarcodeString = null;
		Long nextNo = sequenceDao.getNextSequence(promotionCoin.getPromotionCoinId());
		String serialNo = promotion.getPromotionNo() + nextNo.toString();
		coupon.setSerialNo(serialNo);
		
		couponBarcodeString = barcodeFieldService.generateCouponBarcodeStrings(promotion, coupon, String.valueOf(amount));

		coupon.setBarcodeString(couponBarcodeString);
		coupon.setType("coin");
		
		couponDao.createCoupon(coupon);
		
		return coupon;
	}
	/**
	 * Digital coupon bar code String generator, Passed bar code string contain offer code,
	 * We are use offer code for identify promotion of brand
	 * @author Mahendra Panchal
	 * @param emailId
	 * @param paperCouponBarcodeString
	 */
	public Object scanPaperCoupon(String emailId, String paperCouponBarcodeString){
		
		BarcodeField barcodeField = null;
		Promotion promotion = null;
		Coupon coupon = null;
		String receiverWalletId = "";
		Response response = null;
		ScannedPaperCoupon scannedPaperCoupon = new ScannedPaperCoupon();
		
		response = new Response();
		if(barcodeFieldService.validateBarcodeString(paperCouponBarcodeString)) { // IF Validate bar code string
			
			receiverWalletId = walletServices.findWalletIdByEmailId(emailId);
			
			try {
				barcodeField = new BarcodeField(paperCouponBarcodeString);
				logger.info("promotion offer code " + barcodeField.getOfferCodeValue());
				
				promotion = promotionDao.findByNumber(String.valueOf(Integer.parseInt(barcodeField.getOfferCodeValue())));
				
				if(promotion!=null && promotion.getAvailableCoupons() > 0){ // IF promotion not null 
					
					coupon = this.findIssuedCouponByuserWalletIdAndPromotionId(receiverWalletId, promotion.getPromotionId());
					
					if(coupon==null){
						coupon = couponDao.findAvailableCoupon(promotion.getPromotionId());
						//Coupon Issue to wallet Transaction
						transactionService.transferCoupon(coupon.getCouponId(), coupon.getWalletId(), receiverWalletId, "ISSUE");
						
						// Update Issued Coupon
						coupon.setWalletId(receiverWalletId);
						coupon.setIssuedDate(CommonUtility.getCurrentDate());
						couponDao.createCoupon(coupon);
						
						// Update Promotion
						promotion.setAvailableCoupons(promotion.getAvailableCoupons()-1);
						promotionDao.savePromotion(promotion);
						
						scannedPaperCoupon.setBarcodeString(paperCouponBarcodeString);
						scannedPaperCoupon.setScannedByUser(emailId);
						scannedPaperCoupon.setScannedDate(CommonUtility.getCurrentDate());
						scannedPaperCoupon.setStatus(ScannedPaperCouponStatus.valid);
						
						response.setCode(ResponseStatusCode.saved);
						response.setMessage("Valid paper Coupons...!!");
						
						logger.debug("Coupon " + coupon.getCouponId() + " is assigned to walletId " + receiverWalletId);
						
						scannedPaperCouponService.ScannedPaperCoupon(scannedPaperCoupon);
						return response;
					} else {
						response.setCode(ResponseStatusCode.saved);
						response.setMessage("There is a coupon for this promotion, Already In your wallet..!!");
						return response;
					}
				}
			
			} catch (Exception e) {
					scannedPaperCoupon.setBarcodeString(paperCouponBarcodeString);
					scannedPaperCoupon.setScannedByUser(emailId);
					scannedPaperCoupon.setScannedDate(CommonUtility.getCurrentDate());
					scannedPaperCoupon.setStatus(ScannedPaperCouponStatus.Invalid);
					
					response.setCode(ResponseStatusCode.failed);
					response.setMessage("Invalid paper coupon...!!");
			} // End catch
			
		} 
		
		scannedPaperCoupon.setBarcodeString(paperCouponBarcodeString);
		scannedPaperCoupon.setScannedByUser(emailId);
		scannedPaperCoupon.setScannedDate(CommonUtility.getCurrentDate());
		scannedPaperCoupon.setStatus(ScannedPaperCouponStatus.Invalid);
		
		response.setCode(ResponseStatusCode.failed);
		response.setMessage("Invalid paper coupon...!!");
		
		scannedPaperCouponService.ScannedPaperCoupon(scannedPaperCoupon);
		return response;
	}
	
	public Coupon findIssuedCouponByuserWalletIdAndPromotionId(String userWalletId, String promotionId){
		Coupon coupon = couponDao.findIssuedCoupon(userWalletId, promotionId);
		if(coupon!=null)
			return coupon;
		else
			return null;
	}
	
}
