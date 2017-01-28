
package com.udacity.kavya.admissionbox.model;

public class Auth {
    private String isAuthenticated;

    private String email;

    public String getIsAuthenticated() {
        return isAuthenticated;
    }

    public void setIsAuthenticated(String isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ClassPojo [isAuthenticated = " + isAuthenticated + ", email = " + email + "]";
    }
}
