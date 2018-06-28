package com.yaheen.cis.entity;

import java.util.List;

public class RecordEventBean {

    /**
     * result : true
     * eventList : [{"id":"40287e5e6436a2fa016436ae4225001d","time":"2018\r\n\r\n-06-25 19:22:27","longitude":115.672264,"latitude":23.304787,"emergency":"4","describe":"测试"}]
     * coordinateList : [{"longitude":115.672343,"latitude":23.304756}]
     * recordObject : {"startLongitude":115.672343,"startLatitude":23.304756,"endLongitude":115.672275,"endLatitude":23.304935}
     * code : 1004
     * msg : 成功
     */

    private boolean result;
    private RecordObjectBean recordObject;
    private int code;
    private String msg;
    private List<EventListBean> eventList;
    private List<CoordinateListBean> coordinateList;

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

    public List<CoordinateListBean> getCoordinateList() {
        return coordinateList;
    }

    public void setCoordinateList(List<CoordinateListBean> coordinateList) {
        this.coordinateList = coordinateList;
    }

    public static class RecordObjectBean {
        /**
         * startLongitude : 115.672343
         * startLatitude : 23.304756
         * endLongitude : 115.672275
         * endLatitude : 23.304935
         */

        private double startLongitude;
        private double startLatitude;
        private double endLongitude;
        private double endLatitude;

        public double getStartLongitude() {
            return startLongitude;
        }

        public void setStartLongitude(double startLongitude) {
            this.startLongitude = startLongitude;
        }

        public double getStartLatitude() {
            return startLatitude;
        }

        public void setStartLatitude(double startLatitude) {
            this.startLatitude = startLatitude;
        }

        public double getEndLongitude() {
            return endLongitude;
        }

        public void setEndLongitude(double endLongitude) {
            this.endLongitude = endLongitude;
        }

        public double getEndLatitude() {
            return endLatitude;
        }

        public void setEndLatitude(double endLatitude) {
            this.endLatitude = endLatitude;
        }
    }

    public static class EventListBean {
        /**
         * id : 40287e5e6436a2fa016436ae4225001d
         * time : 2018

         -06-25 19:22:27
         * longitude : 115.672264
         * latitude : 23.304787
         * emergency : 4
         * describe : 测试
         */

        private String id;
        private String time;
        private double longitude;
        private double latitude;
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
    }

    public static class CoordinateListBean {
        /**
         * longitude : 115.672343
         * latitude : 23.304756
         */

        private double longitude;
        private double latitude;

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
    }
}
