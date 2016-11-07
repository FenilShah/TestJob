package com.oomoqu.rest.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="user")
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userId;
	private String firstName;
	private String lastName;
	private String portalUserId;
	private String email;
	private String phone;
	private Date createdDate;
	private Date modifiedDate;
	private String password;
	private String loginType;
	private String loginId;
	private Boolean isDeleted;
	
	@Id
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPortalUserId() {
		return portalUserId;
	}
	public void setPortalUserId(String portalUserId) {
		this.portalUserId = portalUserId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getLoginType() {
		return loginType;
	}
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
	
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	
	public Boolean getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
	
}
