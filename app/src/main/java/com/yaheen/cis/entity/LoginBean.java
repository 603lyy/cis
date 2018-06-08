package com.yaheen.cis.entity;

public class LoginBean {

    /**
     * result : true
     * token : CRSFF757064FA7A451189EA7459AD9963C7
     * expiresTime : 604800000
     */

    private boolean result;
    private String token;
    private String expiresTime;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(String expiresTime) {
        this.expiresTime = expiresTime;
    }
}
