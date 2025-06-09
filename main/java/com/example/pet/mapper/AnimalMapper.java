package com.example.pet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pet.dao.entity.Animal;
import com.example.pet.dao.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface AnimalMapper extends BaseMapper<Animal> {
}
