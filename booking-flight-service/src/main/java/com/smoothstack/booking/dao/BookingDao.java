package com.smoothstack.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.smoothstack.booking.entity.Booking;

public interface BookingDao extends JpaRepository<Booking, Long> {

  public List<Booking> findByBookerId(Integer bookerId);

}
