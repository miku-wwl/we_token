package com.weilai.jwt.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import com.weilai.jwt.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/auth")
public class TokenController {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestParam String refreshToken) {
        try {
            DecodedJWT decodedJWT = jwtUtil.verifyRefreshToken(refreshToken);
            String userId = decodedJWT.getSubject();
            log.info("userId = {}", userId);

            String role = "user"; // 这里应该从数据库或缓存中获取用户的实际角色

            String newAccessToken = jwtUtil.createAccessToken(userId, role);
            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", newAccessToken);
            return ResponseEntity.ok(tokens);
        } catch (JWTVerificationException exception) {
            return ResponseEntity.status(401).body(null);
        }
    }

    @PostMapping("/jwt-token")
    public ResponseEntity<Map<String, String>> jwtToken(@RequestParam String jwtToken) {
        try {
            DecodedJWT decodedJWT = jwtUtil.verifyAccessToken(jwtToken);
            String userId = decodedJWT.getSubject();
            log.info("userId = {}", userId);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("userId", userId);
            return ResponseEntity.ok(tokens);
        } catch (JWTVerificationException exception) {
            return ResponseEntity.status(401).body(null);
        }
    }
}