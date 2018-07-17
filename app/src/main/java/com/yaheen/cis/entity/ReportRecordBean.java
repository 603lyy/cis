package com.yaheen.cis.entity;

import java.util.List;

public class ReportRecordBean {

    /**
     * result : true
     * eventList : [{"id":"4028fde6648c39c801648c687bcf0032","time":"2018-07-12 10:53:35","longitude":113.40469,"latitude":23.131157,"emergency":"2","describe":"正常","flag":"N","username":"林雅烨"},{"id":"402847ec648825c8016488423e1e0007","time":"2018-07-11 15:33:20","longitude":113.40465,"latitude":23.131239,"emergency":"3","describe":"正常","flag":"Y","username":"林雅烨"},{"id":"402847ec648825c80164883ff1ba0003","time":"2018-07-11 15:30:49","longitude":113.40463,"latitude":23.1312,"emergency":"2","describe":"正常","flag":"Y","username":"林雅烨"},{"id":"402847ec6486ff9e0164872db089000c","time":"2018-07-11 10:31:15","longitude":113.404655,"latitude":23.13115,"emergency":"4","describe":"有问题","flag":"Y","username":"林雅烨"},{"id":"402847ec6486ff9e0164872d8703000a","time":"2018-07-11 10:31:05","longitude":113.404655,"latitude":23.13115,"emergency":"2","describe":"正常","flag":"Y","username":"林雅烨"}]
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
         * id : 4028fde6648c39c801648c687bcf0032
         * time : 2018-07-12 10:53:35
         * longitude : 113.40469
         * latitude : 23.131157
         * emergency : 2
         * describe : 正常
         * flag : N
         * username : 林雅烨
         */

        private String id;
        private String time;
        private double longitude;
        private double latitude;
        private String emergency;
        private String describe;
        private String flag;
        private String username;

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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
