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
@TableName("topictb")
@AllArgsConstructor
public class Publish {
    @TableId(value = "publishId", type = IdType.ASSIGN_UUID)
    String publishId;
    @TableField("publishName")
    String publishName;
    @TableField("publishFile")
    String publishFile;
    @TableField("publishImg")
    String publishImg;
    @TableField("publishUserId")
    String publishUserId;
    @TableField("publishUserName")
    String publishUserName;
    @TableField("publishContent")
    String publishContent;
    @TableField("publishTag")
    String publishTag;
    @TableField("publishTagtwo")
    String publishTagtwo;
    @TableField("publishCover")
    String publishCover;
    @TableField("publishTime")
    Date publishTime;

    public String getPublishId() {
        return publishId;
    }

    public void setPublishId(String publishId) {
        this.publishId = publishId;
    }

    public String getPublishName() {
        return publishName;
    }

    public void setPublishName(String publishName) {
        this.publishName = publishName;
    }

    public String getPublishFile() {
        return publishFile;
    }

    public void setPublishFile(String publishFile) {
        this.publishFile = publishFile;
    }

    public String getPublishImg() {
        return publishImg;
    }

    public void setPublishImg(String publishImg) {
        this.publishImg = publishImg;
    }

    public String getPublishUserId() {
        return publishUserId;
    }

    public void setPublishUserId(String publishUserId) {
        this.publishUserId = publishUserId;
    }

    public String getPublishUserName() {
        return publishUserName;
    }

    public void setPublishUserName(String publishUserName) {
        this.publishUserName = publishUserName;
    }

    public String getPublishContent() {
        return publishContent;
    }

    public void setPublishContent(String publishContent) {
        this.publishContent = publishContent;
    }

    public String getPublishTag() {
        return publishTag;
    }

    public void setPublishTag(String publishTag) {
        this.publishTag = publishTag;
    }

    public String getPublishTagtwo() {
        return publishTagtwo;
    }

    public void setPublishTagtwo(String publishTagtwo) {
        this.publishTagtwo = publishTagtwo;
    }

    public String getPublishCover() {
        return publishCover;
    }

    public void setPublishCover(String publishCover) {
        this.publishCover = publishCover;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }
    public Publish(){

    }
}
