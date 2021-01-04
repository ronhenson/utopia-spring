package com.smoothstack.orchestrator.security;

public class JwtProperties {
    //TODO: read secret from environment variable
    public static final String SECRET = "Testing a jwt secret 1234";
    public static final Integer EXPIRATION_TIME = 864000000; // 10 Days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
