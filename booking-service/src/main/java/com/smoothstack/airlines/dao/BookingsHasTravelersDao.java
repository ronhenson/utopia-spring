package com.smoothstack.airlines.dao;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smoothstack.airlines.entity.BookingsHasTravelers;

@Repository
public interface BookingsHasTravelersDao extends JpaRepository<BookingsHasTravelers, Integer> {
	Set<BookingsHasTravelers> findByTravelerId(Integer id); 
}
