package com.smoothstack.orchestrator.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smoothstack.orchestrator.dao.UserDao;
import com.smoothstack.orchestrator.entity.User;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private UserDao userDao;

    private ObjectMapper objectMapper;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserDao userDao, ObjectMapper objectMapper) {
        super(authenticationManager);
        this.userDao = userDao;
        this.objectMapper = objectMapper;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {

        Authentication authentication = getUsernamePasswordAuthentication(request, response);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        try {
            filterChain.doFilter(request, response);
        } catch (IOException | ServletException exc) {
            logger.error(exc.getMessage());
        }
    }

    private String readTokenFromRequest(HttpServletRequest request, HttpServletResponse response) {
        if (request.getCookies() == null) {
            logger.info("request had no cookies");
            return null;
        }
        logger.info("number of cookies: " + request.getCookies().length);
        Arrays.stream(request.getCookies()).forEach(cookie -> logger.info(cookie.getName()));
        Optional<String> token = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("jwt"))
                .peek(cookie -> logger.info(cookie.getName())).map(Cookie::getValue).findAny();

        if (token.isEmpty()) {
            logger.info("token is empty, user needs to log in");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            try {
                objectMapper.writeValue(response.getWriter(), null);
            } catch (Exception exc) {
                logger.error(exc.getMessage());
            }
            return null;
        }
        logger.info("jwt: " + token);
        return token.get();
    }

    private Authentication getUsernamePasswordAuthentication(HttpServletRequest request, HttpServletResponse response) {
        
        String token = readTokenFromRequest(request, response);
        try {
            String userId = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()))
                .build()
                .verify(token)
                .getSubject();

            Long id = Long.parseLong(userId);

            Optional<User> user = userDao.findById(id);

            if (user.isEmpty()) {
                return null;
            }

            UserDetailsImpl userDetails = new UserDetailsImpl(user.get());
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(id, null,
                    userDetails.getAuthorities());
            logger.info("successfully authorized");
            return auth;
        } catch (JWTVerificationException | NumberFormatException ex) {
            logger.error(ex.getMessage());
            return null;
        }
    }
    
}
