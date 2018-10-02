package com.businessanddecisions.models;

public class AuthToken {

    private String token;
    private String expirationDateAndTime;

    public AuthToken() {}

    public AuthToken(String token){
        this.token = token;
    }

    public AuthToken(String token,String exp) {
        this(token);
        this.expirationDateAndTime = exp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpirationDateAndTime() {
        return expirationDateAndTime;
    }

    public void setExpirationDateAndTime(String expirationDateAndTime) {
        this.expirationDateAndTime = expirationDateAndTime;
    }

}

