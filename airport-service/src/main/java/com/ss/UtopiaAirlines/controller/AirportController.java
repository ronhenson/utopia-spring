/**
 * 
 */
package com.ss.UtopiaAirlines.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


import com.ss.UtopiaAirlines.enitity.Airport;
import com.ss.UtopiaAirlines.service.AirportService;
import com.ss.UtopiaAirlines.exceptions.ResourceDoesNotExistsException;
import com.ss.UtopiaAirlines.exceptions.ResourceExistsException;

/**
 * @author ronh
 *
 */

@RestController
@RequestMapping("/airports")
public class AirportController {
	
	@Autowired
	AirportService airportService;
	
	@GetMapping("/id/{airportId}")
	public Airport getAirportById(@PathVariable int airportId, HttpServletResponse response) {
		Optional<Airport> airport = airportService.getAirportById(airportId);
		if (airport.isPresent()) {
			return airport.get();
		}
		response.setStatus(HttpStatus.NOT_FOUND.value());
		return null;
	}
	
	
	@RequestMapping(value = {"/cityName"})
	//@ResponseBody
	public List<Airport> findByCity(@RequestParam( value = "city", defaultValue = "") String cityAirport,  HttpServletResponse response) {
		
		try {
			if ( cityAirport.length() == 0) {
				return airportService.getAllAirports();
			}

			List<Airport> cityList =  airportService.findByCity(cityAirport);
			
			if (cityList.isEmpty()) {
				response.setStatus(HttpStatus.NOT_FOUND.value());
			}
			
			return cityList;
			
		} catch (ResourceDoesNotExistsException e) {
	    	response.setStatus(HttpStatus.BAD_REQUEST.value());
	    	return null;
		}
		    
	}
	
	@PostMapping("/id")
	public Airport addAirport(@RequestBody Airport airportId, HttpServletResponse response) {
		try {
			return airportService.addAirport(airportId);
		} catch (ResourceExistsException e) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return null;
		}
		
	}
	
	@PutMapping("/id")
	public Airport updateAirport(@RequestBody Airport airport, HttpServletResponse response) {
		try {
			return airportService.updateAirport(airport);
		} catch (ResourceDoesNotExistsException e) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return null;
		}
	}
	
	@DeleteMapping("/id/{airportId}")
	public void deleteAirport(@PathVariable int airportId, HttpServletResponse response) {
		try {
			airportService.deleteAirport(airportId);
		} catch (ResourceDoesNotExistsException e) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		}
	}

}
