package com.smoothstack.orchestrator.dao;

import com.smoothstack.orchestrator.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
    List<User> findAll();
    List<User> findByFirstName(String name);
    Optional<User> findByEmail(String email);
}

