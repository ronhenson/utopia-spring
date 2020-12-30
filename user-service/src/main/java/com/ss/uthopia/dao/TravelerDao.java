package com.ss.uthopia.dao;

import com.ss.uthopia.entity.Traveler;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelerDao extends CrudRepository<Traveler, Long> {
    @Override
    List<Traveler> findAll();

}
