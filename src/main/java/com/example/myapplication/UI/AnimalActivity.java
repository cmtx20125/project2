package com.example.myapplication.UI;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.myapplication.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.bean.Animal;
import com.example.myapplication.bean.ApiService;
import com.example.myapplication.bean.HttpResponseEntity;
import com.example.myapplication.bean.HttpResponseEntityS;
import com.example.myapplication.bean.HttpResponseEntityd;
import com.example.myapplication.bean.Pet;
import com.example.myapplication.bean.Report;
import com.example.myapplication.bean.ReviewResponse;
import com.example.myapplication.bean.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AnimalActivity extends AppCompatActivity {

    private ImageView btnBack, imgCat;
    private TextView tvName, tvAge, tvGender, tvVaccine, tvNeuter, tvSource, tvRemark;
    private Button btnEdit, btnAdopt;
    private Animal animal;
    private Retrofit retrofit;
    private ApiService apiService;
    private String userId;
    private String reportId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangan);

        bindViews();
        setupClickListeners();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.2.11:8080/") // 替换为你自己的 API 地址
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        reportId = getIntent().getStringExtra("reportId"); // 从前一个页面传入动物ID
        userId = getIntent().getStringExtra("userId");
        fetchAnimalData(reportId);
    }

    private void bindViews() {
        btnBack = findViewById(R.id.btnBack);
        imgCat = findViewById(R.id.imgCat);
        tvName = findViewById(R.id.tvName);
        tvAge = findViewById(R.id.tvAge);
        tvGender = findViewById(R.id.tvGender);
        tvVaccine = findViewById(R.id.tvVaccine);
        tvNeuter = findViewById(R.id.tvNeuter);
        tvSource = findViewById(R.id.tvSource);
        tvRemark = findViewById(R.id.tvRemark);
        btnEdit = findViewById(R.id.btnEdit);
        btnAdopt = findViewById(R.id.btnAdopt);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(view -> finish());

        btnEdit.setOnClickListener(view -> {
            Toast.makeText(this, "进入编辑模式", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, EditAnimalActivity.class).putExtra("tvName",animal.getAnimalName())
                    .putExtra("tvAge",animal.getAnimalYear()).putExtra("tvGender",animal.getAnimalGender())
                    .putExtra("tvVaccine",animal.getAnimalYM()).putExtra("tvNeuter",animal.getAnimalJue())
                    .putExtra("tvSource",animal.getAnimalYuan()).putExtra("tvRemark",animal.getAnimalContent())
                    .putExtra("tvPic",animal.getAnimalImg()).putExtra("reportId",reportId)
                    .putExtra("userId",userId).putExtra("id",animal.getAnimalId()));
        });

        btnAdopt.setOnClickListener(view -> {

            // 可添加 API 调用处理领养逻辑
            apiService.deleteAnimal(animal).enqueue(new Callback<ReviewResponse>() {
                @Override
                public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                    if (response.isSuccessful()) {
                        showToast("恭喜您已成功领养");
                    } else {
                        showToast("领养失败：" + response.code());
                    }
                }

                @Override
                public void onFailure(Call<ReviewResponse> call, Throwable t) {
                    showToast("同步网络错误：" + t.getMessage());
                }
            });
            Pet pet = new Pet();
            String relativePath = animal.getAnimalImg().substring("http://127.0.0.1:9005/pet/".length());
            pet.setPetImg(relativePath);
            pet.setPetContent(animal.getAnimalContent());
            pet.setPetYM(animal.getAnimalYM());
            pet.setPetGender(animal.getAnimalGender());
            pet.setPetName(animal.getAnimalName());
            pet.setPetJue(animal.getAnimalJue());
            pet.setUserId(userId);
            pet.setPetYear(animal.getAnimalYear());
            pet.setPetYuan(animal.getAnimalYuan());
            apiService.addPet(pet).enqueue(new Callback<ReviewResponse>() {
                @Override
                public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                    if (response.isSuccessful()) {
                        startActivity(new Intent(AnimalActivity.this,ReportActivity.class)
                                .putExtra("userId",userId));
                    } else {
                        showToast("领养失败：" + response.code());
                    }
                }

                @Override
                public void onFailure(Call<ReviewResponse> call, Throwable t) {
                    showToast("同步网络错误：" + t.getMessage());
                }
            });
        });
    }

    private void fetchAnimalData(String reportId) {
        apiService.getAnimalById(reportId).enqueue(new Callback<HttpResponseEntityd>() {
            @Override
            public void onResponse(Call<HttpResponseEntityd> call,
                                   Response<HttpResponseEntityd> response) {
                if (response.isSuccessful() && response.body() != null) {
                    HttpResponseEntityd result = response.body();
                    if ("666".equals(result.getCode())) {
                        // 登录成功
                        animal = result.getData();
                       updateUI(result.getData());
                    } else {
                        // 登录失败
                        Toast.makeText(AnimalActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 服务器错误
                    Toast.makeText(AnimalActivity.this, "服务器错误，请重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HttpResponseEntityd> call, Throwable t) {
                Toast.makeText(AnimalActivity.this,
                        "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showToast(String msg) {
        Toast.makeText(AnimalActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
    private void updateUI(Animal animal) {
        tvName.setText(animal.getAnimalName());
        tvAge.setText(animal.getAnimalYear());
        tvGender.setText(animal.getAnimalGender());
        tvVaccine.setText(animal.getAnimalYM());
        tvNeuter.setText(animal.getAnimalJue());
        tvSource.setText(animal.getAnimalYuan());
        tvRemark.setText(animal.getAnimalContent());

        // 加载图片（建议使用 Glide 或 Picasso）
        String imageUrl = animal.getAnimalImg().replace("127.0.0.1","192.168.2.11");
        Log.d("MinIO", "Loading image URL: " + imageUrl);

        Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .placeholder(R.drawable.index_bg)
                .error(R.drawable.dog)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        if (e != null) {
                            Log.e("Glide", "Image load failed: " + imageUrl, e);
                            if (e.getCause() instanceof java.net.ConnectException) {
                                Log.e("NETWORK", "Connection failed. Please check:\n" +
                                        "1. MinIO server status\n" +
                                        "2. URL correctness: " + imageUrl + "\n" +
                                        "3. Network connection");
                            }
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                                                   Target<Drawable> target, DataSource dataSource,
                                                   boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imgCat);
    }
}