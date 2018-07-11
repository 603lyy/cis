package com.yaheen.cis.entity;

import java.util.List;

public class EventDetailBean {

    /**
     * result : true
     * code : 1004
     * tbEvent : {"type":"消防","describe":"正餐","emergency":"4","longitude":113.40465,"latitude":23.131227,"fireOfficer":"lyy","businessHours":"08:00\u2014\u201418:00","scopeOfOperation":"农家乐特产","householdName":"大众农家乐","householdPhone":"13929327812","inspectionSite":"水唇镇螺洞村大众农家乐","questionnaireArr":[{"questionnaire":"在岗人员"}],"fileArr":[{"imageUrl":"http://lyy.tunnel.echomod.cn/crs/webFile/visit.do?id=402847ec648750150164875bd4cd0005&showName=6dbb8ce42fc34b7a8fb4e42bf3b2c11d.jpg"}]}
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
         * describe : 正餐
         * emergency : 4
         * longitude : 113.40465
         * latitude : 23.131227
         * fireOfficer : lyy
         * businessHours : 08:00——18:00
         * scopeOfOperation : 农家乐特产
         * householdName : 大众农家乐
         * householdPhone : 13929327812
         * inspectionSite : 水唇镇螺洞村大众农家乐
         * questionnaireArr : [{"questionnaire":"在岗人员"}]
         * fileArr : [{"imageUrl":"http://lyy.tunnel.echomod.cn/crs/webFile/visit.do?id=402847ec648750150164875bd4cd0005&showName=6dbb8ce42fc34b7a8fb4e42bf3b2c11d.jpg"}]
         */

        private String type;
        private String describe;
        private String emergency;
        private double longitude;
        private double latitude;
        private String fireOfficer;
        private String businessHours;
        private String scopeOfOperation;
        private String householdName;
        private String householdPhone;
        private String inspectionSite;
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

        public String getFireOfficer() {
            return fireOfficer;
        }

        public void setFireOfficer(String fireOfficer) {
            this.fireOfficer = fireOfficer;
        }

        public String getBusinessHours() {
            return businessHours;
        }

        public void setBusinessHours(String businessHours) {
            this.businessHours = businessHours;
        }

        public String getScopeOfOperation() {
            return scopeOfOperation;
        }

        public void setScopeOfOperation(String scopeOfOperation) {
            this.scopeOfOperation = scopeOfOperation;
        }

        public String getHouseholdName() {
            return householdName;
        }

        public void setHouseholdName(String householdName) {
            this.householdName = householdName;
        }

        public String getHouseholdPhone() {
            return householdPhone;
        }

        public void setHouseholdPhone(String householdPhone) {
            this.householdPhone = householdPhone;
        }

        public String getInspectionSite() {
            return inspectionSite;
        }

        public void setInspectionSite(String inspectionSite) {
            this.inspectionSite = inspectionSite;
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
             * questionnaire : 在岗人员
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
             * imageUrl : http://lyy.tunnel.echomod.cn/crs/webFile/visit.do?id=402847ec648750150164875bd4cd0005&showName=6dbb8ce42fc34b7a8fb4e42bf3b2c11d.jpg
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
