package com.example.myapplication.UI;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.bean.ApiService;
import com.example.myapplication.bean.Article;
import com.example.myapplication.bean.ArticleAdapter;
import com.example.myapplication.bean.HttpResponseEntity;
import com.example.myapplication.bean.Report;
import com.example.myapplication.bean.ReportAdapter;
import com.example.myapplication.bean.RetrofitClient;
import com.example.myapplication.bean.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST = 100;
    private LocationManager locationManager;
    private RecyclerView recyclerView;
    private ReportAdapter adapter;
    private List<Report> reportList = new ArrayList<>();
    private final List<Report> allReports = new ArrayList<>(); // 保留原始数据
    private User user;
    private ProgressBar progressBar;
    private ApiService apiService;
    private String userId;
    private String latitude;  // 改为String类型
    private String longitude; // 改为String类型
    private BottomNavigationView bottomNavigationView;
    private EditText etSearch;
    private Button btnSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shangbao);

        // 获取用户数据

        userId = getIntent().getStringExtra("userId");
        initViews();
        checkLocationPermission(); // 重新尝试获取位置
        setupRecyclerView();
        setupBottomNavigation();
        LinearLayout layoutPostDynamic = findViewById(R.id.layoutPostDynamic);
        layoutPostDynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportActivity.this, ReportTActivity.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_report);
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        etSearch = findViewById(R.id.et_search);
        btnSearch = findViewById(R.id.btn_search);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = etSearch.getText().toString().trim();
                if (!keyword.isEmpty()) {
                    performSearch(keyword);
                } else {
                    Toast.makeText(ReportActivity.this, "请输入关键词", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // 监听文本变化，自动恢复原始数据
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    reportList.clear();
                    reportList.addAll(allReports);
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }
    // 检查位置权限
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
        } else {
            getLocation();
        }
    }

    // 处理权限请求结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(this, "需要位置权限才能自动获取位置", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try {
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                Toast.makeText(this, "请打开定位服务", Toast.LENGTH_SHORT).show();
                return;
            }

            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    // 将 double 转为 String，保留6位小数
                    latitude = String.format("%.6f", location.getLatitude());
                    longitude = String.format("%.6f", location.getLongitude());

                    // ✅ 一旦获取位置成功，就调用后端接口
                    fetchArticlesFromBackend();

                    // 移除监听以节省电量
                    locationManager.removeUpdates(this);
                }

                @Override public void onStatusChanged(String provider, int status, Bundle extras) {}
                @Override public void onProviderEnabled(String provider) {}
                @Override public void onProviderDisabled(String provider) {}
            };

            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        0,
                        0,
                        locationListener);
            }
            if (isGPSEnabled) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0,
                        0,
                        locationListener);
            }

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void performSearch(String keyword) {
        List<Report> filteredList = new ArrayList<>();
        for (Report report : reportList) {
            // 假设 Report 中有 title 和 content 字段
            if ((report.getReportName() != null && report.getReportName().contains(keyword)) ||
                    (report.getReportUserName() != null && report.getReportUserName().contains(keyword))) {
                filteredList.add(report);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "未找到相关内容", Toast.LENGTH_SHORT).show();
        }

        adapter.updateList(filteredList); // 需要在 adapter 中添加该方法
    }


    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReportAdapter(this, reportList,userId); // 移除了MinioClient参数
        recyclerView.setAdapter(adapter);
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_report:
                    return true;

                case R.id.nav_home:
                    startActivity(new Intent(this,HomeActivity.class)
                            .putExtra("user", user).putExtra("userId",userId));
                    finish();
                    return true;

                case R.id.nav_adopt:
                    startActivity(new Intent(this, HospitalActivity.class).putExtra("userId",userId));
                    finish();
                    return true;

                case R.id.nav_trends:
                    startActivity(new Intent(this, TrendsActivity.class).putExtra("userId",userId));
                    finish();
                    return true;

                case R.id.nav_profile:
                    startActivity(new Intent(this, ProfileActivity.class).putExtra("userId",userId));
                    finish();
                    return true;

                default:
                    return false;
            }
        });
    }

    private void fetchArticlesFromBackend() {
//        if (latitude == null || longitude == null) {
//            Toast.makeText(this, "正在获取位置信息...", Toast.LENGTH_SHORT).show();
//            checkLocationPermission(); // 重新尝试获取位置
//            return;
//        }
        progressBar.setVisibility(View.VISIBLE);
        apiService.getAllReports(latitude,longitude,userId).enqueue(new Callback<HttpResponseEntity<List<Report>>>() {
            @Override
            public void onResponse(Call<HttpResponseEntity<List<Report>>> call,
                                   Response<HttpResponseEntity<List<Report>>> response) {
                progressBar.setVisibility(View.GONE);
                handleResponse(response);
            }

            @Override
            public void onFailure(Call<HttpResponseEntity<List<Report>>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ReportActivity.this,
                        "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleResponse(Response<HttpResponseEntity<List<Report>>> response) {
        if (response.isSuccessful() && response.body() != null) {
            HttpResponseEntity<List<Report>> httpResponse = response.body();
            if ("666".equals(httpResponse.getCode())) {
                updateArticleList(httpResponse.getData());
            } else {
                Toast.makeText(this, httpResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "服务器响应异常", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateArticleList(List<Report> newReports) {
        reportList.clear();
        reportList.addAll(newReports);
        allReports.clear();
        allReports.addAll(newReports); // 备份原始完整数据
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 不再需要adapter.cleanup()调用
    }
}