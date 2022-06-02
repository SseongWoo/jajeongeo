package com.example.csappjava.Marketplace;

public class LessonAccount {

    private String 강의;
    private String 교수;
    private String 본분교명;
    private String 학과명;

    public LessonAccount() {
    }

    public String get강의() {
        return 강의;
    }

    public void set강의(String 강의) {
        this.강의 = 강의;
    }

    public String get교수() {
        return 교수;
    }

    public void set교수(String 교수) {
        this.교수 = 교수;
    }

    public String get본분교명() {
        return 본분교명;
    }

    public void set본분교명(String 본분교명) {
        this.본분교명 = 본분교명;
    }

    public String get학과명() {
        return 학과명;
    }

    public void set학과명(String 학과명) {
        this.학과명 = 학과명;
    }
}
