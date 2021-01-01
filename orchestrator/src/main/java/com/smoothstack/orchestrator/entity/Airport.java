package com.smoothstack.orchestrator.entity;

public class Airport {
	
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