package com.example.myapplication.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.example.myapplication.bean.Pet;
import com.example.myapplication.bean.PetAdapter;
import com.example.myapplication.bean.RetrofitClient;
import com.example.myapplication.bean.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PetAdapter adapter;
    private List<Pet> petList = new ArrayList<>();
    private User user;
    private ProgressBar progressBar;
    private ApiService apiService;
    private String userId;
    private ImageView add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets);

        // 获取用户数据
       userId = getIntent().getStringExtra("userId");
        Log.d("MinIO", "用户: " + userId);
        initViews();
        setupRecyclerView();
        fetchArticlesFromBackend(userId);
        // 返回按钮点击事件
        ImageView backButton = findViewById(R.id.btn_back);
        add = findViewById(R.id.btn_add);
        add.setOnClickListener(v->{
            startActivity(new Intent(this, AddPetActivity.class).putExtra("userId",userId));
            finish();
        });
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(PetActivity.this, ProfileActivity.class);
            intent.putExtra("userId",userId);
            startActivity(intent);  // 启动 ProfileActivity
            finish();  // 结束当前活动
        });
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        apiService = RetrofitClient.getInstance().create(ApiService.class);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PetAdapter(this, petList); // 移除了MinioClient参数
        recyclerView.setAdapter(adapter);
    }



    private void fetchArticlesFromBackend(String userId) {
        progressBar.setVisibility(View.VISIBLE);
        apiService.getAllPets(userId).enqueue(new Callback<HttpResponseEntity<List<Pet>>>() {
            @Override
            public void onResponse(Call<HttpResponseEntity<List<Pet>>> call,
                                   Response<HttpResponseEntity<List<Pet>>> response) {
                progressBar.setVisibility(View.GONE);
                handleResponse(response);
            }

            @Override
            public void onFailure(Call<HttpResponseEntity<List<Pet>>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(PetActivity.this,
                        "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleResponse(Response<HttpResponseEntity<List<Pet>>> response) {
        if (response.isSuccessful() && response.body() != null) {
            HttpResponseEntity<List<Pet>> httpResponse = response.body();
            if ("666".equals(httpResponse.getCode())) {
                updateArticleList(httpResponse.getData());
            } else {
                Toast.makeText(this, httpResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "服务器响应异常", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateArticleList(List<Pet> newPets) {
        petList.clear();
        petList.addAll(newPets);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 不再需要adapter.cleanup()调用
    }
}