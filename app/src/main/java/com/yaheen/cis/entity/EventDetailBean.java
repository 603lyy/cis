package com.yaheen.cis.entity;

import java.util.List;

public class EventDetailBean {


    /**
     * detailsList : [{"eventId":"2c91808c750afa0b01751060ec1c0a7b","operationDate":"2020-10-10 10:39:48","flag":"N","operationUserId":"2c91808274dcc9050174dd3abd570043","designateSubUserId":"","imageUrls":[],"stauts":"","describe":"节后安全生产巡查","represent":"上报:张建光"}]
     * stamp : YES
     * reporting : YES
     * result : true
     * code : 1004
     * tbEvent : {"type":"应急管理","username":"张建光","describe":"节后安全生产巡查","emergency":"2","longitude":113.2113,"latitude":29.378439,"questionnaireArr":[{"questionnaire":"自行描述"}],"fileArr":[{"imageUrl":"http://jfq.zl.yafrm.com/webFile/visit.do?id=2c91808c750afa0b01751060ea800a78&showName=f640d0de7eea48a182f2715cbf0892cf.jpg"},{"imageUrl":"http://jfq.zl.yafrm.com/webFile/visit.do?id=2c91808c750afa0b01751060eb910a79&showName=cc884453d22942cb877beee59265ccc9.jpg"}]}
     * msg : 查询成功
     */

    private String stamp;
    private String reporting;
    private boolean result;
    private int code;
    private TbEventBean tbEvent;
    private String msg;
    private List<DetailsListBean> detailsList;

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public String getReporting() {
        return reporting;
    }

    public void setReporting(String reporting) {
        this.reporting = reporting;
    }

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

    public List<DetailsListBean> getDetailsList() {
        return detailsList;
    }

    public void setDetailsList(List<DetailsListBean> detailsList) {
        this.detailsList = detailsList;
    }

    public static class TbEventBean {
        /**
         * type : 应急管理
         * username : 张建光
         * describe : 节后安全生产巡查
         * emergency : 2
         * longitude : 113.2113
         * latitude : 29.378439
         * questionnaireArr : [{"questionnaire":"自行描述"}]
         * fileArr : [{"imageUrl":"http://jfq.zl.yafrm.com/webFile/visit.do?id=2c91808c750afa0b01751060ea800a78&showName=f640d0de7eea48a182f2715cbf0892cf.jpg"},{"imageUrl":"http://jfq.zl.yafrm.com/webFile/visit.do?id=2c91808c750afa0b01751060eb910a79&showName=cc884453d22942cb877beee59265ccc9.jpg"}]
         */

        private String type;
        private String username;
        private String describe;
        private String emergency;
        private double longitude;
        private double latitude;
        private List<QuestionnaireArrBean> questionnaireArr;
        private List<FileArrBean> fileArr;

        private String fireOfficer;
        private String businessHours;
        private String scopeOfOperation;
        private String householdName;
        private String householdPhone;
        private String inspectionSite;
        private String responsiblePerson;
        private String houseId;

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

        public String getResponsiblePerson() {
            return responsiblePerson;
        }

        public void setResponsiblePerson(String responsiblePerson) {
            this.responsiblePerson = responsiblePerson;
        }

        public String getHouseId() {
            return houseId;
        }

        public void setHouseId(String houseId) {
            this.houseId = houseId;
        }

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
             * questionnaire : 自行描述
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
             * imageUrl : http://jfq.zl.yafrm.com/webFile/visit.do?id=2c91808c750afa0b01751060ea800a78&showName=f640d0de7eea48a182f2715cbf0892cf.jpg
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

    public static class DetailsListBean {
        /**
         * eventId : 2c91808c750afa0b01751060ec1c0a7b
         * operationDate : 2020-10-10 10:39:48
         * flag : N
         * operationUserId : 2c91808274dcc9050174dd3abd570043
         * designateSubUserId :
         * imageUrls : []
         * stauts :
         * describe : 节后安全生产巡查
         * represent : 上报:张建光
         */

        private String eventId;
        private String operationDate;
        private String flag;
        private String operationUserId;
        private String designateSubUserId;
        private String stauts;
        private String describe;
        private String represent;
        private List<String> imageUrls;

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }

        public String getOperationDate() {
            return operationDate;
        }

        public void setOperationDate(String operationDate) {
            this.operationDate = operationDate;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getOperationUserId() {
            return operationUserId;
        }

        public void setOperationUserId(String operationUserId) {
            this.operationUserId = operationUserId;
        }

        public String getDesignateSubUserId() {
            return designateSubUserId;
        }

        public void setDesignateSubUserId(String designateSubUserId) {
            this.designateSubUserId = designateSubUserId;
        }

        public String getStauts() {
            return stauts;
        }

        public void setStauts(String stauts) {
            this.stauts = stauts;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public String getRepresent() {
            return represent;
        }

        public void setRepresent(String represent) {
            this.represent = represent;
        }

        public List<?> getImageUrls() {
            return imageUrls;
        }

        public void setImageUrls(List<String> imageUrls) {
            this.imageUrls = imageUrls;
        }
    }
}
