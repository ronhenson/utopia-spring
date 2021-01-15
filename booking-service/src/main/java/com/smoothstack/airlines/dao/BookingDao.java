package com.smoothstack.airlines.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.smoothstack.airlines.entity.Booking;

public interface BookingDao extends JpaRepository<Booking, Long> {

  public List<Booking> findByBookerId(Integer bookerId);

}
