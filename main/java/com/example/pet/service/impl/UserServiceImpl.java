package com.example.pet.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.pet.dao.entity.Pet;
import com.example.pet.dao.entity.User;
import com.example.pet.service.UserService;
import com.example.pet.mapper.*;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@Accessors(chain = true)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MinioClient minioClient;
    @Value("${minio.bucketName}")
    private String bucketName;

    @Value("${minio.publicUrl}")
    private String publicUrl;
    @Override
    public int addUser(User user){
        int result1 = userMapper.insert(user);
        if (result1 == 1 ){
            return 1;
        } else {
            return -1;
        }

    }
    @Override
    public int updateUser(User user){
        User usern = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUserId,user.getUserId()));

        if (usern != null){
            //更新 TaskChild 的属性
            user.setUserTime(usern.getUserTime());

            //update 方法一般接受两个参数：要更新的实体对象和一个 UpdateWrapper 对象，它允许你指定更新的条件。对于联合主键的情况，
            //可以使用 UpdateWrapper 中的 eq 方法来设置多个条件。
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("userId", user.getUserId());

            //rows为受影响的行数
            int rows = userMapper.update(user, updateWrapper);

            if (rows == 1) {
                System.out.println("成功");
            } else {
                System.out.println("失败");
            }

            return rows;
        }

        return -1;
    }
    @Override
    public String uploadToMinio(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            return null;
        }
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            System.out.println("添加图片");
        }

        return fileName;
    }

}
