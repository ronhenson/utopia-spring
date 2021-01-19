package com.smoothstack.utopia.dao;

import com.smoothstack.utopia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Long> {

   // @Query("SELECT e from User e where (e.userId =:userId or :userId = 0) and (e.name=:name or :name = '') and (e.username=:username or :username = '') and (e.role=:role or :role = 0)")
    List<User> findByFirstName(String name);
    List<User> findByLastName(String name);
    List<User> findByFirstNameAndLastName(String firstName, String lastName);
    Optional<User> findByEmail(String email);
    Optional<User> findByUserId(Long id);
    boolean existsByEmail(String email);

}

