package com.yaheen.cis.entity;

public class LoginBean {

    /**
     * result : true
     * token : CRSE914B0169939437AA008D8868B98FCC7
     * username : 林先生
     * mobile : 13428853826
     * expiresTime : 604800000
     * role : PATROLLER
     */

    private boolean result;
    private String token;
    private String username;
    private String mobile;
    private String expiresTime;
    private String role;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
