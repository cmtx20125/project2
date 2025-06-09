package com.example.pet.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.pet.dao.entity.Animal;

import com.example.pet.mapper.AnimalMapper;
import com.example.pet.mapper.PetMapper;
import com.example.pet.service.AnimalService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
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
public class AnimalServiceImpl extends ServiceImpl<AnimalMapper, Animal> implements AnimalService {
    @Autowired
    private AnimalMapper animalMapper;
    @Autowired
    private MinioClient minioClient;
    @Value("${minio.bucketName}")
    private String bucketName;

    @Value("${minio.publicUrl}")
    private String publicUrl;
    @Override
    public int addAnimal(Animal animal){
        int result1 = animalMapper.insert(animal);
        if (result1 == 1 ){
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public int updateAnimal(String name,
                            String gender,
                            String v,
                            String jue,
                            String reportId,
                            String age,
                            String s,
                            String content,String id,
                            MultipartFile animalImage)throws Exception{
       Animal animaln= animalMapper.selectOne(Wrappers.<Animal>lambdaQuery().eq(Animal::getAnimalId, id));
       Date time  = new Date();
       String url = uploadToMinio(animalImage);
       Animal animal = new Animal();
        if (animaln != null){
            //更新 TaskChild 的属性
            //update 方法一般接受两个参数：要更新的实体对象和一个 UpdateWrapper 对象，它允许你指定更新的条件。对于联合主键的情况，
            //可以使用 UpdateWrapper 中的 eq 方法来设置多个条件。
            UpdateWrapper<Animal> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("animalId", id);
            animal.setAnimalYang("0");
            animal.setAnimalTime(time);
            animal.setAnimalImg(url);
            animal.setAnimalName(name);
            animal.setAnimalYear(age);
            animal.setAnimalContent(content);
            animal.setReportId(reportId);
            animal.setAnimalId(id);
            animal.setAnimalYM(v);
            animal.setAnimalYuan(s);
            animal.setAnimalGender(gender);
            animal.setAnimalJue(jue);
            //rows为受影响的行数
            int rows =animalMapper.update(animal, updateWrapper);

            if (rows == 1) {
                System.out.println("成功");
            } else {
                System.out.println("失败");
            }

            return rows;
        }

        return -1;
    }
    public int lingAnimal(Animal animal){
        Animal animaln= animalMapper.selectOne(Wrappers.<Animal>lambdaQuery().eq(Animal::getAnimalId, animal.getAnimalId()));
        if (animaln != null){
            int rows = animalMapper.deleteById(animal.getAnimalId());
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
