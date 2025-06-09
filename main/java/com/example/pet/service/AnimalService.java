package com.example.pet.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pet.dao.entity.Animal;
import com.example.pet.dao.entity.Pet;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

public interface AnimalService extends IService<Animal> {
    @Override
    default Animal getById(Serializable id) {
        return null;
    }
    /**
     * 查询满足条件的文章列表
     */

    public int addAnimal(Animal animal);

    public int updateAnimal(String name,
                            String gender,
                            String v,
                            String jue,
                            String reportId,
                            String age,
                            String s,
                            String content,String id,
                            MultipartFile animalImage)throws Exception;
    public int lingAnimal(Animal animal);
    public String uploadToMinio(MultipartFile file) throws Exception;
}
