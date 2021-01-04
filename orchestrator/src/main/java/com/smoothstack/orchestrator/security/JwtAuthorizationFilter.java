package com.smoothstack.orchestrator.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.smoothstack.orchestrator.dao.UserDao;
import com.smoothstack.orchestrator.entity.User;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private UserDao userDao;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserDao userDao) {
        super(authenticationManager);
        this.userDao = userDao;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String header = request.getHeader(JwtProperties.HEADER_STRING);

        if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = getUsernamePasswordAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {
        String token = request.getHeader(JwtProperties.HEADER_STRING);
        if (token == null) {
            return null;
        }

        String email = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET.getBytes()))
            .build()
            .verify(token.replace(JwtProperties.TOKEN_PREFIX, ""))
            .getSubject();
            
        if (email == null) {
            return null;
        }

        Optional<User> user = userDao.findByEmail(email);

        if (user.isEmpty()) {
            return null;
        }

        UserDetailsImpl userDetails = new UserDetailsImpl(user.get());
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(email, null,
                userDetails.getAuthorities());
        return auth;
    }
    
}
