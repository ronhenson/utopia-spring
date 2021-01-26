package com.smoothstack.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smoothstack.booking.entity.Traveler;

@Repository
public interface TravelerDao extends JpaRepository<Traveler, Integer> {

}
