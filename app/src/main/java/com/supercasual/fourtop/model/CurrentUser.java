package com.supercasual.fourtop.model;

public class CurrentUser {

    private static CurrentUser currentUser;

    private String userToken;
    private String userLogin;

    private CurrentUser() {
        userToken = "";
        userLogin = "";
    }

    public static synchronized CurrentUser get() {
        if (currentUser == null) {
            currentUser = new CurrentUser();
        }
        return currentUser;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }
}
