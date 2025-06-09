package com.example.myapplication.bean;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogueAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_LEFT = 1;
    private static final int TYPE_RIGHT = 2;

    private List<Dialogue> messages;
    private String currentUserId;
    private ApiService apiService;

    // 用户信息缓存：避免重复请求
    private Map<String, User> userCache = new HashMap<>();

    public DialogueAdapter(List<Dialogue> messages, String currentUserId) {
        this.messages = messages;
        this.currentUserId = currentUserId;
        apiService = RetrofitClient.getInstance().create(ApiService.class);
    }

    @Override
    public int getItemViewType(int position) {
        Dialogue message = messages.get(position);
        return message.getDialogueUserId().equals(currentUserId) ? TYPE_RIGHT : TYPE_LEFT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_RIGHT) {
            return new RightMessageHolder(inflater.inflate(R.layout.item_message_send, parent, false));
        } else {
            return new LeftMessageHolder(inflater.inflate(R.layout.item_message, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Dialogue message = messages.get(position);
        String id = message.getDialogueUserId();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String time = sdf.format(message.getDialogueTime());

        // 显示内容
        if (holder instanceof LeftMessageHolder) {
            LeftMessageHolder h = (LeftMessageHolder) holder;
            h.tvContent.setText(message.getDialogueContent());
            h.tvTime.setText(time);

            // 加载头像
            loadUserAvatar(id, h.ivAvatar);

            // 连续消息隐藏头像
            if (position > 0 && messages.get(position - 1).getDialoguePid().equals(message.getDialoguePid())) {
                h.ivAvatar.setVisibility(View.INVISIBLE);
            } else {
                h.ivAvatar.setVisibility(View.VISIBLE);
            }

        } else if (holder instanceof RightMessageHolder) {
            RightMessageHolder h = (RightMessageHolder) holder;
            h.tvContent.setText(message.getDialogueContent());
            h.tvTime.setText(time);

            // 加载头像
            loadUserAvatar(id, h.ivAvatar);
        }
    }

    private void loadUserAvatar(String userId, ImageView imageView) {
        // 如果缓存中有，直接用
        if (userCache.containsKey(userId)) {
            String url = userCache.get(userId).getUserPic().replace("127.0.0.1", "192.168.2.11");
            Glide.with(imageView.getContext())
                    .load(url)
                    .placeholder(R.drawable.index_bg)
                    .error(R.drawable.dog)
                    .circleCrop()
                    .into(imageView);
        } else {
            // 网络请求
            Call<HttpResponseEntityS> call = apiService.getUser(userId);
            call.enqueue(new Callback<HttpResponseEntityS>() {
                @Override
                public void onResponse(Call<HttpResponseEntityS> call, Response<HttpResponseEntityS> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        HttpResponseEntityS result = response.body();
                        if ("666".equals(result.getCode())) {
                            User user = result.getData();
                            userCache.put(userId, user); // 缓存用户

                            // 更新头像
                            String url = user.getUserPic().replace("127.0.0.1", "192.168.2.11");
                            Glide.with(imageView.getContext())
                                    .load(url)
                                    .placeholder(R.drawable.index_bg)
                                    .error(R.drawable.dog)
                                    .circleCrop()
                                    .into(imageView);
                        } else {
                            Log.d("DialogueAdapter", "获取用户失败1");
                        }
                    } else {
                        Log.d("DialogueAdapter", "获取用户失败2");
                    }
                }

                @Override
                public void onFailure(Call<HttpResponseEntityS> call, Throwable t) {
                    Log.d("DialogueAdapter", "获取用户失败3: " + t.getMessage());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class LeftMessageHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvContent, tvTime;

        public LeftMessageHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }

    static class RightMessageHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvContent, tvTime;

        public RightMessageHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
