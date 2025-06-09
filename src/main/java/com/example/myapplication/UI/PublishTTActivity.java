package com.example.myapplication.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.textclassifier.ConversationActions;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.example.myapplication.R;
import com.example.myapplication.bean.ApiService;
import com.example.myapplication.bean.HttpResponseEntityS;
import com.example.myapplication.bean.HttpResponseEntityp;
import com.example.myapplication.bean.Publish;
import com.example.myapplication.bean.RetrofitClient;
import com.example.myapplication.bean.User;
import com.example.myapplication.bean.minIOUploader;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import com.google.gson.Gson;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PublishTTActivity extends AppCompatActivity {
    private static final int REQUEST_SELECT_IMAGE = 1;
    private ArrayList<String> selectedImages = new ArrayList<>();
    private ImageView btnAddImage;
    private EditText etContent;
    private Publish publish;
    private String userId;
    private User user;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabiaot2);
        String title =  getIntent().getStringExtra("content");
        publish = new Publish();
        publish.setPublishName(title);
        userId= getIntent().getStringExtra("userId");
        publish.setPublishUserId(userId);
        ArrayList<String> selectedImage = getIntent().getStringArrayListExtra("selected_images");
        btnAddImage = findViewById(R.id.btn_add_image);
        etContent = findViewById(R.id.et_content);

        btnAddImage.setOnClickListener(v -> {
            Intent intent = new Intent(PublishTTActivity.this, PhotoShowActivity.class);
            startActivityForResult(intent, REQUEST_SELECT_IMAGE);
        });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.2.11:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
        Call<HttpResponseEntityS> call = apiService.getUser(userId);
        call.enqueue(new Callback<HttpResponseEntityS>() {
            @Override
            public void onResponse(Call<HttpResponseEntityS> call, Response<HttpResponseEntityS> response) {
                if (response.isSuccessful() && response.body() != null) {
                    HttpResponseEntityS result = response.body();
                    if ("666".equals(result.getCode())) {
                        // 登录成功
                        Toast.makeText(PublishTTActivity.this, "成功", Toast.LENGTH_SHORT).show();

                        // 获取用户信息（可选）
                        user = result.getData();

                    } else {
                        // 登录失败
                        Toast.makeText(PublishTTActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 服务器错误
                    Toast.makeText(PublishTTActivity.this, "服务器错误，请重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HttpResponseEntityS> call, Throwable t) {
                // 网络请求失败
                Toast.makeText(PublishTTActivity.this, "网络错误，请检查您的连接", Toast.LENGTH_SHORT).show();
            }
        });
        Button btnPublish = findViewById(R.id.btn_publish);
        btnPublish.setOnClickListener(v -> {
            String content = etContent.getText().toString().trim();
            if (content.isEmpty()) {
                Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
                return;
            }

            uploadToBackend(content, selectedImages.get(0),selectedImage.get(0));
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImages = data.getStringArrayListExtra("selected_images");
            if (selectedImages != null && !selectedImages.isEmpty()) {
                // 显示第一张图在 btnAddImage 上
                Glide.with(this)
                        .load(selectedImages.get(0))
                        .into(btnAddImage);
            }
        }
    }
    private void uploadToBackend(String content,String imagePath,String imagePatht){
        // 1. 创建 Publish 对象
        publish.setPublishContent(content);
        publish.setPublishUserName(user.getUserName());

        publish.setPublishTag("1");
// 创建封面图片
        File coverFile = new File(imagePatht);
        RequestBody requestCoverFile = RequestBody.create(MediaType.parse("image/png"), coverFile);
        MultipartBody.Part coverPart = MultipartBody.Part.createFormData("coverImage", coverFile.getName(), requestCoverFile);

// 创建内容图片
        File contentFile = new File(imagePath);
        RequestBody requestContentFile = RequestBody.create(MediaType.parse("image/png"), contentFile);
        MultipartBody.Part contentPart = MultipartBody.Part.createFormData("contentImage", contentFile.getName(), requestContentFile);

// 创建文本参数（如 publishTag、publishContent 等）
        RequestBody publishTag = RequestBody.create(MediaType.parse("text/plain"), "1");
        RequestBody publishContent = RequestBody.create(MediaType.parse("text/plain"), publish.getPublishContent());
        RequestBody publishName = RequestBody.create(MediaType.parse("text/plain"), publish.getPublishName());
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), publish.getPublishUserId());
        RequestBody userName = RequestBody.create(MediaType.parse("text/plain"), publish.getPublishUserName());

// 发起请求
        Call<HttpResponseEntityp> call = apiService.addPublish(
                publishTag,publishContent,userId,publishName,userName,
                coverPart,
                contentPart
        );
        call.enqueue(new Callback<HttpResponseEntityp>() {
            @Override
            public void onResponse(Call<HttpResponseEntityp> call, Response<HttpResponseEntityp> response) {
                if (response.isSuccessful() && response.body() != null) {
                    HttpResponseEntityp result = response.body();
                    if ("666".equals(result.getCode())) {
                        // 登录成功
                        Toast.makeText(PublishTTActivity.this, "上传成功！", Toast.LENGTH_SHORT).show();


                        // 跳转到首页或其他页面
                        Intent intent = new Intent(PublishTTActivity.this, TrendsActivity.class);
                        intent.putExtra("userId",user.getUserId());
                        intent.putExtra("user", user);  // 传递用户信息
                        startActivity(intent);
                        finish();
                    } else {
                        // 登录失败
                        Toast.makeText(PublishTTActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 服务器错误
                    Toast.makeText(PublishTTActivity.this, "服务器错误，请重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HttpResponseEntityp> call, Throwable t) {
                Log.d("text",t.getMessage());
                // 网络请求失败
                Toast.makeText(PublishTTActivity.this, "网络错误，请检查您的连接", Toast.LENGTH_SHORT).show();
            }
        });

    }



}
