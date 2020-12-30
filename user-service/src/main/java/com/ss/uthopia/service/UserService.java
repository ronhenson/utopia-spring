package com.ss.uthopia.service;

import com.ss.uthopia.dao.UserDao;
import com.ss.uthopia.entity.User;
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

    public List<User> findByNameAndUsername(String name, String username) {
        return userDao.findByNameAndUsername(name, username);
    }

    public List<User> findByName(String name) {
        return userDao.findByName(name);
    }

    public List<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public Optional<User> findById(long id) {
        return userDao.findById(id);
    }

    public void deleteById(long id) {
        userDao.deleteById(id);
    }

    public User saveUser(User user) {
        return userDao.save(user);
    }

    public boolean userExists(long userId) {
        return userDao.existsById(userId);
    }

}
