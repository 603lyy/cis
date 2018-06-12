package com.yaheen.cis.entity;

import java.io.Serializable;

public class ImgUploadBean implements Serializable {

    /**
     * result : true
     * code : 1004
     * fileId : 402847eb63d91c3a0163d91f05300000
     * originalFilename : 1528356071879.jpg
     * msg : 上传成功
     */

    private boolean result;
    private int code;
    private String fileId;
    private String originalFilename;
    private String msg;

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

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
