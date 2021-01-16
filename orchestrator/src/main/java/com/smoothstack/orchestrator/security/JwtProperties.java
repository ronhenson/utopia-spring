package com.smoothstack.orchestrator.security;

import org.springframework.core.env.Environment;

public class JwtProperties {
    public static final String SECRET = ("#{Environment.JWT_SECRET}");
    public static final Integer EXPIRATION_TIME = 864000000; // 10 Days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
