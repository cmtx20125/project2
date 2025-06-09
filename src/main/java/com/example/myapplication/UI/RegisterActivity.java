package com.example.myapplication.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.bean.ApiService;
import com.example.myapplication.bean.RetrofitClient;
import com.example.myapplication.bean.ReviewResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText etUsername, etPhone, etCode;
    private Button btnGetCode, btnRegister;
    private ApiService apiService;
    private CountDownTimer countDownTimer;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // 使用您提供的布局文件

        // 初始化视图
        etUsername = findViewById(R.id.name_account);
        etPhone = findViewById(R.id.account);
        etCode = findViewById(R.id.password);
        btnGetCode = findViewById(R.id.get_code_button);
        btnRegister = findViewById(R.id.login_button); // 注意：布局中实际是注册按钮

        // 初始化Retrofit
        apiService = RetrofitClient.getInstance().create(ApiService.class);

        // 初始化加载对话框
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("请稍候...");
        progressDialog.setCancelable(false);

        // 获取验证码按钮点击事件
        btnGetCode.setOnClickListener(v -> {
            String phone = etPhone.getText().toString().trim();
            if (TextUtils.isEmpty(phone)) {
                showToast("请输入手机号");
                return;
            }
            if (!isPhoneValid(phone)) {
                showToast("手机号格式不正确");
                return;
            }
            checkPhoneAvailable(phone);
        });

        // 注册按钮点击事件
        btnRegister.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String code = etCode.getText().toString().trim();

            if (TextUtils.isEmpty(username)) {
                showToast("请输入用户名");
                return;
            }
            if (TextUtils.isEmpty(phone)) {
                showToast("请输入手机号");
                return;
            }
            if (!isPhoneValid(phone)) {
                showToast("手机号格式不正确");
                return;
            }
            if (TextUtils.isEmpty(code)) {
                showToast("请输入验证码");
                return;
            }

            doRegister(username, phone, code);
        });
    }

    // 检查手机号是否可用
    private void checkPhoneAvailable(String phone) {
        progressDialog.show();

        apiService.checkPhoneAvailable(phone).enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        // 手机号可用，发送验证码
                        getVerificationCode(phone);
                    } else {
                        showToast("该手机号已注册");
                    }
                } else {
                    showToast("检查手机号失败");
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                progressDialog.dismiss();
                showToast("网络错误：" + t.getMessage());
            }
        });
    }

    // 获取验证码
    private void getVerificationCode(String phone) {
        btnGetCode.setEnabled(false);

        apiService.getVerificationCode(phone).enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        showToast("验证码已发送");
                        startCountDown(60); // 60秒倒计时
                    } else {
                        showToast(response.body().getMessage());
                        btnGetCode.setEnabled(true);
                    }
                } else {
                    showToast("验证码发送失败");
                    btnGetCode.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                showToast("网络错误：" + t.getMessage());
                btnGetCode.setEnabled(true);
            }
        });
    }

    // 检查用户名是否可用（实时检测）
    private void checkUsernameAvailable(String username) {
        if (TextUtils.isEmpty(username) || username.length() < 4) {
            return;
        }

        apiService.checkUsernameAvailable(username).enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isSuccess()) {
                    showToast("用户名已存在");
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                // 网络错误不做处理
            }
        });
    }

    // 执行注册
    private void doRegister(String username, String phone, String code) {
        progressDialog.show();

        apiService.register(phone, code,username).enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        // 注册成功，跳转到主界面
                        showToast("注册成功");
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        showToast(response.body().getMessage());
                    }
                } else {
                    showToast("注册失败");
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                progressDialog.dismiss();
                showToast("网络错误：" + t.getMessage());
            }
        });
    }

    // 验证码倒计时
    private void startCountDown(int seconds) {
        countDownTimer = new CountDownTimer(seconds * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnGetCode.setText(millisUntilFinished / 1000 + "秒后重试");
            }

            @Override
            public void onFinish() {
                btnGetCode.setText("获取验证码");
                btnGetCode.setEnabled(true);
            }
        }.start();
    }

    // 验证手机号格式
    private boolean isPhoneValid(String phone) {
        return phone.matches("^1[3-9]\\d{9}$");
    }

    // 显示Toast
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        super.onDestroy();
    }
}