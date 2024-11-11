package com.weilai.jwt.controller;

import com.weilai.jwt.entity.User;
import com.weilai.jwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
        if (authenticate(user)) { // 假设有一个方法可以验证用户身份
            String accessToken = jwtUtil.createAccessToken(user.getId(), user.getRole());
            String refreshToken = jwtUtil.createRefreshToken(user.getId());

            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", accessToken);
            tokens.put("refresh_token", refreshToken);
            return ResponseEntity.ok(tokens);
        } else {
            return ResponseEntity.status(401).body(null);
        }
    }

    private boolean authenticate(User user) {
        // 这里实现用户的验证逻辑
        return true; // 假设验证通过
    }
}
