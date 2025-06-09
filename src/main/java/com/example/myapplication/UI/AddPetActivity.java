package com.example.myapplication.UI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.myapplication.R;
import com.example.myapplication.bean.ApiService;
import com.example.myapplication.bean.HttpResponseEntityp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddPetActivity extends AppCompatActivity {
    private static final int REQUEST_SELECT_IMAGE = 1;
    private ArrayList<String> selectedImages = new ArrayList<>();
    private ImageView imgCat;
    private EditText etName, etAge, etGender, etVaccine, etSterilization, etSource, etRemark;
    private Button btnConfirm;
    private ApiService apiService;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petedit); // 假设你的 XML 名叫 activity_edit_animal.xml

        imgCat = findViewById(R.id.imgCat);
        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etGender = findViewById(R.id.etGender);
        etVaccine = findViewById(R.id.etVaccine);
        etSterilization = findViewById(R.id.etSterilization);
        etSource = findViewById(R.id.etSource);
        etRemark = findViewById(R.id.etRemark);
        btnConfirm = findViewById(R.id.btnConfirm);
        iniView();
        // 点击头像选择图片
        imgCat.setOnClickListener(v ->{
            Intent intent = new Intent(AddPetActivity.this, PhotoShowActivity.class);
            startActivityForResult(intent, REQUEST_SELECT_IMAGE);
        });

        // 提交按钮
        btnConfirm.setOnClickListener(v -> handleSubmit());
    }

    protected void iniView() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.2.11:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImages = data.getStringArrayListExtra("selected_images");
            updateImagePreview();
        }
    }

    private void updateImagePreview() {
        if (selectedImages != null && !selectedImages.isEmpty()) {
            Glide.with(this)
                    .load(selectedImages.get(0))
                    .centerCrop()
                    .placeholder(R.drawable.ic_add)
                    .into(imgCat);
        } else {
            imgCat.setImageResource(R.drawable.ic_add);
        }
    }
    private void handleSubmit() {
        String name = etName.getText().toString().trim();
        String age = etAge.getText().toString().trim();
        String gender = etGender.getText().toString().trim();
        String vaccine = etVaccine.getText().toString().trim();
        String sterilization = etSterilization.getText().toString().trim();
        String source = etSource.getText().toString().trim();
        String remark = etRemark.getText().toString().trim();
        userId = getIntent().getStringExtra("userId");
        // 示例：简单校验
        if (name.isEmpty()) {
            Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
            return;
        }
        File coverFile = new File(selectedImages.get(0));
        RequestBody requestCoverFile = RequestBody.create(MediaType.parse("image/png"), coverFile);
        MultipartBody.Part animalImage = MultipartBody.Part.createFormData("animalImage", coverFile.getName(), requestCoverFile);


// 创建文本参数（如 publishTag、publishContent 等）
        RequestBody Name = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody Age = RequestBody.create(MediaType.parse("text/plain"), age);
        RequestBody Gender = RequestBody.create(MediaType.parse("text/plain"), gender);
        RequestBody Vaccine = RequestBody.create(MediaType.parse("text/plain"), vaccine);
        RequestBody Str = RequestBody.create(MediaType.parse("text/plain"), sterilization);
        RequestBody Source = RequestBody.create(MediaType.parse("text/plain"), source);
        RequestBody Remark = RequestBody.create(MediaType.parse("text/plain"), remark);
        RequestBody ReportId = RequestBody.create(MediaType.parse("text/plain"),userId);

        Call<HttpResponseEntityp> call = apiService.addPet(
                Name,Gender,Vaccine,Str,ReportId,Age,Source,Remark,
                animalImage
        );
        call.enqueue(new Callback<HttpResponseEntityp>() {
            @Override
            public void onResponse(Call<HttpResponseEntityp> call, Response<HttpResponseEntityp> response) {
                if (response.isSuccessful() && response.body() != null) {
                    HttpResponseEntityp result = response.body();
                    if ("666".equals(result.getCode())) {
                        // 登录成功
                        Toast.makeText(AddPetActivity.this, "上传成功！", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddPetActivity.this, PetActivity.class).putExtra("userId",userId));
                        finish();
                    } else {
                        // 登录失败
                        Toast.makeText(AddPetActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 服务器错误
                    Toast.makeText(AddPetActivity.this, "服务器错误，请重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HttpResponseEntityp> call, Throwable t) {
                Log.d("text",t.getMessage());
                // 网络请求失败
                Toast.makeText(AddPetActivity.this, "网络错误，请检查您的连接", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
