package com.example.pet.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.pet.beans.HttpResponseEntity;
import com.example.pet.beans.Response;
import com.example.pet.dao.entity.User;
import com.example.pet.mapper.UserMapper;
import com.example.pet.service.SmsService;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth")
@Slf4j
public class SmsController {

    @Autowired
    private SmsService smsService;
    @Resource
    UserMapper userMapper;
    // 替代Redis的内存缓存
    private static final Map<String, String> SMS_CODE_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, Long> CODE_EXPIRE_TIME = new ConcurrentHashMap<>();

    @GetMapping("/sendCode")
    public Response sendCode(@RequestParam("phone") String phoneNumber) {
        // 1. 生成6位随机验证码
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        Response response = new Response();
        // 2. 发送短信
        boolean success = smsService.sendVerificationCode(phoneNumber, code);
        if (success) {
           response.setSuccess(true);
           response.setMessage("验证码获取成功");
        }else{
            response.setSuccess(false);
            response.setMessage("验证码获取失败");
        }

        // 3. 存储验证码（内存缓存）
        SMS_CODE_CACHE.put(phoneNumber, code);
        CODE_EXPIRE_TIME.put(phoneNumber, System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5));

        return response;
    }

    // 验证验证码的接口示例
    @GetMapping("/verifyCode")
    public HttpResponseEntity verifyCode(@RequestParam("phone") String phoneNumber, @RequestParam("code") String code) {
        // 检查验证码是否存在且未过期
        String storedCode = SMS_CODE_CACHE.get(phoneNumber);
        Long expireTime = CODE_EXPIRE_TIME.get(phoneNumber);
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        if (storedCode == null || !storedCode.equals(code)) {
            httpResponseEntity.setCode("0");
        }

        if (expireTime == null || System.currentTimeMillis() > expireTime) {
           httpResponseEntity.setCode("0");
        }

        // 验证通过后清除缓存
        SMS_CODE_CACHE.remove(phoneNumber);
        CODE_EXPIRE_TIME.remove(phoneNumber);
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUserPhone, phoneNumber)
                );
        if (user == null) {
            httpResponseEntity.setCode("0");
            httpResponseEntity.setData(null);
            httpResponseEntity.setMessage("用户或密码错误");
        } else {

            httpResponseEntity.setCode("666");
            httpResponseEntity.setData(user);
            httpResponseEntity.setMessage("登录成功");


        }
        return httpResponseEntity;
    }
    @PostMapping("/register")
    public Response register(@RequestParam("phone") String phone,@RequestParam("code") String code,@RequestParam("userName") String username){
        Response response = new Response();
        User user = new User();
        user.setUserPhone(phone);
        user.setUserName(username);
        user.setUserTago(0);
        user.setUserTagtr(0);
        user.setUserTagt(0);
        String storedCode = SMS_CODE_CACHE.get(phone);
        Long expireTime = CODE_EXPIRE_TIME.get(phone);

        if (storedCode == null || !storedCode.equals(code)) {
            response.setSuccess(false);
        }

        if (expireTime == null || System.currentTimeMillis() > expireTime) {
            response.setSuccess(false);
        }

        // 验证通过后清除缓存
        SMS_CODE_CACHE.remove(phone);
        CODE_EXPIRE_TIME.remove(phone);
       int result = userMapper.insert(user);
        if (result == 1) {
            response.setSuccess(true);
            response.setMessage("成功");

        } else {
            response.setSuccess(false);
            response.setMessage("失败");
        }
        return response;
    }
}
