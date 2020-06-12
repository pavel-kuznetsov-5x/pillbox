package com.cucumber007.pillbox.network;

public class UserData {
    public UserData() {

    }

    public UserData(int id, String detail, String token) {
        this.id = id;
        this.detail = detail;
        this.token = token;
    }

    int id;
    String detail;
    private String token;

    public String getDetail() {
        return detail;
    }

    public int getId() {
        return id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
