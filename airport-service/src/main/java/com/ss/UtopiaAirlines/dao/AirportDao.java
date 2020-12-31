/**
 * 
 */
package com.ss.UtopiaAirlines.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ss.UtopiaAirlines.enitity.Airport;

/**
 * @author ronh
 *
 */
@Repository
public interface AirportDao extends JpaRepository<Airport, Integer >{
	
	List<Airport> findByCityIgnoreCase(String city);

}
