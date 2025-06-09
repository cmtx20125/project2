package com.example.myapplication.UI;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.bean.ApiService;
import com.example.myapplication.bean.HttpResponseEntityS;
import com.example.myapplication.bean.HttpResponseEntityp;
import com.example.myapplication.bean.Publish;
import com.example.myapplication.bean.User;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PublishVActivity extends AppCompatActivity {
    private static final int REQUEST_SELECT_IMAGE = 1;
    private ArrayList<String> selectedImages = new ArrayList<>();
    private static final int PICK_VIDEO_REQUEST = 101;
    private ImageView btnAddImage;
    private Button btnPublish;
    private EditText etTitle;
    private EditText etContent;
    private ApiService apiService;
    private Uri selectedVideoUri = null; // 记录选择的视频路径
    private Publish publish;
    private String userId;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabiaos); // XML 文件名为 activity_content_publish.xml

        btnAddImage = findViewById(R.id.btn_add_image);
        btnPublish = findViewById(R.id.btn_publish);
        etTitle = findViewById(R.id.et_title);
        etContent = findViewById(R.id.et_content);
        publish = new Publish();
        userId = getIntent().getStringExtra("userId");
        // 点击添加视频
        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVideoPicker();
            }
        });
        LinearLayout profileLayout = findViewById(R.id.cover);
        profileLayout.setOnClickListener(v -> {
            // 创建跳转到新界面的 Intent
            Intent intent = new Intent(PublishVActivity.this, PhotoShowActivity.class);
            startActivityForResult(intent, REQUEST_SELECT_IMAGE);


        });
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)  // 上传大文件需要更长时间
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.2.11:8080/")
                .client(client)  // 使用自定义Client
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
                        Toast.makeText(PublishVActivity.this, "成功", Toast.LENGTH_SHORT).show();

                        // 获取用户信息（可选）
                        user = result.getData();

                    } else {
                        // 登录失败
                        Toast.makeText(PublishVActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 服务器错误
                    Toast.makeText(PublishVActivity.this, "服务器错误，请重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HttpResponseEntityS> call, Throwable t) {
                // 网络请求失败
                Toast.makeText(PublishVActivity.this, "网络错误，请检查您的连接", Toast.LENGTH_SHORT).show();
            }
        });
        // 点击发布
        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishContent();
            }
        });

    }

    // 打开系统视频选择器
    private void openVideoPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(Intent.createChooser(intent, "选择视频"), PICK_VIDEO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedVideoUri = data.getData();
            if (selectedVideoUri != null) {
                // 显示视频缩略图
                loadVideoThumbnail(selectedVideoUri);
                Toast.makeText(this, "视频已选择", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImages = data.getStringArrayListExtra("selected_images");
            if (selectedImages != null && !selectedImages.isEmpty()) {
                // 显示第一张图在 btnAddImage 上
                Toast.makeText(this, "封面已选择", Toast.LENGTH_SHORT).show();
            }
        }
    }
    // 专用方法加载视频缩略图
    private void loadVideoThumbnail(Uri videoUri) {
        try {
            Glide.with(this)
                    .asBitmap() // 确保加载为静态图片
                    .load(videoUri)
                    .thumbnail(0.1f)
                    .error(R.drawable.ic_broken_image) // 错误占位图
                    .placeholder(R.drawable.ic_image) // 加载中占位图
                    .into(btnAddImage);

            Log.d("VideoLoad", "成功加载视频URI: " + videoUri.toString());
        } catch (Exception e) {
            Log.e("VideoLoad", "加载视频缩略图失败", e);
            btnAddImage.setImageResource(R.drawable.ic_broken_image);
        }
    }
    // 发布内容
    private void publishContent() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();
        if (title.isEmpty()) {
            Toast.makeText(this, "请输入标题", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedVideoUri == null) {
            Toast.makeText(this, "请先选择视频", Toast.LENGTH_SHORT).show();
            return;
        }
        publish.setPublishName(title);
        publish.setPublishContent(content);
        File coverFile = new File(selectedImages.get(0));
        RequestBody requestCoverFile = RequestBody.create(MediaType.parse("image/png"), coverFile);
        MultipartBody.Part coverPart = MultipartBody.Part.createFormData("coverImage", coverFile.getName(), requestCoverFile);

// 创建内容图片
        String videoPath = getRealPathFromUri(selectedVideoUri);
        File contentFile = new File(videoPath);
        RequestBody requestContentFile = RequestBody.create(MediaType.parse("video/*"), contentFile);
        MultipartBody.Part contentPart = MultipartBody.Part.createFormData("contentImage", contentFile.getName(), requestContentFile);

// 创建文本参数（如 publishTag、publishContent 等）
        RequestBody publishTag = RequestBody.create(MediaType.parse("text/plain"), "2");
        RequestBody publishContent = RequestBody.create(MediaType.parse("text/plain"), publish.getPublishContent());
        RequestBody publishName = RequestBody.create(MediaType.parse("text/plain"), publish.getPublishName());
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"),user.getUserId());
        RequestBody userName = RequestBody.create(MediaType.parse("text/plain"), user.getUserName());

// 发起请求
        Call<HttpResponseEntityp> call = apiService.addPublisht(
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
                        Toast.makeText(PublishVActivity.this, "上传成功！", Toast.LENGTH_SHORT).show();
                        // 跳转到首页或其他页面
                        Intent intent = new Intent(PublishVActivity.this, TrendsActivity.class);
                        intent.putExtra("userId",user.getUserId());
                        intent.putExtra("user", user);  // 传递用户信息
                        startActivity(intent);
                        finish();
                    } else {
                        // 登录失败
                        Toast.makeText(PublishVActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 服务器错误
                    Toast.makeText(PublishVActivity.this, "服务器错误，请重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HttpResponseEntityp> call, Throwable t) {
                Log.d("text",t.getMessage());
                // 网络请求失败
                Toast.makeText(PublishVActivity.this, "网络错误，请检查您的连接", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return uri.getPath();
    }
}
