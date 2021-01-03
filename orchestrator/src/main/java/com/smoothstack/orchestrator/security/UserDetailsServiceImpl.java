package com.smoothstack.orchestrator.security;

import java.text.MessageFormat;
import java.util.Optional;

import com.smoothstack.orchestrator.dao.UserDao;
import com.smoothstack.orchestrator.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        final Optional<User> optionalUser = userDao.findByEmail(email);

        if (optionalUser.isPresent()) {
            return new UserDetailsImpl(optionalUser.get());
        } else {
            throw new UsernameNotFoundException(MessageFormat.format("User with email {0} cannot be found.", email));
        }
    }

}
