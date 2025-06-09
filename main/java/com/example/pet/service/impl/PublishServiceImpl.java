package com.example.pet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.pet.dao.entity.Publish;
import com.example.pet.mapper.PublishMapper;
import com.example.pet.mapper.ReportMapper;
import com.example.pet.service.PublishService;
import io.minio.*;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

@Service
@Accessors(chain = true)
public class PublishServiceImpl extends ServiceImpl<PublishMapper, Publish> implements PublishService {
    @Autowired
    private MinioClient minioClient;

    @Autowired
    private PublishMapper publishMapper; // MyBatis-Plus Mapper

    @Value("${minio.bucketName}")
    private String bucketName;

    @Value("${minio.publicUrl}")
    private String publicUrl;

    @Override
    public int addPublish(
            String publishName,
            String publishContent,
            String publishUserId,
            String publishUserName,
            String publishTag,
            MultipartFile publishImg,
            MultipartFile publishCover) throws Exception {

        // 1. 上传文件到MinIO
        String imgUrl = uploadToMinio(publishImg);
        String coverUrl = uploadToMinio(publishCover);

        // 2. 创建实体对象
        Publish publish = new Publish();
        publish.setPublishId(UUID.randomUUID().toString());
        publish.setPublishName(publishName);
        publish.setPublishContent(publishContent);
        publish.setPublishUserId(publishUserId);
        publish.setPublishUserName(publishUserName);
        publish.setPublishTag(publishTag);
        if(publishTag.equals("1")){
            publish.setPublishImg(coverUrl);
        }else{
            publish.setPublishFile(coverUrl);
        }

        publish.setPublishCover(imgUrl);
        publish.setPublishTime(new Date());

        // 3. 保存到数据库
        int result = publishMapper.insert(publish);

        return result;
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
