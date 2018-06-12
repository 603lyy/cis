package com.yaheen.cis.entity;

import java.util.List;

public class RecordBean {

    /**
     * result : true
     * recordArr : [{"id":"402847f663f35e1d0163f36ce65b0021","startTime":"2018-06-12 17:56:29","endTime":"2018-06-12 17:57:02"},{"id":"402847f663f35e1d0163f36b06a90019","startTime":"2018-06-12 17:54:27","endTime":"2018-06-12 17:55:31"},{"id":"402847f663f35e1d0163f366d6ab0011","startTime":"2018-06-12 17:49:52","endTime":"2018-06-12 17:50:00"},{"id":"402847f663f35e1d0163f35fcdfe0003","startTime":"2018-06-12 17:42:11","endTime":"2018-06-12 17:48:51"}]
     * code : 1004
     * msg : 查询成功
     */

    private boolean result;
    private int code;
    private String msg;
    private List<RecordArrBean> recordArr;

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

    public List<RecordArrBean> getRecordArr() {
        return recordArr;
    }

    public void setRecordArr(List<RecordArrBean> recordArr) {
        this.recordArr = recordArr;
    }

    public static class RecordArrBean {
        /**
         * id : 402847f663f35e1d0163f36ce65b0021
         * startTime : 2018-06-12 17:56:29
         * endTime : 2018-06-12 17:57:02
         */

        private String id;
        private String startTime;
        private String endTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }
}
