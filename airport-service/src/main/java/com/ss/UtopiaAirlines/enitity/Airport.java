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
	private String iataIdent;
	private String city;
	private String name;
	public String getIataIdent() {
		return iataIdent;
	}
	public void setIataIdent(String airportId) {
		this.iataIdent = airportId;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
}