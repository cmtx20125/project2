package com.example.pet.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pet.dao.entity.Report;
import com.example.pet.dao.entity.Review;

import java.util.List;

public interface ReviewService extends IService<Review> {
    @Override
    default List<Review> list(Wrapper<Review> queryWrapper) {
        return null;
    }

    public int addReview(Review review);
}
