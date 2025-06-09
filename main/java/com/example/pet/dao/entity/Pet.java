package com.example.pet.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_pet_dangan")
@AllArgsConstructor
public class Pet {
    @TableId(value = "petId", type = IdType.ASSIGN_UUID)
    String petId;
    @TableField("petName")
    String petName;
    @TableField("userId")
    String userId;
    @TableField("petContext")
    String petContent;
    @TableField("petImg")
    String petImg;
    @TableField("petYM")
    String petYM;
    @TableField("petTag")
    String petTag;
    @TableField("petGender")
    String petGender;
    @TableField("createTime")
    Date createTime;
    @TableField("updateTime")
    Date updateTime;
    @TableField("petYuan")
    String petYuan;
    @TableField("petJue")
    String petJue;
    @TableField("petYear")
    String petYear;

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPetContent() {
        return petContent;
    }

    public void setPetContent(String petContent) {
        this.petContent = petContent;
    }

    public String getPetImg() {
        return petImg;
    }

    public void setPetImg(String petImg) {
        this.petImg = petImg;
    }

    public String getPetYM() {
        return petYM;
    }

    public void setPetYM(String petYM) {
        this.petYM = petYM;
    }

    public String getPetTag() {
        return petTag;
    }

    public void setPetTag(String petTag) {
        this.petTag = petTag;
    }

    public String getPetGender() {
        return petGender;
    }

    public void setPetGender(String petGender) {
        this.petGender = petGender;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getPetYuan() {
        return petYuan;
    }

    public void setPetYuan(String petYuan) {
        this.petYuan = petYuan;
    }

    public String getPetJue() {
        return petJue;
    }

    public void setPetJue(String petJue) {
        this.petJue = petJue;
    }

    public String getPetYear() {
        return petYear;
    }

    public void setPetYear(String petYear) {
        this.petYear = petYear;
    }
    public Pet(){

    }
}
