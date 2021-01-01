package com.ss.uthopia.dao;

import com.ss.uthopia.entity.Traveler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelerDao extends JpaRepository<Traveler, Long> {
    @Override
    List<Traveler> findAll();

}
