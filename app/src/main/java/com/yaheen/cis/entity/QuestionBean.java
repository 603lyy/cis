package com.yaheen.cis.entity;

import java.util.List;

public class QuestionBean {

    /**
     * result : true
     * typeArr : [{"id":"402847f96395431901639547b2530001","name":"一级火灾","describe":"一级火灾"}]
     * code : 1004
     * msg : 查询成功
     */

    private boolean result;
    private int code;
    private String msg;
    private List<TypeArrBean> typeArr;

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

    public static class TypeArrBean {
        /**
         * id : 402847f96395431901639547b2530001
         * name : 一级火灾
         * describe : 一级火灾
         */

        private String id;
        private String name;
        private String describe;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }
    }
}