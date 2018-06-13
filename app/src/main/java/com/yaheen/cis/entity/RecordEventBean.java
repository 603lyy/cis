package com.yaheen.cis.entity;

import java.util.List;

public class RecordEventBean {

    /**
     * result : true
     * eventList : [{"id":"402847f663f35e1d0163f3c17b690033","longitude":"113.403244","latitude":"23.131214","emergency":"3","describe":"好大火啊啊啊啊啊"}]
     * recordObject : {"startLongitude":"113.404437","startLatitude":"23.131203","endLongitude":"113.403246","endLatitude":"23.131215"}
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
         * startLongitude : 113.404437
         * startLatitude : 23.131203
         * endLongitude : 113.403246
         * endLatitude : 23.131215
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
         * id : 402847f663f35e1d0163f3c17b690033
         * longitude : 113.403244
         * latitude : 23.131214
         * emergency : 3
         * describe : 好大火啊啊啊啊啊
         */

        private String id;
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
