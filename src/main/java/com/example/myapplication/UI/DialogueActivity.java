package com.example.myapplication.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.bean.ApiService;
import com.example.myapplication.bean.Dialogue;
import com.example.myapplication.bean.DialogueAdapter;
import com.example.myapplication.bean.HttpResponseEntity;
import com.example.myapplication.bean.HttpResponseEntityS;
import com.example.myapplication.bean.RetrofitClient;
import com.example.myapplication.bean.Review;
import com.example.myapplication.bean.ReviewResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DialogueActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText etMessage;
    private Button btnSend;
    private ImageView btnBack, btnImage;
    private TextView tvChatTitle;

    private List<Dialogue> messages = new ArrayList<>();
    private DialogueAdapter adapter;
    private String currentUserId;
    private String otherUserId;
    private String otherUserName;

    private ProgressDialog progressDialog; // 新增成员变量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siliao);

        // 初始化视图
        initViews();

        // 获取用户信息
        currentUserId = getIntent().getStringExtra("user_id");
        otherUserId = getIntent().getStringExtra("other_user_id");
        otherUserName = getIntent().getStringExtra("other_user_name");

        // 设置标题
        tvChatTitle.setText(otherUserName);

        // 设置适配器
        adapter = new DialogueAdapter(messages, currentUserId);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    // 有文字，启用按钮并更改背景颜色
                    btnSend.setEnabled(true);
                    btnSend.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFA726"))); // 橙色
                } else {
                    // 无文字，禁用按钮并恢复灰色
                    btnSend.setEnabled(false);
                    btnSend.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DADADA"))); // 灰色
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        // 加载消息
        loadMessages();

        // 设置监听器
        setupListeners();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewMessages);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        btnBack = findViewById(R.id.btnBack);
        btnImage = findViewById(R.id.btnImage);
        tvChatTitle = findViewById(R.id.tvChatTitle);
    }

    private void loadMessages() {
        showLoading();

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        apiService.getAllDialogues(currentUserId, otherUserId).enqueue(new Callback<HttpResponseEntity<List<Dialogue>>>() {
            @Override
            public void onResponse(Call<HttpResponseEntity<List<Dialogue>>> call,
                                   Response<HttpResponseEntity<List<Dialogue>>> response) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                handleResponse(response);
            }

            @Override
            public void onFailure(Call<HttpResponseEntity<List<Dialogue>>> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(DialogueActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载消息中...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void handleResponse(Response<HttpResponseEntity<List<Dialogue>>> response) {
        if (response.isSuccessful() && response.body() != null) {
            HttpResponseEntity<List<Dialogue>> httpResponse = response.body();
            if ("666".equals(httpResponse.getCode())) {
                updateMessageList(httpResponse.getData());
            } else {
                Toast.makeText(this, httpResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "服务器响应异常", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateMessageList(List<Dialogue> newMessages) {
        messages.clear();
        messages.addAll(newMessages);
        adapter.notifyDataSetChanged();
        scrollToBottom();
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> onBackPressed());

        btnSend.setOnClickListener(v -> sendMessage());

        etMessage.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnSend.setEnabled(s.length() > 0);
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        btnImage.setOnClickListener(v -> selectImage());
    }

    private void sendMessage() {
        String content = etMessage.getText().toString().trim();
        if (content.isEmpty()) return;

        Dialogue newMessage = new Dialogue();
        newMessage.setDialogueUserId(currentUserId);
        newMessage.setDialoguePid(otherUserId);
        newMessage.setDialogueContent(content);
        newMessage.setDialogueRead("0");
//        Date time = new Date();
//        newMessage.setDialogueTime(time);
//        messages.add(newMessage);
//        adapter.notifyItemInserted(messages.size() - 1);
//        scrollToBottom();

        etMessage.setText("");
//        String message = etMessage.getText().toString().trim();
//        if (message.isEmpty()) {
//            Toast.makeText(this, "请输入消息", Toast.LENGTH_SHORT).show();
//            return;
//        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.2.11:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);

        // 获取用户信息（异步）
        Call<ReviewResponse> call = apiService.addDialogue(newMessage);
        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ReviewResponse reviewResponse = response.body();
                    if (reviewResponse.isSuccess()) {
                        // 添加成功
                        Toast.makeText(DialogueActivity.this, reviewResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        // 重新加载评论（推荐）
                        // 加载消息
                        loadMessages();
                    } else {
                        // 添加失败
                        Toast.makeText(DialogueActivity.this, reviewResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 处理错误响应
                    Toast.makeText(DialogueActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                // 处理网络错误
                Toast.makeText(DialogueActivity.this, "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // TODO: 发送到服务器

    }

    private void selectImage() {
        Toast.makeText(this, "选择图片功能", Toast.LENGTH_SHORT).show();
    }

    private void scrollToBottom() {
        recyclerView.post(() -> {
            if (messages.size() > 0) {
                recyclerView.scrollToPosition(messages.size() - 1);
            }
        });
    }
}
