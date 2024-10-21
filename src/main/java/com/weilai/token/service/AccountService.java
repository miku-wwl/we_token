package com.weilai.token.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weilai.token.entity.Account;
import com.weilai.token.redis.RedisStringCache;
import com.weilai.token.repository.AccountRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);
    @Autowired
    private RedisStringCache redisStringCache;

    @Autowired
    private AccountRepository accountRepository;

    private static final SecureRandom random = new SecureRandom();
    private static final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

    public static String generateToken() {
        byte[] bytes = new byte[9];
        random.nextBytes(bytes);
        String encoded = encoder.encodeToString(bytes);
        return encoded.substring(0, 12); // 截取前12位作为token
    }

    public boolean accountExistInCache(String token) {
        if (StringUtils.isBlank(token)) {
            return false;
        }
        //从缓存获取数据
        String accountJson = redisStringCache.get(token, "Account");
        if (accountJson != null) {
            //重新激活缓存(增加有效期)
            redisStringCache.cache(token, accountJson, "Account");
            return true;
        } else {
            return false;
        }
    }

    public Account login(long uid, String password) throws JsonProcessingException {
        //检验非空，防止无效查询
        if (StringUtils.isAnyBlank(password)) {
            return null;
        }

        //检验账户id和密码信息
        Account account = accountRepository.findByUid(uid);
        if (account == null) {
            return null;
        } else if (password.compareTo(account.getPassword()) != 0) {
            return null;
        } else {
            //增加唯一ID作为身份认证标志
            log.info("account:{}", account);
            if (account.getToken() == null) {
                account.setToken(generateToken());
                accountRepository.save(account);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            //存入缓存,token为标识符,value为account对应的json
            redisStringCache.cache(account.getToken(), objectMapper.writeValueAsString(account), "Account");
            return account;
        }
    }

    public void logout(String token) {
        redisStringCache.remove(token, "Account");
    }
}
