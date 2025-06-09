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
@TableName("dialogue")
@AllArgsConstructor
public class Dialogue {
    @TableId(value = "dialogueId", type = IdType.ASSIGN_UUID)
    String dialogueId;
    @TableField("dialogueUserId")
    String dialogueUserId;
    @TableField("dialogueContent")
    String dialogueContent;
    @TableField("dialoguePid")
    String dialoguePid;
    @TableField("dialogueRead")
    String dialogueRead;
    @TableField("dialogueTime")
    Date dialogueTime;

    public String getDialogueId() {
        return dialogueId;
    }

    public void setDialogueId(String dialogueId) {
        this.dialogueId = dialogueId;
    }

    public String getDialogueUserId() {
        return dialogueUserId;
    }

    public void setDialogueUserId(String dialogueUserId) {
        this.dialogueUserId = dialogueUserId;
    }

    public String getDialogueContent() {
        return dialogueContent;
    }

    public void setDialogueContent(String dialogueContent) {
        this.dialogueContent = dialogueContent;
    }

    public String getDialoguePid() {
        return dialoguePid;
    }

    public void setDialoguePid(String dialoguePid) {
        this.dialoguePid = dialoguePid;
    }

    public String getDialogueRead() {
        return dialogueRead;
    }

    public void setDialogueRead(String dialogueRead) {
        this.dialogueRead = dialogueRead;
    }

    public Date getDialogueTime() {
        return dialogueTime;
    }

    public void setDialogueTime(Date dialogueTime) {
        this.dialogueTime = dialogueTime;
    }
}
