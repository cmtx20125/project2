package com.example.myapplication.UI;

import android.Manifest;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.bean.VideoAdapter;
import com.example.myapplication.bean.VideoItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoShowActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION = 101;
    private RecyclerView recyclerView;
    private Button uploadButton;
    private VideoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        uploadButton = findViewById(R.id.uploadButton);


        // 设置网格布局
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // 检查权限
        checkPermissions();

        // 上传按钮点击事件
        uploadButton.setOnClickListener(v -> {
            List<VideoItem> selectedVideos = adapter.getSelectedVideos();
            if (!selectedVideos.isEmpty()) {
                uploadVideos(selectedVideos);
            } else {
                Toast.makeText(this, "请先选择视频", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_VIDEO}, REQUEST_PERMISSION);
            } else {
                loadVideos();
            }
        } else {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
            } else {
                loadVideos();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadVideos();
            } else {
                Toast.makeText(this, "需要权限才能访问视频", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadVideos() {
        List<VideoItem> videoList = new ArrayList<>();

        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATA
        };

        String sortOrder = MediaStore.Video.Media.DATE_ADDED + " DESC";

        try (Cursor cursor = getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
        )) {
            if (cursor != null) {
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
                int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
                int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
                int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
                int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);

                while (cursor.moveToNext()) {
                    long id = cursor.getLong(idColumn);
                    String name = cursor.getString(nameColumn);
                    long duration = cursor.getLong(durationColumn);
                    long size = cursor.getLong(sizeColumn);
                    String data = cursor.getString(dataColumn);
                    Uri uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);

                    videoList.add(new VideoItem(id, name, duration, size, uri, data));
                }
            }
        }

        // 初始化适配器
        adapter = new VideoAdapter(videoList, selectedCount -> {
            uploadButton.setEnabled(selectedCount > 0);
        });
        recyclerView.setAdapter(adapter);
    }

    private void uploadVideos(List<VideoItem> videos) {
        // 实现上传逻辑
        Toast.makeText(this, "开始上传 " + videos.size() + " 个视频", Toast.LENGTH_SHORT).show();

        // 示例：逐个上传
        for (VideoItem video : videos) {
            uploadSingleVideo(video);
        }
    }

    private void uploadSingleVideo(VideoItem video) {
        // 使用OkHttp或Retrofit实现实际上传
        // 这里只是示例
        new Thread(() -> {
            try {
                File file = new File(video.getPath());
                // 模拟上传延迟
                Thread.sleep(2000);

                runOnUiThread(() -> {
                    Toast.makeText(this, video.getName() + " 上传完成", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(this, video.getName() + " 上传失败", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
}
