package com.smoothstack.utopia.service;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.smoothstack.utopia.dao.UserDao;
import com.smoothstack.utopia.entity.User;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;


@SpringBootTest
public class UserServiceTest {
    @Mock
    private UserDao userDao;
    @InjectMocks
    private UserService userService;

    private static User userOne;
    private static User userTwo;
    private static User userThree;

//    @BeforeAll
//    public static void init() {
//        userOne = new User();
//        userOne.setUserId(1L);
//        userOne.setName("John Paul");
//        userOne.setUsername("jpaul");
//        userOne.setRole(3);
//        userOne.setPassword("412Ioui1&");
//        userTwo = new User();
//        userTwo.setName("John Paul");
//        userTwo.setUsername("pxavier");
//        userTwo.setRole(2);
//        userTwo.setPassword("421412H$@#$");
//        userThree = new User();
//        userThree.setName("Jennifer Lawrence");
//        userThree.setUsername("mockingj");
//        userThree.setRole(1);
//        userThree.setPassword("j4hunger");
//    }
//    @Test
//    public void findAll() {
//        List<User> users;
//        users = Arrays.asList(userOne, userTwo, userThree);
//        doReturn(users).when(userDao).findAll();
//        List<User> actualUsers = userService.findAll();
//        assertThat(actualUsers).isEqualTo(users);
//    }
//
//    @Test
//    public void findByNameAndUsername() {
//        List<User> users = Arrays.asList(userOne);
//        doReturn(users).when(userDao).findByNameAndUsername(userOne.getName(), userOne.getUsername());
//        List<User> resultUsers = userService.findByNameAndUsername(userOne.getName(), userOne.getUsername());
//        assertThat(resultUsers).isEqualTo(users);
//    }
//
//    @Test
//    public void findByName() {
//        List<User> users = Arrays.asList(userOne, userTwo);
//        doReturn(users).when(userDao).findByName(userOne.getName());
//        List<User> resultUsers = userService.findByName(userOne.getName());
//        assertThat(resultUsers).isEqualTo(users);
//        assertThat(resultUsers.size()).isEqualTo(2);
//    }
//
//    @Test void findByUsername() {
//        List<User> users = Arrays.asList(userOne);
//        doReturn(users).when(userDao).findByUsername(userOne.getUsername());
//        List<User> resultUsers = userService.findByUsername(userOne.getUsername());
//        assertThat(resultUsers).isEqualTo(users);
//        assertThat(resultUsers.size()).isEqualTo(1);
//    }
//
//    @Test void findById() {
//        User user = userOne;
//        doReturn(Optional.of(user)).when(userDao).findById(user.getUserId());
//        Optional<User> resultOptional = userService.findById(user.getUserId());
//        assertThat(resultOptional.get()).isEqualTo(user);
//    }
//
//    @Test void saveUser() {
//        User user = userTwo;
//        doReturn(user).when(userDao).save(user);
//        User resultUser = userService.saveUser(user);
//        assertThat(user).isEqualTo(resultUser);
//    }
//
//    @Test void userExists() {
//        User user = userOne;
//        doReturn(true).when(userDao).existsById(user.getUserId());
//        boolean resultUserExists = userService.userExists(user.getUserId());
//        assertThat(true).isEqualTo(resultUserExists);
//    }


}

