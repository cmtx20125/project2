package com.example.myapplication.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.bean.ApiService;
import com.example.myapplication.bean.Article;
import com.example.myapplication.bean.ArticleAdapter;
import com.example.myapplication.bean.HttpResponseEntity;
import com.example.myapplication.bean.RetrofitClient;
import com.example.myapplication.bean.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArticleAdapter adapter;
    private List<Article> articleList = new ArrayList<>();
    private User user;
    private ProgressBar progressBar;
    private ApiService apiService;
    private String userId;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhuye);

        // 获取用户数据
        user = (User) getIntent().getSerializableExtra("user");
        userId= getIntent().getStringExtra("userId");
        initViews();
        setupRecyclerView();
        setupBottomNavigation();
        fetchArticlesFromBackend();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        apiService = RetrofitClient.getInstance().create(ApiService.class);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ArticleAdapter(this, articleList); // 移除了MinioClient参数
        recyclerView.setAdapter(adapter);
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    return true;

                case R.id.nav_report:
                    startActivity(new Intent(this, ReportActivity.class)
                            .putExtra("user", user).putExtra("userId",userId));
                    return true;

                case R.id.nav_adopt:
                    startActivity(new Intent(this, HospitalActivity.class).putExtra("user", user).putExtra("userId",userId));
                    return true;

                case R.id.nav_trends:
                    startActivity(new Intent(this, TrendsActivity.class).putExtra("user", user).putExtra("userId",userId));
                    return true;

                case R.id.nav_profile:
                    startActivity(new Intent(this, ProfileActivity.class).putExtra("user", user).putExtra("userId",userId));
                    return true;

                default:
                    return false;
            }
        });
    }

    private void fetchArticlesFromBackend() {
        progressBar.setVisibility(View.VISIBLE);
        apiService.getAllArticles().enqueue(new Callback<HttpResponseEntity<List<Article>>>() {
            @Override
            public void onResponse(Call<HttpResponseEntity<List<Article>>> call,
                                   Response<HttpResponseEntity<List<Article>>> response) {
                progressBar.setVisibility(View.GONE);
                handleResponse(response);
            }

            @Override
            public void onFailure(Call<HttpResponseEntity<List<Article>>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(HomeActivity.this,
                        "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleResponse(Response<HttpResponseEntity<List<Article>>> response) {
        if (response.isSuccessful() && response.body() != null) {
            HttpResponseEntity<List<Article>> httpResponse = response.body();
            if ("666".equals(httpResponse.getCode())) {
                updateArticleList(httpResponse.getData());
            } else {
                Toast.makeText(this, httpResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "服务器响应异常", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateArticleList(List<Article> newArticles) {
        articleList.clear();
        articleList.addAll(newArticles);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 不再需要adapter.cleanup()调用
    }
}