package com.oomoqu.rest.wrapper;

import java.util.List;

import com.oomoqu.rest.model.Brand;
import com.oomoqu.rest.model.Location;
import com.oomoqu.rest.model.Retailer;
import com.oomoqu.rest.model.Role;
import com.oomoqu.rest.model.User;
import com.oomoqu.rest.model.Wallet;

public class UserRegistration {
	private User user;
	private Wallet wallet;
	private Brand brand;
	private Retailer retailer;
	private Location location;
	private List<Role> roles;
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Wallet getWallet() {
		return wallet;
	}
	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}
	public Brand getBrand() {
		return brand;
	}
	public void setBrand(Brand brand) {
		this.brand = brand;
	}
	public Retailer getRetailer() {
		return retailer;
	}
	public void setRetailer(Retailer retailer) {
		this.retailer = retailer;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
}
