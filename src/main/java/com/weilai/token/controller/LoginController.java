package com.weilai.token.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.weilai.token.entity.Account;
import com.weilai.token.response.CommonRes;
import com.weilai.token.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/userlogin")
    public CommonRes login(@RequestParam long uid, @RequestParam String password) throws JsonProcessingException {
        Account account = accountService.login(uid, password);

        if (account == null || !account.getPassword().equals(password)) {
            return new CommonRes(CommonRes.FAIL, "用户名密码/验证码错误，登陆失败", null);
        } else {
            return new CommonRes(account);
        }
    }

    @GetMapping("/loginfail")
    public CommonRes loginFail() {
        return new CommonRes(CommonRes.RELOGIN, "请重新登录", null);
    }

    @GetMapping("logout")
    public CommonRes logout(@RequestParam String token) {
        accountService.logout(token);
        return new CommonRes(CommonRes.SUCCESS, "退出成功", null);
    }

}