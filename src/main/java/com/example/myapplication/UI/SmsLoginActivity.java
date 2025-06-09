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
import com.example.myapplication.bean.HttpResponseEntityS;
import com.example.myapplication.bean.RetrofitClient;
import com.example.myapplication.bean.ReviewResponse;
import com.example.myapplication.bean.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SmsLoginActivity extends AppCompatActivity {
    private EditText etPhone, etCode;
    private Button btnGetCode, btnLogin;
    private ApiService apiService;
    private CountDownTimer countDownTimer;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shouji); // 使用您提供的布局文件

        // 初始化视图
        etPhone = findViewById(R.id.account);
        etCode = findViewById(R.id.password);
        btnGetCode = findViewById(R.id.get_code_button);
        btnLogin = findViewById(R.id.login_button);

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
            getVerificationCode(phone);
        });

        // 登录按钮点击事件
        btnLogin.setOnClickListener(v -> {
            String phone = etPhone.getText().toString().trim();
            String code = etCode.getText().toString().trim();

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

            doLogin(phone, code);
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

    // 执行登录
    private void doLogin(String phone, String code) {
        progressDialog.show();

        apiService.loginWithCode(phone, code).enqueue(new Callback<HttpResponseEntityS>() {
            @Override
            public void onResponse(Call<HttpResponseEntityS> call, Response<HttpResponseEntityS> response) {
                progressDialog.dismiss();

                if (response.isSuccessful() && response.body() != null) {
                    HttpResponseEntityS result = response.body();
                    if ("666".equals(result.getCode())) {
                        // 登录成功
                        Toast.makeText(SmsLoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

                        // 获取用户信息（可选）
                        User user = result.getData();

                        // 跳转到首页或其他页面
                        Intent intent = new Intent(SmsLoginActivity.this, JinActivity.class);
                        intent.putExtra("userId",user.getUserId());
                        intent.putExtra("user", user);  // 传递用户信息
                        startActivity(intent);
                        finish();
                    } else {
                        // 登录失败
                        Toast.makeText(SmsLoginActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 服务器错误
                    Toast.makeText(SmsLoginActivity.this, "服务器错误，请重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HttpResponseEntityS> call, Throwable t) {
                progressDialog.dismiss();
                showToast("网络错误：" + t.getMessage());
            }
        });
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