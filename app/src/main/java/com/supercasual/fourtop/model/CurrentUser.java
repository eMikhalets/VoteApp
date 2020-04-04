package com.supercasual.fourtop.model;

public class CurrentUser {

    private static CurrentUser currentUser;

    private String token;
    private String login;
    private String pass;
    private String voteToken;

    private CurrentUser() {
        token = "";
        login = "";
        pass = "";
        voteToken = "";
    }

    public static synchronized CurrentUser get() {
        if (currentUser == null) {
            currentUser = new CurrentUser();
        }
        return currentUser;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getVoteToken() {
        return voteToken;
    }

    public void setVoteToken(String voteToken) {
        this.voteToken = voteToken;
    }
}
