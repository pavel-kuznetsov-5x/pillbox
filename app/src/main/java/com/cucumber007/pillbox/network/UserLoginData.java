package com.cucumber007.pillbox.network;

public class UserLoginData {

    String email = "cucumber007@mail.ru";
    String password = "007007";
    int userRole = 0;
    String fullName = "Test Name";

    public UserLoginData() {
    }

    public UserLoginData(String email, String password) {
        this.email = email;
        this.password = password;
        this.userRole = 0;
    }

    public UserLoginData(String email, String password, int userRole) {
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    public UserLoginData(String email, String password, int userRole, String fullName) {
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.fullName = fullName;
    }
}
