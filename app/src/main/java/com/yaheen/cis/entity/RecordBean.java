package com.yaheen.cis.entity;

import java.util.List;

public class RecordBean {


    /**
     * result : true
     * recordArr : [{"id":"402847ec63fd07d20163fd792bba003c","startTime":"2018-06-14 16:46:05","timeDiffrence":"08:00:07","typeArr":["国土","消防","禁毒"]},{"id":"402847ec63fd07d20163fd7743c40037","startTime":"2018-06-14 16:44:00","timeDiffrence":"08:01:53","typeArr":["国土","消防","禁毒"]},{"id":"402847ec63fc0b920163fc1224940004","startTime":"2018-06-14 10:13:56","timeDiffrence":"08:01:09","typeArr":["国土","消防","禁毒"]}]
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
         * id : 402847ec63fd07d20163fd792bba003c
         * startTime : 2018-06-14 16:46:05
         * timeDiffrence : 08:00:07
         * typeArr : ["国土","消防","禁毒"]
         */

        private String id;
        private String startTime;
        private String timeDiffrence;
        private List<String> typeArr;

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

        public String getTimeDiffrence() {
            return timeDiffrence;
        }

        public void setTimeDiffrence(String timeDiffrence) {
            this.timeDiffrence = timeDiffrence;
        }

        public List<String> getTypeArr() {
            return typeArr;
        }

        public void setTypeArr(List<String> typeArr) {
            this.typeArr = typeArr;
        }
    }
}
