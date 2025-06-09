package com.example.myapplication.UI;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.myapplication.R;
import com.example.myapplication.bean.ApiService;
import com.example.myapplication.bean.HttpResponseEntityS;
import com.example.myapplication.bean.ReviewResponse;
import com.example.myapplication.bean.RetrofitClient;
import com.example.myapplication.bean.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {

    private SwitchCompat switchAutoStart;
    private SwitchCompat switchRelatedStart;
    private SwitchCompat switchNameAnonymity;
    private SwitchCompat switchInfoHide;
    private SwitchCompat switchDynamicHide;

    private ApiService apiService;
    private SharedPreferences sharedPreferences;

    private String userId;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quanxian);

        // 初始化SharedPreferences
        sharedPreferences = getSharedPreferences("app_settings", MODE_PRIVATE);

        // 获取传入的userId
        userId = getIntent().getStringExtra("userId");
        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "用户ID获取失败", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        apiService = RetrofitClient.getInstance().create(ApiService.class);

        // 初始化视图
        initViews();

        // 设置返回按钮点击事件
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // 加载保存的设置
        loadSettings();
    }

    private void initViews() {
        switchAutoStart = findViewById(R.id.switch_autostart);
        switchRelatedStart = findViewById(R.id.switch_related_start);
        switchNameAnonymity = findViewById(R.id.switch_nameni);
        switchInfoHide = findViewById(R.id.switch_xinxini);
        switchDynamicHide = findViewById(R.id.switch_dongtaini);

        // 添加监听器
        CompoundButton.OnCheckedChangeListener listener = this::onSwitchChanged;

        switchAutoStart.setOnCheckedChangeListener(listener);
        switchRelatedStart.setOnCheckedChangeListener(listener);
        switchNameAnonymity.setOnCheckedChangeListener(listener);
        switchInfoHide.setOnCheckedChangeListener(listener);
        switchDynamicHide.setOnCheckedChangeListener(listener);
    }

    private void loadSettings() {
        switchAutoStart.setChecked(sharedPreferences.getBoolean("auto_start", false));
        switchRelatedStart.setChecked(sharedPreferences.getBoolean("related_start", false));
        switchNameAnonymity.setChecked(sharedPreferences.getBoolean("name_anonymity", false));
        switchInfoHide.setChecked(sharedPreferences.getBoolean("info_hide", false));
        switchDynamicHide.setChecked(sharedPreferences.getBoolean("dynamic_hide", false));
    }

    private void onSwitchChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        switch (buttonView.getId()) {
            case R.id.switch_autostart:
                editor.putBoolean("auto_start", isChecked);
                showToast("自启动设置已" + (isChecked ? "开启" : "关闭"));
                break;
            case R.id.switch_related_start:
                editor.putBoolean("related_start", isChecked);
                showToast("关联启动设置已" + (isChecked ? "开启" : "关闭"));
                break;
            case R.id.switch_nameni:
                editor.putBoolean("name_anonymity", isChecked);
                showToast("用户名匿名设置已" + (isChecked ? "开启" : "关闭"));
                break;
            case R.id.switch_xinxini:
                editor.putBoolean("info_hide", isChecked);
                showToast("用户信息隐藏设置已" + (isChecked ? "开启" : "关闭"));
                break;
            case R.id.switch_dongtaini:
                editor.putBoolean("dynamic_hide", isChecked);
                showToast("用户动态隐藏设置已" + (isChecked ? "开启" : "关闭"));
                break;
        }

        editor.apply();

        // 实时同步服务器
        sendSettingsToServer();
    }

    private void sendSettingsToServer() {
        // 获取开关值
        boolean info = switchInfoHide.isChecked();
        boolean name = switchNameAnonymity.isChecked();
        boolean hide = switchDynamicHide.isChecked();

        // 请求当前用户信息
        apiService.getUser(userId).enqueue(new Callback<HttpResponseEntityS>() {
            @Override
            public void onResponse(Call<HttpResponseEntityS> call, Response<HttpResponseEntityS> response) {
                if (response.isSuccessful() && response.body() != null) {
                    HttpResponseEntityS httpResponse = response.body();
                    if ("666".equals(httpResponse.getCode())) {
                        user = httpResponse.getData();

                        // 设置用户的隐私标志
                        user.setUserTagt(info ? 1 : 0);  // 信息隐藏
                        user.setUserTago(name ? 1 : 0);  // 匿名
                        user.setUserTagtr(hide ? 1 : 0); // 动态隐藏

                        // 发起更新请求
                        updateUserToServer(user);
                    } else {
                        showToast(httpResponse.getMessage());
                    }
                } else {
                    showToast("获取用户信息失败");
                }
            }

            @Override
            public void onFailure(Call<HttpResponseEntityS> call, Throwable t) {
                showToast("网络错误: " + t.getMessage());
            }
        });
    }

    private void updateUserToServer(User user) {
        String path = user.getUserPic().replace("http://127.0.0.1:9005/pet/", "");
        user.setUserPic(path);
        apiService.updateUserSet(user).enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                if (response.isSuccessful()) {
                    showToast("设置已同步到服务器");
                } else {
                    showToast("同步失败：" + response.code());
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                showToast("同步网络错误：" + t.getMessage());
            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(SettingsActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 设置已在切换时保存，无需在销毁时再处理
    }
}
