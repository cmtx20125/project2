package com.example.pet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.pet.dao.entity.Article;
import com.example.pet.dao.entity.Report;
import com.example.pet.dao.entity.Userd;
import com.example.pet.mapper.ArticleMapper;
import com.example.pet.mapper.ReportMapper;
import com.example.pet.mapper.UserdMapper;
import com.example.pet.service.ReportService;
import com.example.pet.utils.GeoUtils;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Accessors(chain = true)    //支持链式写法
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {
    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private UserdMapper userdMapper;
    @Autowired
    private MinioClient minioClient;
    @Value("${minio.bucketName}")
    private String bucketName;

    @Value("${minio.publicUrl}")
    private String publicUrl;
    // 权重配置（可根据业务调整）
    private static final double DISTANCE_WEIGHT = 0.5;  // 地理位置权重
    private static final double TYPE_WEIGHT = 0.3;      // 类型偏好权重
    private static final double TIME_WEIGHT = 0.2;      // 时间权重
    // 时间衰减系数（72小时即3天）
    private static final double TIME_DECAY_FACTOR = 72.0;

    // 距离标准化因子（10公里）
    private static final double DISTANCE_STANDARD = 10.0;
    @Override
    public int addReport(Report report){
        int result1 = reportMapper.insert(report);
        if (result1 == 1 ){
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public int updateReport(Report report) {
        Report reportn = reportMapper.selectOne(Wrappers.<Report>lambdaQuery().eq(Report::getReportId, report.getReportId()));

        if (reportn != null){
            //更新 TaskChild 的属性
            report.setReportCunZai("不存在");

            //update 方法一般接受两个参数：要更新的实体对象和一个 UpdateWrapper 对象，它允许你指定更新的条件。对于联合主键的情况，
            //可以使用 UpdateWrapper 中的 eq 方法来设置多个条件。
            UpdateWrapper<Report> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("reportId", report.getReportId());

            //rows为受影响的行数
            int rows = reportMapper.update(report, updateWrapper);

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
    @Override
    public List<Report> getSortedReports(String userId, Double userLat, Double userLng) {
        // 1. 获取所有报告（可根据业务需求添加查询条件）
        List<Report> reports = reportMapper.selectList(Wrappers.<Report>lambdaQuery().eq(Report::getReportCunZai, "存在"));

        // 2. 计算用户类型偏好权重
        Map<String, Double> typeWeights = getUserTypeWeights(userId);

        // 3. 计算评分并排序
        return reports.stream()
                .map(report -> {
                    double score = calculateReportScore(report, userLat, userLng, typeWeights);
                    return new ScoredReport(report, score);
                })
                .sorted(Comparator.comparingDouble(ScoredReport::getScore).reversed())
                .map(ScoredReport::getReport)
                .collect(Collectors.toList());
    }

    @Override
    public int recordUserBehavior(String userId, String reportId, String reportType) {
        int result = 0;
        try {
            Userd behavior = new Userd();
            behavior.setUserId(userId);
            behavior.setReportId(reportId);
            behavior.setReportType(reportType);
            behavior.setTime(new Date());
            result= userdMapper.insert(behavior);
        } catch (Exception e) {
            log.error("记录用户行为失败", e);
            throw new RuntimeException("记录用户行为失败", e);
        }
        return result;
    }

    @Override
    public Map<String, Double> getUserTypeWeights(String userId) {
        // 查询用户最近30天的行为记录
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -30);
        Date startTime = calendar.getTime();

        // 查询用户最近30天的行为记录
        List<Userd> behaviors = userdMapper.selectList(
                new LambdaQueryWrapper<Userd>()
                        .eq(Userd::getUserId, userId)
                        .ge(Userd::getTime, startTime)
        );

        // 统计类型偏好
        long videoCount = behaviors.stream()
                .filter(b -> "视频".equals(b.getReportType()))
                .count();
        long textCount = behaviors.size() - videoCount;

        // 计算权重（基础权重0.2 + 偏好权重0.8）
        Map<String, Double> weights = new HashMap<>();
        double total = videoCount + textCount;

        if (total > 0) {
            weights.put("视频", 0.2 + 0.8 * (videoCount / total));
            weights.put("图文", 0.2 + 0.8 * (textCount / total));
        } else {
            // 默认权重
            weights.put("视频", 0.5);
            weights.put("图文", 0.5);
        }

        return weights;
    }

    /**
     * 计算单个报告的排序分数
     */
    private double calculateReportScore(Report report, Double userLat, Double userLng,
                                        Map<String, Double> typeWeights) {
        double lat = Double.parseDouble(report.getReportLa().replaceAll("[^0-9.\\-]", ""));
        double lng = Double.parseDouble(report.getReportLo().replaceAll("[^0-9.\\-]", ""));
        // 1. 使用GeoUtils计算距离分数
        double distance = GeoUtils.calculateDistance(
                userLat, userLng,
                lat, lng);
        double distanceScore = 1.0 / (1.0 + distance / DISTANCE_STANDARD);

        // 2. 类型偏好分数
        double typeScore = typeWeights.getOrDefault(report.getReportTagOne(), 0.5);

        // 3. 时间分数 - 使用Date计算
        double timeScore = calculateTimeScore(report.getReportTime());

        return DISTANCE_WEIGHT * distanceScore
                + TYPE_WEIGHT * typeScore
                + TIME_WEIGHT * timeScore;
    }
    private double calculateTimeScore(Date createTime) {
        if (createTime == null) {
            return 0; // 或者返回一个默认值
        }

        // 计算小时差
        long diffInMillis = System.currentTimeMillis() - createTime.getTime();
        long hours = TimeUnit.MILLISECONDS.toHours(diffInMillis);

        // 使用指数衰减公式
        return Math.exp(-hours / TIME_DECAY_FACTOR);
    }


    /**
     * 内部类：用于存储报告和它的评分
     */
    @Data
    private static class ScoredReport {
        private Report report;
        private double score;
        // 或者手动添加构造函数（如果不想用Lombok）
        public ScoredReport(Report report, double score) {
            this.report = report;
            this.score = score;
        }

        public Report getReport() {
            return report;
        }

        public void setReport(Report report) {
            this.report = report;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }
    }
}
