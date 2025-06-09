package com.example.myapplication.UI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import com.example.myapplication.R;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReportDetaiVActivity extends AppCompatActivity {

    private TextView tvTitle, tvAuthorTime, tvAddress, tvContent;
    private VideoView videoView;
    private ImageView ivPlayButton;
    private Button btnPetProfile;
    private ImageView backButton;
    private ProgressBar progressBar;
    Date reportTime;
    String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shangbaodetails);


        // 初始化视图
        initViews();

        // 设置返回按钮点击事件
        setupBackButton();

        // 接收并显示数据
        displayReportData();

        // 设置按钮点击事件
        setupButtonActions();
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tv_title);
        tvAuthorTime = findViewById(R.id.tv_author_time);
        tvAddress = findViewById(R.id.tv_address);
        tvContent = findViewById(R.id.tv_content);
        videoView = findViewById(R.id.video_view);
        btnPetProfile = findViewById(R.id.btn_pet_profile);
        backButton = findViewById(R.id.back_button);
        ivPlayButton = findViewById(R.id.iv_play_button);
        progressBar = findViewById(R.id.pb_loading);
    }

    private void setupBackButton() {
        backButton.setOnClickListener(v -> finish());
    }

    private void displayReportData() {
        // 从Intent获取数据
        String  reportId = getIntent().getStringExtra("reportId");
        String reportFile = getIntent().getStringExtra("reportFile");
        String reportName = getIntent().getStringExtra("reportName");
        String reportContent = getIntent().getStringExtra("reportContent");
        String reportUserName = getIntent().getStringExtra("reportUserName");
        String reportAddress = getIntent().getStringExtra("reportAddress");
        long date = getIntent().getLongExtra("reportTime", -1);

        if (date != -1) {
            reportTime = new Date(date);
            Log.d("MinIO", "时间" + reportTime);
        } else {
            Toast.makeText(this, "未接收到日期数据", Toast.LENGTH_SHORT).show();
        }
        time = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(reportTime);

        // 设置文本内容
        tvTitle.setText(reportName);
        tvAuthorTime.setText(reportUserName + " · " + time);
        tvAddress.setText(reportAddress);
        tvContent.setText(reportContent);

        // 加载视频（如果有）
        if (reportFile != null && !reportFile.isEmpty()) {
            setupVideoPlayer(reportFile);
        } else {
            videoView.setVisibility(View.GONE);
        }

        // 如果没有关联宠物，隐藏按钮

    }

    private void setupVideoPlayer(String videoUrl) {
        try {
            String url = videoUrl.replace("127.0.0.1","192.168.2.11");
            progressBar.setVisibility(View.VISIBLE); // 显示加载进度条

            videoView.setVideoURI(Uri.parse(url));

            videoView.setOnPreparedListener(mp -> {
                progressBar.setVisibility(View.GONE); // 加载完成后隐藏进度条
                mp.setLooping(true);
                // 调整视频比例
                float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
                float screenRatio = videoView.getWidth() / (float) videoView.getHeight();
                if (videoRatio > screenRatio) {
                    videoView.setScaleX(screenRatio / videoRatio);
                } else {
                    videoView.setScaleY(videoRatio / screenRatio);
                }
            });
            // 设置点击监听（播放/暂停切换）
            videoView.setOnClickListener(v -> {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    ivPlayButton.setVisibility(View.VISIBLE); // 显示播放按钮
                } else {
                    videoView.start();
                    ivPlayButton.setVisibility(View.GONE); // 隐藏播放按钮
                }
            });
            videoView.start();
        } catch (Exception e) {
            e.printStackTrace();
            videoView.setVisibility(View.GONE);
        }
    }

    private void setupButtonActions() {
        btnPetProfile.setOnClickListener(v -> {
            String petId = getIntent().getStringExtra("reportId");
            String userId = getIntent().getStringExtra("userId");
            if (petId != null && !petId.isEmpty()) {
                Intent intent = new Intent(this, AnimalActivity.class);
                intent.putExtra("reportId", petId);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView != null) {
            videoView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (videoView != null && !videoView.isPlaying()) {
            videoView.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.stopPlayback();
        }
    }
}
