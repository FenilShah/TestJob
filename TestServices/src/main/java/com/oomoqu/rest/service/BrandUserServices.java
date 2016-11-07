package com.oomoqu.rest.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oomoqu.rest.dao.BrandDao;
import com.oomoqu.rest.dao.BrandUsersDao;
import com.oomoqu.rest.dao.RoleDao;
import com.oomoqu.rest.dao.UserDao;
import com.oomoqu.rest.dao.UserRolesDao;
import com.oomoqu.rest.dao.WalletUserDao;
import com.oomoqu.rest.exception.CustomException;
import com.oomoqu.rest.model.BrandUsers;
import com.oomoqu.rest.model.User;
import com.oomoqu.rest.model.UserRoles;
import com.oomoqu.rest.model.WalletUser;
import com.oomoqu.rest.util.CommonUtility;
import com.oomoqu.rest.util.Properties;

@Component
public class BrandUserServices {
	private static Logger logger = Logger.getLogger(BrandUserServices.class);
	
	@Autowired
	BrandUsersDao brandUsersDao;
	
	@Autowired
	BrandDao brandDao;
	
	@Autowired
	WalletUserDao walletUserDao;
	
	@Autowired
	RoleDao roleDao;
	
	@Autowired
	UserRolesDao userRolesDao;
	
	@Autowired
	UserDao userDao;
	
	public void saveBrandUser(User user, String brandId) throws CustomException{
		logger.debug("BrandUserServices -> Inside save BrandUser");
		BrandUsers brandUsers = new BrandUsers();
		brandUsers.setUserId(user.getUserId());
		
		brandUsers.setBrandId(brandId);
		brandUsersDao.saveBrandUsers(brandUsers);
	}
	
	public String getBrandIdByUser(){
		logger.debug("BrandUserServices -> Inside get BrandId By User");
		User user = CommonUtility.getUser();
		BrandUsers brandUsers = brandUsersDao.findBrandUsersByUserId(user.getUserId());
		return brandUsers.getBrandId();
	}
	
	public String findWalletIdByBrandUsers(String brandId){
		logger.debug("BrandUserServices -> Inside findWalletIdByBrandUsers");
		
		List<BrandUsers> brandUsers = brandUsersDao.findBrandUsersByBrandId(brandId);
		
		if(brandUsers.isEmpty()){
			return null;
		}else{
			String roleId = roleDao.findRoleByName(Properties.brandAdminRole).getRoleId();
			for(BrandUsers brandUser : brandUsers){
				UserRoles userRoles = userRolesDao.findUserByRole(brandUser.getUserId(), roleId);
				if(userRoles != null){
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
}
