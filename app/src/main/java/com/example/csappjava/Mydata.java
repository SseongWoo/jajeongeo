package com.example.csappjava;

import android.app.Application;

public class Mydata extends Application {
    private static String firstpath;
    private static String secondpath;
    private static String myschool;
    private static String myschoolkr;
    private static String mynickname;
    private static String myaffiliation;
    private static String myprofile;
    private static String mycampus;
    private static String myemail;
    private static String mydepartment;
    private static String postpath1;
    private static String postpath2;
    private static String send;
    private static String nearschool;
    private static String nearschoolkr;

    private static int count;

    @Override
    public void onCreate() {
        super.onCreate();
        firstpath = "";
        secondpath ="";
        myschool = "";
        myschoolkr = "";
        mynickname = "";
        myaffiliation = "";
        myprofile = "";
        mycampus = "";
        myemail = "";
        mydepartment = "";
        postpath1 = "";
        postpath2 = "";
        send = "";
        nearschool = "";
        nearschoolkr = "";
    }

    public void Init(){
        firstpath = "";
        secondpath ="";
        myschool = "";
        myschoolkr = "";
        mynickname = "";
        myaffiliation = "";
        myprofile = "";
        mycampus = "";
        myemail = "";
        mydepartment = "";
        postpath1 = "";
        postpath2 = "";
        send = "";
        nearschool = "";
        nearschoolkr ="";
    }

    public static String getFirstpath() {
        return firstpath;
    }

    public static void setFirstpath(String firstpath) {
        Mydata.firstpath = firstpath;
    }

    public static String getSecondpath() {
        return secondpath;
    }

    public static void setSecondpath(String secondpath) {
        Mydata.secondpath = secondpath;
    }

    public static String getMyschool() {
        return myschool;
    }

    public static void setMyschool(String myschool) {
        Mydata.myschool = myschool;
    }

    public static String getMyschoolkr() {
        return myschoolkr;
    }

    public static void setMyschoolkr(String myschoolkr) {
        Mydata.myschoolkr = myschoolkr;
    }

    public static String getMynickname() {
        return mynickname;
    }

    public static void setMynickname(String mynickname) {
        Mydata.mynickname = mynickname;
    }

    public static String getMyaffiliation() {
        return myaffiliation;
    }

    public static void setMyaffiliation(String myaffiliation) {
        Mydata.myaffiliation = myaffiliation;
    }

    public static String getMyprofile() {
        return myprofile;
    }

    public static void setMyprofile(String myprofile) {
        Mydata.myprofile = myprofile;
    }

    public static String getMycampus() {
        return mycampus;
    }

    public static void setMycampus(String mycampus) {
        Mydata.mycampus = mycampus;
    }

    public static String getMyemail() {
        return myemail;
    }

    public static void setMyemail(String myemail) {
        Mydata.myemail = myemail;
    }

    public static String getMydepartment() {
        return mydepartment;
    }

    public static void setMydepartment(String mydepartment) {
        Mydata.mydepartment = mydepartment;
    }

    public static String getPostpath1() {
        return postpath1;
    }

    public static void setPostpath1(String postpath1) {
        Mydata.postpath1 = postpath1;
    }

    public static String getPostpath2() {
        return postpath2;
    }

    public static void setPostpath2(String postpath2) {
        Mydata.postpath2 = postpath2;
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        Mydata.count = count;
    }

    public static String getSend() {
        return send;
    }

    public static void setSend(String send) {
        Mydata.send = send;
    }

    public static String getNearschool() {
        return nearschool;
    }

    public static void setNearschool(String nearschool) {
        Mydata.nearschool = nearschool;
    }

    public static String getNearschoolkr() {
        return nearschoolkr;
    }

    public static void setNearschoolkr(String nearschoolkr) {
        Mydata.nearschoolkr = nearschoolkr;
    }
}
