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
import com.example.myapplication.UI.PetDetaiActivity;

import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.ViewHolder> {
    private final List<Pet> petList;
    private final Context context;
    private final String minioPublicBaseUrl;

    public PetAdapter(Context context, List<Pet> pets) {
        this.context = context;
        this.petList = pets;
        this.minioPublicBaseUrl = isRunningOnEmulator() ?
                "http://10.0.2.2:9005/pet/" :
                "http://192.168.91.1:9005/pet/";
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView title, content;
        public final ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.pet_name);
            content = itemView.findViewById(R.id.pet_description);
            image = itemView.findViewById(R.id.pet_image);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pet pet = petList.get(position);
        holder.title.setText(pet.getPetName());
        holder.content.setText(pet.getPetContent());

        holder.image.setImageResource(R.drawable.index_bg);

        String imageUrl = pet.getPetImg().replace("127.0.0.1","192.168.2.11");
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
            Intent intent = new Intent(context, PetDetaiActivity.class);
            intent.putExtra("petId", pet.getPetId());
            intent.putExtra("petName",pet.getPetName());
            intent.putExtra("petImg",pet.getPetImg());
            intent.putExtra("petContent",pet.getPetContent());
            intent.putExtra("petYM",pet.getPetYM());
            intent.putExtra("petGender",pet.getPetGender());
            intent.putExtra("petJue",pet.getPetJue());
            intent.putExtra("petYear",pet.getPetYear());
            intent.putExtra("petYuan",pet.getPetYuan());
            intent.putExtra("userId",pet.getUserId());
            context.startActivity(intent);
        });
    }

    private boolean isRunningOnEmulator() {
        return Build.FINGERPRINT.startsWith("generic") ||
                Build.MODEL.contains("Android SDK");
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.with(context).clear(holder.image);
    }
}