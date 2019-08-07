package com.hcl.InstantPickup.models.login;

/** Used to handle login post request
 * @author HCL Intern Team
 * @version 1.0.0
 */
public class LoginPost {

    private String username;

    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public LoginPost(String username, String password){
        this.username = username;
        this.password = password;
    }
}
