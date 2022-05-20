package com.example.csappjava.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class PostCommunity {

    private String postId;      //게시물id
    private String userId; //게시물 올린사람
    private String title; //제목
    private String contents;    //내용
    private String img; //이미지
    private String time; //시간
    private Date date;
    private String recommendation;  //추천
    private String tag;             //태그
    private String commentnum;         //댓글수

    public PostCommunity() {
    }

    public PostCommunity(String postId, String userId, String title, String contents, String img, String recommendation, String tag, String commentnum) {
        this.postId = postId;
        this.userId = userId;
        this.title = title;
        this.contents = contents;
        this.img = img;
        this.time = time;
        this.date = date;
        this.recommendation = recommendation;
        this.tag = tag;
        this.commentnum = commentnum;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCommentnum() {
        return commentnum;
    }

    public void setCommentnum(String commentnum) {
        this.commentnum = commentnum;
    }

    @Override
    public String toString() {
        return "PostCommunity{" +
                "postId='" + postId + '\'' +
                ", userId='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", img='" + img + '\'' +
                ", time='" + time + '\'' +
                ", date=" + date +
                ", recommendation='" + recommendation + '\'' +
                ", tag='" + tag + '\'' +
                ", commentnum='" + commentnum + '\'' +
                '}';
    }
}

