package com.ss.UtopiaAirlines.enitity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ronh
 *
 */

@Entity
@Table( name = "tbl_airport")
public class Airport {
	
	@Id
	private Integer airportId;
	private String city;
	public Integer getAirportId() {
		return airportId;
	}
	public void setAirportId(Integer airportId) {
		this.airportId = airportId;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	
	
}