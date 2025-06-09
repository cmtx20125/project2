package com.example.myapplication.UI;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.bean.ApiService;
import com.example.myapplication.bean.Article;
import com.example.myapplication.bean.RetrofitClient;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportDetailTActivity extends AppCompatActivity {
    private TextView titleTextView, contentTextView, authorTextView, addressTextView;
    private ImageView articleImageView;
    private Button animalButton;
    private ApiService apiService;
    private String reportId, reportImg, reportContent, reportAddress, reportUserName,reportName,userId,time;
    private Date reportTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shangbaodetail);
        userId= getIntent().getStringExtra("userId");
        // 获取传递的文章 ID
        reportId = getIntent().getStringExtra("reportId");
        reportImg = getIntent().getStringExtra("reportImg");
        reportName = getIntent().getStringExtra("reportName");
        reportContent = getIntent().getStringExtra("reportContent");
        reportUserName = getIntent().getStringExtra("reportUserName");
        reportAddress = getIntent().getStringExtra("reportAddress");
        long date = getIntent().getLongExtra("reportTime", -1);

        if (date != -1) {
           reportTime = new Date(date);
            Log.d("MinIO", "时间" + reportTime);
        } else {
            Toast.makeText(this, "未接收到日期数据", Toast.LENGTH_SHORT).show();
        }
       time = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(reportTime);
        // 初始化 UI 组件
        titleTextView = findViewById(R.id.tv_title);
        contentTextView = findViewById(R.id.tv_content);
        articleImageView = findViewById(R.id.iv_image);
        animalButton = findViewById(R.id.btn_pet_profile);
        authorTextView = findViewById(R.id.tv_author_time);
        addressTextView = findViewById(R.id.tv_address);

        // 初始化 Retrofit
        apiService = RetrofitClient.getInstance().create(ApiService.class);

        // 获取文章详细内容
        fetchArticleDetail();

        // 返回按钮点击事件
        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        // 救助按钮点击事件
        animalButton.setOnClickListener(v -> {
            // 这里可以实现救助按钮的功能
            Toast.makeText(ReportDetailTActivity.this, "已请求！", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,AnimalActivity.class)
                    .putExtra("reportId",reportId).putExtra("userId",userId));
        });
    }

    private void fetchArticleDetail() {
        // 假设你已经有一个获取单篇文章详细信息的 API 接口

        titleTextView.setText(reportName);
        contentTextView.setText(reportContent);
        String text = reportUserName + "  " + time;
        authorTextView.setText(text);
        addressTextView.setText(reportAddress);
        // 格式化时间并显示
        // 使用 Fresco 加载图片
        String imageUrl = reportImg.replace("127.0.0.1","192.168.2.11");
        Log.d("MinIO", "Loading image URL: " + imageUrl);
        Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)  // 缓存所有版本图片
                .placeholder(R.drawable.index_bg)
                .error(R.drawable.dog)           // 错误图
                .transition(DrawableTransitionOptions.withCrossFade(300)) // 淡入淡出动画
                .into(articleImageView);  // 你的ImageView变量名



    }
}

