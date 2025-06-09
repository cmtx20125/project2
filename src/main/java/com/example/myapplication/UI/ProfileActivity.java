package com.example.myapplication.UI;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.bean.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.myapplication.R;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private User user;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wode);
        userId = getIntent().getStringExtra("userId");
        // 初始化底部导航栏
        setupBottomNavigation();

        // 设置按钮点击事件
        setupButtonActions();
        setupListItemClicks();
    }
    private void setupListItemClicks() {
        // 方法二：更好的方式 - 使用Map（推荐）
        Map<Integer, Class<?>> itemNavigationMap = new HashMap<>();
        itemNavigationMap.put(R.id.item_profile, ProfileDetailActivity.class);
        itemNavigationMap.put(R.id.item_settings, MessagesActivity.class);
        itemNavigationMap.put(R.id.item_messages, DialogueUserActivity.class);
        itemNavigationMap.put(R.id.item_collections, SettingsActivity.class);
        itemNavigationMap.put(R.id.item_help, PetActivity.class);

        for (Map.Entry<Integer, Class<?>> entry : itemNavigationMap.entrySet()) {
            findViewById(entry.getKey()).setOnClickListener(v -> {
                startActivity(new Intent(ProfileActivity.this, entry.getValue()).putExtra("user", user).putExtra("userId",userId));
            });
        }
    }

    private void setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    startActivity(new Intent(this, HomeActivity.class).putExtra("userId",userId));
                    return true;
                case R.id.nav_report:
                    startActivity(new Intent(this, ReportActivity.class).putExtra("userId",userId));
                    return true;
                case R.id.nav_adopt:
                    startActivity(new Intent(this, AdoptActivity.class).putExtra("userId",userId));
                    return true;
                case R.id.nav_trends:
                    startActivity(new Intent(this, TrendsActivity.class).putExtra("userId",userId));
                    return true;
                case R.id.nav_profile:
                    // 当前已在个人资料页，无需处理
                    return true;
            }
            return false;
        });

        // 高亮当前选中的菜单项
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
    }

    private void setupButtonActions() {
        // 切换账号按钮
        findViewById(R.id.btn_switch_account).setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("切换账号")
                    .setMessage("确定要切换账号吗？")
                    .setPositiveButton("确定", (dialog, which) -> {
                        // 清除当前登录状态
                        // 跳转到登录页
                        startActivity(new Intent(this, LoginActivity.class));
                        finishAffinity(); // 关闭所有现有Activity
                    })
                    .setNegativeButton("取消", null)
                    .show();
        });

        // 退出登录按钮
        findViewById(R.id.btn_logout).setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("退出登录")
                    .setMessage("确定要退出当前账号吗？")
                    .setPositiveButton("确定", (dialog, which) -> {
                        // 清除登录状态
                        // 返回登录页
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    })
                    .setNegativeButton("取消", null)
                    .show();
        });
    }

    // 处理返回键，避免退出应用
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
