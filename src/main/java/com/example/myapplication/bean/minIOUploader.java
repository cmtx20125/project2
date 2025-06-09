package com.example.myapplication.bean;

import android.util.Log;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import java.io.File;
import java.nio.file.Files;

public class minIOUploader {
    private static final String ENDPOINT = "http://10.0.2.2:9005";
    private static final String ACCESS_KEY = "admin"; // MinIO登录账号
    private static final String SECRET_KEY = "12345678"; // MinIO登录密码
    private static final String BUCKET_NAME = "pet";

    public static String uploadPng(File pngFile) {
        try {
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(ENDPOINT)
                    .credentials(ACCESS_KEY, SECRET_KEY)
                    .build();

            // 上传 PNG 文件
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(pngFile.getName()) // 存储的文件名
                            .stream(Files.newInputStream(pngFile.toPath()), pngFile.length(), -1)
                            .contentType("image/png")  // 关键：指定 PNG 类型
                            .build());

            // 获取公开访问 URL
            String imageUrl = ENDPOINT + "/" + BUCKET_NAME + "/" + pngFile.getName();
            Log.d("MinIO Upload", "Success! URL: " + imageUrl);
        } catch (Exception e) {
            Log.e("MinIO Upload", "Error: " + e.getMessage());
            e.printStackTrace();
        }
        return pngFile.getName();
    }
}
