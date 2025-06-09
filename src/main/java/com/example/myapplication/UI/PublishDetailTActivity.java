package com.example.myapplication.UI;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.myapplication.bean.ApiService;
import com.example.myapplication.bean.HttpResponseEntity;
import com.example.myapplication.bean.HttpResponseEntityS;
import com.example.myapplication.bean.Review;
import com.example.myapplication.bean.ReviewAdapter;
import com.example.myapplication.R;
import com.example.myapplication.bean.ReviewResponse;
import com.example.myapplication.bean.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PublishDetailTActivity extends AppCompatActivity {

    // Views
    private ImageView backButton, articleImage, btnImage;
    private TextView tvTitle, tvAuthorTime, tvContent;
    private EditText etMessage;
    private Button btnSend;
    private RecyclerView recyclerView;
    private Date publishTime;
    private String publishId;
    private User user;
    private  String userId;

    private List<Review> commentList; // Comment data for RecyclerView
    private ReviewAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dongtaili); // Use the XML layout you provided

        // Initialize views
        backButton = findViewById(R.id.back_button);
        articleImage = findViewById(R.id.iv_image);
        tvTitle = findViewById(R.id.tv_title);
        tvAuthorTime = findViewById(R.id.tv_author_time);
        tvContent = findViewById(R.id.tv_content);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        btnImage = findViewById(R.id.btnImage);
        recyclerView = findViewById(R.id.recyclerView);
         userId = getIntent().getStringExtra("userId");

        // Set RecyclerView for comments
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentList = new ArrayList<>();
        commentAdapter = new ReviewAdapter(this,commentList,userId);
        recyclerView.setAdapter(commentAdapter);

        // Get intent data (Article details)
        String title = getIntent().getStringExtra("publishName");
        String author = getIntent().getStringExtra("publishUserName");
        long date = getIntent().getLongExtra("publishTime", -1);

        if (date != -1) {
            publishTime = new Date(date);
            Log.d("MinIO", "时间" + publishTime);
        } else {
            Toast.makeText(this, "未接收到日期数据", Toast.LENGTH_SHORT).show();
        }
        String content = getIntent().getStringExtra("publishContent");
        String imageUrl = getIntent().getStringExtra("publishImg");
        String time = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(publishTime);
        // Set article data
        tvTitle.setText(title);
        tvAuthorTime.setText(author + " | " + time);
        tvContent.setText(content);

        // Load article image with Glide
        String userImage = imageUrl.replace("127.0.0.1","192.168.2.11");
        Glide.with(this)
                .load(userImage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)  // 缓存所有版本图片
                .placeholder(R.drawable.index_bg)
                .error(R.drawable.dog)           // 错误图
                .transition(DrawableTransitionOptions.withCrossFade(300)) // 淡入淡出动画
                .into(articleImage);
// 动态监听 EditText 内容变化
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    // 有文字，启用按钮并更改背景颜色
                    btnSend.setEnabled(true);
                    btnSend.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFA726"))); // 橙色
                } else {
                    // 无文字，禁用按钮并恢复灰色
                    btnSend.setEnabled(false);
                    btnSend.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DADADA"))); // 灰色
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Handle the back button click
        backButton.setOnClickListener(v -> {Intent intent = new Intent(PublishDetailTActivity.this, TrendsActivity.class);
            intent.putExtra("userId",userId);
            intent.putExtra("user",user);
            startActivity(intent);
            finish(); // 可选：关闭当前页面
        });
        publishId = getIntent().getStringExtra("publishId");
        // Handle send button click
        btnSend.setOnClickListener(v -> sendMessage());

        // Load some dummy comments for testing
        loadComments(publishId);
    }


        private void sendMessage() {
            String message = etMessage.getText().toString().trim();
            if (message.isEmpty()) {
                Toast.makeText(this, "请输入消息", Toast.LENGTH_SHORT).show();
                return;
            }

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.2.11:8080/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiService apiService = retrofit.create(ApiService.class);
            String userId = getIntent().getStringExtra("userId");

            // 获取用户信息（异步）
            Call<HttpResponseEntityS> call = apiService.getUser(userId);
            call.enqueue(new Callback<HttpResponseEntityS>() {
                @Override
                public void onResponse(Call<HttpResponseEntityS> call, Response<HttpResponseEntityS> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        HttpResponseEntityS result = response.body();
                        if ("666".equals(result.getCode())) {
                            user = result.getData(); // 获取到用户信息

                            // 创建评论对象
                            Review newReview = new Review();
                            newReview.setReviewContent(message);
                            newReview.setPublishId(publishId);
                            newReview.setReviewUserName(user.getUserName());
                            newReview.setReviewUserImg(user.getUserPic());
                            newReview.setReviewUserId(user.getUserId());
                            

                            // 添加评论到服务器
                            addReviewToServer(newReview);

                            // 清空输入框
                            etMessage.setText("");

                        } else {
                            Toast.makeText(PublishDetailTActivity.this, "用户信息获取失败：" + result.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(PublishDetailTActivity.this, "获取用户信息失败", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<HttpResponseEntityS> call, Throwable t) {
                    Toast.makeText(PublishDetailTActivity.this, "网络错误，请检查您的连接", Toast.LENGTH_SHORT).show();
                }
            });
        }



    private void loadComments(String publishId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.2.11:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<HttpResponseEntity<List<Review>>> call = apiService.getAllReviews(publishId);
        call.enqueue(new Callback<HttpResponseEntity<List<Review>>>() {
            @Override
            public void onResponse(Call<HttpResponseEntity<List<Review>>> call, Response<HttpResponseEntity<List<Review>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    HttpResponseEntity<List<Review>> httpResponse = response.body();
                    if ("666".equals(httpResponse.getCode())) {
                        List<Review> reviews = httpResponse.getData();
                        commentList.clear();
                        commentList.addAll(reviews);
                        commentAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(PublishDetailTActivity.this, "没有评论", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PublishDetailTActivity.this, "加载评论失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HttpResponseEntity<List<Review>>> call, Throwable t) {
                Toast.makeText(PublishDetailTActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addReviewToServer(Review review) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.2.11:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<ReviewResponse> call = apiService.addReview(review);

        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ReviewResponse reviewResponse = response.body();
                    if (reviewResponse.isSuccess()) {
                        // 添加成功
                            Toast.makeText(PublishDetailTActivity.this, reviewResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            // 重新加载评论（推荐）
                            loadComments(publishId);
                    } else {
                        // 添加失败
                        Toast.makeText(PublishDetailTActivity.this, reviewResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 处理错误响应
                    Toast.makeText(PublishDetailTActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                // 处理网络错误
                Toast.makeText(PublishDetailTActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
