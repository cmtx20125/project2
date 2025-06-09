package com.example.myapplication.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.example.myapplication.bean.updateEntity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditUserActivity extends AppCompatActivity {
    private static final int REQUEST_SELECT_IMAGE = 1;
    private ArrayList<String> selectedImages = new ArrayList<>();

    // 声明所有EditText控件
    private EditText etName, etGender, etAge, etDate, etAddress, etPhone, etPassword;
    private ImageView ivUserPic;
    private ProgressBar progressBar;
    private Button btnSave;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wodegai); // 替换为你的XML布局文件名

        // 初始化所有视图
        initViews();

        // 从Intent获取数据并设置到对应EditText
        bindIntentDataToViews();
// 点击头像选择图片
        ivUserPic.setOnClickListener(v ->{
            Intent intent = new Intent(EditUserActivity.this, PhotoShowActivity.class);
            startActivityForResult(intent, REQUEST_SELECT_IMAGE);
        });
        // 返回按钮点击事件
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());

        // 保存按钮点击事件
        btnSave.setOnClickListener(v -> saveData());
    }

    private void initViews() {
        ivUserPic = findViewById(R.id.my_pic);
        etName = findViewById(R.id.my_name);
        etGender = findViewById(R.id.my_gender);
        etAge = findViewById(R.id.my_age);
        etDate = findViewById(R.id.my_date);
        etAddress = findViewById(R.id.my_address);
        etPhone = findViewById(R.id.my_phone);
        etPassword = findViewById(R.id.my_password);
        progressBar = findViewById(R.id.progressBar);
        btnSave = findViewById(R.id.btn_edit);

        // 设置生日字段不可直接编辑（只能通过点击选择）
        etDate.setKeyListener(null);
        etDate.setOnClickListener(v -> showDatePicker());
    }

    private void bindIntentDataToViews() {
        Intent intent = getIntent();

        // 设置基本数据到对应EditText
        etName.setText(intent.getStringExtra("myName"));
        etGender.setText(intent.getStringExtra("myGender"));
        etPhone.setText(intent.getStringExtra("myPhone"));
        etAddress.setText(intent.getStringExtra("myAddress"));
        etPassword.setText(intent.getStringExtra("myPassword"));

        // 设置日期（转换时间戳为可读格式）
        long dateMillis = intent.getLongExtra("myDate", System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        etDate.setText(sdf.format(new Date(dateMillis)));

        String imageUrl =getIntent().getStringExtra("myIC").replace("127.0.0.1","192.168.2.11");
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
                .into(ivUserPic);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.2.11:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        // 年龄字段需要特殊处理（如果没有传递age数据）
        // etAge.setText(""); // 如果没有age数据可以留空或设置提示
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
                    .into(ivUserPic);
        } else {
            ivUserPic.setImageResource(R.drawable.ic_add);
        }
    }
    private void showDatePicker() {
        // 获取当前显示的日期
        String currentDateStr = etDate.getText().toString();
        Calendar calendar = Calendar.getInstance();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
            Date date = sdf.parse(currentDateStr);
            if (date != null) {
                calendar.setTime(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 创建日期选择对话框
        DatePickerDialog datePicker = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    // 用户选择日期后的回调
                    String selectedDate = String.format(Locale.getDefault(),
                            "%d年%02d月%02d日", year, month + 1, dayOfMonth);
                    etDate.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePicker.show();
    }

    private void saveData() {
        // 显示加载进度条
        progressBar.setVisibility(View.VISIBLE);
        String userName = etName.getText().toString().trim();
        String userGender = etGender.getText().toString().trim();
        String userPhone = etPhone.getText().toString().trim();
        String userPassword = etPassword.getText().toString().trim();
        String userId = getIntent().getStringExtra("userId"); // 从Intent获取
        String userAddress = etAddress.getText().toString().trim();
        String userBirthDate = etDate.getText().toString().trim();
        String backendFormatDate = convertToBackendFormat(userBirthDate);
        // 在 saveData() 方法中替换为：

        // 收集所有字段数据
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), userName);
        RequestBody gender = RequestBody.create(MediaType.parse("text/plain"), userGender);
        RequestBody phone = RequestBody.create(MediaType.parse("text/plain"), userPhone);
        RequestBody pwd = RequestBody.create(MediaType.parse("text/plain"), userPassword);
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), userId);
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), userAddress);
        RequestBody time = RequestBody.create(MediaType.parse("text/plain"), backendFormatDate);
        File coverFile = new File(selectedImages.get(0));
        RequestBody requestCoverFile = RequestBody.create(MediaType.parse("image/png"), coverFile);
        MultipartBody.Part animalImage = MultipartBody.Part.createFormData("animalImage", coverFile.getName(), requestCoverFile);

        Call<updateEntity> call = apiService.updateUser(
                name, gender, phone, pwd, id, address, time, animalImage
        );
        call.enqueue(new Callback<updateEntity>() {
            @Override
            public void onResponse(Call<updateEntity> call, Response<updateEntity> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateEntity result = response.body();
                    if ("666".equals(result.getCode())) {
                        // 登录成功
                        Toast.makeText(EditUserActivity.this, "上传成功！", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(EditUserActivity.this, ProfileDetailActivity.class).putExtra("userId",userId));
                        finish();
                    } else {
                        // 登录失败
                        Toast.makeText(EditUserActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 服务器错误
                    Toast.makeText(EditUserActivity.this, "服务器错误，请重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<updateEntity> call, Throwable t) {
                Log.d("text",t.getMessage());
                // 网络请求失败
                Toast.makeText(EditUserActivity.this, "网络错误，请检查您的连接", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // 转换方法
    private String convertToBackendFormat(String displayDate) {
        try {
            SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
            SimpleDateFormat backendFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = displayFormat.parse(displayDate);
            return backendFormat.format(date); // "2025-04-12"
        } catch (ParseException e) {
            e.printStackTrace();
            return ""; // 或抛出异常提示用户输入正确格式
        }
    }
    @Override
    protected void onDestroy() {
        // 先清理 Glide，再调用 super.onDestroy()
        Glide.with(this).clear(ivUserPic);  // 清理特定 ImageView
        Glide.get(this).clearMemory();      // 可选：清除内存缓存（激进策略）
        super.onDestroy();
    }
}