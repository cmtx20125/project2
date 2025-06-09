package com.example.myapplication.bean;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.myapplication.R;
import com.example.myapplication.UI.ArticleDetailActivity;
import com.example.myapplication.UI.DialogueActivity;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private final List<Review> reviewList;
    private final Context context;
    private final String minioPublicBaseUrl;
    private String userId;
    private ApiService apiService;
    // 匿名缓存，key 为用户 ID，value 为是否匿名
    private final java.util.Map<String, Boolean> anonymityCache = new java.util.HashMap<>();


    public ReviewAdapter(Context context, List<Review> reviews,String userId) {
        this.context = context;
        this.reviewList = reviews;
        this.userId = userId;
        this.minioPublicBaseUrl = isRunningOnEmulator() ?
                "http://10.0.2.2:9005/pet/" :
                "http://192.168.91.1:9005/pet/";
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView title, content, tag;
        public final ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.message_name);
            content = itemView.findViewById(R.id.mtvContent);
            image = itemView.findViewById(R.id.message_image);
            tag = itemView.findViewById(R.id.message_state);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviewList.get(position);
        String reviewUserId = review.getReviewUserId();

        holder.title.setText(review.getReviewUserName());
        holder.content.setText(review.getReviewContent());
        holder.tag.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(review.getReviewTime()));

        // ✅ 匿名缓存命中时，直接处理
        if (anonymityCache.containsKey(reviewUserId)) {
            boolean isAnonymous = anonymityCache.get(reviewUserId);
            if (isAnonymous && !userId.equals(reviewUserId)) {
                review.setReviewUserName("匿名");
                review.setReviewUserImg("http://192.168.2.11:9005/pet/icon_logo_pet.png"); // 替换为匿名头像
            }
        } else {
            // 否则发起请求并缓存
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.2.11:8080/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(ApiService.class);
            Call<HttpResponseEntityS> call = apiService.getUser(reviewUserId);
            call.enqueue(new Callback<HttpResponseEntityS>() {
                @Override
                public void onResponse(Call<HttpResponseEntityS> call, Response<HttpResponseEntityS> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        HttpResponseEntityS result = response.body();
                        if ("666".equals(result.getCode())) {
                            User user = result.getData();
                            boolean isAnonymous = user.getUserTago() == 1;
                            anonymityCache.put(reviewUserId, isAnonymous); // ✅ 缓存匿名状态

                            if (isAnonymous && !userId.equals(reviewUserId)) {
                                review.setReviewUserName("匿名");
                                review.setReviewUserImg("http://192.168.2.11:9005/pet/icon_logo_pet.png");
                            } else {
                                review.setReviewUserName(user.getUserName());
                                review.setReviewUserImg(user.getUserPic());
                            }

                            int adapterPosition = holder.getAdapterPosition();
                            if (adapterPosition != RecyclerView.NO_POSITION) {
                                notifyItemChanged(adapterPosition);
                            }
                        }
                    } else {
                        Log.d("ReviewAdapter", "服务器响应错误");
                    }
                }

                @Override
                public void onFailure(Call<HttpResponseEntityS> call, Throwable t) {
                    Log.e("ReviewAdapter", "请求失败", t);
                }
            });
        }
        holder.title.setText(review.getReviewUserName());
        // 加载头像
        String imageUrl = review.getReviewUserImg().replace("127.0.0.1", "192.168.2.11");
        Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .placeholder(R.drawable.index_bg)
                .error(R.drawable.dog)
                .into(holder.image);

        holder.image.setOnClickListener(v -> {
            Intent intent = new Intent(context, DialogueActivity.class);
            intent.putExtra("other_user_id", review.getReviewUserId());
            intent.putExtra("user_id", userId);
            intent.putExtra("other_user_name", review.getReviewUserName());
            context.startActivity(intent);
        });
    }


    private boolean isRunningOnEmulator() {
        return Build.FINGERPRINT.startsWith("generic") ||
                Build.MODEL.contains("Android SDK");
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.with(context).clear(holder.image);
    }
}