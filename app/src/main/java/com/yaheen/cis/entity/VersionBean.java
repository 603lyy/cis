package com.yaheen.cis.entity;

public class VersionBean {

    private int SHUICHUNversion;
    private String SHUICHUNurl;
    private int GUANGDONGversion;
    private String GUANGDONGurl;

    public int getVersion() {
        return GUANGDONGversion;
    }

    public void setVersion(int GUANGDONGversion) {
        this.GUANGDONGversion = GUANGDONGversion;
    }

    public String getUrl() {
        return GUANGDONGurl;
    }

    public void setUrl(String GUANGDONGurl) {
        this.GUANGDONGurl = GUANGDONGurl;
    }
}
