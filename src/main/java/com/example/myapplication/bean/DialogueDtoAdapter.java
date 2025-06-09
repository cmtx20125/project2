package com.example.myapplication.bean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class DialogueDtoAdapter extends RecyclerView.Adapter<DialogueDtoAdapter.UserViewHolder> {
    private List<DialogueDto> userList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onChatClick(int position);
        void onProfileClick(int position);
    }

    public DialogueDtoAdapter(List<DialogueDto> userList, Context context, OnItemClickListener listener) {
        this.userList = userList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_p, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        DialogueDto user = userList.get(position);

        // 设置用户数据
        holder.tvNotice.setText(user.getUserName());

        // 点击聊天区域
        holder.tvMore.setOnClickListener(v -> {
            if (listener != null) {
                listener.onChatClick(position);
            }
        });

        // 点击右侧箭头（同聊天区域）
        holder.ivArrow.setOnClickListener(v -> {
            if (listener != null) {
                listener.onChatClick(position);
            }
        });

        // 点击头像区域
        holder.ivProfile.setOnClickListener(v -> {
            if (listener != null) {
                // 检查用户标签
                if (user.getUserTagt() == 1) {
                    // 显示无法查看的提示
                    showPrivateProfileDialog(context);
                } else {
                    listener.onProfileClick(position);
                }
            }
        });
    }

    // 显示隐私设置的对话框
    private void showPrivateProfileDialog(Context context) {
        new AlertDialog.Builder(context)
                .setTitle("提示")
                .setMessage("该用户设置不可查看个人信息")
                .setPositiveButton("确定", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfile;
        TextView tvNotice;
        TextView tvMore;
        ImageView ivArrow;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.iv_profile); // 需要修改布局中的id
            tvNotice = itemView.findViewById(R.id.tv_notice);
            tvMore = itemView.findViewById(R.id.tv_more);
            ivArrow = itemView.findViewById(R.id.iv_arrow); // 需要修改布局中的id
        }
    }
}
