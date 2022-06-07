package com.example.csappjava.models;

import java.util.Date;

public class PostMarketplaceSearch {

    private String postId;      //게시물id
    private String userId;      //게시물 올린사람
    private String lecture;     //강의명
    private String professor;   //교수명
    private String campus;      //본분교명
    private String department;  //학과명
    private String year;        //년도
    private String month;       //학기



    public PostMarketplaceSearch() {
    }

    public PostMarketplaceSearch(String lecture, String professor, String campus, String department, String year, String month) {
        this.postId = postId;
        this.userId = userId;
        this.lecture = lecture;
        this.professor = professor;
        this.campus = campus;
        this.department = department;
        this.year = year;
        this.month = month;
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

    public String getLecture() {
        return lecture;
    }

    public void setLecture(String lecture) {
        this.lecture = lecture;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    @Override
    public String toString() {
        return "PostMarketplaceSearch{" +
                "postId='" + postId + '\'' +
                ", userId='" + userId + '\'' +
                ", lecture='" + lecture + '\'' +
                ", professor='" + professor + '\'' +
                ", campus='" + campus + '\'' +
                ", department='" + department + '\'' +
                ", year='" + year + '\'' +
                ", month='" + month + '\'' +
                '}';
    }
}

