package com.oomoqu.rest.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import com.oomoqu.rest.model.Address;

@Service
public class AddressDao {
	private static Logger logger = Logger.getLogger(AddressDao.class);
	
	@Autowired
	MongoOperations mongoOperations;

	public void save(Address address){
		logger.debug("AddressDao -> Inside save");
		mongoOperations.save(address);
	}

	public void saveAll(List<Address> addresses){
		logger.debug("AddressDao -> Inside saveAll");
		mongoOperations.insertAll(addresses);
	}
	
	public Address findByTransactionId(String transactionId){
		Query query = new Query(Criteria.where("transactionId").is(transactionId));
		Address address = mongoOperations.findOne(query, Address.class);
		return address;
	}
	
	public Address findById(String addressId){
		Query query = new Query(Criteria.where("addressId").is(addressId));
		Address address = mongoOperations.findOne(query, Address.class);
		return address;
	}
	
	public void delete(Address address){
		mongoOperations.remove(address);
	}
	
	public Address findAddressByLatitudeAndLongitude(double latitude,double longitude){
		logger.debug("AddressDao -> Inside find Address By Latitude and Longitude");
		Query searchAddress = new Query(Criteria.where("latitude").is(latitude).and("longitude").is(longitude));
		Address address = mongoOperations.findOne(searchAddress, Address.class);
		return address;
	}
	
	public Address findAddressByParameters(String addressname,String stree1,String postalCode){
		logger.debug("AddressDao -> Inside find Address By Name, Street1, PostalCode ");
		Query searchAddress = new Query(Criteria.where("name").is(addressname).and("street1").is(stree1).and("postalCode").is(postalCode));
		Address address = mongoOperations.findOne(searchAddress, Address.class);
		return address;
	}
	
	public List<Address> getAllAddress(){
		return mongoOperations.find(new Query(), Address.class);
	}
}
