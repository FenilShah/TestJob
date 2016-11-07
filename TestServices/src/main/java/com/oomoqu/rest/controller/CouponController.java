
package com.oomoqu.rest.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.oomoqu.rest.exception.CustomException;
import com.oomoqu.rest.model.Coupon;
import com.oomoqu.rest.model.Response;
import com.oomoqu.rest.model.ResponseStatusCode;
import com.oomoqu.rest.model.SharedCoupon;
import com.oomoqu.rest.service.CouponService;
import com.oomoqu.rest.service.TransactionService;
import com.oomoqu.rest.wrapper.TransactionDetail;
import com.oomoqu.rest.wrapper.mobile.BrandDetail;
import com.oomoqu.rest.wrapper.portal.WalletCouponDetail;

@RestController
public class CouponController {
	private static Logger logger = Logger.getLogger(CouponController.class);
	
	@Autowired
	CouponService couponService;

	@Autowired
	TransactionService transactionService;
	
	//http://localhost:8081/TestServices/test/shahfenil07@gmail.com/service
	@RequestMapping(value = "test/{emailId}/service", method = RequestMethod.GET)
	public String testService(@PathVariable("emailId") String emailId) throws CustomException {
		logger.info("Inside active coupons --> emailId " + emailId);
		return emailId;
	}
	
	@PreAuthorize("hasAnyRole('WalletUser','Admin','RetailerAdmin','RetailerBranchAdmin')")
	@RequestMapping(value = "wallets/{emailId}/coupons", method = RequestMethod.GET)
	public Object activeCoupons(@PathVariable("emailId") String emailId) throws CustomException {
		logger.info("Inside active coupons --> emailId " + emailId);
		return couponService.getCouponsByWallet(emailId);
	}
	
	@PreAuthorize("hasAnyRole('WalletUser,Admin')")
	@RequestMapping(value = "wallets/{emailId}/coupons/used", method = RequestMethod.POST)
	public void redeemCoupon(@PathVariable("emailId") String emailId, @RequestBody List<TransactionDetail> transactionDetail) throws CustomException{
		logger.info("Inside coupon redemption for a mobile");
		transactionService.saveCouponTransaction(emailId,transactionDetail);
	}
	
	@PreAuthorize("hasAnyRole('WalletUser,Admin')")
	@RequestMapping(value = "wallets/{emailId}/coupons/history", method = RequestMethod.GET)
	public List<BrandDetail> getHistoryCoupons(@PathVariable("emailId") String emailId) {
		logger.info("Inside retriving history of coupons for a mobile --> email " + emailId);
		return couponService.getHistoryCoupons(emailId);
	}
	
	/*@PreAuthorize("hasAnyRole('WalletUser,Admin')")
	@RequestMapping(value = "coupons/issued/wallet/{walletId}", method = RequestMethod.GET)
	public List<PromotionReport> getIssuedCoupons(@PathVariable("walletId") String walletId) {
		logger.info("Inside getting issued coupons for a mobile --> walletId " + walletId);
		return couponService.getIssuedCoupons(walletId);
	}*/
	
	@PreAuthorize("hasAnyRole('WalletUser,Admin')")
	@RequestMapping(value = "wallets/{emailId}/coupons/share", method = RequestMethod.POST)
	public Response shareCoupons(@PathVariable("emailId") String emailId, @RequestBody SharedCoupon sharedCoupon) throws Exception{
		logger.info("Inside sharing coupons --> walletId " + sharedCoupon.getSenderWalletId() + " login type is " + sharedCoupon.getLoginType() + " and loginId is " + sharedCoupon.getLoginId());
		couponService.shareCoupon(emailId, sharedCoupon);
		Response response = new Response(ResponseStatusCode.saved);
		return response;
	}
	
	@PreAuthorize("hasAnyRole('CampaignUser,BrandAdmin,Admin')")
	@RequestMapping(value = "tempBarcodeForCoupon", method = RequestMethod.POST)
	public Coupon generateTempBarcode(@RequestBody Coupon coupon ){
		logger.info("Coupon TempBarcode Temp Field...");
		return couponService.generateTempBarcode(coupon);
	}
	
	@PreAuthorize("hasAnyRole('Admin,RetailerAdmin,RetailerBranchAdmin')")
	@RequestMapping(value = "retailer/payment", method = RequestMethod.POST)
	public void getCouponsForPayment(@RequestBody List<String> couponIds) {
		logger.info("Inside get all couponIds For Payment");
		couponService.couponsForPayments(couponIds);
	}
	
	@PreAuthorize("hasAnyRole('Admin,RetailerAdmin,RetailerBranchAdmin')")
	@RequestMapping(value = "retailer/{retailerName}/location/{locationName}/coupons/validate", method = RequestMethod.POST)
	public Object couponValidation(@PathVariable("retailerName") String retailerName, @PathVariable("locationName") String locationName, @RequestBody WalletCouponDetail walletCouponDetail){
		logger.info("Inside validate coupon");
		return couponService.validateCoupon(retailerName, locationName, walletCouponDetail);
	}
	
	@PreAuthorize("hasAnyRole('Admin,RetailerAdmin,RetailerBranchAdmin')")
	@RequestMapping(value = "coupons/redeem", method = RequestMethod.POST)
	public Object redeemCoupon(@RequestBody WalletCouponDetail walletCouponDetail){
		logger.info("Inside coupon redemption for a mobile");
		return couponService.redeemCouponValue(walletCouponDetail);
	}
	
	@PreAuthorize("hasAnyRole('WalletUser','Admin','RetailerAdmin','RetailerBranchAdmin')")
	@RequestMapping(value = "brand/{brandName}/wallets/{emailId}/coupons/amount/{amount}", method = RequestMethod.GET)
	public Object createCouponForCoin(@PathVariable("brandName") String brandName, @PathVariable("emailId") String emailId, @PathVariable("amount") Integer amount) throws CustomException {
		logger.info("Inside createCouponForCoin --> emailId " + emailId);
		return couponService.createCouponForCoin(brandName, emailId, amount);
	}
	
	@PreAuthorize("hasAnyRole('WalletUser','Admin','BrandAdmin')")
	@RequestMapping(value = "wallets/{emailId}/paperCoupon/{paperPromotionBarcodeString}", method = RequestMethod.GET)
	public Object convertPaperCouponToDigitalCoupon(@PathVariable("emailId") String emailId, @PathVariable("paperPromotionBarcodeString") String paperPromotionBarcodeString){
		logger.info("Inside Convert PaperCoupon To DigitalCoupon --> emailId " + emailId + " : papercouponString " + paperPromotionBarcodeString );
		return couponService.scanPaperCoupon(emailId, paperPromotionBarcodeString);
	}
	
}
