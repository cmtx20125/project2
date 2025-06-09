package com.example.myapplication.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.textclassifier.ConversationActions;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.example.myapplication.R;
import com.example.myapplication.bean.ApiService;
import com.example.myapplication.bean.HttpResponseEntityS;
import com.example.myapplication.bean.HttpResponseEntityp;
import com.example.myapplication.bean.Publish;
import com.example.myapplication.bean.Report;
import com.example.myapplication.bean.RetrofitClient;
import com.example.myapplication.bean.User;
import com.example.myapplication.bean.minIOUploader;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReportTTActivity extends AppCompatActivity {
    private static final int REQUEST_SELECT_IMAGE = 1;
    private ArrayList<String> selectedImages = new ArrayList<>();
    private ImageView btnAddImage;
    private EditText etContent;
    private Report report;
    private String userId;
    private User user;
    private ApiService apiService;
    private static final int LOCATION_PERMISSION_REQUEST = 100;
    private LocationManager locationManager;
    private TextView locationText;
    private LinearLayout locationLayout;
    private String lan = "hello";
    private String log = "hello";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shangbaot2);
        String title =  getIntent().getStringExtra("content");
        report = new Report();
        report.setReportName(title);
        userId= getIntent().getStringExtra("userId");
        report.setReportUserId(userId);
        ArrayList<String> selectedImage = getIntent().getStringArrayListExtra("selected_images");
        btnAddImage = findViewById(R.id.btn_add_image);
        etContent = findViewById(R.id.et_content);
        report.setReportId(UUID.randomUUID().toString());
        // 找到布局并设置点击事件
        LinearLayout profileLayout = findViewById(R.id.profile_layout);
        profileLayout.setOnClickListener(v -> {
            // 创建跳转到新界面的 Intent
            Intent intent = new Intent(ReportTTActivity.this, AddAnimalActivity.class);

            // 如果需要传递数据
             intent.putExtra("publishId", report.getReportId());

            startActivity(intent);


        });
        locationText = findViewById(R.id.locationText);
        locationLayout = findViewById(R.id.locationLayout);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationLayout.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST);
            } else {
                getCurrentLocation();
            }
        });

        btnAddImage.setOnClickListener(v -> {
            Intent intent = new Intent(ReportTTActivity.this, PhotoShowActivity.class);
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
                        Toast.makeText(ReportTTActivity.this, "成功", Toast.LENGTH_SHORT).show();

                        // 获取用户信息（可选）
                        user = result.getData();

                    } else {
                        // 登录失败
                        Toast.makeText(ReportTTActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 服务器错误
                    Toast.makeText(ReportTTActivity.this, "服务器错误，请重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HttpResponseEntityS> call, Throwable t) {
                // 网络请求失败
                Toast.makeText(ReportTTActivity.this, "网络错误，请检查您的连接", Toast.LENGTH_SHORT).show();
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
        report.setReportContent(content);
        report.setReportUserName(user.getUserName());

        report.setReportTagOne("1");
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
        RequestBody publishContent = RequestBody.create(MediaType.parse("text/plain"), report.getReportContent());
        RequestBody publishName = RequestBody.create(MediaType.parse("text/plain"), report.getReportName());
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), report.getReportUserId());
        RequestBody userName = RequestBody.create(MediaType.parse("text/plain"), report.getReportUserName());
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), report.getReportAddress());
        RequestBody reportId = RequestBody.create(MediaType.parse("text/plain"), report.getReportId());
        RequestBody la = RequestBody.create(MediaType.parse("text/plain"), lan);
        RequestBody lo = RequestBody.create(MediaType.parse("text/plain"), log);
// 发起请求
        Call<HttpResponseEntityp> call = apiService.addReport(
                publishTag,publishContent,userId,publishName,address,userName,reportId,la,lo,
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
                        Toast.makeText(ReportTTActivity.this, "上传成功！", Toast.LENGTH_SHORT).show();


                        // 跳转到首页或其他页面
                        Intent intent = new Intent(ReportTTActivity.this, ReportActivity.class);
                        intent.putExtra("userId",user.getUserId());
                        intent.putExtra("user", user);  // 传递用户信息
                        startActivity(intent);
                        finish();
                    } else {
                        // 登录失败
                        Toast.makeText(ReportTTActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 服务器错误
                    Toast.makeText(ReportTTActivity.this, "服务器错误，请重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HttpResponseEntityp> call, Throwable t) {
                Log.d("text",t.getMessage());
                // 网络请求失败
                Toast.makeText(ReportTTActivity.this, "网络错误，请检查您的连接", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getCurrentLocation() {
        try {
            // 优先使用 NETWORK_PROVIDER，提高室内定位成功率
            Location location = null;
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }

                if (location == null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }

                if (location != null) {
                    updateLocationInfo(location);
                } else {
                    // 注册监听等待一次位置更新
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            1000,
                            1,
                            new LocationListener() {
                                @Override
                                public void onLocationChanged(@NonNull Location location) {
                                    updateLocationInfo(location);
                                    locationManager.removeUpdates(this); // 停止监听
                                }
                            });
                }
            } else {
                Toast.makeText(this, "定位权限未授予", Toast.LENGTH_SHORT).show();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(this, "定位失败，权限异常", Toast.LENGTH_SHORT).show();
        }
    }
    private void updateLocationInfo(Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        lan =  String.valueOf(lat);
        log =  String.valueOf(lon);
        Log.d("定位", "获取到经纬度：" + lat + ", " + lon);

        Geocoder geocoder = new Geocoder(ReportTTActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String addressText = address.getCountryName() + " "
                        + address.getAdminArea() + " "
                        + address.getLocality() + " "
                        + address.getSubLocality() + " "
                        + address.getThoroughfare();

                locationText.setText("当前位置：\n" + addressText);
                report.setReportAddress(addressText);
            } else {
                locationText.setText("未能获取详细地址");
            }
        } catch (IOException e) {
            e.printStackTrace();
            locationText.setText("反地理编码失败");
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "请开启定位权限", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
