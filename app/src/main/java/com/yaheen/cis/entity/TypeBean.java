package com.yaheen.cis.entity;

import java.io.Serializable;
import java.util.List;

public class TypeBean implements Serializable {

    /**
     * result : true
     * typeArr : [{"id":"402847f26390cebb016390d3a8db0001","name":"国土"},{"id":"402847f26390db7b016390df8bd90001","name":"消防"},{"id":"402847f26390db7b016390dfb3dd0002","name":"禁毒"}]
     * code : 1004
     * msg : 查询成功
     */

    private boolean result;
    private int code;
    private String msg;
    private String recordId = "";
    private String recordStartTime;
    private List<TypeArrBean> typeArr;

    public String getRecordStartTime() {
        return recordStartTime;
    }

    public void setRecordStartTime(String recordStartTime) {
        this.recordStartTime = recordStartTime;
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

    public List<TypeArrBean> getTypeArr() {
        return typeArr;
    }

    public void setTypeArr(List<TypeArrBean> typeArr) {
        this.typeArr = typeArr;
    }
}
