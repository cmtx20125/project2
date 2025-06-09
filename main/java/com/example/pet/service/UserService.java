package com.example.pet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pet.dao.entity.Pet;
import com.example.pet.dao.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService extends IService<User> {
    public int addUser(User user);

    public int updateUser(User user);
    public String uploadToMinio(MultipartFile file) throws Exception;
}
