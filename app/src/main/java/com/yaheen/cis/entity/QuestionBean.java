package com.yaheen.cis.entity;

import java.util.List;

public class QuestionBean {


    /**
     * result : true
     * questionaireArr : [{"id":"2c928617645fc36201646325ed1400f0","name":"占用","describe":"占用"},{"id":"2c928617645fc3620164632611b400f1","name":"挖沙","describe":"挖沙"},{"id":"2c928617645fc36201646326532700f2","name":"采石","describe":"采石"},{"id":"2c928617645fc362016463267fd100f3","name":"采矿","describe":"采矿"},{"id":"2c928617645fc36201646326bd8100f4","name":"取土","describe":"取土"},{"id":"2c928617645fc36201646326e65100f5","name":"堆放","describe":"堆放"},{"id":"2c928617645fc36201646327536300f6","name":"建坟","describe":"建坟"},{"id":"2c928617645f5c5e01645f7b89320005","name":"违建","describe":"违建"},{"id":"2c92861764684da70164685ffa720020","name":"其他","describe":"其他"}]
     * code : 1004
     * msg : 查询成功
     */

    private boolean result;
    private int code;
    private String msg;
    private List<QuestionaireArrBean> questionaireArr;

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

    public List<QuestionaireArrBean> getQuestionaireArr() {
        return questionaireArr;
    }

    public void setQuestionaireArr(List<QuestionaireArrBean> questionaireArr) {
        this.questionaireArr = questionaireArr;
    }

    public static class QuestionaireArrBean {
        /**
         * id : 2c928617645fc36201646325ed1400f0
         * name : 占用
         * describe : 占用
         */

        private String id;
        private String name;
        private String describe;
        private boolean isSelected = false;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }
    }
}
