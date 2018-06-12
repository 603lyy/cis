package com.yaheen.cis.entity;

import java.util.List;

public class RecordEventBean {

    /**
     * result : true
     * eventList : [{"id":"402847f663f35e1d0163f3c17b690033","longitude":"113.403244","latitude":"23.131214","emergency":"1"}]
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
         * id : 402847f663f35e1d0163f3c17b690033
         * longitude : 113.403244
         * latitude : 23.131214
         * emergency : 1
         */

        private String id;
        private String longitude;
        private String latitude;
        private String emergency;

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
    }
}
