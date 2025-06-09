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
@TableName("dongwu")
@AllArgsConstructor
public class Animal {
    @TableId(value = "animalId", type = IdType.ASSIGN_UUID)
    String animalId;
    @TableField("animalName")
    String animalName;
    @TableField("reportId")
    String reportId;
    @TableField("animalContent")
    String animalContent;
    @TableField("animalImg")
    String animalImg;
    @TableField("animalYM")
    String animalYM;
    @TableField("animalTag")
    String animalTag;
    @TableField("animalGender")
    String animalGender;
    @TableField("animalTime")
    Date animalTime;

    @TableField("animalYuan")
    String animalYuan;
    @TableField("animalJue")
    String animalJue;
    @TableField("animalYear")
    String animalYear;
    @TableField("animalYang")
    String animalYang;

    public String getAnimalYang() {
        return animalYang;
    }

    public void setAnimalYang(String animalYang) {
        this.animalYang = animalYang;
    }

    public String getAnimalId() {
        return animalId;
    }

    public void setAnimalId(String animalId) {
        this.animalId = animalId;
    }

    public String getAnimalName() {
        return animalName;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getAnimalContent() {
        return animalContent;
    }

    public void setAnimalContent(String animalContent) {
        this.animalContent = animalContent;
    }

    public String getAnimalImg() {
        return animalImg;
    }

    public void setAnimalImg(String animalImg) {
        this.animalImg = animalImg;
    }

    public String getAnimalYM() {
        return animalYM;
    }

    public void setAnimalYM(String animalYM) {
        this.animalYM = animalYM;
    }

    public String getAnimalTag() {
        return animalTag;
    }

    public void setAnimalTag(String animalTag) {
        this.animalTag = animalTag;
    }

    public String getAnimalGender() {
        return animalGender;
    }

    public void setAnimalGender(String animalGender) {
        this.animalGender = animalGender;
    }

    public Date getAnimalTime() {
        return animalTime;
    }

    public void setAnimalTime(Date animalTime) {
        this.animalTime = animalTime;
    }

    public String getAnimalYuan() {
        return animalYuan;
    }

    public void setAnimalYuan(String animalYuan) {
        this.animalYuan = animalYuan;
    }

    public String getAnimalJue() {
        return animalJue;
    }

    public void setAnimalJue(String animalJue) {
        this.animalJue = animalJue;
    }

    public String getAnimalYear() {
        return animalYear;
    }

    public void setAnimalYear(String animalYear) {
        this.animalYear = animalYear;
    }
    public Animal(){

    }
}
