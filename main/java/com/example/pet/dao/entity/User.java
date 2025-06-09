package com.example.pet.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
@AllArgsConstructor
public class User {
    @TableId(value = "userId", type = IdType.ASSIGN_UUID)
    private String userId;
    @TableField("userName")
    private String userName;
    @TableField("userPhone")
    private String userPhone;
    @TableField("userPwd")
    private String userPwd;
    @TableField("userPic")
    private String userPic;
    @JsonFormat(pattern = "MMM d, yyyy HH:mm:ss", locale = "en")
    @TableField("userTime")
    private Date userTime;
    @TableField("userTago")
    private int userTago;
    @TableField("userTagt")
    private int userTagt;
    @TableField("userTagtr")
    private int userTagtr;
    @TableField("userAddress")
    private String userAddress;
    @TableField("userGender")
    private String userGender;
    @JsonFormat(pattern = "MMM d, yyyy HH:mm:ss", locale = "en")
    @TableField("userDate")
    private Date userDate;

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public Date getUserDate() {
        return userDate;
    }

    public void setUserDate(Date userDate) {
        this.userDate = userDate;
    }
    public User(){

    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public Date getUserTime() {
        return userTime;
    }

    public void setUserTime(Date userTime) {
        this.userTime = userTime;
    }

    public int getUserTago() {
        return userTago;
    }

    public void setUserTago(int userTago) {
        this.userTago = userTago;
    }

    public int getUserTagt() {
        return userTagt;
    }

    public void setUserTagt(int userTagt) {
        this.userTagt = userTagt;
    }

    public int getUserTagtr() {
        return userTagtr;
    }

    public void setUserTagtr(int userTagtr) {
        this.userTagtr = userTagtr;
    }
}
