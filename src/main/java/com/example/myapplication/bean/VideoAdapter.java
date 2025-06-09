package com.example.myapplication.bean;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private List<VideoItem> videoList;
    private Set<VideoItem> selectedVideos = new HashSet<>();
    private OnSelectionChangedListener selectionListener;

    public interface OnSelectionChangedListener {
        void onSelectionChanged(int selectedCount);
    }

    public VideoAdapter(List<VideoItem> videoList, OnSelectionChangedListener listener) {
        this.videoList = videoList;
        this.selectionListener = listener;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        VideoItem video = videoList.get(position);

        // 加载缩略图
        Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(
                video.getPath(),
                MediaStore.Images.Thumbnails.MINI_KIND
        );
        holder.thumbnail.setImageBitmap(thumbnail);

        // 设置视频时长
        holder.duration.setText(formatDuration(video.getDuration()));

        // 设置选择状态
        boolean isSelected = selectedVideos.contains(video);
        holder.checkBox.setChecked(isSelected);
        holder.overlay.setVisibility(isSelected ? View.VISIBLE : View.GONE);

        // 点击事件
        holder.itemView.setOnClickListener(v -> {
            toggleSelection(video);
            notifyItemChanged(position);
        });

        // CheckBox点击事件
        holder.checkBox.setOnClickListener(v -> {
            toggleSelection(video);
            holder.overlay.setVisibility(holder.checkBox.isChecked() ? View.VISIBLE : View.GONE);
        });
    }

    private void toggleSelection(VideoItem video) {
        if (selectedVideos.contains(video)) {
            selectedVideos.remove(video);
        } else {
            selectedVideos.add(video);
        }
        if (selectionListener != null) {
            selectionListener.onSelectionChanged(selectedVideos.size());
        }
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public List<VideoItem> getSelectedVideos() {
        return new ArrayList<>(selectedVideos);
    }

    private String formatDuration(long duration) {
        long seconds = duration / 1000;
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView duration;
        CheckBox checkBox;
        View overlay;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.videoThumbnail);
            duration = itemView.findViewById(R.id.videoDuration);
            checkBox = itemView.findViewById(R.id.checkBox);
            overlay = itemView.findViewById(R.id.selectedOverlay);
        }
    }
}
