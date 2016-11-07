package com.oomoqu.rest.util;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Properties {
	public static String portalIp;
	public static Integer portalPort;
	public static String portalScheme;
	public static String portalUser;
	public static String portalPassword;
	
	public static String adminRole;
	public static String registrationRole;
	public static String walletUserRole;
	public static String campaignUserRole;
	public static String brandAdminRole;
	public static String retailerAdminRole;
	public static String retailerBranchAdminRole;
	
	/*@Value("${portal.ip}")
	public void setPortalIp(String portalIp) {
		Properties.portalIp = portalIp;
	}
	
	@Value("${portal.port:80}")
	public void setPortalPort(Integer portalPort) {
		Properties.portalPort = portalPort;
	}
	
	@Value("${portal.scheme}")
	public void setPortalScheme(String portalScheme) {
		Properties.portalScheme = portalScheme;
	}

	@Value("${portal.user}")
	public void setPortalUser(String portalUser) {
		Properties.portalUser = portalUser;
	}

	@Value("${portal.password}")
	public void setPortalPassword(String portalPassword) {
		Properties.portalPassword = portalPassword;
	}
	
	@Value("${role.admin}")
	public void setAdminRole(String adminRole) {
		Properties.adminRole = adminRole;
	}

	@Value("${role.registration}")
	public void setRegistrationRole(String registrationRole) {
		Properties.registrationRole = registrationRole;
	}

	@Value("${role.walletUser}")
	public void setWalletUserRole(String walletUserRole) {
		Properties.walletUserRole = walletUserRole;
	}

	@Value("${role.campaignUser}")
	public void setCampaignUserRole(String campaignUserRole) {
		Properties.campaignUserRole = campaignUserRole;
	}

	@Value("${role.brandAdmin}")
	public void setBrandAdminRole(String brandAdminRole) {
		Properties.brandAdminRole = brandAdminRole;
	}

	@Value("${role.retailerAdmin}")
	public void setRetailerAdminRole(String retailerAdminRole) {
		Properties.retailerAdminRole = retailerAdminRole;
	}

	@Value("${role.retailerBranchAdmin}")
	public void setRetailerBranchAdminRole(String retailerBranchAdminRole) {
		Properties.retailerBranchAdminRole = retailerBranchAdminRole;
	}*/
	
	
}
