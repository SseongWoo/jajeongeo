package com.example.csappjava.models;

import java.util.Date;

public class PostCommunityComment {

    private String userId; //게시물 올린사람
    private String postid;      //게시글id
    private String contents;    //내용
    private String img; //이미지
    private String time; //시간
    private String nickname; //닉네임
    private Date date;


    public PostCommunityComment() {
    }

    public PostCommunityComment(String userId, String postid, String contents, String img, String nickname) {
        this.userId = userId;
        this.postid = postid;
        this.contents = contents;
        this.img = img;
        this.time = time;
        this.nickname = nickname;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "PostCommunityComment{" +
                "userId='" + userId + '\'' +
                ", postid='" + postid + '\'' +
                ", contents='" + contents + '\'' +
                ", img='" + img + '\'' +
                ", time='" + time + '\'' +
                ", nickname='" + nickname + '\'' +
                ", date=" + date +
                '}';
    }
}

