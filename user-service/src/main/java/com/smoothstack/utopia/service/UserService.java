package com.smoothstack.utopia.service;

import com.smoothstack.utopia.dao.UserDao;
import com.smoothstack.utopia.entity.User;
import com.smoothstack.utopia.exception.DuplicateEmailException;
import com.smoothstack.utopia.exception.EmailNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {


    private PasswordEncoder encoder =  new BCryptPasswordEncoder();

    @Autowired
    private UserDao userDao;

    public UserService() {
    }

    public List<User> findAll() {
        return this.userDao.findAll();
    }

    public List<User> findByFirstNameAndLastName(String firstName, String lastName) {
        return userDao.findByFirstNameAndLastName(firstName, lastName);
    }

    public List<User> findByFirstName(String firstName) {
        return userDao.findByFirstName(firstName);
    }

    public List<User> findByLastName(String lastName) {
        return userDao.findByLastName(lastName);
    }

    public User saveUser(User user) {
        if (userExists(user.getEmail())) throw new DuplicateEmailException("User already exists");
        user.setPassword(encoder.encode(user.getPassword()));
        return userDao.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return userDao.findByUserId(id);
    }

    public void deleteById(long id) {
        userDao.deleteById(id);
    }

    public boolean userExists(String email) {
        return userDao.existsByEmail(email);
    }

}
