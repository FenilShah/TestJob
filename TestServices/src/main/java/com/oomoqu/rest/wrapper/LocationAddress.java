package com.oomoqu.rest.wrapper;

import com.oomoqu.rest.model.Address;
import com.oomoqu.rest.model.Location;

public class LocationAddress {
	private Location location;
	private Address address;
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
}
