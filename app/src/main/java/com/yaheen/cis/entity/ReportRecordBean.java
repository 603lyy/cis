package com.yaheen.cis.entity;

import java.util.List;

public class ReportRecordBean {

    /**
     * result : true
     * eventList : [{"id":"402847ec6483d893016483da133b000d","time":"2018-07-10 19:01:04","longitude":113.40462,"latitude":23.131184,"emergency":"1","describe":"我还是想提醒一下，确保你的域名前缀唯一，这里的方式有很多，比如你的qq或者你的手机号码，甚至你的微信号，这可以对你的项目起到一定的保护作用。","flag":"N"}]
     * code : 1004
     * msg : 成功
     */

    private boolean result;
    private int code;
    private String msg;
    private List<EventListBean> eventList;

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

    public List<EventListBean> getEventList() {
        return eventList;
    }

    public void setEventList(List<EventListBean> eventList) {
        this.eventList = eventList;
    }

    public static class EventListBean {
        /**
         * id : 402847ec6483d893016483da133b000d
         * time : 2018-07-10 19:01:04
         * longitude : 113.40462
         * latitude : 23.131184
         * emergency : 1
         * describe : 我还是想提醒一下，确保你的域名前缀唯一，这里的方式有很多，比如你的qq或者你的手机号码，甚至你的微信号，这可以对你的项目起到一定的保护作用。
         * flag : N
         */

        private String id;
        private String time;
        private double longitude;
        private double latitude;
        private String emergency;
        private String describe;
        private String flag;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getEmergency() {
            return emergency;
        }

        public void setEmergency(String emergency) {
            this.emergency = emergency;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }
    }
}
