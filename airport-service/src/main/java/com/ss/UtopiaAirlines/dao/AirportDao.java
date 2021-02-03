/**
 *
 */
package com.ss.UtopiaAirlines.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ss.UtopiaAirlines.entity.Airport;

/**
 * @author ronh
 *
 */
@Repository
public interface AirportDao extends JpaRepository<Airport, String>{

	List<Airport> findByCityIgnoreCase(String city);

	@Query(value =
		// "select * from tbl_airport "
		// 	+ "where iataIdent like %?1% "
		// 	+ "or city like %?1% "
		// 	+ "or name like %?1% "
		// 	+ "limit 15",
		"select * from tbl_airport "
		+ "where match (iataIdent, city, name) against (concat(?1, '*') in boolean mode) "
		+ "order by case when ?1 = iataIdent then 0 else 1 end "
		+ "limit 15",
			nativeQuery = true
	)
	List<Airport> search(@Param("query") String query);

}
