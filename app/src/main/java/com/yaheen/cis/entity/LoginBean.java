package com.yaheen.cis.entity;

import java.util.List;

public class LoginBean {

    /**
     * recordId : 402881ef6c03d4d2016c03d69ef5000a
     * recordStartTime:"2019-07-18 14:48:12",
     * typeArr : [{"id":"2c9252926bdba61f016bdedb22f2001e","name":"旱厕"},{"id":"2c9252926bdba61f016bded8b4090019","name":"环境卫生"},{"id":"2c9252926bdba61f016bded9e287001b","name":"房前屋后水沟"},{"id":"2c9252926bdba61f016bdedb6fcd0028","name":"围栏"},{"id":"2c9252926bdba61f016bdedc2e3f0029","name":"环境卫生保洁费1元/人/月"},{"id":"2c9286176bbff8ad016bc7907fea00c4","name":"\u201c四房\u201d拆除"},{"id":"2c9252926bdba61f016bded94859001a","name":"乱堆乱放"},{"id":"2c9252926bdba61f016bdeda749d001d","name":"禽畜养殖污染"}]
     * result : true
     * token : CRS0FE2EE1E0C39487D88DE47B8E6EAC611
     * username : 林先生
     * mobile : 13428853826
     * expiresTime : 86400000
     * role : PATROLLER
     */

    private String recordId;
    private boolean result;
    private int code;
    private String token;
    private String username;
    private String mobile;
    private String expiresTime;
    private String role;
    private String msg;
    private String unitName;

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    private String recordStartTime;
    private List<TypeArrBean> typeArr;

    public String getRecordStartTime() {
        return recordStartTime;
    }

    public void setRecordStartTime(String recordStartTime) {
        this.recordStartTime = recordStartTime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
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

    public List<TypeArrBean> getTypeArr() {
        return typeArr;
    }

    public void setTypeArr(List<TypeArrBean> typeArr) {
        this.typeArr = typeArr;
    }
}
