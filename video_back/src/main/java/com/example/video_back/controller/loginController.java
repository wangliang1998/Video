package com.example.video_back.controller;

import com.example.video_back.pojo.Account;
import com.example.video_back.service.UserService;
import com.example.video_back.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@CrossOrigin
@RestController
public class loginController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping(value = "/user/front/login")
    public Result login(@RequestParam(value = "username") String username,
                        @RequestParam(value = "password") String password
                       ){
        Account aa = userService.getAccount(username);
        if(aa==null){
            return Result.fail("用户名不存在");
        }
        if(!password.equals(aa.getPassword())){
            return Result.fail("密码不正确");
        }

        //redis记录用户登录状态
        String token = UUID.randomUUID().toString();
        //key为token，value为用户信息
        redisTemplate.opsForValue().set("token:"+token, aa.getAccess()+"",1,TimeUnit.DAYS);

        //用户名密码正确
        Map<String,Object> map = new HashMap<>();
        map.put("user",aa);
        map.put("token","token:"+token);
        return Result.succ(map);
    }


}
