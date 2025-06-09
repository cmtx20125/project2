package com.example.myapplication.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.bean.ApiService;
import com.example.myapplication.bean.DialogueDto;
import com.example.myapplication.bean.DialogueDtoAdapter;
import com.example.myapplication.bean.HttpResponseEntity;
import com.example.myapplication.bean.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogueUserActivity extends AppCompatActivity implements DialogueDtoAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private DialogueDtoAdapter adapter;
    private ProgressBar progressBar;
    private List<DialogueDto> dialogueList = new ArrayList<>();
    private ApiService apiService;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duixiang);

        initViews();

        // 返回按钮点击事件
        ImageView backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(DialogueUserActivity.this, ProfileActivity.class);
            intent.putExtra("userId",currentUserId);
            startActivity(intent);  // 启动 ProfileActivity
            finish();  // 结束当前活动
        });

        currentUserId = getIntent().getStringExtra("userId");
        Log.d("DialogueUserActivity", "当前用户ID：" + currentUserId);

        loadDialogueData();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new DialogueDtoAdapter(dialogueList, this, this);
        recyclerView.setAdapter(adapter);

        progressBar = findViewById(R.id.progressBar);
        apiService = RetrofitClient.getInstance().create(ApiService.class);
    }

    private void loadDialogueData() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        if (currentUserId == null || currentUserId.isEmpty()) {
            Toast.makeText(this, "用户ID为空", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }

        apiService.getUsers(currentUserId).enqueue(new Callback<HttpResponseEntity<List<DialogueDto>>>() {
            @Override
            public void onResponse(Call<HttpResponseEntity<List<DialogueDto>>> call,
                                   Response<HttpResponseEntity<List<DialogueDto>>> response) {
                Log.d("DialogueUserActivity", "接口响应：" + response.body());
                handleResponse(response);
            }

            @Override
            public void onFailure(Call<HttpResponseEntity<List<DialogueDto>>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(DialogueUserActivity.this,
                        "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("DialogueUserActivity", "网络请求失败", t);
            }
        });
    }

    private void handleResponse(Response<HttpResponseEntity<List<DialogueDto>>> response) {
        progressBar.setVisibility(View.GONE);

        if (response.isSuccessful() && response.body() != null) {
            HttpResponseEntity<List<DialogueDto>> httpResponse = response.body();

            if ("666".equals(httpResponse.getCode())) {
                List<DialogueDto> data = httpResponse.getData();
                if (data != null && !data.isEmpty()) {
                    dialogueList.clear();
                    dialogueList.addAll(data);

                    adapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(this, "暂无对话数据", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, httpResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "服务器响应异常", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onChatClick(int position) {
        DialogueDto dialogue = dialogueList.get(position);
        Intent intent = new Intent(this, DialogueActivity.class);
        intent.putExtra("other_user_id", dialogue.getUserId());
        intent.putExtra("other_user_name", dialogue.getUserName());
        intent.putExtra("user_id", dialogue.getUserIdNow());
        startActivity(intent);
    }

    @Override
    public void onProfileClick(int position) {
        DialogueDto dialogue = dialogueList.get(position);
        Intent intent = new Intent(this, ProfileTAActivity.class);
        intent.putExtra("userId", dialogue.getUserId());
        intent.putExtra("userName", dialogue.getUserName());
        intent.putExtra("userPic", dialogue.getUserPic());
        intent.putExtra("userAddress", dialogue.getUserAddress());
        intent.putExtra("userGender", dialogue.getUserGender());
        startActivity(intent);
    }

    public void refreshData() {
        loadDialogueData();
    }
}
