package com.example.pet.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pet.dao.entity.Article;

import java.io.Serializable;
import java.util.List;

public interface ArticleService extends IService<Article> {
    @Override
    default Article getById(Serializable id) {
        return null;
    }
    /**
     * 查询满足条件的文章列表
     */
    @Override
    default List<Article> list(Wrapper<Article> queryWrapper) {
        return null;
    }

}
