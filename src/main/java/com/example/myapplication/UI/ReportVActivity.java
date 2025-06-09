package com.example.myapplication.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.bean.ApiService;
import com.example.myapplication.bean.HttpResponseEntityS;
import com.example.myapplication.bean.HttpResponseEntityp;
import com.example.myapplication.bean.Publish;
import com.example.myapplication.bean.Report;
import com.example.myapplication.bean.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
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

public class ReportVActivity extends AppCompatActivity {
    private static final int REQUEST_SELECT_IMAGE = 1;
    private ArrayList<String> selectedImages = new ArrayList<>();
    private static final int PICK_VIDEO_REQUEST = 101;
    private ImageView btnAddImage;
    private Button btnPublish;
    private EditText etTitle;
    private EditText etContent;
    private ApiService apiService;
    private Uri selectedVideoUri = null; // 记录选择的视频路径
    private String userId;
    private User user;
    private Report report;
    private static final int LOCATION_PERMISSION_REQUEST = 100;
    private LocationManager locationManager;
    private TextView locationText;
    private LinearLayout locationLayout;
    private String lan = "hello";
    private String log = "hello";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shangbaos); // XML 文件名为 activity_content_publish.xml

        btnAddImage = findViewById(R.id.btn_add_image);
        btnPublish = findViewById(R.id.btn_publish);
        etTitle = findViewById(R.id.et_title);
        etContent = findViewById(R.id.et_content);
             report = new Report();
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
            Intent intent = new Intent(ReportVActivity.this, PhotoShowActivity.class);
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
                        Toast.makeText(ReportVActivity.this, "成功", Toast.LENGTH_SHORT).show();

                        // 获取用户信息（可选）
                        user = result.getData();

                    } else {
                        // 登录失败
                        Toast.makeText(ReportVActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 服务器错误
                    Toast.makeText(ReportVActivity.this, "服务器错误，请重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HttpResponseEntityS> call, Throwable t) {
                // 网络请求失败
                Toast.makeText(ReportVActivity.this, "网络错误，请检查您的连接", Toast.LENGTH_SHORT).show();
            }
        });
        report.setReportId(UUID.randomUUID().toString());
        LinearLayout Layout = findViewById(R.id.profile_layout);
        Layout.setOnClickListener(v -> {
            // 创建跳转到新界面的 Intent
            Intent intent = new Intent(ReportVActivity.this, AddAnimalActivity.class);
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
        RequestBody publishContent = RequestBody.create(MediaType.parse("text/plain"), content);
        RequestBody publishName = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"),user.getUserId());
        RequestBody userName = RequestBody.create(MediaType.parse("text/plain"), user.getUserName());
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), report.getReportAddress());
        RequestBody reportId = RequestBody.create(MediaType.parse("text/plain"), report.getReportId());
        RequestBody la = RequestBody.create(MediaType.parse("text/plain"), lan);
        RequestBody lo = RequestBody.create(MediaType.parse("text/plain"), log);
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
// 发起请求
        Call<HttpResponseEntityp> call = apiService.addReportt(
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
                        Toast.makeText(ReportVActivity.this, "上传成功！", Toast.LENGTH_SHORT).show();
                        // 跳转到首页或其他页面
                        Intent intent = new Intent(ReportVActivity.this, ReportActivity.class);
                        intent.putExtra("userId",user.getUserId());
                        intent.putExtra("user", user);  // 传递用户信息
                        startActivity(intent);
                        finish();
                    } else {
                        // 登录失败
                        Toast.makeText(ReportVActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 服务器错误
                    Toast.makeText(ReportVActivity.this, "服务器错误，请重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HttpResponseEntityp> call, Throwable t) {
                Toast.makeText(ReportVActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                // 网络请求失败
                Toast.makeText(ReportVActivity.this, "网络错误，请检查您的连接", Toast.LENGTH_SHORT).show();
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

        Geocoder geocoder = new Geocoder(ReportVActivity.this, Locale.getDefault());
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
