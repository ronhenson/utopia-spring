package com.smoothstack.airlines.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smoothstack.airlines.entity.Traveler;

@Repository
public interface TravelerDao extends JpaRepository<Traveler, Integer> {

}
