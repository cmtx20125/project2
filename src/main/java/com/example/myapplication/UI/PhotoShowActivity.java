package com.example.myapplication.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.bean.ImageAdapter;

import java.util.ArrayList;
import java.util.List;

public class PhotoShowActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION = 1001;
    private static final String TAG = "PhotoShowActivity";

    private RecyclerView recyclerView;
    private ImageAdapter adapter;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoshow);

        initializeViews();
        setupRecyclerView();
        setupSendButton();
        checkAndRequestPermissions();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerView);
        sendButton = findViewById(R.id.sendButton);
        sendButton.setEnabled(false); // 初始状态不可点击
        updateButtonState(0); // 初始化按钮状态
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setHasFixedSize(true);
    }

    private void setupSendButton() {
        sendButton.setOnClickListener(v -> {
            if (adapter == null) {
                Toast.makeText(this, "图片加载未完成，请稍候", Toast.LENGTH_SHORT).show();
                return;
            }

            List<String> selected = adapter.getSelectedImages();
            Log.d(TAG, "Selected images: " + selected.toString());

            if (selected.isEmpty()) {
                Toast.makeText(this, "请至少选择一张图片", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent resultIntent = new Intent();
            resultIntent.putStringArrayListExtra("selected_images", new ArrayList<>(selected));
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void updateButtonState(int selectedCount) {
        runOnUiThread(() -> {
            sendButton.setEnabled(selectedCount > 0);
            sendButton.setBackgroundResource(
                    selectedCount > 0 ? R.drawable.btn_enabled : R.drawable.btn_disabled
            );
            sendButton.setTextColor(ContextCompat.getColor(
                    this,
                    selectedCount > 0 ? android.R.color.white : android.R.color.darker_gray
            ));
        });
    }

    private void setupAdapterListener() {
        if (adapter != null) {
            adapter.setOnImageSelectedListener(selectedCount -> {
                Log.d(TAG, "Selected count changed: " + selectedCount);
                updateButtonState(selectedCount);
            });
        }
    }


    private void checkAndRequestPermissions() {
        if (hasRequiredPermissions()) {
            loadImages();
        } else {
            requestPermissionsWithRationale();
        }
    }

    private boolean hasRequiredPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager() ||
                    ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermissionsWithRationale() {
        String permission = getRequiredPermission();

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            showPermissionRationaleDialog(permission);
        } else {
            requestPermissions(permission);
        }
    }

    private String getRequiredPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            return Manifest.permission.READ_EXTERNAL_STORAGE;
        }
    }

    private void showPermissionRationaleDialog(String permission) {
        new AlertDialog.Builder(this)
                .setTitle("需要权限")
                .setMessage("需要访问您的照片以便选择图片")
                .setPositiveButton("确定", (dialog, which) -> requestPermissions(permission))
                .setNegativeButton("取消", (dialog, which) -> {
                    Toast.makeText(this, "权限被拒绝，无法加载图片", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setCancelable(false)
                .show();
    }

    private void requestPermissions(String permission) {
        ActivityCompat.requestPermissions(this,
                new String[]{permission},
                REQUEST_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadImages();
            } else {
                handlePermissionDenied(permissions[0]);
            }
        }
    }

    private void handlePermissionDenied(String permission) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            showPermissionSettingsDialog();
        } else {
            Toast.makeText(this, "权限被拒绝，无法加载图片", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void showPermissionSettingsDialog() {
        new AlertDialog.Builder(this)
                .setTitle("权限被永久拒绝")
                .setMessage("您已永久拒绝必要的权限，请在设置中手动授予权限")
                .setPositiveButton("去设置", (dialog, which) -> openAppSettings())
                .setNegativeButton("取消", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    private void openAppSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "无法打开应用设置", e);
            Toast.makeText(this, "无法打开设置页面", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void loadImages() {
        new Thread(() -> {
            List<String> imagePaths = new ArrayList<>();
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            String[] projection = {
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATE_ADDED
            };

            String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";

            try (Cursor cursor = getContentResolver().query(uri, projection, null, null, sortOrder)) {
                if (cursor != null) {
                    int pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    while (cursor.moveToNext()) {
                        String path = cursor.getString(pathColumn);
                        if (path != null && !path.isEmpty()) {
                            imagePaths.add(path);
                            Log.d(TAG, "Found image: " + path);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Error loading images", e);
                runOnUiThread(() ->
                        Toast.makeText(this, "加载图片出错", Toast.LENGTH_SHORT).show());
            }

            runOnUiThread(() -> {
                if (imagePaths.isEmpty()) {
                    Toast.makeText(this, "未找到图片", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "Total images loaded: " + imagePaths.size());
                }
                adapter = new ImageAdapter(imagePaths, PhotoShowActivity.this);
                recyclerView.setAdapter(adapter);
                setupAdapterListener();
            });
        }).start();
    }
}