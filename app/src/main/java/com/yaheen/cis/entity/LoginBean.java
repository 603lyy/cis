package com.yaheen.cis.entity;

public class LoginBean {

    /**
     * result : true
     * token : CRSC9D9F2C382364793AF2C93A685538EA7
     * username : 林先生
     * mobile : 13428853826
     * expiresTime : 604800000
     */

    private boolean result;
    private String token;
    private String username;
    private String mobile;
    private String expiresTime;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(String expiresTime) {
        this.expiresTime = expiresTime;
    }
}
