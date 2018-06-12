package com.yaheen.cis.entity;

public class ReportBean {

    /**
     * result : true
     * eventId : 402847f663f300e50163f30276aa0003
     * code : 1004
     * msg : 上报成功
     */

    private boolean result;
    private String eventId;
    private int code;
    private String msg;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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
