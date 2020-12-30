package com.smoothstack.airlines.dao;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smoothstack.airlines.entity.BookingsHasTravelers;
import com.smoothstack.airlines.entity.primaryKeys.BookingsHasTravelersKey;

@Repository
public interface BookingsHasTravelersDao extends JpaRepository<BookingsHasTravelers, BookingsHasTravelersKey> {
	Set<BookingsHasTravelers> findByTravelerTravelerId(Integer id); 
}
