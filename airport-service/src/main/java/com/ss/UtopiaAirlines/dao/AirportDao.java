/**
 * 
 */
package com.ss.UtopiaAirlines.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ss.UtopiaAirlines.enitity.Airport;

/**
 * @author ronh
 *
 */
@Repository
public interface AirportDao extends JpaRepository<Airport, String>{
	
	List<Airport> findByCityIgnoreCase(String city);

	@Query(value = 
		"select * from tbl_airport "
			+ "where iataIdent like %?1% "
			+ "or city like %?1% "
			+ "or name like %?1% "
			+ "limit 15",
			nativeQuery = true
	)
	List<Airport> search(String query);

}
