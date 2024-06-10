package com.dwcloud.auth.cloud.server.controller;

import com.dwcloud.auth.cloud.common.model.LoginVal;
import com.dwcloud.auth.cloud.common.model.ResultMsg;
import com.dwcloud.auth.cloud.common.model.SysConstant;
import com.dwcloud.auth.cloud.common.utils.OauthUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/oauth")
@Slf4j
public class AuthController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @PostMapping("/logout")
    public ResultMsg logout(){
        LoginVal loginVal = OauthUtils.getCurrentUser();
        log.info("令牌唯一ID：{},过期时间：{}",loginVal.getJti(),loginVal.getExpireIn());
        //这个jti放入redis中，并且过期时间设置为token的过期时间
        stringRedisTemplate.opsForValue().set(SysConstant.JTI_KEY_PREFIX+loginVal.getJti(),"",loginVal.getExpireIn(), TimeUnit.SECONDS);
        return new ResultMsg(200,"注销成功",null);
    }
}
