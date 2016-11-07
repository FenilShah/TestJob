package com.oomoqu.rest.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.oomoqu.rest.dao.AddressDao;
import com.oomoqu.rest.dao.BrandDao;
import com.oomoqu.rest.dao.CouponDao;
import com.oomoqu.rest.dao.LocationDao;
import com.oomoqu.rest.dao.PromotionDao;
import com.oomoqu.rest.dao.RetailerDao;
import com.oomoqu.rest.dao.TransactionDao;
import com.oomoqu.rest.dao.WalletDao;
import com.oomoqu.rest.exception.CustomException;
import com.oomoqu.rest.model.Address;
import com.oomoqu.rest.model.Coupon;
import com.oomoqu.rest.model.Location;
import com.oomoqu.rest.model.Response;
import com.oomoqu.rest.model.ResponseStatusCode;
import com.oomoqu.rest.model.Retailer;
import com.oomoqu.rest.model.Transaction;
import com.oomoqu.rest.util.CommonUtility;
import com.oomoqu.rest.util.HaversineAlgorithm;
import com.oomoqu.rest.wrapper.LocationAddress;
import com.oomoqu.rest.wrapper.portal.BrandDetail;
import com.oomoqu.rest.wrapper.portal.LocationDetail;
import com.oomoqu.rest.wrapper.portal.PromotionDetail;

@Component
public class LocationService {
	private static Logger logger = Logger.getLogger(LocationService.class);
	
	@Autowired
	LocationDao locationDao;
	
	@Autowired
	AddressDao addressDao;
	
	@Autowired
	CommonUtility commonUtility;
	
	@Autowired
	RetailerDao retailerDao;
	
	@Autowired
	AddressService addressService;
	
	@Autowired
	RetailerService retailerService;
	
	@Autowired
	BrandDao brandDao;
	
	@Autowired
	TransactionDao transactionDao;
	
	@Autowired
	PromotionDao promotionDao;
	
	@Autowired
	CouponDao couponDao;
	
	@Autowired
	WalletUserService walletUserService;
	
	@Autowired
	LocationUserService locationUserService;
	
	@Autowired
	WalletDao walletDao;
	
	@Autowired
	WalletServices walletServices;
	
	@Autowired
	BarcodeFieldService barcodeFieldService;
	
	@Autowired
	CouponService couponService;
	
	public Response save(String retailerName,LocationAddress locationAddress) throws CustomException{
		logger.debug("LocationService -> Inside save Location");
		
		try {
			
		Response respons = null;
		
		Retailer retailer = retailerDao.findByName(retailerName);
		
		if(retailer==null){
			throw new CustomException("Retailer does not exist.");
		}
		
		if(StringUtils.isEmpty(locationAddress.getLocation().getPortalLocationId())){
			throw new CustomException("PortalLocationId should not be empty.");
		}
		
		if(StringUtils.isNotEmpty(locationAddress.getLocation().getLocationId())){
			locationAddress.getLocation().setLocationId(this.findLocationByLocationId(retailer.getName(), 
					locationAddress.getLocation().getName()));
		}
		
		Address address1 = addressService.verifyAddress(locationAddress.getAddress());
		
		if(address1 != null){
			locationAddress.getLocation().setAddressId(address1.getAddressId());
			locationAddress.setAddress(address1);
		} else {
			addressDao.save(locationAddress.getAddress());
			locationAddress.getLocation().setAddressId(locationAddress.getAddress().getAddressId());
		}
		
		if(validateLocation(locationAddress.getLocation(), retailer.getPortalRetailerId())){
			
			// locationAddress.getLocation().setPortalRetailerId(retailer.getPortalRetailerId());
			locationAddress.getLocation().setRetailerId(retailer.getRetailerId());
			locationDao.save(locationAddress.getLocation());
			
			return new Response(ResponseStatusCode.saved, locationAddress);
			
		} else {
			respons = new Response(ResponseStatusCode.failed);
			respons.setReason("Duplicate location name.");
		}

		return respons;
		} catch(CustomException e){
			return CommonUtility.caughtError(e.getMessage());
		}
	}
	
	public List<Location> findLocationsByRetailer(String retailerName){
		String retailerId = retailerService.getRetailerIdByRetailerName(retailerName);
		return locationDao.findLocations(retailerId);
	}
	
	public List<LocationDetail> findLocationDetailsByRetailer(String retailerName){
		String retailerId = retailerService.getRetailerIdByRetailerName(retailerName);
		return locationDao.findLocationDetails(retailerId);
	}
	
	private boolean validateLocation(Location location,String portalRetailerId){
		logger.debug("LocationService -> Inside Validate Location");
		return locationDao.validateLocation(location, portalRetailerId);
	}
	
	public Location findById(String locationId){
		return locationDao.findLocationByLocationId(locationId);
	}
	
	/*private boolean validateAddress(Address address){
		logger.debug("AddressService -> Inside Validate Address");
		Address address1 = addressService.verifyAddress(address);
		return address1!=null?true:false;
	}*/
	
	public Response remove(String retailerName, String locationName){
		logger.debug("LocationService -> Inside remove Location");
		
		Retailer retailer = retailerDao.findByName(retailerName);
			
		Location location = locationDao.delete(retailer.getPortalRetailerId() ,locationName);
		
		if(location != null){
			return new Response(ResponseStatusCode.deleted);
		} else {
			return new Response(ResponseStatusCode.failed, "Location does not exist.");
		}
	}
	
	public String findLocationByLocationId(String retailerName, String locationName){
		Location location = this.getLocationByLocationName(retailerName, locationName);
		return location!=null?location.getLocationId():null;
	}
	
	public Object findLocationByNames(String retailerName, String locationName){
		Retailer retailer = retailerDao.findByName(retailerName);
		
		if(retailer == null)
			return new Response(ResponseStatusCode.failed, "Retailer does not exist.");
		
		Location location = this.getLocationByLocationName(retailerName, locationName);
		
		if(location == null){
			return new Response(ResponseStatusCode.failed, "Location does not exist.");
		} else {
			return location;
		}
		
	}
	
	public Location getLocationByLocationName(String retailerName, String locationName){
		String retailerId = retailerService.getRetailerIdByRetailerName(retailerName);
		return locationDao.findByNames(retailerId, locationName);
	}
	
	public Location findLocation(double latitude, double longitude){
		Address address = addressDao.findAddressByLatitudeAndLongitude(latitude, longitude);
		
		if(address != null){
			return locationDao.findByAddressId(address.getAddressId());
		} else {
			return null;
		}
	}
	
	public Location findLocation(Address address) {
		
		Address findedAddress = null;
		List<Address> addresses = null;
		
		findedAddress = addressDao.findAddressByLatitudeAndLongitude(address.getLatitude(), address.getLongitude());
		
		if(findedAddress == null){
			findedAddress = addressDao.findAddressByParameters(address.getName(), address.getStreet1(), address.getPostalCode());
		}
		
		if(findedAddress == null){
			// Haver sine algorithm
			addresses = addressDao.getAllAddress();

			for(Address transactionAddress : addresses ){
			 
				double distance =	HaversineAlgorithm.HaversineInM(transactionAddress.getLatitude(), transactionAddress.getLongitude(), address.getLatitude(), address.getLongitude());
				
				if( distance > 0 && distance <= 2 ){
					// One meter radius
					findedAddress = addressDao.findAddressByLatitudeAndLongitude(transactionAddress.getLatitude(), transactionAddress.getLongitude());
					break;
				}
					
			}
		}
		
		if(findedAddress != null){
			return locationDao.findByAddressId(findedAddress.getAddressId());
		} else {
			return null;
		}
	}
	
	public List<Coupon> findCouponsByLocationId(String locationId){
		logger.debug("CouponService -- > Inside find Coupons By LocationId");
		List<Coupon> Coupons = null;
		List<Transaction> transactions = transactionDao.findTransactionsByLocationId(locationId);
		List<String> couponIds = new ArrayList<String>();
		for(Transaction transaction : transactions){
			couponIds.add(transaction.getCouponId());
		}
		Coupons = couponDao.findCouponsByCouponIds(couponIds);
		return Coupons;
	} 
	
	public List<Coupon> findCouponsByWalletWithoutPaymentRequestedDate(String walletId){
		return couponDao.findCouponsByWalletWithoutPaymentRequestedDate(walletId);
	}
	
	public LocationDetail findLocationDetailById(String locationId){
		return locationDao.findLocationReportByLocationId(locationId);
	}
	
	public Set<BrandDetail> brandDetailsByRedeemedCoupons(List<Coupon> redeemCoupon){
		logger.debug("CouponService -- > Inside retailer Brand Wrapper");

		Map<String, List<Coupon>> promoCoupons = new HashMap<String,List<Coupon>>();
		List<Coupon> coupons = null;
		
		for(Coupon coupon : redeemCoupon){
			
			 if(coupon.getPaymentRequestedDate() == null){
				 
				coupons = new ArrayList<Coupon>();
				
				if(promoCoupons.get(coupon.getPromotionId()) != null){
					
					List<Coupon> cp = promoCoupons.get(coupon.getPromotionId());
					cp.add(coupon);
					
				} else {
					
					coupons.add(coupon);
					promoCoupons.put(coupon.getPromotionId(), coupons);
					
				}
			}
		}
		
		Map<String, List<PromotionDetail>> brandPromotions = new HashMap<String,List<PromotionDetail>>();
		List<PromotionDetail> promotionReports = null;
		
		for(Map.Entry<String, List<Coupon>> cpn : promoCoupons.entrySet() ){
			
			promotionReports =  new ArrayList<PromotionDetail>();
			PromotionDetail promotionReport = promotionDao.findPromotionByPromotionPortalId(cpn.getKey());
			
			if(brandPromotions.get(promotionReport.getBrandId()) != null ){
				
				List<PromotionDetail> promotionReportByBrand = brandPromotions.get(promotionReport.getBrandId());
				promotionReportByBrand.add(promotionReport);
				
			} else {
				
				promotionReports.add(promotionReport);
				brandPromotions.put(promotionReport.getBrandId(), promotionReports);
				
			}
		}
		
		Set<BrandDetail> brandReportSet = new HashSet<BrandDetail>();

		for(Map.Entry<String, List<PromotionDetail>> brandPromotionReport : brandPromotions.entrySet()){
			
			BrandDetail bReport = brandDao.findBrandBybrandId(brandPromotionReport.getKey());
			
			if(bReport != null){
				
				List<PromotionDetail> pr = brandPromotionReport.getValue();
				
				for(PromotionDetail pReport : pr){
					pReport.setCoupons(promoCoupons.get(pReport.getPromotionId()));
				}
				
				bReport.setPromotionDetails(brandPromotionReport.getValue());
				brandReportSet.add(bReport);
			}
		}
		return brandReportSet;
	}
	
	/*public void couponTransaction(String emailId, List<TransactionDetail> transacationDetails){
		logger.info("LocationService -> Inside save Transaction");
		
		Wallet wallet = walletServices.findWalletByEmailId(emailId);
		
		for(TransactionDetail transactionDetail : transacationDetails){
			
			Location location = null;
			BarcodeField barcodeField = null;
			
			Transaction transaction = transactionDetail.getTransaction();
			
			Address address = transactionDetail.getAddress();
			
			location = this.findLocation(address);
			
			Coupon coupon = couponDao.findById(transaction.getCouponId());
			
			if(coupon.getValidatedDate() != null) {
				
				if(location != null){
						
					String walletId = locationUserService.findWalletIdByLocationUsers(location.getLocationId());
					
					*//**
					// generate system wide wallet, if no wallet mapped to location
					if(walletId == null){
						wallet = walletServices.saveWallet(null);
						walletId = wallet.getWalletId();
					}
					*//*
					
					coupon.setReedemedDate(CommonUtility.getCurrentDate());
						
					barcodeField = new BarcodeField(coupon.getBarcodeString());
						
					double saveValue =	barcodeFieldService.convertRedeemAmount(barcodeField.getSaveValueValue());
						
					coupon.setReedemedAmount(saveValue);
					coupon.setWalletId(walletId);
	
					transaction.setLocationId(location.getLocationId());
					transaction.setRecieverWalletId(walletId);
					
					} else {
						addressDao.save(address);
						coupon.setWalletId(null);
						transaction.setAddressId(address.getAddressId());
					}
				
				transaction.setSenderWalletId(wallet.getWalletId());
				transaction.setTransactionType("REDEEM");
				transactionDao.save(transaction);
			
			}
			couponDao.createCoupon(coupon);
		}
		
	}*/
	
}
