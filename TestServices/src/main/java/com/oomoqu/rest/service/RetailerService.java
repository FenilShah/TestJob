package com.oomoqu.rest.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.oomoqu.rest.dao.BrandDao;
import com.oomoqu.rest.dao.CouponDao;
import com.oomoqu.rest.dao.LocationDao;
import com.oomoqu.rest.dao.PromotionDao;
import com.oomoqu.rest.dao.RetailerDao;
import com.oomoqu.rest.dao.TransactionDao;
import com.oomoqu.rest.model.Address;
import com.oomoqu.rest.model.Coupon;
import com.oomoqu.rest.model.Location;
import com.oomoqu.rest.model.Response;
import com.oomoqu.rest.model.ResponseStatusCode;
import com.oomoqu.rest.model.Retailer;
import com.oomoqu.rest.model.User;
import com.oomoqu.rest.util.CommonUtility;
import com.oomoqu.rest.wrapper.portal.BrandDetail;
import com.oomoqu.rest.wrapper.portal.LocationDetail;
import com.oomoqu.rest.wrapper.portal.RetailerDetail;

@Component
public class RetailerService {
	private static Logger logger = Logger.getLogger(RetailerService.class);
	
	@Autowired
	RetailerDao retailerDao;
	
	@Autowired
	CommonUtility commonUtility;
	
	@Autowired
	BrandDao brandDao;
	
	@Autowired
	BrandUserServices brandUserServices;
	
	@Autowired
	LocationDao locationDao;
	
	@Autowired
	LocationUserService locationUserService;
	
	@Autowired
	LocationService locationService;
	
	@Autowired
	TransactionDao transactionDao;
	
	@Autowired
	CouponDao couponDao;
	
	@Autowired
	PromotionDao promotionDao;
	
	@Autowired
	RetailerUsersService retailerUsersService;
	
	@Autowired
	AddressService addressService;
	
	public Response save(Retailer retailer){
		logger.debug("RetailerService -> Inside Save retailer");
		Response respons;
		
		if(validate(retailer)){
			retailer.setCreatedDate(CommonUtility.getCurrentDate());
			retailer.setModifiedDate(CommonUtility.getCurrentDate());

			User user = CommonUtility.getUser();
			retailer.setCreatedByUserId(user.getUserId());
			
			retailerDao.save(retailer);
			
			respons = new Response(ResponseStatusCode.saved);
			respons.setData(retailer);
		} else {
			respons = new Response(ResponseStatusCode.failed);
			respons.setReason("Duplicate retailerName.");
		}
		return respons;
	}

	private boolean validate(Retailer retailer) {
		logger.debug("RetailerService -> Inside validate retailer;");
		return retailerDao.findRetailerNameById(retailer)!=null?false:true;
	}
	
	public Response deleteRetailer(String retailerName){
		logger.debug("RetailerService -> Inside delete retailer;");
		
		Retailer retailer = retailerDao.remove(retailerName);
		
		if(retailer != null){
			return new Response(ResponseStatusCode.deleted);
		} else {
			return new Response(ResponseStatusCode.failed,"Retailer does not exist.");
		}
	}
	
	public Set<LocationDetail> getLocationDetail(String userId){
		
		Set<LocationDetail> locationDetails = null;
		List<Coupon> coupons = null;
		Set<BrandDetail> brandDetail = null;
		LocationDetail locationDetail = null;
		List<Location> locations = null;
		Location locationByUser = null;
		String walletId;
		
		Retailer retailer = retailerUsersService.findRetailerByUserId(userId);
		if(retailer != null) {
			locations = locationService.findLocationsByRetailer(retailer.getName());
		} else {
			locations= new ArrayList<Location>();
			String locationId = locationUserService.findLocationIdByUserId(userId);
			if(!StringUtils.isEmpty(locationId)){
				locationByUser = locationService.findById(locationId);
				locations.add(locationByUser);
			}
		}
		locationDetails = new HashSet<LocationDetail>();
		
		for(Location location : locations) {
			
			walletId = this.findWalletIdByLocationId(location.getLocationId()); 
			
			coupons = locationService.findCouponsByWalletWithoutPaymentRequestedDate(walletId);
			
			brandDetail = locationService.brandDetailsByRedeemedCoupons(coupons);
			
			locationDetail = locationService.findLocationDetailById(location.getLocationId()); 
			
			locationDetail.setBrandDetail(brandDetail);
			
			if(!CollectionUtils.isEmpty(brandDetail)){
				if(!CollectionUtils.isEmpty(coupons)){
					locationDetails.add(locationDetail);
				}
			}
			
		}

		return locationDetails;
	}

	private String findWalletIdByLocationId(String locationId){
		return locationUserService.findWalletIdByLocationUsers(locationId);
	}
	
	public String getRetailerIdByRetailerName(String retailerName){
		Retailer retailer = getRetailerByRetailerName(retailerName);
		return retailer!=null?retailer.getRetailerId():null;
	}
	
	public Retailer getRetailerByRetailerName(String retailerName){
		return retailerDao.findByName(retailerName);
	}
	
	public List<Retailer> getAllRetailers(){
		return retailerDao.findAllRetailer();
	}
	
	public List<RetailerDetail> getLocationsByRetailer(){
		
		RetailerDetail retailerLocation = null;
		List<RetailerDetail> retailerLocations = new ArrayList<RetailerDetail>();
		List<Retailer> retailers = retailerDao.findAllRetailer();
		
		for(Retailer retailer : retailers ){
			retailerLocation = new RetailerDetail(retailer);
			
			List<LocationDetail> locationDetails = new ArrayList<LocationDetail>();
			
			List<LocationDetail> locationDetailsByRetailer =  locationService.findLocationDetailsByRetailer(retailer.getName());
			
			for(LocationDetail locationDetail : locationDetailsByRetailer){
				Address address = addressService.findById(locationDetail.getAddressId());
				
				if(address!=null){
					locationDetail.setAddress(address);
				}
				
				locationDetails.add(locationDetail);
			}
			
			retailerLocation.setLocationDetails(locationDetails);
			
			retailerLocations.add(retailerLocation);
		}
		
		return retailerLocations;
	}
	
	public Retailer findById(String retailerId){
		return retailerDao.findById(retailerId);
	}
}
