package com.example.csappjava.models;

public class UserProfile {
    public String profile;

    public UserProfile(){}

    public UserProfile(String profile) { this.profile = profile;}
    public String getprofile() { return profile; }

    public void setprofile(String profile) { this.profile = profile; }

    @Override
    public String toString(){
        return profile;
    }
}
