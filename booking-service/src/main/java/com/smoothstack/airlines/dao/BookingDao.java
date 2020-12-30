package com.smoothstack.airlines.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smoothstack.airlines.entity.Booking;
import com.smoothstack.airlines.entity.primaryKeys.BookingKey;

public interface BookingDao extends JpaRepository<Booking, BookingKey> {
	Booking findByBookingId(Integer id);
}
