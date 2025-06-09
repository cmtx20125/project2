package com.example.myapplication;
import android.app.Application;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 先同意隐私协议
        SDKInitializer.setAgreePrivacy(getApplicationContext(), true);

        // 然后再初始化百度地图
        SDKInitializer.initialize(getApplicationContext());
        // 可选：设置坐标类型
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }

}