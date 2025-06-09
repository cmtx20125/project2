package com.example.myapplication.bean;

import java.util.Date;

public class UnreadUser {
    private String userId;          // 发送方用户ID
    private String username;        // 发送方用户名
    private String avatar;          // 发送方头像
    private String lastContent;     // 最后一条消息内容
    private String receiverId;

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public String getLastContent() {
        return lastContent;
    }

    public void setLastContent(String lastContent) {
        this.lastContent = lastContent;
    }

}
