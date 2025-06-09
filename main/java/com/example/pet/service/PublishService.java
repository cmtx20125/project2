package com.example.pet.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pet.dao.entity.Publish;
import com.example.pet.dao.entity.Report;
import com.example.pet.dao.entity.Review;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PublishService extends IService<Publish> {
    @Override
    default List<Publish> list(Wrapper<Publish> queryWrapper) {
        return null;
    }
    public String uploadToMinio(MultipartFile file) throws Exception;
    public int addPublish(String publishName,
                          String publishContent,
                          String publishUserId,
                          String publishUserName,
                          String publishTag,
                          MultipartFile publishImg,
                          MultipartFile publishCover)throws Exception ;
}
