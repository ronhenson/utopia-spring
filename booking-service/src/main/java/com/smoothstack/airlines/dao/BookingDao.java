package com.smoothstack.airlines.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smoothstack.airlines.entity.Booking;

public interface BookingDao extends JpaRepository<Booking, Long> {
	
}
