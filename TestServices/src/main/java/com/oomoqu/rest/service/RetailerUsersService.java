package com.oomoqu.rest.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.oomoqu.rest.dao.LocationDao;
import com.oomoqu.rest.dao.RetailerUsersDao;
import com.oomoqu.rest.dao.RoleDao;
import com.oomoqu.rest.dao.UserDao;
import com.oomoqu.rest.dao.UserRolesDao;
import com.oomoqu.rest.dao.WalletUserDao;
import com.oomoqu.rest.exception.CustomException;
import com.oomoqu.rest.model.Location;
import com.oomoqu.rest.model.Response;
import com.oomoqu.rest.model.ResponseStatusCode;
import com.oomoqu.rest.model.Retailer;
import com.oomoqu.rest.model.RetailerUsers;
import com.oomoqu.rest.model.User;
import com.oomoqu.rest.model.UserRoles;
import com.oomoqu.rest.model.WalletUser;
import com.oomoqu.rest.util.CommonUtility;
import com.oomoqu.rest.util.Properties;
import com.oomoqu.rest.wrapper.UserRegistration;

@Component
public class RetailerUsersService {
	private static Logger logger = Logger.getLogger(RetailerUsersService.class);
	
	@Autowired
	RetailerUsersDao retailerUsersDao;
	
	@Autowired
	RoleDao roleDao;
	
	@Autowired
	UserRolesDao userRolesDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	WalletUserDao walletUserDao;
	
	@Autowired
	UserService userService;
	
	@Autowired
	RetailerService retailerService;
	
	@Autowired
	WalletServices walletServices;
	
	@Autowired
	WalletUserService walletUserService;
	
	@Autowired
	LocationDao locationDao;
	
	@Autowired
	LocationUserService locationUserService;
	
	@Autowired
	RetailerService retailService;
	
	public void saveRetailerUser(User user,String retailerId){
		logger.debug("RetailerUsersService -> Inside save Retailer users");
		RetailerUsers retailerUsers = new RetailerUsers();
		retailerUsers.setUserId(user.getUserId());
		retailerUsers.setRetailerId(retailerId);
		retailerUsersDao.save(retailerUsers);
	}
	
	public String findWalletIdByRetailerUsers(String retailerId){
		logger.debug("");
		
		List<RetailerUsers> retailerUsers = retailerUsersDao.findRetailerUsersByRetailerId(retailerId);
		
		if(retailerUsers.isEmpty()){
			return null;
		} else {
			String roleId = roleDao.findRoleByName(Properties.retailerAdminRole).getRoleId();
			
			for(RetailerUsers retailerUser : retailerUsers){
				UserRoles userRoles = userRolesDao.findUserByRole(retailerUser.getUserId(), roleId);
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

	/** save retailer user 
	 * @throws CustomException */
	public Response saveRetailerUser(UserRegistration userRegistration, String retailerName) throws CustomException{
		User user = userRegistration.getUser();
		try {
			
			
			//String retailerId =  retailerService.getRetailerIdByRetailerName(retailerName);
			
			Retailer retailer = retailerService.getRetailerByRetailerName(retailerName);
			
			if(retailer == null){
				throw new CustomException("Retailer does not exist.");
			}
			
			if(!StringUtils.isEmpty(user.getUserId())){
				throw new CustomException("User id should be blank.");
			} else if(!StringUtils.isEmpty(user.getPortalUserId())){
				
				User user1 = userDao.findUserByPortalId(user.getPortalUserId());
				
				if(user1 != null){
					throw new CustomException("User already in the system with this portalUserId.");
				}
			}
			
			userService.saveOrUpdateUser(user);
			
			userService.saveUserRoles(userRegistration);
			
			/** bind retailer user with locations wallet */
			List<Location> locations = locationDao.findLocations(retailer.getPortalRetailerId());
			
			for(Location location : locations){
				
				String walletId = locationUserService.findWalletIdByLocationUsers(location.getLocationId());
				
				if(walletId != null){
					walletUserService.saveWalletUser(user.getUserId(), walletId);
				}
				
			}
			
			this.saveRetailerUser(user, retailer.getRetailerId());
			
			return new Response(ResponseStatusCode.saved, user);
		} catch (CustomException e) {
			// TODO Auto-generated catch block
			String userId = user!=null?user.getUserId():null;
			
			if(!StringUtils.isEmpty(userId)){
				userDao.removeUser(user);
			}
			
			return CommonUtility.caughtError(e.getMessage());
		}
	}
	
	public List<RetailerUsers> findRetailerUsersByRetailerId(String retailerId){
		return retailerUsersDao.findRetailerUsersByRetailerId(retailerId);
	}
	
	public Retailer findRetailerByUserId(String userId){
		RetailerUsers retailerUser = retailerUsersDao.findRetailerUserByUserId(userId);
		if(retailerUser != null)
			return retailerService.findById(retailerUser.getRetailerId());
		else
			return null;
	}
	
}
