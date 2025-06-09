package com.example.myapplication.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.example.myapplication.R;
import com.example.myapplication.bean.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HospitalActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST = 100;
    private MapView mapView;
    private BaiduMap baiduMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private PoiSearch poiSearch;
    private LatLng currentLatLng;
    private BottomNavigationView bottomNavigationView;
    private String userId;
    private User user;
    private InfoWindow infoWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yiliao);

        // 初始化百度地图
        mapView = findViewById(R.id.mapView);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_adopt);
        userId = getIntent().getStringExtra("userId");
        user = (User) getIntent().getSerializableExtra("user");
        setupMarkerClickListener();
        // 初始化定位服务
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // 检查定位权限
        checkLocationPermission();
        setupBottomNavigation();
    }
    private void setupMarkerClickListener() {
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Bundle bundle = marker.getExtraInfo();
                String name = bundle.getString("name");
                String address = bundle.getString("address");

                TextView infoView = new TextView(HospitalActivity.this);
                infoView.setText(name + "\n" + address);
                infoView.setTextColor(Color.BLACK);
                infoView.setPadding(30, 20, 30, 20);
                infoView.setBackgroundResource(R.drawable.infowindow_bg); // 你可以自定义一个背景

                infoWindow = new InfoWindow(infoView, marker.getPosition(), -80);
                baiduMap.showInfoWindow(infoWindow);

                return true;
            }
        });

        // 点击地图其他地方关闭 InfoWindow
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                baiduMap.hideInfoWindow();
            }

            @Override
            public void onMapPoiClick(MapPoi mapPoi) {
                baiduMap.hideInfoWindow();
            }
        });
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);
        }
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_adopt:
                    return true;

                case R.id.nav_report:
                    startActivity(new Intent(this, ReportActivity.class)
                            .putExtra("user", user).putExtra("userId",userId));
                    return true;

                case R.id.nav_home:
                    startActivity(new Intent(this, HomeActivity.class).putExtra("user", user).putExtra("userId",userId));
                    return true;

                case R.id.nav_trends:
                    startActivity(new Intent(this, TrendsActivity.class).putExtra("user", user).putExtra("userId",userId));
                    return true;

                case R.id.nav_profile:
                    startActivity(new Intent(this, ProfileActivity.class).putExtra("user", user).putExtra("userId",userId));
                    return true;

                default:
                    return false;
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(this, "需要定位权限才能查找宠物医院", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startLocationUpdates() {
        // 检查定位服务是否开启
        if (!isLocationEnabled()) {
            Toast.makeText(this, "请先开启定位服务", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            return;
        }

        // 定义定位监听器
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                runOnUiThread(() -> handleNewLocation(location));
            }

            @Override public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override public void onProviderEnabled(String provider) {}
            @Override public void onProviderDisabled(String provider) {}
        };

        try {
            // 同时请求GPS和网络定位
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        5000,  // 5秒更新间隔
                        10,    // 10米距离变化
                        locationListener);

                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        5000,
                        10,
                        locationListener);

                // 立即获取最后一次已知位置
                Location lastLocation = getLastKnownLocation();
                if (lastLocation != null) {
                    handleNewLocation(lastLocation);
                }
            }
        } catch (Exception e) {
            Log.e("Location", "定位启动失败", e);
            Toast.makeText(this, "定位启动失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private Location getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        return location;
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void handleNewLocation(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        currentLatLng = new LatLng(latitude, longitude);

        // 移动地图到当前位置
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngZoom(currentLatLng, 16f));

        // 搜索周边宠物医院
        searchNearbyPetHospitals(latitude, longitude);
    }

    private void searchNearbyPetHospitals(double latitude, double longitude) {
        if (poiSearch == null) {
            poiSearch = PoiSearch.newInstance();
            poiSearch.setOnGetPoiSearchResultListener(poiListener);
        }

        PoiNearbySearchOption option = new PoiNearbySearchOption()
                .location(new LatLng(latitude, longitude))
                .radius(3000) // 3公里范围
                .keyword("宠物医院");

        poiSearch.searchNearby(option);
    }

    private final OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
        @Override
        public void onGetPoiResult(PoiResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(HospitalActivity.this, "未找到宠物医院", Toast.LENGTH_SHORT).show();
                return;
            }

            baiduMap.clear();
            for (PoiInfo poi : result.getAllPoi()) {
                Bundle extraInfo = new Bundle();
                extraInfo.putString("name", poi.name);
                extraInfo.putString("address", poi.address);
                // 设置Marker图标
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(poi.location)
                        .title(poi.name)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.dingwei2))
                        .extraInfo(extraInfo);

                // 缩小到原始的 30%;



                baiduMap.addOverlay(markerOptions);
            }
        }

        @Override public void onGetPoiDetailResult(com.baidu.mapapi.search.poi.PoiDetailResult result) {}

        @Override
        public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

        }

        @Override public void onGetPoiIndoorResult(com.baidu.mapapi.search.poi.PoiIndoorResult result) {}
    };

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        // 每次回到界面时检查定位
        if (locationManager != null && locationListener != null) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (poiSearch != null) {
            poiSearch.destroy();
        }
    }
}