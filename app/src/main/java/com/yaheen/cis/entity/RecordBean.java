package com.yaheen.cis.entity;

import java.util.List;

public class RecordBean {


    /**
     * result : true
     * recordArr : [{"typeArr":[{"id":"2c928617645fc362016463d5fbc5012e","name":"环境卫生","serialNumber":1,"disable":"0","link":"https://blog.csdn.net/fakerswe/article/details/79012978"},{"id":"2c928617645fc362016463d61f40012f","name":"环保","serialNumber":2,"disable":"0"},{"id":"402847f26390cebb016390d3a8db0001","name":"禁毒","serialNumber":3,"disable":"0"},{"id":"402847f26390db7b016390df8bd90001","name":"消防","serialNumber":4,"disable":"0"},{"id":"402847f26390db7b016390dfb3dd0002","name":"\u201c两违\u201d行为","serialNumber":5,"disable":"0"},{"id":"2c92861767c1bc170167c1c0dcd50002","name":"其他","serialNumber":6,"disable":"0"},{"id":"2c9286176d3dda29016d3ddf57be0002","name":"三进三同","serialNumber":7,"disable":"0","link":"https://temporary.zl.yafrm.com/contact/user/contact.html"}],"id":"2c9286176d3debd7016d707d5955051d","startTime":"2019-09-27 10:12:06","timeDiffrence":"00:00:29"},{"typeArr":[{"id":"2c928617645fc362016463d5fbc5012e","name":"环境卫生","serialNumber":1,"disable":"0","link":"https://blog.csdn.net/fakerswe/article/details/79012978"},{"id":"2c928617645fc362016463d61f40012f","name":"环保","serialNumber":2,"disable":"0"},{"id":"402847f26390cebb016390d3a8db0001","name":"禁毒","serialNumber":3,"disable":"0"},{"id":"402847f26390db7b016390df8bd90001","name":"消防","serialNumber":4,"disable":"0"},{"id":"402847f26390db7b016390dfb3dd0002","name":"\u201c两违\u201d行为","serialNumber":5,"disable":"0"},{"id":"2c92861767c1bc170167c1c0dcd50002","name":"其他","serialNumber":6,"disable":"0"},{"id":"2c9286176d3dda29016d3ddf57be0002","name":"三进三同","serialNumber":7,"disable":"0","link":"https://temporary.zl.yafrm.com/contact/user/contact.html"}],"id":"2c9286176d3debd7016d6d0daf34048a","startTime":"2019-09-26 18:11:16","timeDiffrence":"00:00:04"},{"typeArr":[{"id":"2c928617645fc362016463d5fbc5012e","name":"环境卫生","serialNumber":1,"disable":"0","link":"https://blog.csdn.net/fakerswe/article/details/79012978"},{"id":"2c928617645fc362016463d61f40012f","name":"环保","serialNumber":2,"disable":"0"},{"id":"402847f26390cebb016390d3a8db0001","name":"禁毒","serialNumber":3,"disable":"0"},{"id":"402847f26390db7b016390df8bd90001","name":"消防","serialNumber":4,"disable":"0"},{"id":"402847f26390db7b016390dfb3dd0002","name":"\u201c两违\u201d行为","serialNumber":5,"disable":"0"},{"id":"2c92861767c1bc170167c1c0dcd50002","name":"其他","serialNumber":6,"disable":"0"},{"id":"2c9286176d3dda29016d3ddf57be0002","name":"三进三同","serialNumber":7,"disable":"0","link":"https://temporary.zl.yafrm.com/contact/user/contact.html"}],"id":"2c9286176d3debd7016d48ab5e4e016e","startTime":"2019-09-19 16:37:33","timeDiffrence":"00:30:55"},{"typeArr":[{"id":"2c928617645fc362016463d5fbc5012e","name":"环境卫生","serialNumber":1,"disable":"0","link":"https://blog.csdn.net/fakerswe/article/details/79012978"},{"id":"2c928617645fc362016463d61f40012f","name":"环保","serialNumber":2,"disable":"0"},{"id":"402847f26390cebb016390d3a8db0001","name":"禁毒","serialNumber":3,"disable":"0"},{"id":"402847f26390db7b016390df8bd90001","name":"消防","serialNumber":4,"disable":"0"},{"id":"402847f26390db7b016390dfb3dd0002","name":"\u201c两违\u201d行为","serialNumber":5,"disable":"0"},{"id":"2c92861767c1bc170167c1c0dcd50002","name":"其他","serialNumber":6,"disable":"0"},{"id":"2c9286176d3dda29016d3ddf57be0002","name":"三进三同","serialNumber":7,"disable":"0","link":"https://temporary.zl.yafrm.com/contact/user/contact.html"}],"id":"2c9286176d3debd7016d487c2c8700f2","startTime":"2019-09-19 15:46:00","timeDiffrence":"00:27:31"}]
     * code : 1004
     * msg : 查询成功
     */

    private boolean result;
    private int code;
    private String msg;
    private List<RecordArrBean> recordArr;

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

    public List<RecordArrBean> getRecordArr() {
        return recordArr;
    }

    public void setRecordArr(List<RecordArrBean> recordArr) {
        this.recordArr = recordArr;
    }

    public static class RecordArrBean {
        /**
         * typeArr : [{"id":"2c928617645fc362016463d5fbc5012e","name":"环境卫生","serialNumber":1,"disable":"0","link":"https://blog.csdn.net/fakerswe/article/details/79012978"},{"id":"2c928617645fc362016463d61f40012f","name":"环保","serialNumber":2,"disable":"0"},{"id":"402847f26390cebb016390d3a8db0001","name":"禁毒","serialNumber":3,"disable":"0"},{"id":"402847f26390db7b016390df8bd90001","name":"消防","serialNumber":4,"disable":"0"},{"id":"402847f26390db7b016390dfb3dd0002","name":"\u201c两违\u201d行为","serialNumber":5,"disable":"0"},{"id":"2c92861767c1bc170167c1c0dcd50002","name":"其他","serialNumber":6,"disable":"0"},{"id":"2c9286176d3dda29016d3ddf57be0002","name":"三进三同","serialNumber":7,"disable":"0","link":"https://temporary.zl.yafrm.com/contact/user/contact.html"}]
         * id : 2c9286176d3debd7016d707d5955051d
         * startTime : 2019-09-27 10:12:06
         * timeDiffrence : 00:00:29
         */

        private String id;
        private String startTime;
        private String timeDiffrence;
        private List<TypeArrBean> typeArr;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getTimeDiffrence() {
            return timeDiffrence;
        }

        public void setTimeDiffrence(String timeDiffrence) {
            this.timeDiffrence = timeDiffrence;
        }

        public List<TypeArrBean> getTypeArr() {
            return typeArr;
        }

        public void setTypeArr(List<TypeArrBean> typeArr) {
            this.typeArr = typeArr;
        }

        public static class TypeArrBean {
            /**
             * id : 2c928617645fc362016463d5fbc5012e
             * name : 环境卫生
             * serialNumber : 1
             * disable : 0
             * link : https://blog.csdn.net/fakerswe/article/details/79012978
             */

            private String id;
            private String name;
            private int serialNumber;
            private String disable;
            private String link;

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

            public int getSerialNumber() {
                return serialNumber;
            }

            public void setSerialNumber(int serialNumber) {
                this.serialNumber = serialNumber;
            }

            public String getDisable() {
                return disable;
            }

            public void setDisable(String disable) {
                this.disable = disable;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }
        }
    }
}
