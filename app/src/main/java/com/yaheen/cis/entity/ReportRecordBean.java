package com.yaheen.cis.entity;

import java.util.List;

public class ReportRecordBean {


    /**
     * result : true
     * eventList : [{"id":"2c91808c750afa0b01751060ec1c0a7b","time":"2020-10-10 10:39:48","longitude":113.2113,"latitude":29.378439,"emergency":"2","describe":"节后安全生产巡查","flag":"N","status":"PATROLLER","signFlag":"Y","designateFlag":"N","username":"张建光","detailFlag":"report"},{"id":"2c91808c750afa0b0175104f4ef70a70","time":"2020-10-10 10:20:34","longitude":113.19868,"latitude":29.395935,"emergency":"2","describe":"节后安全生产巡查","flag":"N","status":"PATROLLER","signFlag":"Y","designateFlag":"N","username":"张建光","detailFlag":"report"},{"id":"2c91808c750afa0b01751046feed0a5c","time":"2020-10-10 10:11:29","longitude":113.17909,"latitude":29.387669,"emergency":"1","describe":"节后安全生产企业巡查","flag":"N","status":"PATROLLER","signFlag":"Y","designateFlag":"N","username":"易立","detailFlag":"report"},{"id":"2c91808c750afa0b0175103f12d70a46","time":"2020-10-10 10:02:50","longitude":113.18104,"latitude":29.386002,"emergency":"1","describe":"节后安全生产企业巡查","flag":"N","status":"PATROLLER","signFlag":"Y","designateFlag":"N","username":"易立","detailFlag":"report"},{"id":"2c91808c750afa0b0175103efcbe0a39","time":"2020-10-10 10:02:44","longitude":113.19321,"latitude":29.38581,"emergency":"2","describe":"无隐患","flag":"N","status":"PATROLLER","signFlag":"Y","designateFlag":"N","username":"张建光","detailFlag":"report"},{"id":"2c91808c750afa0b017510254b5a0986","time":"2020-10-10 09:34:40","longitude":113.182594,"latitude":29.396433,"emergency":"1","describe":"节后安全生产企业巡查","flag":"N","status":"PATROLLER","signFlag":"Y","designateFlag":"N","username":"易立","detailFlag":"report"},{"id":"2c91808c750afa0b01751022116d0977","time":"2020-10-10 09:31:09","longitude":113.181435,"latitude":29.398863,"emergency":"1","describe":"节后安全生产企业巡查","flag":"N","status":"PATROLLER","signFlag":"Y","designateFlag":"N","username":"易立","detailFlag":"report"},{"id":"2c91808c750afa0b0175101fdd110966","time":"2020-10-10 09:28:45","longitude":113.18062,"latitude":29.399044,"emergency":"1","describe":"节后安全生产企业巡查","flag":"N","status":"PATROLLER","signFlag":"Y","designateFlag":"N","username":"易立","detailFlag":"report"}]
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
         * id : 2c91808c750afa0b01751060ec1c0a7b
         * time : 2020-10-10 10:39:48
         * longitude : 113.2113
         * latitude : 29.378439
         * emergency : 2
         * describe : 节后安全生产巡查
         * flag : N
         * status : PATROLLER
         * signFlag : Y
         * designateFlag : N
         * username : 张建光
         * detailFlag : report
         */

        private String id;
        private String time;
        private double longitude;
        private double latitude;
        private String emergency;
        private String describe;
        private String flag;
        private String status;
        private String signFlag;
        private String designateFlag;
        private String username;
        private String detailFlag;

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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSignFlag() {
            return signFlag;
        }

        public void setSignFlag(String signFlag) {
            this.signFlag = signFlag;
        }

        public String getDesignateFlag() {
            return designateFlag;
        }

        public void setDesignateFlag(String designateFlag) {
            this.designateFlag = designateFlag;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getDetailFlag() {
            return detailFlag;
        }

        public void setDetailFlag(String detailFlag) {
            this.detailFlag = detailFlag;
        }
    }
}
