package com.yaheen.cis.entity;

import java.util.List;

public class EventDetailBean {

    /**
     * result : true
     * code : 1004
     * tbEvent : {"modifiedUserName":"","createUserId":"","groupPropertyNameList":[],"deleteUnitId":"","emergency":"3","operatedDate":"","createUserName":"","operatedUnitCode":"","type":"","operatedUnitName":"","deleteFlag":false,"recordId":"","operateRelationMap":{},"operateRelation":{},"criterionAlias":"","modifiedUserCode":"","deleteUserCode":"","id":"402847f663f35e1d0163f3c17b690033","longModifiedDate":0,"longitude":"113.403244","area":"","operateValue":{},"longDeleteDate":0,"operatedName":"","qustionaires":[],"defCriterion":null,"createUnitCode":"","modifiedUnitCode":"","filterMap":{},"excludeCopyValueFieldNames":["createUnitCode","createUnitName","createUnitId","modifiedUnitCode","modifiedUnitName","modifiedUnitId","deleteUnitCode","deleteUnitName","deleteUnitId","longCreateDate","longModifiedDate","longDeleteDate","createName","createUserCode","modifiedName","modifiedUserCode","deleteName","deleteUserCode","createUserName","createUserId","modifiedUserName","modifiedUserId","deleteUserName","deleteUserId","createDate","modifiedDate","deleteDate","onlyCopyValueFieldNames","excludeCopyValueFieldNames","interrupt"],"modifiedDate":"","criteriaLogicExpression":"","typeId":"402847f26390db7b016390df8bd90001","createName":"","modifiedUnitId":"","status":"","createUnitId":"","modifiedName":"","createUnitName":"","flag":"","questionId":"402847f663f1ce600163f2ac2adf005c,402847f663f1ce600163f2abf7c4005b","operatedUserName":"","longCreateDate":0,"latitude":"23.131214","criterionOrders":[],"criteriaConfig":null,"modelDTOSearchOnField":false,"operatedUserId":"","modifiedUnitName":"","deleteUnitName":"","deleteUserId":"","createDate":"","deleteDate":"","createUserCode":"","operatedUnitId":"","dealRemaind":true,"longOperatedDate":0,"deleteUnitCode":"","modifiedUserId":"","operatedUserCode":"","associatedImageFileDTOs":[],"describe":"好大火啊啊啊啊啊","onlyCopyValueFieldNames":[],"deleteName":"","deleteUserName":""}
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
         * modifiedUserName :
         * createUserId :
         * groupPropertyNameList : []
         * deleteUnitId :
         * emergency : 3
         * operatedDate :
         * createUserName :
         * operatedUnitCode :
         * type :
         * operatedUnitName :
         * deleteFlag : false
         * recordId :
         * operateRelationMap : {}
         * operateRelation : {}
         * criterionAlias :
         * modifiedUserCode :
         * deleteUserCode :
         * id : 402847f663f35e1d0163f3c17b690033
         * longModifiedDate : 0
         * longitude : 113.403244
         * area :
         * operateValue : {}
         * longDeleteDate : 0
         * operatedName :
         * qustionaires : []
         * defCriterion : null
         * createUnitCode :
         * modifiedUnitCode :
         * filterMap : {}
         * excludeCopyValueFieldNames : ["createUnitCode","createUnitName","createUnitId","modifiedUnitCode","modifiedUnitName","modifiedUnitId","deleteUnitCode","deleteUnitName","deleteUnitId","longCreateDate","longModifiedDate","longDeleteDate","createName","createUserCode","modifiedName","modifiedUserCode","deleteName","deleteUserCode","createUserName","createUserId","modifiedUserName","modifiedUserId","deleteUserName","deleteUserId","createDate","modifiedDate","deleteDate","onlyCopyValueFieldNames","excludeCopyValueFieldNames","interrupt"]
         * modifiedDate :
         * criteriaLogicExpression :
         * typeId : 402847f26390db7b016390df8bd90001
         * createName :
         * modifiedUnitId :
         * status :
         * createUnitId :
         * modifiedName :
         * createUnitName :
         * flag :
         * questionId : 402847f663f1ce600163f2ac2adf005c,402847f663f1ce600163f2abf7c4005b
         * operatedUserName :
         * longCreateDate : 0
         * latitude : 23.131214
         * criterionOrders : []
         * criteriaConfig : null
         * modelDTOSearchOnField : false
         * operatedUserId :
         * modifiedUnitName :
         * deleteUnitName :
         * deleteUserId :
         * createDate :
         * deleteDate :
         * createUserCode :
         * operatedUnitId :
         * dealRemaind : true
         * longOperatedDate : 0
         * deleteUnitCode :
         * modifiedUserId :
         * operatedUserCode :
         * associatedImageFileDTOs : []
         * describe : 好大火啊啊啊啊啊
         * onlyCopyValueFieldNames : []
         * deleteName :
         * deleteUserName :
         */

        private String modifiedUserName;
        private String createUserId;
        private String deleteUnitId;
        private String emergency;
        private String operatedDate;
        private String createUserName;
        private String operatedUnitCode;
        private String type;
        private String operatedUnitName;
        private boolean deleteFlag;
        private String recordId;
        private OperateRelationMapBean operateRelationMap;
        private OperateRelationBean operateRelation;
        private String criterionAlias;
        private String modifiedUserCode;
        private String deleteUserCode;
        private String id;
        private int longModifiedDate;
        private String longitude;
        private String area;
        private OperateValueBean operateValue;
        private int longDeleteDate;
        private String operatedName;
        private Object defCriterion;
        private String createUnitCode;
        private String modifiedUnitCode;
        private FilterMapBean filterMap;
        private String modifiedDate;
        private String criteriaLogicExpression;
        private String typeId;
        private String createName;
        private String modifiedUnitId;
        private String status;
        private String createUnitId;
        private String modifiedName;
        private String createUnitName;
        private String flag;
        private String questionId;
        private String operatedUserName;
        private int longCreateDate;
        private String latitude;
        private Object criteriaConfig;
        private boolean modelDTOSearchOnField;
        private String operatedUserId;
        private String modifiedUnitName;
        private String deleteUnitName;
        private String deleteUserId;
        private String createDate;
        private String deleteDate;
        private String createUserCode;
        private String operatedUnitId;
        private boolean dealRemaind;
        private int longOperatedDate;
        private String deleteUnitCode;
        private String modifiedUserId;
        private String operatedUserCode;
        private String describe;
        private String deleteName;
        private String deleteUserName;
        private List<?> groupPropertyNameList;
        private List<?> qustionaires;
        private List<String> excludeCopyValueFieldNames;
        private List<?> criterionOrders;
        private List<?> associatedImageFileDTOs;
        private List<?> onlyCopyValueFieldNames;

        public String getModifiedUserName() {
            return modifiedUserName;
        }

        public void setModifiedUserName(String modifiedUserName) {
            this.modifiedUserName = modifiedUserName;
        }

        public String getCreateUserId() {
            return createUserId;
        }

        public void setCreateUserId(String createUserId) {
            this.createUserId = createUserId;
        }

        public String getDeleteUnitId() {
            return deleteUnitId;
        }

        public void setDeleteUnitId(String deleteUnitId) {
            this.deleteUnitId = deleteUnitId;
        }

        public String getEmergency() {
            return emergency;
        }

        public void setEmergency(String emergency) {
            this.emergency = emergency;
        }

        public String getOperatedDate() {
            return operatedDate;
        }

        public void setOperatedDate(String operatedDate) {
            this.operatedDate = operatedDate;
        }

        public String getCreateUserName() {
            return createUserName;
        }

        public void setCreateUserName(String createUserName) {
            this.createUserName = createUserName;
        }

        public String getOperatedUnitCode() {
            return operatedUnitCode;
        }

        public void setOperatedUnitCode(String operatedUnitCode) {
            this.operatedUnitCode = operatedUnitCode;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getOperatedUnitName() {
            return operatedUnitName;
        }

        public void setOperatedUnitName(String operatedUnitName) {
            this.operatedUnitName = operatedUnitName;
        }

        public boolean isDeleteFlag() {
            return deleteFlag;
        }

        public void setDeleteFlag(boolean deleteFlag) {
            this.deleteFlag = deleteFlag;
        }

        public String getRecordId() {
            return recordId;
        }

        public void setRecordId(String recordId) {
            this.recordId = recordId;
        }

        public OperateRelationMapBean getOperateRelationMap() {
            return operateRelationMap;
        }

        public void setOperateRelationMap(OperateRelationMapBean operateRelationMap) {
            this.operateRelationMap = operateRelationMap;
        }

        public OperateRelationBean getOperateRelation() {
            return operateRelation;
        }

        public void setOperateRelation(OperateRelationBean operateRelation) {
            this.operateRelation = operateRelation;
        }

        public String getCriterionAlias() {
            return criterionAlias;
        }

        public void setCriterionAlias(String criterionAlias) {
            this.criterionAlias = criterionAlias;
        }

        public String getModifiedUserCode() {
            return modifiedUserCode;
        }

        public void setModifiedUserCode(String modifiedUserCode) {
            this.modifiedUserCode = modifiedUserCode;
        }

        public String getDeleteUserCode() {
            return deleteUserCode;
        }

        public void setDeleteUserCode(String deleteUserCode) {
            this.deleteUserCode = deleteUserCode;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getLongModifiedDate() {
            return longModifiedDate;
        }

        public void setLongModifiedDate(int longModifiedDate) {
            this.longModifiedDate = longModifiedDate;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public OperateValueBean getOperateValue() {
            return operateValue;
        }

        public void setOperateValue(OperateValueBean operateValue) {
            this.operateValue = operateValue;
        }

        public int getLongDeleteDate() {
            return longDeleteDate;
        }

        public void setLongDeleteDate(int longDeleteDate) {
            this.longDeleteDate = longDeleteDate;
        }

        public String getOperatedName() {
            return operatedName;
        }

        public void setOperatedName(String operatedName) {
            this.operatedName = operatedName;
        }

        public Object getDefCriterion() {
            return defCriterion;
        }

        public void setDefCriterion(Object defCriterion) {
            this.defCriterion = defCriterion;
        }

        public String getCreateUnitCode() {
            return createUnitCode;
        }

        public void setCreateUnitCode(String createUnitCode) {
            this.createUnitCode = createUnitCode;
        }

        public String getModifiedUnitCode() {
            return modifiedUnitCode;
        }

        public void setModifiedUnitCode(String modifiedUnitCode) {
            this.modifiedUnitCode = modifiedUnitCode;
        }

        public FilterMapBean getFilterMap() {
            return filterMap;
        }

        public void setFilterMap(FilterMapBean filterMap) {
            this.filterMap = filterMap;
        }

        public String getModifiedDate() {
            return modifiedDate;
        }

        public void setModifiedDate(String modifiedDate) {
            this.modifiedDate = modifiedDate;
        }

        public String getCriteriaLogicExpression() {
            return criteriaLogicExpression;
        }

        public void setCriteriaLogicExpression(String criteriaLogicExpression) {
            this.criteriaLogicExpression = criteriaLogicExpression;
        }

        public String getTypeId() {
            return typeId;
        }

        public void setTypeId(String typeId) {
            this.typeId = typeId;
        }

        public String getCreateName() {
            return createName;
        }

        public void setCreateName(String createName) {
            this.createName = createName;
        }

        public String getModifiedUnitId() {
            return modifiedUnitId;
        }

        public void setModifiedUnitId(String modifiedUnitId) {
            this.modifiedUnitId = modifiedUnitId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreateUnitId() {
            return createUnitId;
        }

        public void setCreateUnitId(String createUnitId) {
            this.createUnitId = createUnitId;
        }

        public String getModifiedName() {
            return modifiedName;
        }

        public void setModifiedName(String modifiedName) {
            this.modifiedName = modifiedName;
        }

        public String getCreateUnitName() {
            return createUnitName;
        }

        public void setCreateUnitName(String createUnitName) {
            this.createUnitName = createUnitName;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getQuestionId() {
            return questionId;
        }

        public void setQuestionId(String questionId) {
            this.questionId = questionId;
        }

        public String getOperatedUserName() {
            return operatedUserName;
        }

        public void setOperatedUserName(String operatedUserName) {
            this.operatedUserName = operatedUserName;
        }

        public int getLongCreateDate() {
            return longCreateDate;
        }

        public void setLongCreateDate(int longCreateDate) {
            this.longCreateDate = longCreateDate;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public Object getCriteriaConfig() {
            return criteriaConfig;
        }

        public void setCriteriaConfig(Object criteriaConfig) {
            this.criteriaConfig = criteriaConfig;
        }

        public boolean isModelDTOSearchOnField() {
            return modelDTOSearchOnField;
        }

        public void setModelDTOSearchOnField(boolean modelDTOSearchOnField) {
            this.modelDTOSearchOnField = modelDTOSearchOnField;
        }

        public String getOperatedUserId() {
            return operatedUserId;
        }

        public void setOperatedUserId(String operatedUserId) {
            this.operatedUserId = operatedUserId;
        }

        public String getModifiedUnitName() {
            return modifiedUnitName;
        }

        public void setModifiedUnitName(String modifiedUnitName) {
            this.modifiedUnitName = modifiedUnitName;
        }

        public String getDeleteUnitName() {
            return deleteUnitName;
        }

        public void setDeleteUnitName(String deleteUnitName) {
            this.deleteUnitName = deleteUnitName;
        }

        public String getDeleteUserId() {
            return deleteUserId;
        }

        public void setDeleteUserId(String deleteUserId) {
            this.deleteUserId = deleteUserId;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getDeleteDate() {
            return deleteDate;
        }

        public void setDeleteDate(String deleteDate) {
            this.deleteDate = deleteDate;
        }

        public String getCreateUserCode() {
            return createUserCode;
        }

        public void setCreateUserCode(String createUserCode) {
            this.createUserCode = createUserCode;
        }

        public String getOperatedUnitId() {
            return operatedUnitId;
        }

        public void setOperatedUnitId(String operatedUnitId) {
            this.operatedUnitId = operatedUnitId;
        }

        public boolean isDealRemaind() {
            return dealRemaind;
        }

        public void setDealRemaind(boolean dealRemaind) {
            this.dealRemaind = dealRemaind;
        }

        public int getLongOperatedDate() {
            return longOperatedDate;
        }

        public void setLongOperatedDate(int longOperatedDate) {
            this.longOperatedDate = longOperatedDate;
        }

        public String getDeleteUnitCode() {
            return deleteUnitCode;
        }

        public void setDeleteUnitCode(String deleteUnitCode) {
            this.deleteUnitCode = deleteUnitCode;
        }

        public String getModifiedUserId() {
            return modifiedUserId;
        }

        public void setModifiedUserId(String modifiedUserId) {
            this.modifiedUserId = modifiedUserId;
        }

        public String getOperatedUserCode() {
            return operatedUserCode;
        }

        public void setOperatedUserCode(String operatedUserCode) {
            this.operatedUserCode = operatedUserCode;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public String getDeleteName() {
            return deleteName;
        }

        public void setDeleteName(String deleteName) {
            this.deleteName = deleteName;
        }

        public String getDeleteUserName() {
            return deleteUserName;
        }

        public void setDeleteUserName(String deleteUserName) {
            this.deleteUserName = deleteUserName;
        }

        public List<?> getGroupPropertyNameList() {
            return groupPropertyNameList;
        }

        public void setGroupPropertyNameList(List<?> groupPropertyNameList) {
            this.groupPropertyNameList = groupPropertyNameList;
        }

        public List<?> getQustionaires() {
            return qustionaires;
        }

        public void setQustionaires(List<?> qustionaires) {
            this.qustionaires = qustionaires;
        }

        public List<String> getExcludeCopyValueFieldNames() {
            return excludeCopyValueFieldNames;
        }

        public void setExcludeCopyValueFieldNames(List<String> excludeCopyValueFieldNames) {
            this.excludeCopyValueFieldNames = excludeCopyValueFieldNames;
        }

        public List<?> getCriterionOrders() {
            return criterionOrders;
        }

        public void setCriterionOrders(List<?> criterionOrders) {
            this.criterionOrders = criterionOrders;
        }

        public List<?> getAssociatedImageFileDTOs() {
            return associatedImageFileDTOs;
        }

        public void setAssociatedImageFileDTOs(List<?> associatedImageFileDTOs) {
            this.associatedImageFileDTOs = associatedImageFileDTOs;
        }

        public List<?> getOnlyCopyValueFieldNames() {
            return onlyCopyValueFieldNames;
        }

        public void setOnlyCopyValueFieldNames(List<?> onlyCopyValueFieldNames) {
            this.onlyCopyValueFieldNames = onlyCopyValueFieldNames;
        }

        public static class OperateRelationMapBean {
        }

        public static class OperateRelationBean {
        }

        public static class OperateValueBean {
        }

        public static class FilterMapBean {
        }
    }
}
