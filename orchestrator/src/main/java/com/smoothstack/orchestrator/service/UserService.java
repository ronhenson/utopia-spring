package com.smoothstack.orchestrator.service;

import com.smoothstack.orchestrator.dao.UserDao;
import com.smoothstack.orchestrator.entity.ConfirmationToken;
import com.smoothstack.orchestrator.entity.User;

import com.smoothstack.orchestrator.exception.DuplicateEmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {

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

    @Autowired
    DaoAuthenticationProvider authenticationProvider;

    public User signUpUser(User user) {
        final String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        if(userDao.existsByEmail(user.getEmail()))
            throw new DuplicateEmailException(user.getEmail());
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
        mailMessage.setText("Thank you for registering. Please click on the below link to activate your account.\n"
                + "http://localhost:4200/signup/" + token);

        emailSenderService.sendEmail(mailMessage);
    }

    public boolean userExists(String email) {
        return userDao.existsByEmail(email);
    }
}
