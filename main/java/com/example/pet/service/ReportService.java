package com.example.pet.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pet.dao.entity.Article;
import com.example.pet.dao.entity.Report;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface ReportService extends IService<Report> {
    @Override
    default Report getById(Serializable id) {
        return null;
    }
    /**
     * 查询满足条件的文章列表
     */
    @Override
    default List<Report> list(Wrapper<Report> queryWrapper) {
        return null;
    }

    public int addReport(Report report);

    public int updateReport(Report report);
    public String uploadToMinio(MultipartFile file) throws Exception;
    List<Report> getSortedReports(String userId, Double latitude, Double longitude);

    /**
     * 记录用户行为（用于收集用户偏好）
     * @param userId 用户ID
     * @param reportId 报告ID
     * @param reportType 报告类型("视频"或"图文")
     */
    int recordUserBehavior(String userId, String reportId, String reportType);

    /**
     * 获取用户对报告类型的偏好权重
     * @param userId 用户ID
     * @return 包含"视频"和"图文"权重的Map
     */
    Map<String, Double> getUserTypeWeights(String userId);

}
