package com.example.myapplication.UI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.R;
import com.example.myapplication.bean.ApiService;
import com.example.myapplication.bean.RetrofitClient;
import com.example.myapplication.bean.User;

import java.util.Arrays;
import java.util.List;

public class JinActivity extends AppCompatActivity {
        private ViewPager2 viewPager;
    private LinearLayout indicatorLayout;
    private Button btnEnterApp;
    private int[] images = {R.drawable.img, R.drawable.img_1, R.drawable.img_2};// 替换成你的图片资源
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jinruapp);
        user = (User) getIntent().getSerializableExtra("user");
        viewPager = findViewById(R.id.viewPager);
        indicatorLayout = findViewById(R.id.indicatorLayout);
        btnEnterApp = findViewById(R.id.btnEnterApp);

        // 设置适配器
        List<Integer> imageList = Arrays.asList(images[0], images[1], images[2]);
        ImageAdapter adapter = new ImageAdapter(this, imageList);
        viewPager.setAdapter(adapter);

        // 设置指示器
        setupIndicators(imageList.size());
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                setCurrentIndicator(position);
            }
        });

        // 按钮点击事件
        btnEnterApp.setOnClickListener(v -> {
            Intent intent = new Intent(JinActivity.this, HomeActivity.class);
            String userId = getIntent().getStringExtra("userId");
            intent.putExtra("user", user);  // 传递用户信息
            intent.putExtra("userId",userId);
            startActivity(intent);
            finish(); // 关闭当前界面
        });
    }

    private void setupIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        for (int i = 0; i < count; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageResource(R.drawable.indicator_inactive);
            indicators[i].setPadding(5, 0, 5, 0);
            indicatorLayout.addView(indicators[i]);
        }
        setCurrentIndicator(0);
    }

    private void setCurrentIndicator(int index) {
        int childCount = indicatorLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) indicatorLayout.getChildAt(i);
            if (i == index) {
                imageView.setImageResource(R.drawable.indicator_active);
            } else {
                imageView.setImageResource(R.drawable.indicator_inactive);
            }
        }
    }
}
