package com.supercasual.fourtop.model;

public class User {

    private String login;
    private String pass;

    public User() {
        this.login = "";
        this.pass = "";
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
}
