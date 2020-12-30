/**
 * 
 */
package com.ss.UtopiaAirlines.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ss.UtopiaAirlines.dao.AirportDao;
import com.ss.UtopiaAirlines.enitity.Airport;
import com.ss.UtopiaAirlines.exceptions.ResourceDoesNotExistsException;
import com.ss.UtopiaAirlines.exceptions.ResourceExistsException;

/**
 * @author ronh
 *
 */

@Component
public class AirportService {

		@Autowired
		AirportDao airportDao;
		
		public Optional<Airport> getAirportById(int id) {
			return airportDao.findById(id);
		}
		
		public List<Airport> getAllAirports() {
			return airportDao.findAll();
		}
		
		public List<Airport> findByCity(String cityName) throws ResourceDoesNotExistsException {
			if (cityName == null) {
				return airportDao.findAll();
			}
			return airportDao.findByCityIgnoreCase(cityName);
		}
		
		public Airport addAirport(Airport airport) throws ResourceExistsException {
			if (airportDao.existsById(airport.getAirportId())) {
				throw new ResourceExistsException();
			}
				return airportDao.save(airport);
		}
			
		public Airport updateAirport(Airport airport) throws ResourceDoesNotExistsException {
			if (airportDao.existsById(airport.getAirportId())) {
				return airportDao.save(airport);
			}
			throw new ResourceDoesNotExistsException();
		}
			
		public void deleteAirport(int id) throws ResourceDoesNotExistsException {

			if (airportDao.existsById(id)) {
				airportDao.deleteById(id);

			} else {
				throw new ResourceDoesNotExistsException();
			}
		}
}
