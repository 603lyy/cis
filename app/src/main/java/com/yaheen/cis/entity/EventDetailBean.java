package com.yaheen.cis.entity;

import java.util.List;

public class EventDetailBean {


    /**
     * result : true
     * code : 1004
     * tbEvent : {"type":"消防","describe":"好大火啊啊啊啊啊","emergency":"3","longitude":"113.40793","latitude":"23.131159","questionnaireArr":[{"questionnaire":"三级火灾"},{"questionnaire":"二级火灾"},{"questionnaire":"一级火灾"}],"fileArr":[{"imageUrl":"http://192.168.199.108:8080/crs/webFile/visit.do?id=402847f663f2f0870163f2f419a10004&showName=6e57dfa1e9fc48e090c738a2caa7d08c.jpg"},{"imageUrl":"http://192.168.199.108:8080/crs/webFile/visit.do?id=402847eb63d91c3a0163d91f05300000&showName=c9b482b4c0374b4aa5f8988b018e69c8.jpg"},{"imageUrl":"http://192.168.199.108:8080/crs/webFile/visit.do?id=402847f763f749e60163f7dd9fb70017&showName=a769d2f6b6744b8bb4b620d08c3e8e64.jpg"}]}
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
         * type : 消防
         * describe : 好大火啊啊啊啊啊
         * emergency : 3
         * longitude : 113.40793
         * latitude : 23.131159
         * questionnaireArr : [{"questionnaire":"三级火灾"},{"questionnaire":"二级火灾"},{"questionnaire":"一级火灾"}]
         * fileArr : [{"imageUrl":"http://192.168.199.108:8080/crs/webFile/visit.do?id=402847f663f2f0870163f2f419a10004&showName=6e57dfa1e9fc48e090c738a2caa7d08c.jpg"},{"imageUrl":"http://192.168.199.108:8080/crs/webFile/visit.do?id=402847eb63d91c3a0163d91f05300000&showName=c9b482b4c0374b4aa5f8988b018e69c8.jpg"},{"imageUrl":"http://192.168.199.108:8080/crs/webFile/visit.do?id=402847f763f749e60163f7dd9fb70017&showName=a769d2f6b6744b8bb4b620d08c3e8e64.jpg"}]
         */

        private String type;
        private String describe;
        private String emergency;
        private String longitude;
        private String latitude;
        private List<QuestionnaireArrBean> questionnaireArr;
        private List<FileArrBean> fileArr;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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
             * questionnaire : 三级火灾
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
             * imageUrl : http://192.168.199.108:8080/crs/webFile/visit.do?id=402847f663f2f0870163f2f419a10004&showName=6e57dfa1e9fc48e090c738a2caa7d08c.jpg
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
