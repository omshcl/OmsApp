package com.hcl.omsapplication.models.login;

public class loginPost {

    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public loginPost(String username, String password){
        this.username = username;
        this.password = password;
    }
}
