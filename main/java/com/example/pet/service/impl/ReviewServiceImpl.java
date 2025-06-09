package com.example.pet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.pet.dao.entity.Report;
import com.example.pet.dao.entity.Review;
import com.example.pet.mapper.ReportMapper;
import com.example.pet.mapper.ReviewMapper;
import com.example.pet.service.ReviewService;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Accessors(chain = true)
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService {
    @Autowired
    private ReviewMapper reviewMapper;
    @Override
    public int addReview(Review review){
        int result1 = reviewMapper.insert(review);
        if (result1 == 1 ){
            return 1;
        } else {
            return -1;
        }
    }
}
