package com.smoothstack.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.smoothstack.booking.entity.BookingsHasTravelers;

@Repository
public interface BookingsHasTravelersDao extends JpaRepository<BookingsHasTravelers, Integer> {
	Optional<BookingsHasTravelers> findByTravelerId(Integer id);
	void deleteByTravelerId(Integer travelerId);
}
