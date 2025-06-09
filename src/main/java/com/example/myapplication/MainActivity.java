package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.UI.LoginActivity;
import com.example.myapplication.UI.RegisterActivity;

public class MainActivity extends AppCompatActivity {

//    private ViewPager2 viewPager;
//    private LinearLayout indicatorLayout;
//    private Button btnEnterApp;
//    private int[] images = {R.drawable.img, R.drawable.img_1, R.drawable.img_2}; // 替换成你的图片资源
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_jinruapp);
//
//        viewPager = findViewById(R.id.viewPager);
//        indicatorLayout = findViewById(R.id.indicatorLayout);
//        btnEnterApp = findViewById(R.id.btnEnterApp);
//
//        // 设置适配器
//        List<Integer> imageList = Arrays.asList(images[0], images[1], images[2]);
//        ImageAdapter adapter = new ImageAdapter(this, imageList);
//        viewPager.setAdapter(adapter);
//
//        // 设置指示器
//        setupIndicators(imageList.size());
//        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                setCurrentIndicator(position);
//            }
//        });
//
//        // 按钮点击事件
//        btnEnterApp.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//            startActivity(intent);
//            finish(); // 关闭当前界面
//        });
//    }
//
//    private void setupIndicators(int count) {
//        ImageView[] indicators = new ImageView[count];
//        for (int i = 0; i < count; i++) {
//            indicators[i] = new ImageView(this);
//            indicators[i].setImageResource(R.drawable.indicator_inactive);
//            indicators[i].setPadding(5, 0, 5, 0);
//            indicatorLayout.addView(indicators[i]);
//        }
//        setCurrentIndicator(0);
//    }
//
//    private void setCurrentIndicator(int index) {
//        int childCount = indicatorLayout.getChildCount();
//        for (int i = 0; i < childCount; i++) {
//            ImageView imageView = (ImageView) indicatorLayout.getChildAt(i);
//            if (i == index) {
//                imageView.setImageResource(R.drawable.indicator_active);
//            } else {
//                imageView.setImageResource(R.drawable.indicator_inactive);
//            }
//        }
//    }
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login); // 设置布局文件

    // 找到登录按钮
    Button btnLogin = findViewById(R.id.btnLogin);
    Button btnRegister = findViewById(R.id.btnRegister);

    // 设置按钮点击事件
    btnLogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 创建 Intent，跳转到 LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent); // 启动新的 Activity
        }
    });
    btnRegister.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            // 创建 Intent，跳转到 LoginActivity
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent); // 启动新的 Activity
        }
    });
}
}
