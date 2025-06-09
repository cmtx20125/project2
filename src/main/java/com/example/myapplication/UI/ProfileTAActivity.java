package com.example.myapplication.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.bean.ApiService;
import com.example.myapplication.bean.Article;
import com.example.myapplication.bean.ArticleAdapter;
import com.example.myapplication.bean.HttpResponseEntity;
import com.example.myapplication.bean.HttpResponseEntityS;
import com.example.myapplication.bean.RetrofitClient;
import com.example.myapplication.bean.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileTAActivity extends AppCompatActivity {
    private User user;
    private ProgressBar progressBar;
    private ApiService apiService;
    private TextView myName,myDate,myGender,myAddress;
    private ImageView ic;
    private String userId;
    private Button editbun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taren);

        // 获取用户数据
        userId= getIntent().getStringExtra("userId");
        initViews();
        fetchArticlesFromBackend();
        // 返回按钮点击事件
        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());
        // 救助按钮点击事件

    }

    private void initViews() {
        progressBar = findViewById(R.id.progressBar);
        myName = findViewById(R.id.my_name);
        myDate = findViewById(R.id.my_date);
        myGender = findViewById(R.id.my_gender);
        myAddress = findViewById(R.id.my_address);
        ic = findViewById(R.id.my_pic);
        apiService = RetrofitClient.getInstance().create(ApiService.class);
    }


    private void fetchArticlesFromBackend() {
        progressBar.setVisibility(View.VISIBLE);
        apiService.getUser(userId).enqueue(new Callback<HttpResponseEntityS>() {
            @Override
            public void onResponse(Call<HttpResponseEntityS> call,
                                   Response<HttpResponseEntityS> response) {
                progressBar.setVisibility(View.GONE);
                handleResponse(response);
            }

            @Override
            public void onFailure(Call<HttpResponseEntityS> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ProfileTAActivity.this,
                        "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleResponse(Response<HttpResponseEntityS> response) {
        if (response.isSuccessful() && response.body() != null) {
            HttpResponseEntityS httpResponse = response.body();
            if ("666".equals(httpResponse.getCode())) {
                updateArticleList(httpResponse.getData());
            } else {
                Toast.makeText(this, httpResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "服务器响应异常", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateArticleList(User usern) {
        user = usern;
        myName.setText("昵称"+"    "+user.getUserName());
        myAddress.setText("所在地"+"    "+user.getUserAddress());
        myGender.setText("性别"+"    "+user.getUserGender());
        myDate.setText("生日"+"    "+new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(user.getUserDate()));
        String imageUrl = user.getUserPic().replace("127.0.0.1","192.168.2.11");
        Log.d("MinIO", "Loading image URL: " + imageUrl);
        // 使用Glide加载用户头像（圆形裁剪）
        Glide.with(this)
                .load(imageUrl)
                .apply(RequestOptions.circleCropTransform()) // 圆形头像
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.index_bg)
                .error(R.drawable.dog)
                .into(ic);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 不再需要adapter.cleanup()调用
    }
}