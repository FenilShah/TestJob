package com.oomoqu.rest.util;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.time.DateUtils;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oomoqu.rest.model.Response;
import com.oomoqu.rest.model.ResponseStatusCode;
import com.oomoqu.rest.model.User;
import com.oomoqu.rest.model.UserDetails;

@Component
public class CommonUtility {
	
	public String prepareObjectToJson(Object object){
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonResult = null;
		try {
			jsonResult = objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonResult;
	}
	
	public String failedToPerformOperation(){
		JSONObject failedJObject = new JSONObject();
		failedJObject.put("success", 0);
		failedJObject.put("error", 1);
		failedJObject.put("error_msg", "already registered");
		return failedJObject.toString();
	}
	
	public String invalidField(){
		JSONObject invalidJObject = new JSONObject();
		invalidJObject.put("success", 0);
		invalidJObject.put("error", 1);
		invalidJObject.put("error_msg", "field not found");
		return invalidJObject.toString();
	}
	
	public String failedToRegisterOomoquUser(){
		JSONObject registrationFailJObject = new JSONObject();
		registrationFailJObject.put("success", 0);
		registrationFailJObject.put("error", 1);
		registrationFailJObject.put("error_msg", "failed to register oomoqu user");
		return registrationFailJObject.toString();
	}
	
	public String removeObject(){
		JSONObject removedJObject = new JSONObject();
		removedJObject.put("success", 1);
		removedJObject.put("error", 0);
		removedJObject.put("success_msg", "delete successfull");
		return removedJObject.toString();
	}
	
	public static String updateObject(){
		JSONObject updatedJObject = new JSONObject();
		updatedJObject.put("success", 1);
		updatedJObject.put("error", 0);
		updatedJObject.put("success_msg", "update successfull");
		return updatedJObject.toString();
	}
	
	public static String failedToUpdateObject(){
		JSONObject updatedJObject = new JSONObject();
		updatedJObject.put("success", 0);
		updatedJObject.put("error", 1);
		updatedJObject.put("success_msg", "failed to udpate a record");
		return updatedJObject.toString();
	}
	
	public String setWalletToRegisterUser(String walletID){
		JSONObject getWalletIdJObject = new JSONObject();
		getWalletIdJObject.put("success", 1);
		getWalletIdJObject.put("error", 0);
		getWalletIdJObject.put("walletId", walletID);
		return getWalletIdJObject.toString();
	}
	
	public String operationFailure(String error_message){
		JSONObject failureJObject = new JSONObject();
		failureJObject.put("success", 0);
		failureJObject.put("error", 1);
		failureJObject.put("error_msg", error_message);
		return failureJObject.toString();
	}
	
	public String successOperation(String success_message){
		JSONObject successJObject = new JSONObject();
		successJObject.put("success", 1);
		successJObject.put("error", 0);
		successJObject.put("success_message", success_message);
		return successJObject.toString();
	}
	
	@SuppressWarnings("unchecked")
	public static List<GrantedAuthority> getAuthorities(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (List<GrantedAuthority>) authentication.getAuthorities();
	}
	
	public static Boolean findRole(String roleName){
		List<GrantedAuthority> roles = CommonUtility.getAuthorities();
		for(GrantedAuthority role: roles){
			if(roleName.contains(role.getAuthority())){
				return true;
			}
		}
		return false;
	}
	
	public static Boolean findExactRole(String roleName){
		List<GrantedAuthority> roles = CommonUtility.getAuthorities();
		for(GrantedAuthority role: roles){
			if(!roleName.equals(role.getAuthority())){
				return false;
			}
		}
		return true;
	}
	
	public static User getUser(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		return userDetails.getUser();
	}
	
	public static Boolean compareDateBeforeAndEquals(Date date1, Date date2){
		if(date1 == null || date2 == null)
			return null;
		return date1.before(date2) || DateUtils.isSameDay(date1, date2);
	}
	
	public static Boolean compareDateAfterAndEquals(Date date1, Date date2){
		if(date1 == null || date2 == null)
			return null;
		return date1.after(date2) || DateUtils.isSameDay(date1, date2);
	}
	
	public static String getFormattedDate(Date date){
		if(date == null){
			return null;
		}
		return new SimpleDateFormat("MM/dd/yyyy").format(date);
	}
	
	public static Date getDateFromString(String stringDate){
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		try {
			return formatter.parse(stringDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static int randInt(int min, int max) {

	    // NOTE: Usually this should be a field rather than a method
	    // variable so that it is not re-seeded every call.
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	
	public static Date getCurrentDate(){
		return new Date();
	}
	
	public static Response caughtError(String reason){
		Response response = new Response(ResponseStatusCode.failed);
		response.setReason(reason);
		return response;
	}
	
	public static String decodeBase64(String s){
		if(s != null){
			try {
				return new String(Base64.decodeBase64(s),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				return null;
			}
		}else{
			return null;
		}
	}
}