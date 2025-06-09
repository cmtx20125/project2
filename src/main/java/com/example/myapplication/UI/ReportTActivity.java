package com.example.myapplication.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import java.util.ArrayList;

public class ReportTActivity extends AppCompatActivity {
    private static final int REQUEST_SELECT_IMAGE = 1;
    private static final int REQUEST_CONTENT_PAGE = 2;

    private ArrayList<String> selectedImages = new ArrayList<>();
    private ImageView btnAddImage;
    private EditText etContent;
    private Button btnPublish;
    private ImageView btnBack;
    private TextView tvContentJump;
    private TextView tvTitleJump;
    private String userId;
    private TextView jumpVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shangbaot);

        initViews();
        initData();
        setupListeners();
    }

    private void initViews() {
        btnAddImage = findViewById(R.id.btn_add_image);
        etContent = findViewById(R.id.et_content);
        btnPublish = findViewById(R.id.btn_publish);
        btnBack = findViewById(R.id.btn_back);
        tvContentJump = findViewById(R.id.tv_content_jump);
        tvTitleJump = findViewById(R.id.tv_title_jump);
        jumpVideo =  findViewById(R.id.sendVideo);
        userId = getIntent().getStringExtra("userId");
    }

    private void initData() {

        updatePublishButtonState();
    }

    private void setupListeners() {
        // 返回按钮
        btnBack.setOnClickListener(v -> finish());

        // 添加图片按钮
        btnAddImage.setOnClickListener(v -> {
            Intent intent = new Intent(ReportTActivity.this, PhotoShowActivity.class);
            startActivityForResult(intent, REQUEST_SELECT_IMAGE);
        });
        jumpVideo.setOnClickListener(v ->{
            Intent intent = new Intent(ReportTActivity.this, ReportVActivity.class);
            intent.putExtra("userId",userId);
            startActivity(intent);
            finish();
        });
        // 发布按钮
        btnPublish.setOnClickListener(v -> {
            if (validateInput()) {
                publishContent();
            }
        });

        // 内容跳转按钮 - 跳转到新页面显示内容
        tvContentJump.setOnClickListener(v -> {
            // 检查是否有选中的图片
            if (selectedImages.isEmpty()) {
                Toast.makeText(this, "请先选择图片", Toast.LENGTH_SHORT).show();
                return;
            }

            // 创建Intent并传递数据和图片
            Intent intent = new Intent(ReportTActivity.this, ReportTTActivity.class);
            intent.putExtra("content", etContent.getText().toString());
            intent.putExtra("userId",userId);
            intent.putStringArrayListExtra("selected_images", selectedImages);
            startActivityForResult(intent, REQUEST_CONTENT_PAGE);
        });

        // 大封面按钮 - 保持当前页面
        tvTitleJump.setOnClickListener(v -> {
            // 可以在这里添加大封面特定的逻辑
            Toast.makeText(this, "当前是大封面编辑模式", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean validateInput() {
        if (selectedImages.isEmpty()) {
            Toast.makeText(this, "请添加至少一张图片", Toast.LENGTH_SHORT).show();
            return false;
        }

        String content = etContent.getText().toString().trim();
        if (content.isEmpty()) {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void publishContent() {
        // 这里替换为您的实际发布逻辑
        Toast.makeText(this, "发布成功: " + etContent.getText().toString(), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SELECT_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImages = data.getStringArrayListExtra("selected_images");
            updateImagePreview();
            updatePublishButtonState();
        } else if (requestCode == REQUEST_CONTENT_PAGE && resultCode == RESULT_OK && data != null) {
            // 从内容页面返回时，可以获取返回的数据
            String updatedContent = data.getStringExtra("updated_content");
            if (updatedContent != null) {
                etContent.setText(updatedContent);
            }
        }
    }

    private void updateImagePreview() {
        if (selectedImages != null && !selectedImages.isEmpty()) {
            Glide.with(this)
                    .load(selectedImages.get(0))
                    .centerCrop()
                    .placeholder(R.drawable.ic_add)
                    .into(btnAddImage);
        } else {
            btnAddImage.setImageResource(R.drawable.ic_add);
        }
    }

    private void updatePublishButtonState() {
        boolean canPublish = !selectedImages.isEmpty() && !etContent.getText().toString().trim().isEmpty();
        btnPublish.setEnabled(canPublish);
        btnPublish.setTextColor(getResources().getColor(
                canPublish ? android.R.color.holo_orange_dark : android.R.color.darker_gray));
    }
}