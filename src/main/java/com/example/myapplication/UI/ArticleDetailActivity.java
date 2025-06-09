package com.example.myapplication.UI;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.bean.ApiService;
import com.example.myapplication.bean.Article;
import com.example.myapplication.bean.RetrofitClient;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleDetailActivity extends AppCompatActivity {
    private TextView titleTextView, contentTextView, authorTextView, timeTextView;
    private ImageView articleImageView, articleUserIc;
    private Button rescueButton;
    private ApiService apiService;
    private String articleId, articleImg, articleContent, articleUserName, articleUserPic,articleName;
    private Date articleCreateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhuli);

        // 获取传递的文章 ID
        articleId = getIntent().getStringExtra("articleId");
        articleImg = getIntent().getStringExtra("articleImg");
        articleContent = getIntent().getStringExtra("articleContent");
        long date = getIntent().getLongExtra("articleCreateTime", -1);
        if (date != -1) {
            articleCreateTime = new Date(date);
        } else {
            Toast.makeText(this, "未接收到日期数据", Toast.LENGTH_SHORT).show();
        }
        articleUserName = getIntent().getStringExtra("articleUserName");
        articleUserPic = getIntent().getStringExtra("articleUserPic");
        articleName = getIntent().getStringExtra("articleName");

        // 初始化 UI 组件
        titleTextView = findViewById(R.id.articleTitle);
        contentTextView = findViewById(R.id.articleContent);
        articleImageView = findViewById(R.id.article_image);
        articleUserIc = findViewById(R.id.article_ic_user);
        rescueButton = findViewById(R.id.rescueButton);
        authorTextView = findViewById(R.id.authorTextView);
        timeTextView = findViewById(R.id.timeTextView);

        // 初始化 Retrofit
        apiService = RetrofitClient.getInstance().create(ApiService.class);

        // 获取文章详细内容
        fetchArticleDetail(articleId);

        // 返回按钮点击事件
        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        String tag = getIntent().getStringExtra("articleTag"); // 从Intent获取tag
        setButtonTextByTag(rescueButton, tag);
        // 救助按钮点击事件
        rescueButton.setOnClickListener(v -> {
            // 这里可以实现救助按钮的功能
            Intent intent = new Intent(ArticleDetailActivity.this, PayActivity.class);
            intent.putExtra("product_name", rescueButton.getText());
            startActivity(intent);  // 启动 ProfileActivity
        });

    }
    private void setButtonTextByTag(Button button, String tag) {
        if (tag == null) {
            button.setText("默认救助");
            return;
        }

        switch (tag.toLowerCase()) {
            case "救助":
                button.setText("救助");
                break;
            default:
                button.setText("购买");
        }
    }

    private void fetchArticleDetail(String articleId) {
        // 假设你已经有一个获取单篇文章详细信息的 API 接口

        titleTextView.setText(articleName);
        contentTextView.setText(articleContent);
        authorTextView.setText(articleUserName);
        // 格式化时间并显示
        timeTextView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(articleCreateTime));
        Log.d("MinIO", "Loading image URL: " + articleName+" "+articleContent);
        // 使用 Fresco 加载图片
        String imageUrl = articleImg.replace("127.0.0.1","192.168.2.11");
        Log.d("MinIO", "Loading image URL: " + imageUrl);
        String userImage = articleUserPic.replace("127.0.0.1","192.168.2.11");
        Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)  // 缓存所有版本图片
                .placeholder(R.drawable.index_bg)
                .error(R.drawable.dog)           // 错误图
                .transition(DrawableTransitionOptions.withCrossFade(300)) // 淡入淡出动画
                .into(articleImageView);  // 你的ImageView变量名

// 使用Glide加载用户头像（圆形裁剪）
        Glide.with(this)
                .load(userImage)
                .apply(RequestOptions.circleCropTransform()) // 圆形头像
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.index_bg)
                .error(R.drawable.dog)
                .into(articleUserIc);

    }
}

