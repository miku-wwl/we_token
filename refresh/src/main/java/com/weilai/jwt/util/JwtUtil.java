package com.weilai.jwt.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET = "YourSecretKey"; // 这里应该是你的密钥
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30分钟
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7天

    public String createAccessToken(String userId, String role) {
        return JWT.create()
                .withSubject(userId)
                .withClaim("role", role)
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME))
                .sign(Algorithm.HMAC256(SECRET));
    }

    public String createRefreshToken(String userId) {
        return JWT.create()
                .withSubject(userId)
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME))
                .sign(Algorithm.HMAC256(SECRET));
    }

    public DecodedJWT verifyAccessToken(String token) throws JWTVerificationException {
        return JWT.require(Algorithm.HMAC256(SECRET))
                .build()
                .verify(token);
    }

    public DecodedJWT verifyRefreshToken(String token) throws JWTVerificationException {
        return JWT.require(Algorithm.HMAC256(SECRET))
                .build()
                .verify(token);
    }
}
