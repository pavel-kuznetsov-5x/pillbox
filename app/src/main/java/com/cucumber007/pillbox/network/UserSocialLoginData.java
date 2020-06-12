package com.cucumber007.pillbox.network;

public class UserSocialLoginData {

    String accessToken;
    int socialType;
    boolean isSocial = true;
    int userRole = 0;

    public static final int TYPE_FACEBOOK = 0;
    public static final int TYPE_GOOGLE = 1;

    public UserSocialLoginData(String accessToken, int socialType) {
        this.accessToken = accessToken;
        this.socialType = socialType;
    }

}
