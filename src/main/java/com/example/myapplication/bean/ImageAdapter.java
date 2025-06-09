package com.example.myapplication.bean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private final List<String> imagePaths;
    private final Context context;
    private final Set<String> selectedPaths = new HashSet<>();
    private OnImageSelectedListener onImageSelectedListener;

    public interface OnImageSelectedListener {
        void onImageSelectionChanged(int selectedCount);
    }

    public void setOnImageSelectedListener(OnImageSelectedListener listener) {
        this.onImageSelectedListener = listener;
    }

    public ImageAdapter(List<String> imagePaths, Context context) {
        this.imagePaths = imagePaths;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String path = imagePaths.get(position);

        Glide.with(context)
                .load(path)
                .into(holder.imageView);

        holder.checkBox.setChecked(selectedPaths.contains(path));

        holder.itemView.setOnClickListener(v -> {
            toggleSelection(path, holder);
            if (onImageSelectedListener != null) {
                onImageSelectedListener.onImageSelectionChanged(selectedPaths.size());
            }
        });

        holder.checkBox.setOnClickListener(v -> {
            toggleSelection(path, holder);
            if (onImageSelectedListener != null) {
                onImageSelectedListener.onImageSelectionChanged(selectedPaths.size());
            }
        });
    }

    private void toggleSelection(String path, ViewHolder holder) {
        if (selectedPaths.contains(path)) {
            selectedPaths.remove(path);
        } else {
            selectedPaths.add(path);
        }
        holder.checkBox.setChecked(selectedPaths.contains(path));
    }

    @Override
    public int getItemCount() {
        return imagePaths.size();
    }

    public List<String> getSelectedImages() {
        return new ArrayList<>(selectedPaths);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        CheckBox checkBox;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}