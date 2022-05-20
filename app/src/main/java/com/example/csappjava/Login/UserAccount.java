package com.example.csappjava.Login;

/**
 *사용자 계정 정보 모델 클래스
 *
 */
public class UserAccount {

    private String idToken;               // Firebase UID 고유정보토큰
    private String emailID;              // 이메일 아이디
    private String password;             // 비밀번호
    public String nickNames;             // 닉네임
    public String profile;
    public UserAccount() { }

    public String getIdToken() { return idToken; }
    public void setIdToken(String idToken) { this.idToken = idToken; }

    public String getEmailID() { return emailID; }
    public void setEmailID(String emailID) { this.emailID = emailID; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNickNames() { return nickNames; }
    public void setNickNames(String nickNames) { this.nickNames = nickNames; }

    public String getprofile() { return profile; }
    public void setprofile(String profile) { this.profile = profile; }
}
