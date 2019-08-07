package com.hcl.InstantPickup.models.login;

/** Used to handle login post request
 * return value
 * @author HCL Intern Team
 * @version 1.0.0
 */
public class LoginStatus {
    public boolean isValid;

    public boolean isAdmin;

    public LoginStatus(Boolean isValid, Boolean isAdmin){
        this.isValid = isValid;
        this.isAdmin = isAdmin;
    }
}
