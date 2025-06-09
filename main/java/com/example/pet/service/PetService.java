package com.example.pet.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pet.dao.entity.Pet;
import com.example.pet.dao.entity.Report;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

public interface PetService extends IService<Pet> {
    @Override
    default Pet getById(Serializable id) {
        return null;
    }
    /**
     * 查询满足条件的文章列表
     */
    @Override
    default List<Pet> list(Wrapper<Pet> queryWrapper) {
        return null;
    }

    public int addPet(Pet pet);

    public int updatePet(Pet pet);
    public String uploadToMinio(MultipartFile file) throws Exception;
}
