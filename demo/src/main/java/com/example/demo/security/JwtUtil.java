package com.example.demo.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt_secret}")
    private String secret;

    public String generateToken(String userName) {
        return JWT.create()
                .withSubject("User Details")
                .withClaim("userName",userName)
                .withIssuedAt(new Date())
                .withIssuer("JOB TRACKER")
                .sign(Algorithm.HMAC256(secret));
    }
}
