package com.yaheen.cis.entity;

import java.util.List;

public class RecordEventBean {

    /**
     * result : true
     * eventList : [{"id":"402847ec63fc0b920163fc131b8e0007","time":"2018-06-14 10:15:00","longitude":"113.40793","latitude":"23.131159","emergency":"3","describe":"好大火啊啊啊啊啊"},{"id":"402847ec63fc0b920163fc1299b80005","time":"2018-06-14 10:14:27","longitude":"113.40473","latitude":"23.131159","emergency":"2","describe":"好大火啊啊啊啊啊"}]
     * recordObject : {"startLongitude":"113.404734","startLatitude":"23.131158","endLongitude":"113.404734","endLatitude":"23.131158"}
     * code : 1004
     * msg : 成功
     */

    private boolean result;
    private RecordObjectBean recordObject;
    private int code;
    private String msg;
    private List<EventListBean> eventList;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public RecordObjectBean getRecordObject() {
        return recordObject;
    }

    public void setRecordObject(RecordObjectBean recordObject) {
        this.recordObject = recordObject;
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

    public static class RecordObjectBean {
        /**
         * startLongitude : 113.404734
         * startLatitude : 23.131158
         * endLongitude : 113.404734
         * endLatitude : 23.131158
         */

        private String startLongitude;
        private String startLatitude;
        private String endLongitude;
        private String endLatitude;

        public String getStartLongitude() {
            return startLongitude;
        }

        public void setStartLongitude(String startLongitude) {
            this.startLongitude = startLongitude;
        }

        public String getStartLatitude() {
            return startLatitude;
        }

        public void setStartLatitude(String startLatitude) {
            this.startLatitude = startLatitude;
        }

        public String getEndLongitude() {
            return endLongitude;
        }

        public void setEndLongitude(String endLongitude) {
            this.endLongitude = endLongitude;
        }

        public String getEndLatitude() {
            return endLatitude;
        }

        public void setEndLatitude(String endLatitude) {
            this.endLatitude = endLatitude;
        }
    }

    public static class EventListBean {
        /**
         * id : 402847ec63fc0b920163fc131b8e0007
         * time : 2018-06-14 10:15:00
         * longitude : 113.40793
         * latitude : 23.131159
         * emergency : 3
         * describe : 好大火啊啊啊啊啊
         */

        private String id;
        private String time;
        private String longitude;
        private String latitude;
        private String emergency;
        private String describe;

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

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
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
    }
}
