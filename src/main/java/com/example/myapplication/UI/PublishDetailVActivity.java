package com.example.myapplication.UI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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

public class PublishDetailVActivity extends AppCompatActivity {

    // Views
    private ImageView backButton, btnImage;
    private TextView tvTitle, tvAuthorTime, tvContent;
    private EditText etMessage;
    private VideoView videoView;
    private ImageView ivPlayButton;
    private ProgressBar progressBar;
    private Button btnSend;
    private RecyclerView recyclerView;
    private Date publishTime;
    private String publishId;
    private User user;

    private List<Review> commentList; // Comment data for RecyclerView
    private ReviewAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dongtais); // Use the XML layout you provided

        // Initialize views
        backButton = findViewById(R.id.back_button);
        videoView = findViewById(R.id.video_view);
        tvTitle = findViewById(R.id.tv_title);
        tvAuthorTime = findViewById(R.id.tv_author_time);
        tvContent = findViewById(R.id.tv_content);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        btnImage = findViewById(R.id.btnImage);
        recyclerView = findViewById(R.id.recyclerView);
        ivPlayButton = findViewById(R.id.iv_play_button);
        progressBar = findViewById(R.id.pb_loading);
        String userId = getIntent().getStringExtra("userId");
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
        } else {
            Toast.makeText(this, "未接收到日期数据", Toast.LENGTH_SHORT).show();
        }
        String content = getIntent().getStringExtra("publishContent");
        String imageUrl = getIntent().getStringExtra("publishFile");
        String time = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(publishTime);
        // Set article data
        tvTitle.setText(title);
        tvAuthorTime.setText(author + " | " + time);
        tvContent.setText(content);

        // Load article image with Glide
        String userImage = imageUrl.replace("127.0.0.1","192.168.2.11");
        // 加载视频（如果有）
        if (userImage != null && !userImage.isEmpty()) {
            setupVideoPlayer(userImage);
        } else {
            videoView.setVisibility(View.GONE);
        }

        // Handle the back button click
        backButton.setOnClickListener(v -> {Intent intent = new Intent(PublishDetailVActivity.this, TrendsActivity.class);
            intent.putExtra("userId",userId);
            intent.putExtra("user",user);
            startActivity(intent);
            finish(); // 可选：关闭当前页面
        });

        // Handle send button click
        btnSend.setOnClickListener(v -> sendMessage());
        publishId = getIntent().getStringExtra("publishId");

        // Load some dummy comments for testing
        loadComments(publishId);
    }
    private void setupVideoPlayer(String videoUrl) {
        try {
            String url = videoUrl.replace("127.0.0.1","192.168.2.11");
            progressBar.setVisibility(View.VISIBLE); // 显示加载进度条

            videoView.setVideoURI(Uri.parse(url));

            videoView.setOnPreparedListener(mp -> {
                progressBar.setVisibility(View.GONE); // 加载完成后隐藏进度条
                mp.setLooping(true);
                // 调整视频比例
                float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
                float screenRatio = videoView.getWidth() / (float) videoView.getHeight();
                if (videoRatio > screenRatio) {
                    videoView.setScaleX(screenRatio / videoRatio);
                } else {
                    videoView.setScaleY(videoRatio / screenRatio);
                }
            });
            // 设置点击监听（播放/暂停切换）
            videoView.setOnClickListener(v -> {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    ivPlayButton.setVisibility(View.VISIBLE); // 显示播放按钮
                } else {
                    videoView.start();
                    ivPlayButton.setVisibility(View.GONE); // 隐藏播放按钮
                }
            });
            videoView.start();
        } catch (Exception e) {
            e.printStackTrace();
            videoView.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (videoView != null) {
            videoView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (videoView != null && !videoView.isPlaying()) {
            videoView.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.stopPlayback();
        }
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
        String userId = getIntent().getStringExtra("publishUserId");

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
                        Toast.makeText(PublishDetailVActivity.this, "用户信息获取失败：" + result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PublishDetailVActivity.this, "获取用户信息失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HttpResponseEntityS> call, Throwable t) {
                Toast.makeText(PublishDetailVActivity.this, "网络错误，请检查您的连接", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(PublishDetailVActivity.this, "没有评论", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PublishDetailVActivity.this, "加载评论失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HttpResponseEntity<List<Review>>> call, Throwable t) {
                Toast.makeText(PublishDetailVActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(PublishDetailVActivity.this, reviewResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        // 重新加载评论（推荐）
                        loadComments(publishId);
                    } else {
                        // 添加失败
                        Toast.makeText(PublishDetailVActivity.this, reviewResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 处理错误响应
                    Toast.makeText(PublishDetailVActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                // 处理网络错误
                Toast.makeText(PublishDetailVActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
