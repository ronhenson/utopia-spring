package com.smoothstack.orchestrator.dao;

import com.smoothstack.orchestrator.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Long> {

   // @Query("SELECT e from User e where (e.userId =:userId or :userId = 0) and (e.name=:name or :name = '') and (e.username=:username or :username = '') and (e.role=:role or :role = 0)")
    List<User> findAll();
    List<User> findByFirstName(String name);
    Optional<User> findByEmail(String email);
}

