package com.example.myapplication.bean;

import java.util.Date;

public class Dialogue {
    String dialogueId;
    String dialogueContent;
    String dialogueUserId;
    String dialoguePid;
    Date dialogueTime;
    String dialogueRead;

    public String getDialogueId() {
        return dialogueId;
    }

    public void setDialogueId(String dialogueId) {
        this.dialogueId = dialogueId;
    }

    public String getDialogueContent() {
        return dialogueContent;
    }

    public void setDialogueContent(String dialogueContent) {
        this.dialogueContent = dialogueContent;
    }

    public String getDialogueUserId() {
        return dialogueUserId;
    }

    public void setDialogueUserId(String dialogueUserId) {
        this.dialogueUserId = dialogueUserId;
    }

    public String getDialoguePid() {
        return dialoguePid;
    }

    public void setDialoguePid(String dialoguePid) {
        this.dialoguePid = dialoguePid;
    }

    public Date getDialogueTime() {
        return dialogueTime;
    }

    public void setDialogueTime(Date dialogueTime) {
        this.dialogueTime = dialogueTime;
    }

    public String getDialogueRead() {
        return dialogueRead;
    }

    public void setDialogueRead(String dialogueRead) {
        this.dialogueRead = dialogueRead;
    }
}
