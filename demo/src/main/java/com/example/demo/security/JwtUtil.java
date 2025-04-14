package com.example.demo.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Tạo mã thông báo JWT cho người dùng được xác thực
 * Xác nhận token và trích xuất tên người dùng
 */
@Component
public class JwtUtil {
    @Value("${jwt_secret}")
    private String secret;

    public String generateToken(String userName) {
        return JWT.create()
                .withSubject("User Details")
                .withClaim("userName", userName)
                .withIssuedAt(new Date())
                .withIssuer("JOB TRACKER")
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndRetrieveObject(String token) throws
            JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User Details")
                .withIssuer("JOB TRACKER")
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("userName").asString();
    }
}
