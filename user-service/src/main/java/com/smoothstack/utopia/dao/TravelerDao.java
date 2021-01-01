package com.smoothstack.utopia.dao;

import com.smoothstack.utopia.entity.Traveler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelerDao extends JpaRepository<Traveler, Long> {
    @Override
    List<Traveler> findAll();

}
