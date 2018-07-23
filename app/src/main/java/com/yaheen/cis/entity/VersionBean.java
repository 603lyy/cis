package com.yaheen.cis.entity;

public class VersionBean {

    /**
     * SHUICHUNversion : 1011
     * SHUICHUNurl : https://www.yaheen.com/app/zongzhi.apk
     */

//    private int SHUICHUNversion;
//    private String SHUICHUNurl;

    private int HEKOUversion;
    private String HEKOUurl;

    public int getVersion() {
        return HEKOUversion;
    }

    public void setVersion(int HEKOUversion) {
        this.HEKOUversion = HEKOUversion;
    }

    public String getUrl() {
        return HEKOUurl;
    }

    public void setUrl(String HEKOUurl) {
        this.HEKOUurl = HEKOUurl;
    }
}
