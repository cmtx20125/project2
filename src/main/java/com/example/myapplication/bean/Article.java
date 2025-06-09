package com.example.myapplication.bean;

import java.util.Date;

public class Article {
    String articleId;
    String articleName;
    String articleContent;
    String articlePic;

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    public String getArticlePic() {
        return articlePic;
    }

    public void setArticlePic(String articlePic) {
        this.articlePic = articlePic;
    }

    public String getArticleUserName() {
        return articleUserName;
    }

    public void setArticleUserName(String articleUserName) {
        this.articleUserName = articleUserName;
    }

    public String getArticleUserPic() {
        return articleUserPic;
    }

    public void setArticleUserPic(String articleUserPic) {
        this.articleUserPic = articleUserPic;
    }

    public String getArticleTag() {
        return articleTag;
    }

    public void setArticleTag(String articleTag) {
        this.articleTag = articleTag;
    }

    public Date getArticleCreateTime() {
        return articleCreateTime;
    }

    public void setArticleCreateTime(Date articleCreateTime) {
        this.articleCreateTime = articleCreateTime;
    }

    String articleUserName;
    String articleUserPic;
    String articleTag;
    Date articleCreateTime;
}
