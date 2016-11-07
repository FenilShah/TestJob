package com.oomoqu.rest.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oomoqu.rest.dao.BatchDetailDao;
import com.oomoqu.rest.dao.BatchMasterDao;
import com.oomoqu.rest.dao.BrandDao;
import com.oomoqu.rest.dao.CouponDao;
import com.oomoqu.rest.dao.LocationDao;
import com.oomoqu.rest.dao.PromotionDao;
import com.oomoqu.rest.dao.RetailerDao;
import com.oomoqu.rest.dao.SequenceDao;
import com.oomoqu.rest.exception.CustomException;
import com.oomoqu.rest.model.BatchDetail;
import com.oomoqu.rest.model.BatchMaster;
import com.oomoqu.rest.model.Coupon;
import com.oomoqu.rest.model.Location;
import com.oomoqu.rest.model.Promotion;
import com.oomoqu.rest.model.Response;
import com.oomoqu.rest.model.ResponseStatusCode;
import com.oomoqu.rest.model.Retailer;
import com.oomoqu.rest.util.CommonUtility;
import com.oomoqu.rest.wrapper.portal.BrandDetail;
import com.oomoqu.rest.wrapper.portal.LocationDetail;
import com.oomoqu.rest.wrapper.portal.PromotionDetail;
import com.oomoqu.rest.wrapper.portal.RetailerDetail;

@Service
public class BatchService {
	
	@Autowired
	CouponDao couponDao;
	
	@Autowired
	PromotionDao promotionDao;
	
	@Autowired
	SequenceDao sequenceDao;
	
	@Autowired
	BatchMasterDao batchMasterDao;
	
	@Autowired
	BatchDetailDao batchDetailDao;
	
	@Autowired
	LocationService locationService;
	
	@Autowired
	BrandService brandService;
	
	@Autowired
	BrandDao brandDao;
	
	@Autowired
	LocationDao locationDao;
	
	@Autowired
	RetailerDao retailerDao;
	
	public Response createBatch(List<BatchDetail> batchDetails, String retailerName, String locationName) throws CustomException{
		Location location = this.getLocation(retailerName, locationName);
		
		List<BatchMaster> batchMasters =  this.creatBatches(batchDetails, location.getLocationId());
		
		return new Response(ResponseStatusCode.saved, batchMasters);
	}
	
	public List<BatchMaster> creatBatches(List<BatchDetail> batchDetails, String locationId) throws CustomException{
		Map<String, BatchMaster> batchMasters = new HashMap<String, BatchMaster>();
		
		for(BatchDetail batchDetail : batchDetails){
			String couponId = batchDetail.getCouponId();
			Coupon coupon = couponDao.findById(couponId);
			if(coupon == null){
				throw new CustomException("Coupon not found.");
			}
			coupon.setPaymentRequestedDate(CommonUtility.getCurrentDate());
			couponDao.createCoupon(coupon);
			String promotionId = coupon.getPromotionId();
			
			BatchMaster batchMaster = batchMasters.get(promotionId);
			if(batchMaster == null){
				batchMaster = new BatchMaster();
				batchMaster.setBatchNo(sequenceDao.getNextSequence("BATCH"));
				batchMaster.setCreatedDate(CommonUtility.getCurrentDate());
				batchMaster.setRequestedAmount(coupon.getReedemedAmount()==null?0.0:coupon.getReedemedAmount());
				batchMaster.setStatus("Pending");
				batchMaster.setLocationId(locationId);
				batchMaster.setPromotionId(promotionId);
			}else{
				Double redeemptionAmount = coupon.getReedemedAmount()==null?0.0:coupon.getReedemedAmount();
				batchMaster.setRequestedAmount(batchMaster.getRequestedAmount() + redeemptionAmount);
			}
			
			batchMasterDao.save(batchMaster);
			batchMasters.put(promotionId, batchMaster);
			
			batchDetail.setBatchId(batchMaster.getBatchId());
			batchDetailDao.save(batchDetail);
		}
		
		return new ArrayList<BatchMaster>(batchMasters.values());
	}
	
	public Object listBatches(String retailerName, String locationName) throws CustomException{
		Location location = this.getLocation(retailerName, locationName);
		
		List<BatchMaster> batchMasters = batchMasterDao.findByLocationId(location.getLocationId());
		return batchMasters;
	}
	
	public Object listBatchDetails(String retailerName, String locationName, Long batchNo) throws CustomException{
		this.getLocation(retailerName, locationName);

		return this.getBatchDetailByBatchNo(batchNo);
	}
	
	public Location getLocation(String retailerName, String locationName) throws CustomException{
		Location location = locationService.getLocationByLocationName(retailerName, locationName);
		
		if(location == null){
			throw new CustomException("Location not found.");
		}
		return location;
	}
	
	public Object listBrandBatches(String brandName, String promotionName) throws CustomException{
		Promotion promotion = this.getPromotion(brandName, promotionName);
		return batchMasterDao.findByPromotionId(promotion.getPromotionId());
	}
	
	public Object listBrandBatchDetails(String brandName, String promotionName, Long batchNo) throws CustomException{
		this.getPromotion(brandName, promotionName);

		return this.getBatchDetailByBatchNo(batchNo);
	}

	
	public Promotion getPromotion(String brandName, String promotionName) throws CustomException{
		String brandId = brandService.getBrandIdByBrandName(brandName);
		Promotion promotion = promotionDao.findPromotionByNameAndBrand(brandId, promotionName);
		
		if(promotion == null){
			throw new CustomException("promotion not found.");
		}
		return promotion;
	}
	
	public List<BatchDetail> getBatchDetailByBatchNo(Long batchNo) throws CustomException{
		BatchMaster batchMaster = batchMasterDao.findByBatchNo(batchNo);
		
		if(batchMaster == null){
			throw new CustomException("Batch Not found.");
		}
		
		List<BatchDetail> batchDetails = batchDetailDao.findByBatchId(batchMaster.getBatchId());
		return batchDetails;
	}
	
	public Response updateBatches(List<BatchDetail> batchDetails, String brandName, String promotionName ) throws CustomException{
		this.getPromotion(brandName, promotionName);
		List<BatchDetail> batchDetail = this.updateBatches(batchDetails);
		return new Response(ResponseStatusCode.updated, batchDetail);
	}
	
	public List<BatchDetail> updateBatches(List<BatchDetail> batchDetails) throws CustomException{
		Map<String, List<BatchDetail>> batchMasters = new HashMap<String, List<BatchDetail>>();
		
		for(BatchDetail batchDetail : batchDetails){
			if(!"Approved".equals(batchDetail.getStatus()) && !"Rejected".equals(batchDetail.getStatus())){
				throw new CustomException("Invalid status.");
			}
			
			String batchId = batchDetail.getBatchId();
			List<BatchDetail> mapBatchDetails = batchMasters.get(batchId);
			if(mapBatchDetails == null){
				mapBatchDetails = new ArrayList<BatchDetail>();
			}
			mapBatchDetails.add(batchDetail);
			batchMasters.put(batchId, mapBatchDetails);
		}
		
		for(Map.Entry<String, List<BatchDetail>> sortedBatchMaster : batchMasters.entrySet()){
			List<BatchDetail> sortedBatchDetails = sortedBatchMaster.getValue();
			boolean approved = true;
			for(BatchDetail batchDetail : sortedBatchDetails){
				String couponId = batchDetail.getCouponId();
				Coupon coupon = couponDao.findById(couponId);
				
				if("Approved".equals(batchDetail.getStatus())){
					coupon.setApprovedDate(CommonUtility.getCurrentDate());
					coupon.setRejectedDate(null);
				}else{
					approved = false;
					coupon.setRejectedDate(CommonUtility.getCurrentDate());
					coupon.setApprovedDate(null);
				}
				couponDao.createCoupon(coupon);
				BatchMaster batchMaster = batchMasterDao.findById(sortedBatchMaster.getKey());
				if(approved){
					batchMaster.setStatus("Approved");
				}else{
					batchMaster.setStatus("Rejected");
				}
				
				batchMasterDao.save(batchMaster);
			}
		}	
		return batchDetails;
	}
	
	public Object listBatchesWrapper(String retailerName) throws CustomException{
		List<Location> locations = locationService.findLocationsByRetailer(retailerName);
		List<LocationDetail> locationDetails = new ArrayList<LocationDetail>();
		for(Location location : locations){
			LocationDetail locationDetail = listBatchesWrapper(location);
			locationDetails.add(locationDetail);
		}
		return locationDetails;
	}
	
	public Object listBatchesWrapper(String retailerName, String locationName) throws CustomException{
		Location location = this.getLocation(retailerName, locationName);
		return this.listBatchesWrapper(location);
	}
	
	public LocationDetail listBatchesWrapper(Location location) throws CustomException{
		List<BatchMaster> batchMastersRaw = batchMasterDao.findByLocationId(location.getLocationId());
		
		Map<String, List<BatchMaster>> batchMasters = new HashMap<String, List<BatchMaster>>();
		Map<String, List<PromotionDetail>> promotionsMap = new HashMap<String, List<PromotionDetail>>(); 
		
		for(BatchMaster batchMaster : batchMastersRaw){
			List<BatchDetail> batchDetails = getBatchDetailByBatchNo(batchMaster.getBatchNo());
			Coupon coupon = null;
			for(BatchDetail batchDetail : batchDetails){
				coupon = couponDao.findById(batchDetail.getCouponId());
				batchDetail.setCoupon(coupon);
			}
			batchMaster.setBatchDetails(batchDetails);
			
			List<BatchMaster> batchMasterList = batchMasters.get(batchMaster.getPromotionId());
			if(batchMasterList == null){
				batchMasterList = new ArrayList<BatchMaster>();
			}
			batchMasterList.add(batchMaster);
			batchMasters.put(batchMaster.getPromotionId(), batchMasterList);
		}
		
		for(Map.Entry<String, List<BatchMaster>> data : batchMasters.entrySet()){
			
			Promotion promotion = promotionDao.findPromotioanBypromotionId(data.getKey());
			PromotionDetail promotionDetail = new PromotionDetail(promotion);
			promotionDetail.setBatchMasters(data.getValue());
			
			List<PromotionDetail> promotionDetails = promotionsMap.get(promotion.getBrandId());
			if(promotionDetails == null){
				promotionDetails = new ArrayList<PromotionDetail>();
			}
			promotionDetails.add(promotionDetail);
			
			promotionsMap.put(promotion.getBrandId(), promotionDetails);
		}
		
		List<BrandDetail> brandDetails = new ArrayList<BrandDetail>();
		for(Map.Entry<String, List<PromotionDetail>> data : promotionsMap.entrySet()){
			BrandDetail brandDetail = brandDao.findBrandBybrandId(data.getKey());
			
			brandDetail.setPromotionDetails(data.getValue());
			brandDetails.add(brandDetail);
		}
		
		
		LocationDetail locationDetail = locationDao.findLocationReportByLocationId(location.getLocationId());
		locationDetail.setBrandDetails(brandDetails);
		return locationDetail;
	}
	
	public Object listBatchesWrapperForBrand(String brandName, String status) throws CustomException{
		String brandId = brandService.getBrandIdByBrandName(brandName);
		
		List<Promotion> promotions = promotionDao.getPromotionListByBrandId(brandId, null, null);
		
		List<PromotionDetail> promotionDetails = new ArrayList<PromotionDetail>();
		for(Promotion promotion : promotions){
			PromotionDetail promotionDetail = this.listBatchesWrapper(promotion, status);
			if(promotionDetail != null){
				promotionDetails.add(promotionDetail);
			}	
		}
		return promotionDetails;
	}
	
	public PromotionDetail listBatchesWrapper(Promotion promotion, String status) throws CustomException{
		boolean statusFlag;
		if("new".equals(status)){
			statusFlag = true;
		}else if("history".equals(status)){
			statusFlag = false;
		}else{
			throw new CustomException("Invalid status.");
		}
		
		
		List<BatchMaster> batchMastersRaw = batchMasterDao.findByPromotionAndStatus(promotion.getPromotionId(), statusFlag);
		if(batchMastersRaw.isEmpty()){
			return null;
		}
		
		Map<String, List<BatchMaster>> batchMasters = new HashMap<String, List<BatchMaster>>();
		Map<String, List<LocationDetail>> locationsMap = new HashMap<String, List<LocationDetail>>(); 
		
		for(BatchMaster batchMaster : batchMastersRaw){
			List<BatchDetail> batchDetails = getBatchDetailByBatchNo(batchMaster.getBatchNo());
			Coupon coupon = null;
			for(BatchDetail batchDetail : batchDetails){
				coupon = couponDao.findById(batchDetail.getCouponId());
				batchDetail.setCoupon(coupon);
			}
			batchMaster.setBatchDetails(batchDetails);
			
			List<BatchMaster> batchMasterList = batchMasters.get(batchMaster.getLocationId());
			if(batchMasterList == null){
				batchMasterList = new ArrayList<BatchMaster>();
			}
			batchMasterList.add(batchMaster);
			batchMasters.put(batchMaster.getLocationId(), batchMasterList);
		}
		
		for(Map.Entry<String, List<BatchMaster>> data : batchMasters.entrySet()){
			LocationDetail locationDetail = locationDao.findLocationReportByLocationId(data.getKey());
			locationDetail.setBatchMasters(data.getValue());
			
			List<LocationDetail> locationDetails = locationsMap.get(locationDetail.getRetailerId());
			if(locationDetails == null){
				locationDetails = new ArrayList<LocationDetail>();
			}
			locationDetails.add(locationDetail);
			
			locationsMap.put(locationDetail.getRetailerId(), locationDetails);
		}
		
		
		List<RetailerDetail> retailerDetails = new ArrayList<RetailerDetail>();
		for(Map.Entry<String, List<LocationDetail>> data : locationsMap.entrySet()){
			Retailer retailer = retailerDao.findById(data.getKey());
			
			RetailerDetail retailerDetail = new RetailerDetail(retailer);
			retailerDetail.setLocationDetails(data.getValue());
			
			retailerDetails.add(retailerDetail);
		}
		
		PromotionDetail promotionDetail = new PromotionDetail(promotion);
		promotionDetail.setRetailerDetails(retailerDetails);
		return promotionDetail;
	}
}
