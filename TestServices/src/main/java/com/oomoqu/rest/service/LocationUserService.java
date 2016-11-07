package com.oomoqu.rest.service;


import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.oomoqu.rest.dao.LocationUsersDao;
import com.oomoqu.rest.dao.RoleDao;
import com.oomoqu.rest.dao.UserDao;
import com.oomoqu.rest.dao.UserRolesDao;
import com.oomoqu.rest.dao.WalletUserDao;
import com.oomoqu.rest.exception.CustomException;
import com.oomoqu.rest.model.Location;
import com.oomoqu.rest.model.LocationUsers;
import com.oomoqu.rest.model.Response;
import com.oomoqu.rest.model.ResponseStatusCode;
import com.oomoqu.rest.model.Retailer;
import com.oomoqu.rest.model.RetailerUsers;
import com.oomoqu.rest.model.User;
import com.oomoqu.rest.model.UserRoles;
import com.oomoqu.rest.model.Wallet;
import com.oomoqu.rest.model.WalletUser;
import com.oomoqu.rest.util.CommonUtility;
import com.oomoqu.rest.util.Properties;
import com.oomoqu.rest.wrapper.UserRegistration;

@Component
public class LocationUserService {
	private static Logger logger = Logger.getLogger(LocationUserService.class);
	
	@Autowired
	LocationService locationService;
	
	@Autowired
	LocationUsersDao locationUsersDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	UserService userService;
	
	@Autowired
	RoleDao roleDao;
	
	@Autowired
	UserRolesDao userRoleDao;
	
	@Autowired
	WalletUserDao walletUserDao;
	
	@Autowired
	WalletServices walletServices;
	
	@Autowired
	WalletUserService walletUserService;
	
	@Autowired
	RetailerService retailerService;
	
	@Autowired
	RetailerUsersService retaierUsersService;
	
	public Response saveLocationUser(UserRegistration userRegistration, String retailerName, String locationName) throws CustomException {
		User user = userRegistration.getUser();
		try{	
			
			Retailer retailer = retailerService.getRetailerByRetailerName(retailerName);
			
			if(retailer==null){
				throw new CustomException("Retailer does not exist.");
			}
			
			Location location = locationService.getLocationByLocationName(retailer.getName(), locationName);
			
			if(location == null){
				throw new CustomException("Location does not exist.");
			}
			
			if(!StringUtils.isEmpty(user.getUserId())){
				throw new CustomException("User is should be Blank");
			} else if(!StringUtils.isEmpty(user.getPortalUserId())){
				User user1 = userDao.findUserByPortalId(user.getPortalUserId());
				
				if(user1 != null){
					throw new CustomException("User already in the system with this portalUserId.");
				}
			}	
				userService.saveOrUpdateUser(user);
				
				userService.saveUserRoles(userRegistration);

				if(userService.isRoleExist(userRegistration.getRoles(), Properties.retailerBranchAdminRole)){
					String walletId = this.findWalletIdByLocationUsers(location.getLocationId());
					
					if(walletId == null){
						Wallet wallet = walletServices.saveWallet(userRegistration.getWallet());
						walletId = wallet.getWalletId();
					}
					
					walletUserService.saveWalletUser(userRegistration.getUser().getUserId() , walletId);
					
					List<RetailerUsers> retailerUsers = retaierUsersService.findRetailerUsersByRetailerId(retailer.getRetailerId());
					
					for(RetailerUsers retailerUser : retailerUsers){
						walletUserService.saveWalletUser(retailerUser.getUserId() , walletId);
					}
					
				}
				
				this.saveLocationUser(userRegistration.getUser(),location.getLocationId());
				return new Response(ResponseStatusCode.saved, user);
		} catch(CustomException e){
			// TODO Auto-generated catch block
			String userId = user!=null?user.getUserId():null;
						
			if(!StringUtils.isEmpty(userId)){
				userDao.removeUser(user);
			}
			
			return CommonUtility.caughtError(e.getMessage());
		}
	}

	public String findWalletIdByLocationUsers(String locationId){
		List<LocationUsers> locationUsers = locationUsersDao.findLocationUsersByLocationId(locationId);
		
		if( locationUsers.isEmpty()){
			return null;
		} else {
			String roleId = roleDao.findRoleByName(Properties.retailerBranchAdminRole).getRoleId();
			
			for(LocationUsers locationUser : locationUsers){
				UserRoles userRoles = userRoleDao.findUserByRole(locationUser.getUserId(), roleId);
				
				if(userRoles!=null){
					String userId = userRoles.getUserId();
					User user = userDao.findById(userId);
					
					if(user.getIsDeleted()==null?false:user.getIsDeleted()){
						continue;
					}
					
					WalletUser walletUser = walletUserDao.findWalletUserByUserId(userId);
					return walletUser.getWalletId();
				}
			}
			return null;
		}
	}
	
	public void saveLocationUser(User user, String locationId){
		logger.debug("LocationUserService -- > Inside save LocationUser");
		LocationUsers locationUsers = new LocationUsers();
		locationUsers.setUserId(user.getUserId());
		locationUsers.setLocationId(locationId);
		locationUsersDao.saveLocationUsers(locationUsers);
	}
	
	public String findLocationIdByUserId(String userId){
		LocationUsers locationUsers = locationUsersDao.findLocationUsersByUserId(userId);
		if(locationUsers != null)
			return locationUsers.getLocationId();
		else
			return null;
	}
	
	public String findLocationIdByUser(){
		logger.debug("LocationUserService -- > Inside getLocationIdByUser");
		User user = CommonUtility.getUser();
		LocationUsers locationUsers = locationUsersDao.findLocationUsersByUserId(user.getUserId());
		return locationUsers.getLocationId();
	}
}
