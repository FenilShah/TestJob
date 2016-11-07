package com.oomoqu.rest.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.oomoqu.rest.dao.BrandDao;
import com.oomoqu.rest.dao.BrandUsersDao;
import com.oomoqu.rest.dao.LocationDao;
import com.oomoqu.rest.dao.LocationUsersDao;
import com.oomoqu.rest.dao.PromotionDao;
import com.oomoqu.rest.dao.RoleDao;
import com.oomoqu.rest.dao.UserDao;
import com.oomoqu.rest.dao.UserRolesDao;
import com.oomoqu.rest.dao.WalletDao;
import com.oomoqu.rest.dao.WalletUserDao;
import com.oomoqu.rest.exception.CustomException;
import com.oomoqu.rest.model.Response;
import com.oomoqu.rest.model.ResponseStatusCode;
import com.oomoqu.rest.model.Role;
import com.oomoqu.rest.model.User;
import com.oomoqu.rest.model.UserRoles;
import com.oomoqu.rest.model.Wallet;
import com.oomoqu.rest.util.CommonUtility;
import com.oomoqu.rest.util.Properties;
import com.oomoqu.rest.util.Security;
import com.oomoqu.rest.wrapper.UserRegistration;


@Component
public class UserService {
	private static Logger logger = Logger.getLogger(UserService.class);
	
	@Autowired
	UserDao userDao;

	@Autowired
	CommonUtility commonUtility;

	@Autowired
	UserRolesDao userRoleDao;

	@Autowired
	RoleDao roleDao;

	@Autowired
	WalletUserDao walletUserDao;

	@Autowired
	WalletDao walletDao;

	@Autowired
	WalletUserService walletUserService;

	@Autowired
	WalletServices walletServices;

	@Autowired
	BrandUsersDao brandUsersDao;

	@Autowired 
	BrandUserServices brandUserServices;

	@Autowired
	BrandDao brandDao;
	
	@Autowired
	CouponService couponService;
	
	@Autowired
	LocationUsersDao locationUsersDao;
	
	@Autowired
	LocationDao locationDao;
	
	@Autowired
	LocationUserService locationUserService;
	
	@Autowired
	WalletUserDao WalletUserDao;
	
	@Autowired
	PromotionDao promotionDao;
	
	@Autowired
	BrandService brandService;
	
	@Autowired
	RetailerService retailerService;
	
	@Autowired
	RetailerUsersService retailerUsersService;
	
	@Autowired
	private CoinService coinService;
	
	//@Value("${portal.companyId}") String companyId;
	
	public Response saveBrandUser(UserRegistration userRegistration, String brandName){
		User user = userRegistration.getUser();
		try {
			
			String brandId = brandService.getBrandIdByBrandName(brandName);
			if(StringUtils.isEmpty(brandId)){
				throw new CustomException("Brand does not exist.");
			}

			if(CollectionUtils.isEmpty(userRegistration.getRoles())){
				throw new CustomException("Please provide roles.");
			}
			
			if(!StringUtils.isEmpty(user.getUserId())){
				throw new CustomException("User id should be blank.");
			} else if(!StringUtils.isEmpty(user.getPortalUserId())){
				User user1 = userDao.findUserByPortalId(user.getPortalUserId());

				if(user1 != null){
					throw new CustomException("User already in the system with this portalUserId.");
				}
			}

			this.saveOrUpdateUser(user);
			
			saveUserRoles(userRegistration);
			
			if(this.isRoleExist(userRegistration.getRoles(), Properties.brandAdminRole)){
				String walletId = brandUserServices.findWalletIdByBrandUsers(brandId);
				
				if(walletId == null){
					Wallet wallet = walletServices.saveWallet(userRegistration.getWallet());
					walletId = wallet.getWalletId();
				}
				
				walletUserService.saveWalletUser(userRegistration.getUser().getUserId(), walletId);
			}
			
			brandUserServices.saveBrandUser(user, brandId);
			
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
	
	public Response updateUser(UserRegistration userRegistration, String emailId){
		User user = userDao.findByEmailId(emailId);
		
			try {
				if(user == null){
					throw new CustomException("User not available in a system to update.");
				}else{
					userRegistration.getUser().setUserId(user.getUserId());
					this.saveOrUpdateUser(userRegistration.getUser());
					return new Response(ResponseStatusCode.updated);
				}
			} catch (CustomException e) {
				// TODO Auto-generated catch block
				return CommonUtility.caughtError(e.getMessage());
			}
		
	}
	
	public User saveOrUpdateUser(User user) throws CustomException{
		logger.debug(" UserService -> Inside save or update User");
		
		if(validateUser(user)){
			user.setCreatedDate(CommonUtility.getCurrentDate());
			user.setModifiedDate(CommonUtility.getCurrentDate());
			user.setPassword(Security.getEncryptedPasswordWithHMAC(user.getPassword()));
			userDao.save(user);
		}else{
			throw new CustomException("Duplicate email.");
		}
		
		return user;
	}
	
	public Response saveAppUser(UserRegistration userRegistration){
		try {
			User user = userRegistration.getUser();
			if(!StringUtils.isEmpty(user.getUserId())){
				throw new CustomException("User id should be blank.");
			}
			
			if(!userDao.validateUser(user)){   //old user
				User oldUser = userDao.findByEmailId(user.getEmail());
				
				Wallet wallet = walletServices.findWalletByEmailId(user.getEmail());
				if(wallet.getDeviceId().equals(userRegistration.getWallet().getDeviceId())){
					user.setUserId(oldUser.getUserId());
					user.setModifiedDate(CommonUtility.getCurrentDate());
					user.setPassword(Security.getEncryptedPasswordWithHMAC(user.getPassword()));
					userDao.save(user);
					return new Response(ResponseStatusCode.saved,user);
				}else{
					return new Response(ResponseStatusCode.failed,"User already available with this Email Id.");
				}
			}else{
				this.saveOrUpdateUser(user);
				
				saveUserRoles(userRegistration);
				
				Wallet wallet = walletServices.saveWallet(userRegistration.getWallet());
				walletUserService.saveWalletUser(userRegistration.getUser().getUserId(), wallet.getWalletId());
				
				logger.debug(" User Registration successfull with userId: " + user.getUserId() + "walletId:" + wallet.getWalletId());
				
				couponService.findSharedCoupons(user);
				
				couponService.issueCoupons(user, wallet.getWalletId());
				
				coinService.distributeCoins(user, wallet);
				
				return new Response(ResponseStatusCode.saved,user);
			}
			
		} catch (CustomException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return CommonUtility.caughtError(e.getMessage());
		}
	}

	public boolean isRoleExist(List<Role> roles, String roleName){
		for(Role role: roles){
			if(roleName.equals(role.getAuthority())){
				return true;
			}
		}
		return false;
	}
	
	public Response deleteUser(String emailId){
		User user = userDao.deleteUser(emailId);
		if(user != null){
			return new Response(ResponseStatusCode.deleted);
		}else{
			return new Response(ResponseStatusCode.failed,"User does not exist.");
		}
	}
	
	public Object findUserByuserId(String emailId){
		logger.debug("UserService -> Inside find User By emailId" + emailId);
		User user = userDao.findByEmailId(emailId);
		if(user != null){
			return user;
		}else{
			return new Response(800,"User does not exist.");
		}
	}
	
	/** validate user email already registered or not 
	 * @throws CustomException */
	private boolean validateUser(User user) throws CustomException{
		logger.debug("UserService -> Inside validate UserEmail");
		if(StringUtils.isEmpty(user.getFirstName())){
			throw new CustomException("Please provide first name");
		}else if(StringUtils.isEmpty(user.getEmail())){
			throw new CustomException("Please provide email address");
		}
		return userDao.validateUser(user);
	}
	
	public void saveUserRoles(UserRegistration userRegistrationWrapper) throws CustomException{
		logger.debug("UserService -> Inside save UserRoles");
		  User user = userRegistrationWrapper.getUser();
		  
		  UserRoles userRoles = new UserRoles();
		  userRoles.setUserId(user.getUserId());
		  
		  Role role = null;
		  /*** Following part is use when first time database created to register user and roles
		  1 If. Brand Registration default user
		  2 else If. User Registration default user
		  ***/
		  
		  List<Role> roles = userRegistrationWrapper.getRoles();
		  List<UserRoles> userRolesList = new ArrayList<UserRoles>();
		  
		  if(!CollectionUtils.isEmpty(roles)){
			  userRoles = new UserRoles();
			  userRoles.setUserId(user.getUserId());
			  for(Role role1 : roles){
				  role = new Role();
				  role = roleDao.findRoleByName(role1.getName());
				  
				  if(role != null){
					  userRoles.setRoleId(role.getRoleId());
					  userRolesList.add(userRoles);
				  }else{
					  throw new CustomException("Role is not defined in the system");
				  }
			  }
			  
			  userRoleDao.saveUserRoles(userRolesList);
		  }else{
			  throw new CustomException("Please specify user roles.");
		  }
	}
	
	/** operation method for save user and register user in oomoqu-system */
	/*public String saveOrUpdateLocationUser(UserRegistration userRegistrationWrapper){
		logger.debug("UserService -> Inside saveOrUpdate LocationUser");
		LocationUsers locationUsers = new LocationUsers();
		Location locationByPortalLocationId = locationDao.findByPortalLocationId(userRegistrationWrapper.getLocation().getPortalLocationId());
		if(locationByPortalLocationId != null)
			locationUsers.setLocationId(locationByPortalLocationId.getLocationId());
		locationUsers.setUserId(userRegistrationWrapper.getUser().getPortalUserId());
		locationUsersDao.saveLocationUsers(locationUsers);
		return commonUtility.prepareObjectToJson(locationUsers);
	}*/
	
}
