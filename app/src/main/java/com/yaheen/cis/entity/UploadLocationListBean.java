package com.yaheen.cis.entity;

import java.io.Serializable;
import java.util.List;

public class UploadLocationListBean implements Serializable {


    public List<LocationBean> getLocationArr() {
        return list;
    }

    public void setLocationArr(List<LocationBean> list) {
        this.list = list;
    }

    private List<LocationBean> list;

    public static class LocationBean {

        private String longitude;
        private String latitude;

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
    }

}
