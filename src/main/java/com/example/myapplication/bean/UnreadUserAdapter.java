package com.example.myapplication.bean;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.UI.DialogueActivity;
import com.example.myapplication.UI.MessagesActivity;
import com.example.myapplication.UI.PublishDetailTActivity;
import com.example.myapplication.UI.TrendsActivity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnreadUserAdapter extends RecyclerView.Adapter<UnreadUserAdapter.ViewHolder> {

    private List<UnreadUser> userList;
    private Context context;

    public UnreadUserAdapter(Context context, List<UnreadUser> list) {
        this.context = context;
        this.userList = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon, arrow;
        TextView tvNotice, tvMore;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNotice = itemView.findViewById(R.id.tv_notice);
            tvMore = itemView.findViewById(R.id.tv_more);
        }
    }

    @NonNull
    @Override
    public UnreadUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_xiaoxi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UnreadUserAdapter.ViewHolder holder, int position) {
        UnreadUser user = userList.get(position);
        holder.tvNotice.setText(user.getUsername() + "：" + user.getLastContent());

        // 可选：加载头像、处理“更多”点击事件
        holder.tvMore.setOnClickListener(v -> {
            ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
            apiService.updateDialogue(user.getReceiverId(),user.getUserId()).enqueue(new Callback<updateEntity>() {
                @Override
                public void onResponse(Call<updateEntity> call,
                                       Response<updateEntity> response) {
                    handleResponse(response);
                }

                @Override
                public void onFailure(Call<updateEntity> call, Throwable t) {
                    Toast.makeText(context, "查看更多来自 " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            Toast.makeText(context, "查看更多来自 " + user.getUsername() + " 的消息", Toast.LENGTH_SHORT).show();
            Intent intent;
            intent = new Intent(context, DialogueActivity.class);
            intent.putExtra("other_user_id",user.getUserId());
            intent.putExtra("user_id",user.getReceiverId());
            intent.putExtra("other_user_name",user.getUsername());
            context.startActivity(intent);

            // 可跳转到消息详情页面
        });
    }

    private void handleResponse(Response<updateEntity> response) {
        if (response.isSuccessful() && response.body() != null) {
            updateEntity httpResponse = response.body();
            if ("666".equals(httpResponse.getCode())) {
                Toast.makeText(context, "进入聊天界面", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, httpResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "服务器响应异常", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public int getItemCount() {
        return userList.size();
    }
}

