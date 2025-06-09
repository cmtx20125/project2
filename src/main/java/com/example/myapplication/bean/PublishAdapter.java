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
import com.example.myapplication.UI.PublishDetailTActivity;
import com.example.myapplication.UI.PublishDetailVActivity;
import com.example.myapplication.UI.ReportDetaiVActivity;
import com.example.myapplication.UI.ReportDetailTActivity;

import java.util.List;

public class PublishAdapter extends RecyclerView.Adapter<PublishAdapter.ViewHolder> {
    private final List<Publish> publishList;
    private final Context context;
    private final String minioPublicBaseUrl;
    private User user;
    private String userId;

    public PublishAdapter(Context context, List<Publish> publishs,String userId) {
        this.context = context;
        this.publishList = publishs;
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
            title = itemView.findViewById(R.id.videoTitle);
            content = itemView.findViewById(R.id.videoAuthor);
            image = itemView.findViewById(R.id.videoThumbnail);
            tag = itemView.findViewById(R.id.videoTag);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Publish publish = publishList.get(position);
        holder.title.setText(publish.getPublishName());
        holder.content.setText(publish.getPublishUserName());
        holder.tag.setText(publish.getPublishTag());

        holder.image.setImageResource(R.drawable.index_bg);

        String imageUrl = publish.getPublishCover().replace("127.0.0.1","192.168.2.11");
        Log.d("MinIO", "Loading image URL: " + imageUrl);

        Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .placeholder(R.drawable.index_bg)
                .error(R.drawable.dog)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        if (e != null) {
                            Log.e("Glide", "Image load failed: " + imageUrl, e);
                            if (e.getCause() instanceof java.net.ConnectException) {
                                Log.e("NETWORK", "Connection failed. Please check:\n" +
                                        "1. MinIO server status\n" +
                                        "2. URL correctness: " + imageUrl + "\n" +
                                        "3. Network connection");
                            }
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                                                   Target<Drawable> target, DataSource dataSource,
                                                   boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.image);

        holder.itemView.setOnClickListener(v -> {
            Intent intent;
            String tag = publish.getPublishTag(); // 获取报告标签

            // 根据不同的tag跳转到不同界面
            switch (tag) {
                case "图文": // 紧急报告
                    intent = new Intent(context, PublishDetailTActivity.class);
                    break;
                default:    // 普通报告
                    intent = new Intent(context, PublishDetailVActivity.class);
            }

            // 传递公共参数
            intent.putExtra("publishId", publish.getPublishId());
            intent.putExtra("publishTag", tag);
            intent.putExtra("publishImg", publish.getPublishImg());
            intent.putExtra("publishName", publish.getPublishName());
            intent.putExtra("publishContent", publish.getPublishContent());
            intent.putExtra("publishFile", publish.getPublishFile());
            intent.putExtra("publishUserName", publish.getPublishUserName());
            long createTimeMillis = publish.getPublishTime().getTime();
            intent.putExtra("publishTime",createTimeMillis);
            intent.putExtra("userId",userId);
            intent.putExtra("publishUserId", publish.getPublishUserId());
            // 启动Activity
            context.startActivity(intent);
        });
    }

    private boolean isRunningOnEmulator() {
        return Build.FINGERPRINT.startsWith("generic") ||
                Build.MODEL.contains("Android SDK");
    }
    public void updateList(List<Publish> newList) {
        publishList.clear();
        publishList.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return publishList.size();
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.with(context).clear(holder.image);
    }
}