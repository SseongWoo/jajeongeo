package com.example.csappjava.models;

public class ChatNicknameModel {

        private String nickNames;             // 닉네임

        public ChatNicknameModel() { }

    public ChatNicknameModel(String nick){
            this.nickNames = nick;
    }
        public String getNickNames() { return nickNames; }

        public void setNickNames(String nickNames) { this.nickNames = nickNames; }

    @Override
    public String toString(){
            return nickNames;
    }
}

