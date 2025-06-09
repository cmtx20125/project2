package com.example.pet.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.pet.beans.HttpResponseEntity;
import com.example.pet.beans.Response;
import com.example.pet.dao.entity.Dialogue;
import com.example.pet.dao.entity.User;
import com.example.pet.mapper.UserMapper;
import com.example.pet.service.PetService;
import com.example.pet.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RestController
@RequestMapping("/api")
public class UserController {
    @Resource
    UserMapper userMapper;
    @Autowired
    private UserService userService;
    @GetMapping("/login")
    public HttpResponseEntity loginChild(@RequestParam("userName") String username,
                                         @RequestParam("password") String password){
        HttpResponseEntity httpResponseEntity=new HttpResponseEntity();
        //将输入的数据与数据库进行匹配，看是否存在
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUserName, username)
                .eq(User::getUserPwd, password));
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
    @GetMapping("/getUser")
    public HttpResponseEntity getUserById(@RequestParam("userId") String userId){
        System.out.println("我要寻找user"+userId);
        HttpResponseEntity httpResponseEntity=new HttpResponseEntity();
        //将输入的数据与数据库进行匹配，看是否存在
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUserId, userId));
        if (user == null) {
            httpResponseEntity.setCode("0");
            httpResponseEntity.setData(null);
            httpResponseEntity.setMessage("用户或密码错误");
        } else {
            String minioPublicBaseUrl = "http://127.0.0.1:9005/pet/";
            // 直接拼接公开URL
                String publicUrl = minioPublicBaseUrl + user.getUserPic();
                user.setUserPic(publicUrl);
            httpResponseEntity.setCode("666");
            httpResponseEntity.setData(user);
            httpResponseEntity.setMessage("登录成功");
        }
        System.out.println(user.getUserPic());
        return httpResponseEntity;
    }
    @PostMapping("/updateUserV")
    public HttpResponseEntity updateUser(@RequestPart("name") String name,
                               @RequestPart("gender") String gender,
                               @RequestPart("phone") String phone,
                               @RequestPart("pwd") String password,
                               @RequestPart("id") String id,
                               @RequestPart("address") String address,
                                         @RequestPart("time") String birthTimeStr,
                               @RequestPart("animalImage") MultipartFile animalImage)throws Exception {
        System.out.println("进入user更新");
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        String url = userService.uploadToMinio(animalImage);
        User users = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUserId, id));
        User user = new User();
        user.setUserId(id);
        user.setUserName(name);
        user.setUserAddress(address);
        user.setUserGender(gender);
        user.setUserPhone(phone);
        user.setUserPwd(password);
        user.setUserPic(url);
        user.setUserTago(users.getUserTago());
        user.setUserTagt(users.getUserTagt());
        user.setUserTagtr(users.getUserTagtr());
        // 如果需要转为 Date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = sdf.parse(birthTimeStr);
        user.setUserDate(birthDate);
      int result =userService.updateUser(user);
        if (result == 1) {
            httpResponseEntity.setCode("666");
            httpResponseEntity.setMessage("更新成功");
            httpResponseEntity.setData(result);

        } else {
            httpResponseEntity.setCode("0");
            httpResponseEntity.setMessage("更新失败");
            httpResponseEntity.setData(result);
        }
        return httpResponseEntity;
    }
    @PostMapping("/updateUser")
    public Response updateUsers(@RequestBody User user){
        Response response = new Response();
        int result =userService.updateUser(user);
        if (result == 1) {
            response.setSuccess(true);
            response.setMessage("成功");

        } else {
            response.setSuccess(false);
            response.setMessage("失败");
        }
        return response;
    }
    @GetMapping("/sendPhone")
    public Response getPhone(@RequestParam("phone") String phone){
        System.out.println("我要寻找user");
        Response response = new Response();
        //将输入的数据与数据库进行匹配，看是否存在
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUserPhone, phone));
        if (user == null) {
            response.setSuccess(true);
            response.setMessage("手机号不存在");
        } else {
            response.setSuccess(false);
            response.setMessage("手机号存在");
        }

        return response;
    }
    @GetMapping("/sendUser")
    public Response getName(@RequestParam("userName") String username){
        System.out.println("我要寻找user");
        Response response = new Response();
        //将输入的数据与数据库进行匹配，看是否存在
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUserName,username));
        if (user == null) {
            response.setSuccess(true);
            response.setMessage("手机号不存在");
        } else {
            response.setSuccess(false);
            response.setMessage("手机号存在");
        }
        return response;
    }

}
