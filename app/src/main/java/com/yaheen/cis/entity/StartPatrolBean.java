package com.yaheen.cis.entity;

public class StartPatrolBean {

    /**
     * result : true
     * recordId : 402847ef63de3ef30163de40c01f0002
     * code : 1004
     * msg : 成功
     */

    private boolean result;
    private String recordId;
    private int code;
    private String msg;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
