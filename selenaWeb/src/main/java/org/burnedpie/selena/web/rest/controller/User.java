package org.burnedpie.selena.web.rest.controller;

/**
 * Created by jibe on 08/09/16.
 */
public class User {

    private String password;

    public User() {

    }

    public User(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
