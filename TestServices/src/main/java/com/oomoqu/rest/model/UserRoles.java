package com.oomoqu.rest.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="userroles")
public class UserRoles {
	private String userRoleId;
	private String userId;
	private String roleId;
	
	@Id
	public String getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(String userRoleId) {
		this.userRoleId = userRoleId;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	

}
