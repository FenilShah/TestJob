package com.oomoqu.rest.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.oomoqu.rest.model.Location;
import com.oomoqu.rest.wrapper.portal.LocationDetail;

@Service
public class LocationDao {
	private static Logger logger = Logger.getLogger(LocationDao.class);
	
	private static final String NAME = "name";
	
	@Autowired
	MongoOperations mongoOperations;
	
	public void save(Location location){
		logger.debug("LocationDao -> Inside save Location");
		
		Location location1 = mongoOperations.findOne(new Query(getCriteria().and("portalLocationId").is(location.getPortalLocationId())), Location.class);
		
		if(location1!=null) {
			location.setLocationId(location1.getLocationId());
		}
		
		mongoOperations.save(location);
	}
	
	public Boolean validateLocation(Location location, String portalRetailerId){
		logger.debug(" -> Inside find Location Name By Id ");
		Criteria criteria = getCriteria().and(NAME).is(location.getName()).and("portalRetailerId").is(portalRetailerId);
		if(location.getLocationId() != null){
			criteria.and("locationId").ne(location.getLocationId());
		}
		return !mongoOperations.exists(new Query(criteria), Location.class);
	}
	
	public Location delete(String portalRetailerId,String locationName){
		logger.debug("LocationDao -> Inside delete Location");
		
		Query query = new Query(Criteria.where("name").is(locationName).and("portalRetailerId").is(portalRetailerId).and("isDeleted").ne(true));
		
		Update update = new Update();
		update.set("isDeleted", true);
		
		return mongoOperations.findAndModify(query, update, Location.class);
	}

	public Location findLocationByLocationId(String locationId){
		logger.debug("LocationDao -> Inside find Location By LocationId");
		Query searchLocationOne = new Query(Criteria.where("locationId").is(locationId));
		Location location = mongoOperations.findOne(searchLocationOne, Location.class);
		return location;
	}
	
	public Location findByName(String locationName){
		logger.debug("LocationDao -> Inside find By name " + locationName);
		return mongoOperations.findOne(new Query(getCriteria().and(NAME).is(locationName)), Location.class);
	}
	
	public List<Location> findLocationsByOrganizationId(String organizationId){
		logger.debug("LocationDao -> Inside find Locations By OrganizationId");
		Query searchLocationQuery = new Query(Criteria.where("organizationId").is(organizationId));
		List<Location> locations = mongoOperations.find(searchLocationQuery, Location.class);
		return locations;
	}
	
	public LocationDetail findLocationReportByLocationId(String locationId){
		logger.debug("LocationDao -> Inside find Location Report By LocationId");
		Query query = new Query();
		query.addCriteria(Criteria.where("locationId").is(locationId));
		return mongoOperations.findOne(query, LocationDetail.class);
	}
	
	public Location findByAddressId(String addressId){
		logger.debug("LocationDao -> Inside find By AddressId");
		Query searchLocation = new Query(Criteria.where("addressId").is(addressId));
		Location location = mongoOperations.findOne(searchLocation, Location.class);
		return location;
	}
	
	public Location findByNames(String retailerId, String locationName){
		logger.debug("LocationDao -> Inside find By name");
		return mongoOperations.findOne(new Query(getCriteria().and("retailerId").is(retailerId).and(NAME).is(locationName)), Location.class);
	}
	
	public Location findByPortalLocationId(String portalLocationId){
		logger.debug("LocationDao -> Inside find By portalLocationId");
		return mongoOperations.findOne(new Query(getCriteria().and("portalLocationId").is(portalLocationId)), Location.class);
	}
	
	public List<Location> findLocations(String retailerId){
		return mongoOperations.find(new Query(getCriteria().and("retailerId").is(retailerId)), Location.class);
	}
	
	public List<LocationDetail> findLocationDetails(String retailerId){
		return mongoOperations.find(new Query(getCriteria().and("retailerId").is(retailerId)), LocationDetail.class);
	}
	
	private Criteria getCriteria(){
		return Criteria.where("isDeleted").ne(true);
	}
}
