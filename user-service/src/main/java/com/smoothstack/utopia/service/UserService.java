package com.smoothstack.utopia.service;

import com.smoothstack.utopia.dao.UserDao;
import com.smoothstack.utopia.entity.User;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserDao userDao;
    public UserService(UserDao userDao) {
        this.userDao = userDao;
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
