package com.supercasual.fourtop.network;

public class CurrentUser {

    private static CurrentUser user;
    private static String userToken;
    private static String login;

    private CurrentUser(String userToken) {
        CurrentUser.userToken = userToken;
    }

    public static CurrentUser getUser() {
        if (user == null) {
            return new CurrentUser("");
        }
        return user;
    }

    public static String getUserToken() {
        return userToken;
    }

    public static void setUserToken(String userToken) {
        CurrentUser.userToken = userToken;
    }

    public static String getLogin() {
        return login;
    }

    public static void setLogin(String login) {
        CurrentUser.login = login;
    }
}
