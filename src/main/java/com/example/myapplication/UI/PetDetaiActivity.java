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
import com.example.myapplication.bean.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PetDetaiActivity extends AppCompatActivity {

    private ImageView btnBack, imgCat;
    private TextView tvName, tvAge, tvGender, tvVaccine, tvNeuter, tvSource, tvRemark;
    private Button btnEdit;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);

        bindViews();
        setupClickListeners();



        String petId = getIntent().getStringExtra("petId");; // 从前一个页面传入动物ID
        userId = getIntent().getStringExtra("userId");
        fetchAnimalData();
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
        btnEdit = findViewById(R.id.btnConfirm);

    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(PetDetaiActivity.this, PetActivity.class);
            intent.putExtra("userId",userId);
            startActivity(intent);  // 启动 ProfileActivity
            finish();  // 结束当前活动
        });

        btnEdit.setOnClickListener(view -> {
            Toast.makeText(this, "进入编辑模式", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, EditPetActivity.class).putExtra("tvName",getIntent().getStringExtra("petName"))
                    .putExtra("tvAge",getIntent().getStringExtra("petYear")).putExtra("tvGender",getIntent().getStringExtra("petGender"))
                    .putExtra("tvVaccine",getIntent().getStringExtra("petYM")).putExtra("tvNeuter",getIntent().getStringExtra("petJue"))
                    .putExtra("tvSource",getIntent().getStringExtra("petYuan")).putExtra("tvRemark",getIntent().getStringExtra("petContent"))
                    .putExtra("tvPic",getIntent().getStringExtra("petImg"))
                    .putExtra("id",getIntent().getStringExtra("petId")).putExtra("userId",userId));
        });


    }

    private void fetchAnimalData() {
        Pet pet = new Pet();
        String petId = getIntent().getStringExtra("petId");
        String petName = getIntent().getStringExtra("petName");
        String petGender = getIntent().getStringExtra("petGender");
        String petContent = getIntent().getStringExtra("petContent");
        String petYM = getIntent().getStringExtra("petYM");
        String petJue = getIntent().getStringExtra("petJue");
        String petYear = getIntent().getStringExtra("petYear");
        String petYuan = getIntent().getStringExtra("petYuan");
        String petImg = getIntent().getStringExtra("petImg");
        pet.setPetContent(petContent);
        pet.setPetGender(petGender);
        pet.setPetImg(petImg);
        pet.setPetJue(petJue);
        pet.setPetName(petName);
        pet.setPetYM(petYM);
        pet.setPetYear(petYear);
        pet.setPetYuan(petYuan);
        updateUI(pet);
    }

    private void updateUI(Pet pet) {
        tvName.setText(pet.getPetName());
        tvAge.setText(pet.getPetYear());
        tvGender.setText(pet.getPetGender());
        tvVaccine.setText(pet.getPetYM());
        tvNeuter.setText(pet.getPetJue());
        tvSource.setText(pet.getPetYuan());
        tvRemark.setText(pet.getPetContent());

        // 加载图片（建议使用 Glide 或 Picasso）
        String imageUrl = pet.getPetImg().replace("127.0.0.1","192.168.2.11");
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