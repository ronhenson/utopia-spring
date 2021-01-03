package com.smoothstack.orchestrator.service;

import com.smoothstack.orchestrator.dao.UserDao;
import com.smoothstack.orchestrator.entity.ConfirmationToken;
import com.smoothstack.orchestrator.entity.User;
import com.smoothstack.orchestrator.exception.EmailNotFoundException;
import com.smoothstack.orchestrator.exception.InvalidPasswordException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    JavaMailSender sender;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        final Optional<User> optionalUser = userDao.findByEmail(email);

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new UsernameNotFoundException(MessageFormat.format("User with email {0} cannot be found.", email));
        }
    }

    public User signUpUser(User user) {
        final String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        final User createdUser = userDao.save(user);
        final ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        try {
            sendConfirmationMail(user.getEmail(), confirmationToken.getConfirmationToken());
        } catch (MailAuthenticationException e) {
            confirmationTokenService.deleteConfirmationToken(confirmationToken.getId());
            userDao.deleteById(user.getUserId());
            System.err.println(e);
            throw e;
        }

        return createdUser;
    }

    public void confirmUser(ConfirmationToken confirmationToken) {
        final User user = confirmationToken.getUser();
        user.setEnabled(true);
        userDao.save(user);
        confirmationTokenService.deleteConfirmationToken(confirmationToken.getId());
    }

    void sendConfirmationMail(String userMail, String token) {

        final SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(userMail);
        mailMessage.setSubject("Utopia Mail Confirmation Link!");
        mailMessage.setFrom("<MAIL>");
        mailMessage.setText("Thank you for registering. Please click on the below link to activate your account. "
                + "http://localhost:8085/auth/confirm?token=" + token);

        emailSenderService.sendEmail(mailMessage);
    }

    public List<User> findAll() {
        return this.userDao.findAll();
    }

    // public List<User> findByName(String name) {
    // return userDao.findByName(name);
    // }

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

    public void login(String email, String password) throws EmailNotFoundException, InvalidPasswordException {
        Optional<User> user = userDao.findByEmail(email);
        if (user.isEmpty()) {
            throw new EmailNotFoundException(email);
        }

        String encodedPassword = user.get().getPassword();
        if (bCryptPasswordEncoder.matches(password, encodedPassword)) {
            // TODO: return a JWT
        } else {
            throw new InvalidPasswordException();
        }

    }

}
