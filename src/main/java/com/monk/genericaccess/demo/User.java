package com.monk.genericaccess.demo;

import com.monk.genericaccess.annotations.Key;

public class User {
    @Key
    private String userId;
    private String userPw;
    public String pid;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPw() {
        return userPw;
    }

    public void setUserPw(String userPw) {
        this.userPw = userPw;
    }

    public String getPid() {
        return pid;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userPw='" + userPw + '\'' +
                ", pid='" + pid + '\'' +
                '}';
    }
}
