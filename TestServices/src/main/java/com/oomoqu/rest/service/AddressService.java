package com.oomoqu.rest.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oomoqu.rest.dao.AddressDao;
import com.oomoqu.rest.model.Address;

@Component
public class AddressService {
	private static Logger logger = Logger.getLogger(AddressService.class);
	
	@Autowired
	AddressDao addressDao;
	
	public void addAddress(Address address){
		logger.debug("AddressService -- > Inside add service");
		addressDao.save(address);
	}
	
	public void deleteAddress(String addressId){
		Address address = addressDao.findById(addressId);
		addressDao.delete(address);
	}
	
	public Address verifyAddress(Address address){
		Address storedAddress = null;
		
		storedAddress = addressDao.findAddressByLatitudeAndLongitude(address.getLatitude(), address.getLongitude());
	
		if(storedAddress == null){
			storedAddress = addressDao.findAddressByParameters(address.getName(), address.getStreet1(), address.getPostalCode());
		}
		return storedAddress;
	}
	
	public Address findById(String addressId){
		return addressDao.findById(addressId);
	}
	
}
