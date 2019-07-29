package com.hcl.omsapplication.models.login;

public class loginStatus {
    public boolean isValid;

    public boolean isAdmin;

    public loginStatus(Boolean isValid, Boolean isAdmin){
        this.isValid = isValid;
        this.isAdmin = isAdmin;
    }
}
