package com.example.myapplication.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.myapplication.R;
import com.example.myapplication.bean.ApiService;
import com.example.myapplication.bean.HttpResponseEntity;
import com.example.myapplication.bean.HttpResponseEntityS;
import com.example.myapplication.bean.User;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText accountEditText, passwordEditText;
    private Button loginButton;
    private TextView registerLink, smsLoginLink;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginin);

        // 初始化视图
        accountEditText = findViewById(R.id.account);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        registerLink = findViewById(R.id.register_link);
        smsLoginLink = findViewById(R.id.sms_login_link);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY); // 打印请求和响应所有数据

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.2.11:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        Log.d("Retrofit", "Retrofit 初始化完成");

        apiService = retrofit.create(ApiService.class);

        // 登录按钮点击事件
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = accountEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                login(userName, password);
            }
        });

        // 注册链接点击事件
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // 短信登录链接点击事件
        smsLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SmsLoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login(String userName, String password) {
        Call<HttpResponseEntityS> call = apiService.login(userName, password);
        call.enqueue(new Callback<HttpResponseEntityS>() {
            @Override
            public void onResponse(Call<HttpResponseEntityS> call, Response<HttpResponseEntityS> response) {
                if (response.isSuccessful() && response.body() != null) {
                    HttpResponseEntityS result = response.body();
                    if ("666".equals(result.getCode())) {
                        // 登录成功
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

                        // 获取用户信息（可选）
                        User user = result.getData();

                        // 跳转到首页或其他页面
                        Intent intent = new Intent(LoginActivity.this, JinActivity.class);
                        intent.putExtra("userId",user.getUserId());
                        intent.putExtra("user", user);  // 传递用户信息
                        startActivity(intent);
                        finish();
                    } else {
                        // 登录失败
                        Toast.makeText(LoginActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 服务器错误
                    Toast.makeText(LoginActivity.this, "服务器错误，请重试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HttpResponseEntityS> call, Throwable t) {
                Log.e("RetrofitError", "Request failed", t);
                // 网络请求失败
                Toast.makeText(LoginActivity.this, "网络错误，请检查您的连接", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
