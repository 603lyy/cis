package com.yaheen.cis.entity;

import java.util.List;

public class EventDetailBean {


    /**
     * result : true
     * code : 1004
     * tbEvent : {"type":"环境卫生","username":"1号巡查员","describe":"正常","emergency":"2","longitude":113.40693,"latitude":23.128172,"houseId":"2c9252926ad2ea6b016ad3d7c5040005","questionnaireArr":[{"questionnaire":"其他"}],"fileArr":[{"imageUrl":"http://47.106.72.58:10080/webFile/visit.do?id=2c9286176bd5e9eb016bd60655b90022&showName=afe7643850d44aa5a981eee4a27a5d4a.jpg"}]}
     * msg : 查询成功
     */

    private boolean result;
    private int code;
    private TbEventBean tbEvent;
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

    public TbEventBean getTbEvent() {
        return tbEvent;
    }

    public void setTbEvent(TbEventBean tbEvent) {
        this.tbEvent = tbEvent;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class TbEventBean {
        /**
         * type : 环境卫生
         * username : 1号巡查员
         * describe : 正常
         * emergency : 2
         * longitude : 113.40693
         * latitude : 23.128172
         * houseId : 2c9252926ad2ea6b016ad3d7c5040005
         * questionnaireArr : [{"questionnaire":"其他"}]
         * fileArr : [{"imageUrl":"http://47.106.72.58:10080/webFile/visit.do?id=2c9286176bd5e9eb016bd60655b90022&showName=afe7643850d44aa5a981eee4a27a5d4a.jpg"}]
         */

        private String type;
        private String username;
        private String describe;
        private String emergency;
        private double longitude;
        private double latitude;
        private String houseId;
        private List<QuestionnaireArrBean> questionnaireArr;
        private List<FileArrBean> fileArr;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public String getEmergency() {
            return emergency;
        }

        public void setEmergency(String emergency) {
            this.emergency = emergency;
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

        public String getHouseId() {
            return houseId;
        }

        public void setHouseId(String houseId) {
            this.houseId = houseId;
        }

        public List<QuestionnaireArrBean> getQuestionnaireArr() {
            return questionnaireArr;
        }

        public void setQuestionnaireArr(List<QuestionnaireArrBean> questionnaireArr) {
            this.questionnaireArr = questionnaireArr;
        }

        public List<FileArrBean> getFileArr() {
            return fileArr;
        }

        public void setFileArr(List<FileArrBean> fileArr) {
            this.fileArr = fileArr;
        }

        public static class QuestionnaireArrBean {
            /**
             * questionnaire : 其他
             */

            private String questionnaire;

            public String getQuestionnaire() {
                return questionnaire;
            }

            public void setQuestionnaire(String questionnaire) {
                this.questionnaire = questionnaire;
            }
        }

        public static class FileArrBean {
            /**
             * imageUrl : http://47.106.72.58:10080/webFile/visit.do?id=2c9286176bd5e9eb016bd60655b90022&showName=afe7643850d44aa5a981eee4a27a5d4a.jpg
             */

            private String imageUrl;

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }
        }
    }
}
