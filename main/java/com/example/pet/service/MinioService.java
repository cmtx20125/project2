package com.example.pet.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
@Service
public class MinioService {
    @Autowired
    private MinioClient minioClient;
    public String upload(MultipartFile file) throws Exception {
        String filename = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        minioClient.putObject(PutObjectArgs.builder()
                .bucket("pet")
                .object(filename)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build());
        return filename;
    }

}
