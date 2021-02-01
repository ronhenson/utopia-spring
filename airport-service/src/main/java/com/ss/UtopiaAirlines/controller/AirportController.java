/**
 *
 */
package com.ss.UtopiaAirlines.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


import com.ss.UtopiaAirlines.entity.Airport;
import com.ss.UtopiaAirlines.service.AirportService;
import com.ss.UtopiaAirlines.exceptions.ResourceDoesNotExistsException;
import com.ss.UtopiaAirlines.exceptions.ResourceExistsException;

/**
 * @author ronh
 *
 */

@RestController
@RequestMapping("/airport")
public class AirportController {

	@Autowired
	AirportService airportService;

	@GetMapping("/{airportId}")
	public Airport getAirportById(@PathVariable String airportId, HttpServletResponse response) {
		Optional<Airport> airport = airportService.getAirportById(airportId);
		if (airport.isPresent()) {
			return airport.get();
		}
		response.setStatus(HttpStatus.NOT_FOUND.value());
		return null;
	}

	@PostMapping("/list")
	public ResponseEntity<List<Airport>> getAirportList(@RequestBody List<String> airportIds) {
		List<Airport> airports = airportService.getAirportList(airportIds);
		return ResponseEntity.ok(airports);
	}


	@GetMapping
	public List<Airport> findByCity(@RequestParam(required = false) String query, @RequestParam(value = "city", required = false) String cityAirport,
			HttpServletResponse response) {
		if (query != null) {
			return airportService.search(query);
		}

		try {
			if (cityAirport != null) {
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

	@PostMapping
	public Airport addAirport(@RequestBody Airport airport, HttpServletResponse response) {
		try {
			return airportService.addAirport(airport);
		} catch (ResourceExistsException e) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return null;
		}

	}

	@PutMapping
	public Airport updateAirport(@RequestBody Airport airport, HttpServletResponse response) {
		try {
			return airportService.updateAirport(airport);
		} catch (ResourceDoesNotExistsException e) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return null;
		}
	}

	@DeleteMapping("/{airportId}")
	public void deleteAirport(@PathVariable String airportId, HttpServletResponse response) {
		try {
			airportService.deleteAirport(airportId);
		} catch (ResourceDoesNotExistsException e) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		}
	}

}
