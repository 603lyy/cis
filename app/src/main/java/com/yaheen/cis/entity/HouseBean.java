package com.yaheen.cis.entity;

import java.util.List;

public class HouseBean {

    /**
     * result : true
     * msg : 获取成功
     * code : 1
     * json : [{"houseNumber":{"precisionPoverty":"F","technologyDemonstration":"F","helper":"","breedingSpecialist":"F","gridInspectionPoint":"Y","fireInspectionPoint":"","gloriousArmy":"F","lock":"1","id":"2c9252926ad2ea6b016ad3d7c5040005","longitude":113.102844,"civilizationHouseholds":"F","telephone":"","community":"N","mentallyHandicapped":"","fiveGuarantees":"F","beekeepingProfessionals":"F","latitude":29.32928,"remark":"","agriculturalProductsInformation":"","unitId":"","chipId":"","keyMonitoringHouseholds":"","address":"西塘镇联合村商户牌2","userName":"","peopleNumber":"1","category":"B","partyMember":"F","user":""},"partyMember":[{"promise":"努力工作","id":"402881ec6bd034b3016bd04c03be0002","phone":"15989302613","name":"李四","position":"党员","unitId":"402881ec6bd034b3016bd04c03be0002","address":"西塘镇联合村商户牌1","avatar":"","time":"20190708"}],"user":[{"education":"","jobTitle":"","id":"2c9252926ad2ea6b016ad3d8d11f0006","ethnic":"","phone":"","name":"张三","employmentInformation":"","skill":"","unitId":"","politicalStatus":"","sex":"M","mobile":"","toolUnitId":"40289f4f6abfc9af016abfd1e51b0007","age":"","username":"张三"}],"merchants":{"type":"餐饮","id":"2c9252926ad2ea6b016ad3f3c5ef0037","longitude":113.347719,"businessScope":"","houseOwner":"2c9252926ad2ea6b016ad3d8d11f0006","coverimage":"2c9252926ad2ea6b016ad3ec71790020","phone":"","name":"张三虾饼店","houseId":"2c9252926ad2ea6b016ad3d7c5040005","latitude":29.279217,"houseOwnerName":"张三","unitId":"40289f4f6abfc9af016abfd1e51b0007","address":"","userName":"联合村村管理员","userId":"40289f4f6ac37671016ac378703f0002","fireowner":"","storephotos":"2c9252926ad2ea6b016ad3ec71790020","time":"9:00-18:00","nameList":"[虾饼.jpg]"}}]
     */

    private boolean result;
    private String msg;
    private String code;
    private List<JsonBean> json;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<JsonBean> getJson() {
        return json;
    }

    public void setJson(List<JsonBean> json) {
        this.json = json;
    }

    public static class JsonBean {
        /**
         * houseNumber : {"precisionPoverty":"F","technologyDemonstration":"F","helper":"","breedingSpecialist":"F","gridInspectionPoint":"Y","fireInspectionPoint":"","gloriousArmy":"F","lock":"1","id":"2c9252926ad2ea6b016ad3d7c5040005","longitude":113.102844,"civilizationHouseholds":"F","telephone":"","community":"N","mentallyHandicapped":"","fiveGuarantees":"F","beekeepingProfessionals":"F","latitude":29.32928,"remark":"","agriculturalProductsInformation":"","unitId":"","chipId":"","keyMonitoringHouseholds":"","address":"西塘镇联合村商户牌2","userName":"","peopleNumber":"1","category":"B","partyMember":"F","user":""}
         * partyMember : [{"promise":"努力工作","id":"402881ec6bd034b3016bd04c03be0002","phone":"15989302613","name":"李四","position":"党员","unitId":"402881ec6bd034b3016bd04c03be0002","address":"西塘镇联合村商户牌1","avatar":"","time":"20190708"}]
         * user : [{"education":"","jobTitle":"","id":"2c9252926ad2ea6b016ad3d8d11f0006","ethnic":"","phone":"","name":"张三","employmentInformation":"","skill":"","unitId":"","politicalStatus":"","sex":"M","mobile":"","toolUnitId":"40289f4f6abfc9af016abfd1e51b0007","age":"","username":"张三"}]
         * merchants : {"type":"餐饮","id":"2c9252926ad2ea6b016ad3f3c5ef0037","longitude":113.347719,"businessScope":"","houseOwner":"2c9252926ad2ea6b016ad3d8d11f0006","coverimage":"2c9252926ad2ea6b016ad3ec71790020","phone":"","name":"张三虾饼店","houseId":"2c9252926ad2ea6b016ad3d7c5040005","latitude":29.279217,"houseOwnerName":"张三","unitId":"40289f4f6abfc9af016abfd1e51b0007","address":"","userName":"联合村村管理员","userId":"40289f4f6ac37671016ac378703f0002","fireowner":"","storephotos":"2c9252926ad2ea6b016ad3ec71790020","time":"9:00-18:00","nameList":"[虾饼.jpg]"}
         */

        private HouseNumberBean houseNumber;
        private MerchantsBean merchants;
        private List<PartyMemberBean> partyMember;
        private List<UserBean> user;

        public HouseNumberBean getHouseNumber() {
            return houseNumber;
        }

        public void setHouseNumber(HouseNumberBean houseNumber) {
            this.houseNumber = houseNumber;
        }

        public MerchantsBean getMerchants() {
            return merchants;
        }

        public void setMerchants(MerchantsBean merchants) {
            this.merchants = merchants;
        }

        public List<PartyMemberBean> getPartyMember() {
            return partyMember;
        }

        public void setPartyMember(List<PartyMemberBean> partyMember) {
            this.partyMember = partyMember;
        }

        public List<UserBean> getUser() {
            return user;
        }

        public void setUser(List<UserBean> user) {
            this.user = user;
        }

        public static class HouseNumberBean {
            /**
             * precisionPoverty : F
             * technologyDemonstration : F
             * helper :
             * breedingSpecialist : F
             * gridInspectionPoint : Y
             * fireInspectionPoint :
             * gloriousArmy : F
             * lock : 1
             * id : 2c9252926ad2ea6b016ad3d7c5040005
             * longitude : 113.102844
             * civilizationHouseholds : F
             * telephone :
             * community : N
             * mentallyHandicapped :
             * fiveGuarantees : F
             * beekeepingProfessionals : F
             * latitude : 29.32928
             * remark :
             * agriculturalProductsInformation :
             * unitId :
             * chipId :
             * keyMonitoringHouseholds :
             * address : 西塘镇联合村商户牌2
             * userName :
             * peopleNumber : 1
             * category : B
             * partyMember : F
             * user :
             */

            private String precisionPoverty;
            private String technologyDemonstration;
            private String helper;
            private String breedingSpecialist;
            private String gridInspectionPoint;
            private String fireInspectionPoint;
            private String gloriousArmy;
            private String lock;
            private String id;
            private double longitude;
            private String civilizationHouseholds;
            private String telephone;
            private String community;
            private String mentallyHandicapped;
            private String fiveGuarantees;
            private String beekeepingProfessionals;
            private double latitude;
            private String remark;
            private String agriculturalProductsInformation;
            private String unitId;
            private String chipId;
            private String keyMonitoringHouseholds;
            private String address;
            private String userName;
            private String peopleNumber;
            private String category;
            private String partyMember;
            private String user;

            public String getPrecisionPoverty() {
                return precisionPoverty;
            }

            public void setPrecisionPoverty(String precisionPoverty) {
                this.precisionPoverty = precisionPoverty;
            }

            public String getTechnologyDemonstration() {
                return technologyDemonstration;
            }

            public void setTechnologyDemonstration(String technologyDemonstration) {
                this.technologyDemonstration = technologyDemonstration;
            }

            public String getHelper() {
                return helper;
            }

            public void setHelper(String helper) {
                this.helper = helper;
            }

            public String getBreedingSpecialist() {
                return breedingSpecialist;
            }

            public void setBreedingSpecialist(String breedingSpecialist) {
                this.breedingSpecialist = breedingSpecialist;
            }

            public String getGridInspectionPoint() {
                return gridInspectionPoint;
            }

            public void setGridInspectionPoint(String gridInspectionPoint) {
                this.gridInspectionPoint = gridInspectionPoint;
            }

            public String getFireInspectionPoint() {
                return fireInspectionPoint;
            }

            public void setFireInspectionPoint(String fireInspectionPoint) {
                this.fireInspectionPoint = fireInspectionPoint;
            }

            public String getGloriousArmy() {
                return gloriousArmy;
            }

            public void setGloriousArmy(String gloriousArmy) {
                this.gloriousArmy = gloriousArmy;
            }

            public String getLock() {
                return lock;
            }

            public void setLock(String lock) {
                this.lock = lock;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public String getCivilizationHouseholds() {
                return civilizationHouseholds;
            }

            public void setCivilizationHouseholds(String civilizationHouseholds) {
                this.civilizationHouseholds = civilizationHouseholds;
            }

            public String getTelephone() {
                return telephone;
            }

            public void setTelephone(String telephone) {
                this.telephone = telephone;
            }

            public String getCommunity() {
                return community;
            }

            public void setCommunity(String community) {
                this.community = community;
            }

            public String getMentallyHandicapped() {
                return mentallyHandicapped;
            }

            public void setMentallyHandicapped(String mentallyHandicapped) {
                this.mentallyHandicapped = mentallyHandicapped;
            }

            public String getFiveGuarantees() {
                return fiveGuarantees;
            }

            public void setFiveGuarantees(String fiveGuarantees) {
                this.fiveGuarantees = fiveGuarantees;
            }

            public String getBeekeepingProfessionals() {
                return beekeepingProfessionals;
            }

            public void setBeekeepingProfessionals(String beekeepingProfessionals) {
                this.beekeepingProfessionals = beekeepingProfessionals;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getAgriculturalProductsInformation() {
                return agriculturalProductsInformation;
            }

            public void setAgriculturalProductsInformation(String agriculturalProductsInformation) {
                this.agriculturalProductsInformation = agriculturalProductsInformation;
            }

            public String getUnitId() {
                return unitId;
            }

            public void setUnitId(String unitId) {
                this.unitId = unitId;
            }

            public String getChipId() {
                return chipId;
            }

            public void setChipId(String chipId) {
                this.chipId = chipId;
            }

            public String getKeyMonitoringHouseholds() {
                return keyMonitoringHouseholds;
            }

            public void setKeyMonitoringHouseholds(String keyMonitoringHouseholds) {
                this.keyMonitoringHouseholds = keyMonitoringHouseholds;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getPeopleNumber() {
                return peopleNumber;
            }

            public void setPeopleNumber(String peopleNumber) {
                this.peopleNumber = peopleNumber;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public String getPartyMember() {
                return partyMember;
            }

            public void setPartyMember(String partyMember) {
                this.partyMember = partyMember;
            }

            public String getUser() {
                return user;
            }

            public void setUser(String user) {
                this.user = user;
            }
        }

        public static class MerchantsBean {
            /**
             * type : 餐饮
             * id : 2c9252926ad2ea6b016ad3f3c5ef0037
             * longitude : 113.347719
             * businessScope :
             * houseOwner : 2c9252926ad2ea6b016ad3d8d11f0006
             * coverimage : 2c9252926ad2ea6b016ad3ec71790020
             * phone :
             * name : 张三虾饼店
             * houseId : 2c9252926ad2ea6b016ad3d7c5040005
             * latitude : 29.279217
             * houseOwnerName : 张三
             * unitId : 40289f4f6abfc9af016abfd1e51b0007
             * address :
             * userName : 联合村村管理员
             * userId : 40289f4f6ac37671016ac378703f0002
             * fireowner :
             * storephotos : 2c9252926ad2ea6b016ad3ec71790020
             * time : 9:00-18:00
             * nameList : [虾饼.jpg]
             */

            private String type;
            private String id;
            private double longitude;
            private String businessScope;
            private String houseOwner;
            private String coverimage;
            private String phone;
            private String name;
            private String houseId;
            private double latitude;
            private String houseOwnerName;
            private String unitId;
            private String address;
            private String userName;
            private String userId;
            private String fireowner;
            private String storephotos;
            private String time;
            private String nameList;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public String getBusinessScope() {
                return businessScope;
            }

            public void setBusinessScope(String businessScope) {
                this.businessScope = businessScope;
            }

            public String getHouseOwner() {
                return houseOwner;
            }

            public void setHouseOwner(String houseOwner) {
                this.houseOwner = houseOwner;
            }

            public String getCoverimage() {
                return coverimage;
            }

            public void setCoverimage(String coverimage) {
                this.coverimage = coverimage;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getHouseId() {
                return houseId;
            }

            public void setHouseId(String houseId) {
                this.houseId = houseId;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
                this.latitude = latitude;
            }

            public String getHouseOwnerName() {
                return houseOwnerName;
            }

            public void setHouseOwnerName(String houseOwnerName) {
                this.houseOwnerName = houseOwnerName;
            }

            public String getUnitId() {
                return unitId;
            }

            public void setUnitId(String unitId) {
                this.unitId = unitId;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getFireowner() {
                return fireowner;
            }

            public void setFireowner(String fireowner) {
                this.fireowner = fireowner;
            }

            public String getStorephotos() {
                return storephotos;
            }

            public void setStorephotos(String storephotos) {
                this.storephotos = storephotos;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getNameList() {
                return nameList;
            }

            public void setNameList(String nameList) {
                this.nameList = nameList;
            }
        }

        public static class PartyMemberBean {
            /**
             * promise : 努力工作
             * id : 402881ec6bd034b3016bd04c03be0002
             * phone : 15989302613
             * name : 李四
             * position : 党员
             * unitId : 402881ec6bd034b3016bd04c03be0002
             * address : 西塘镇联合村商户牌1
             * avatar :
             * time : 20190708
             */

            private String promise;
            private String id;
            private String phone;
            private String name;
            private String position;
            private String unitId;
            private String address;
            private String avatar;
            private String time;

            public String getPromise() {
                return promise;
            }

            public void setPromise(String promise) {
                this.promise = promise;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public String getUnitId() {
                return unitId;
            }

            public void setUnitId(String unitId) {
                this.unitId = unitId;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }

        public static class UserBean {
            /**
             * education :
             * jobTitle :
             * id : 2c9252926ad2ea6b016ad3d8d11f0006
             * ethnic :
             * phone :
             * name : 张三
             * employmentInformation :
             * skill :
             * unitId :
             * politicalStatus :
             * sex : M
             * mobile :
             * toolUnitId : 40289f4f6abfc9af016abfd1e51b0007
             * age :
             * username : 张三
             */

            private String education;
            private String jobTitle;
            private String id;
            private String ethnic;
            private String phone;
            private String name;
            private String employmentInformation;
            private String skill;
            private String unitId;
            private String politicalStatus;
            private String sex;
            private String mobile;
            private String toolUnitId;
            private String age;
            private String username;

            public String getEducation() {
                return education;
            }

            public void setEducation(String education) {
                this.education = education;
            }

            public String getJobTitle() {
                return jobTitle;
            }

            public void setJobTitle(String jobTitle) {
                this.jobTitle = jobTitle;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getEthnic() {
                return ethnic;
            }

            public void setEthnic(String ethnic) {
                this.ethnic = ethnic;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getEmploymentInformation() {
                return employmentInformation;
            }

            public void setEmploymentInformation(String employmentInformation) {
                this.employmentInformation = employmentInformation;
            }

            public String getSkill() {
                return skill;
            }

            public void setSkill(String skill) {
                this.skill = skill;
            }

            public String getUnitId() {
                return unitId;
            }

            public void setUnitId(String unitId) {
                this.unitId = unitId;
            }

            public String getPoliticalStatus() {
                return politicalStatus;
            }

            public void setPoliticalStatus(String politicalStatus) {
                this.politicalStatus = politicalStatus;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getToolUnitId() {
                return toolUnitId;
            }

            public void setToolUnitId(String toolUnitId) {
                this.toolUnitId = toolUnitId;
            }

            public String getAge() {
                return age;
            }

            public void setAge(String age) {
                this.age = age;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }
        }
    }
}
