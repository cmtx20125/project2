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
@TableName("report")
@AllArgsConstructor
public class Report {
    @TableId(value = "reportId", type = IdType.ASSIGN_UUID)
    String reportId;
    @TableField("reportName")
    String reportName;
    @TableField("reportAddress")
    String reportAddress;
    @TableField("reportContent")
    String reportContent;
    @TableField("reportImg")
    String reportImg;
    @TableField("reportFile")
    String reportFile;
    @TableField("reportTagOne")
    String reportTagOne;

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportAddress() {
        return reportAddress;
    }

    public void setReportAddress(String reportAddress) {
        this.reportAddress = reportAddress;
    }

    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    public String getReportImg() {
        return reportImg;
    }

    public void setReportImg(String reportImg) {
        this.reportImg = reportImg;
    }

    public String getReportFile() {
        return reportFile;
    }

    public void setReportFile(String reportFile) {
        this.reportFile = reportFile;
    }

    public String getReportTagOne() {
        return reportTagOne;
    }

    public void setReportTagOne(String reportTagOne) {
        this.reportTagOne = reportTagOne;
    }

    public String getReportTagTwo() {
        return reportTagTwo;
    }

    public void setReportTagTwo(String reportTagTwo) {
        this.reportTagTwo = reportTagTwo;
    }

    public String getReportCover() {
        return reportCover;
    }

    public void setReportCover(String reportCover) {
        this.reportCover = reportCover;
    }

    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    public String getReportCunZai() {
        return reportCunZai;
    }

    public void setReportCunZai(String reportCunZai) {
        this.reportCunZai = reportCunZai;
    }

    public String getReportUserId() {
        return reportUserId;
    }

    public void setReportUserId(String reportUserId) {
        this.reportUserId = reportUserId;
    }

    @TableField("reportTagTwo")
    String reportTagTwo;
    @TableField("reportCover")
    String reportCover;
    @TableField("reportTime")
    Date reportTime;
    @TableField("reportCunZai")
    String reportCunZai;
    @TableField("reportUserId")
    String reportUserId;
    @TableField("reportLo")
    String reportLo;
    @TableField("reportLa")
    String reportLa;

    public String getReportLo() {
        return reportLo;
    }

    public void setReportLo(String reportLo) {
        this.reportLo = reportLo;
    }

    public String getReportLa() {
        return reportLa;
    }

    public void setReportLa(String reportLa) {
        this.reportLa = reportLa;
    }

    public String getReportUserName() {
        return reportUserName;
    }

    public void setReportUserName(String reportUserName) {
        this.reportUserName = reportUserName;
    }
    @TableField("reportUserName")
    String reportUserName;
    public Report(){

    }
}
