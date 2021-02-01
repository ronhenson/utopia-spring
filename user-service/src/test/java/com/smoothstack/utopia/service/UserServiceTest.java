package com.smoothstack.utopia.service;

import com.smoothstack.utopia.dao.UserDao;
import com.smoothstack.utopia.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    @BeforeAll
    public static void init() {
        userOne = new User();
        userOne.setUserId(1L);
        userOne.setFirstName("John");
        userOne.setLastName("Paul");
        userOne.setEmail("jpaul@gmail.com");
        userOne.setUserRole(3);
        userOne.setPassword("412Ioui1&");
        userTwo = new User();
        userTwo.setFirstName("John");
        userTwo.setLastName("Paul");
        userTwo.setEmail("pxavier@gmail.com");
        userTwo.setUserRole(2);
        userTwo.setPassword("421412H$@#$");
        userThree = new User();
        userThree.setFirstName("Jennifer");
        userThree.setLastName("Larence");
        userThree.setEmail("mockingj@hungergames.com");
        userThree.setUserRole(1);
        userThree.setPassword("j4hunger");
    }
    @Test
    public void findAll() {
        List<User> users;
        users = Arrays.asList(userOne, userTwo, userThree);
        doReturn(users).when(userDao).findAll();
        List<User> actualUsers = userService.findAll();
        assertThat(actualUsers).isEqualTo(users);
    }

    @Test
    public void findByFirstNameAndLastName() {
        List<User> users = Arrays.asList(userOne);
        doReturn(users).when(userDao).findByFirstNameAndLastName(userOne.getFirstName(), userOne.getLastName());
        List<User> resultUsers = userService.findByFirstNameAndLastName(userOne.getFirstName(), userOne.getLastName());
        assertThat(resultUsers).isEqualTo(users);
    }

    @Test
    public void findByFirstName() {
        List<User> users = Arrays.asList(userOne, userTwo);
        doReturn(users).when(userDao).findByFirstName(userOne.getFirstName());
        List<User> resultUsers = userService.findByFirstName(userOne.getFirstName());
        assertThat(resultUsers).isEqualTo(users);
        assertThat(resultUsers.size()).isEqualTo(2);
    }

    @Test void findByLastName() {
        List<User> users = Arrays.asList(userOne);
        doReturn(users).when(userDao).findByLastName(userOne.getLastName());
        List<User> resultUsers = userService.findByLastName(userOne.getLastName());
        assertThat(resultUsers).isEqualTo(users);
        assertThat(resultUsers.size()).isEqualTo(1);
    }

    @Test void findByUserId() {
        User user = userOne;
        doReturn(Optional.of(user)).when(userDao).findByUserId(user.getUserId());
        Optional<User> resultOptional = userService.findById(user.getUserId());
        assertThat(resultOptional.isPresent()).isTrue();
        assertThat(resultOptional.get()).isEqualTo(user);
    }

    @Test void findByEmail() {
        User user = userOne;
        doReturn(Optional.of(user)).when(userDao).findByEmail(user.getEmail());
        Optional<User> resultOptional = userService.findByEmail(user.getEmail());
        assertThat(resultOptional.get()).isEqualTo(user);
    }

    @Test void saveUser() {
        User user = userTwo;
        doReturn(user).when(userDao).save(user);
        User resultUser = userService.saveUser(user);
        assertThat(user).isEqualTo(resultUser);
    }

    @Test void userExists() {
        User user = userOne;
        doReturn(true).when(userDao).existsByEmail(user.getEmail());
        boolean resultUserExists = userService.userExists(user.getEmail());
        assertThat(true).isEqualTo(resultUserExists);
    }


}

