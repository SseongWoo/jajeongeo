package com.example.csappjava.models;

import java.util.Date;

public class PostMarketplace {

    private String postId;      //게시물id
    private String userId; //게시물 올린사람
    private String title; //제목
    private String contents;    //내용
    private String img; //이미지
    private String time; //시간
    private String price;   //가격
    private String nick;
    private String lecture;
    private String professor;
    private String department;
    private String adapteryear;
    private String adaptermonth;
    private String bookname;
    private String bookprice;
    private String bookpublisher;
    private String bookimg;
    private String author;
    private String booklink;

    private Date date;
    private String transaction; //거래 완료 유무


    public PostMarketplace() {
    }

    public PostMarketplace(String postId, String userId, String title, String contents, String img, String price, String nick, String time, String transaction , String lecture, String professor, String department, String adapteryear, String adaptermonth, String bookname, String bookprice, String bookpublisher, String bookimg, String author, String booklink) {
        this.postId = postId;
        this.userId = userId;
        this.title = title;
        this.contents = contents;
        this.img = img;
        this.time = time;
        this.price = price;
        this.nick = nick;
        this.lecture = lecture;
        this.professor = professor;
        this.department = department;
        this.adapteryear = adapteryear;
        this.adaptermonth = adaptermonth;
        this.bookname = bookname;
        this.bookprice = bookprice;
        this.bookpublisher = bookpublisher;
        this.bookimg = bookimg;
        this.author = author;
        this.booklink = booklink;
        this.transaction = transaction;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAdapteryear() {
        return adapteryear;
    }

    public void setAdapteryear(String adapteryear) {
        this.adapteryear = adapteryear;
    }

    public String getAdaptermonth() {
        return adaptermonth;
    }

    public void setAdaptermonth(String adaptermonth) {
        this.adaptermonth = adaptermonth;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getBookprice() {
        return bookprice;
    }

    public void setBookprice(String bookprice) {
        this.bookprice = bookprice;
    }

    public String getBookpublisher() {
        return bookpublisher;
    }

    public void setBookpublisher(String bookpublisher) {
        this.bookpublisher = bookpublisher;
    }

    public String getBookimg() {
        return bookimg;
    }

    public void setBookimg(String bookimg) {
        this.bookimg = bookimg;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBooklink() {
        return booklink;
    }

    public void setBooklink(String booklink) {
        this.booklink = booklink;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    @Override
    public String toString() {
        return "PostMarketplace{" +
                "postId='" + postId + '\'' +
                ", userId='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", img='" + img + '\'' +
                ", time='" + time + '\'' +
                ", price='" + price + '\'' +
                ", nick='" + nick + '\'' +
                ", lecture='" + lecture + '\'' +
                ", professor='" + professor + '\'' +
                ", department='" + department + '\'' +
                ", adapteryear='" + adapteryear + '\'' +
                ", adaptermonth='" + adaptermonth + '\'' +
                ", bookname='" + bookname + '\'' +
                ", bookprice='" + bookprice + '\'' +
                ", bookpublisher='" + bookpublisher + '\'' +
                ", bookimg='" + bookimg + '\'' +
                ", author='" + author + '\'' +
                ", booklink='" + booklink + '\'' +
                ", date=" + date +
                ", transaction='" + transaction + '\'' +
                '}';
    }
}

