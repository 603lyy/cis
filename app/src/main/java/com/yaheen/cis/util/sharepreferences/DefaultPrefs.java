
package com.yaheen.cis.util.sharepreferences;

import android.content.Context;

import com.yaheen.cis.BaseApp;

/**
 * @author lyy 10-11-20
 * @desc 存储公用的Preference
 */
final class DefaultPrefs extends AbstractSharePreference {

    private static DefaultPrefs instance;

    DefaultPrefs(String name, Context ctx) {
        super(name, ctx);
    }

    static synchronized DefaultPrefs getInstance() {
        if (instance == null) {
            Context context = BaseApp.getInstance();
            String name = "default" + "_preferences";
            instance = new DefaultPrefs(name, context);
        }
        return instance;
    }

    static interface Keys {

        /**
         * 应用是否关闭
         */
        String IS_STOP = "is_stop";

        /**
         * 课程编号
         */
        String COURSE_CODE = "coursecode";

        /**
         * 用户uuid
         */
        String UUID = "uuid";

        /**
         * 用户token
         */
        String TOKEN = "token";

        /**
         * 老师名字
         */
        String TEACHER_NAME = "teacherName";

        /**
         * 用户名字
         */
        String USER_NAME = "username";

        /**
         * 用户密码
         */
        String PASSWORD = "password";

        /**
         * 用户登陆时间
         */
        String LOGIN_TIME = "loginTime";

        /**
         * 机器码
         */
        String MACHINE_CODE = "machine";

        /**
         * ip_url
         */
        String IP_URL = "ipurl";

        /**
         * 项目版本号
         */
        String APP_VERSION = "version";

        /**
         * 巡查类型
         */
        String PATROL_TYPE = "patrol_type";

        /**
         * 巡查具体问题
         */
        String PATROL_QUESTION = "patrol_question";

        /**
         * 巡查时长
         */
        String PATROL_START = "patrol_start";

        /**
         * 巡查记录ID
         */
        String PATROL_RECORD_ID = "patrol_record_id";

        /**
         * 用户手机号
         */
        String PHONE = "phone_num";

        /**
         * 用户类型
         */
        String ROLE = "user_role";

        /**
         * 接口域名
         */
        String BASE_URL = "base_url";

        /**
         * 门牌域名
         */
        String HOUSE_URL = "house_url";

        /**
         * 门牌经度
         */
        String HOUSE_LONGITUDE = "house_longitude";

        /**
         * 门牌纬度
         */
        String HOUSE_LATITUE = "house_latitude";
    }

    /**
     * 屏幕是否是横屏
     */
    boolean getIsStop() {
        return getBoolean(Keys.IS_STOP, false);
    }

    void setIsStop(boolean isStop) {
        putBoolean(Keys.IS_STOP, isStop);
    }

    /**
     * 项目版本号
     */
    int getVersion() {
        return getInt(Keys.APP_VERSION, 0);
    }

    void setVersion(int version) {
        putInt(Keys.APP_VERSION, version);
    }

    /**
     * 当前登录的用户名
     */
    String getCourseCode() {
        return getString(Keys.COURSE_CODE, "");
    }

    void setCourseCode(String courseCode) {
        putString(Keys.COURSE_CODE, courseCode);
    }

    /**
     * 用户uuid
     */
    String getUUID() {
        return getString(Keys.UUID, "");
    }

    void setUUID(String uuid) {
        putString(Keys.UUID, uuid);
    }

    /**
     * 用户token
     */
    String getToken() {
        return getString(Keys.TOKEN, "");
    }

    void setToken(String token) {
        putString(Keys.TOKEN, token);
    }

    /**
     * 老师名字
     */
    String getTeacherName() {
        return getString(Keys.TEACHER_NAME, "");
    }

    void setTeacherName(String teacherName) {
        putString(Keys.TEACHER_NAME, teacherName);
    }

    /**
     * 用户名字
     */
    String getUserName() {
        return getString(Keys.USER_NAME, "");
    }

    void setUserName(String userName) {
        putString(Keys.USER_NAME, userName);
    }

    /**
     * 用户密码
     */
    String getPassword() {
        return getString(Keys.PASSWORD, "");
    }

    void setPassword(String password) {
        putString(Keys.PASSWORD, password);
    }

    /**
     * 用户登陆时间
     */
    String getLoginTime() {
        return getString(Keys.LOGIN_TIME, "");
    }

    void setLoginTime(String loginTime) {
        putString(Keys.LOGIN_TIME, loginTime);
    }

    /**
     * 课程编码
     */
    String getCourseUid(String courseName) {
        return getString(courseName, "");
    }

    void setCourseUid(String courseName, String courseUid) {
        putString(courseName, courseUid);
    }

    /**
     * 机器码
     */
    String getMachineCode() {
        return getString(Keys.MACHINE_CODE, "");
    }

    void setMachineCode(String machineCode) {
        putString(Keys.MACHINE_CODE, machineCode);
    }

    /**
     * ip_url
     */
    String getIpUrl() {
        return getString(Keys.IP_URL, "");
    }

    void setIpUrl(String ipUrl) {
        putString(Keys.IP_URL, ipUrl);
    }

    /**
     * PATROL_TYPE
     */
    String getPatrolType() {
        return getString(Keys.PATROL_TYPE, "");
    }

    void setPatrolType(String patrolType) {
        putString(Keys.PATROL_TYPE, patrolType);
    }

    /**
     * PATROL_QUESTION
     */
    String getPatrolQuestion() {
        return getString(Keys.PATROL_QUESTION, "");
    }

    void setPatrolQuestion(String patrolQuestion) {
        putString(Keys.PATROL_QUESTION, patrolQuestion);
    }

    /**
     * PATROL_START
     */
    long getPatrolStart() {
        return getLong(Keys.PATROL_START, 0);
    }

    void setPatrolStart(long patrolStart) {
        putLong(Keys.PATROL_START, patrolStart);
    }

    /**
     * PATROL_RECORD_ID
     */
    String getPatrolRecordId() {
        return getString(Keys.PATROL_RECORD_ID, "");
    }

    void setPatrolRecordId(String patrolRecordId) {
        putString(Keys.PATROL_RECORD_ID, patrolRecordId);
    }

    /**
     * phone
     */
    String getPhone() {
        return getString(Keys.PHONE, "");
    }

    void setPhone(String phone) {
        putString(Keys.PHONE, phone);
    }

    /**
     * role
     */
    String getRole() {
        return getString(Keys.ROLE, "");
    }

    void setRole(String role) {
        putString(Keys.ROLE, role);
    }

    /**
     * 接口域名
     */
    String getBaseUrl() {
        return getString(Keys.BASE_URL, "");
    }

    void setBaseUrl(String baseUrl) {
        putString(Keys.BASE_URL, baseUrl);
    }

    /**
     * 门牌域名
     */
    String getHouseUrl() {
        return getString(Keys.HOUSE_URL, "");
    }

    void setHouseUrl(String houseUrl) {
        putString(Keys.HOUSE_URL, houseUrl);
    }

    /**
     * 门牌经度
     */
    float getHouseLongitude() {
        return getFloat(Keys.HOUSE_LONGITUDE, 0);
    }

    void setHouseLongitude(float houseLongitude) {
        putFloat(Keys.HOUSE_LONGITUDE, houseLongitude);
    }

    /**
     * 门牌纬度
     */
    float getHouseLatitude() {
        return getFloat(Keys.HOUSE_LATITUE, 0);
    }

    void setHouseLatitude(float houselatitude) {
        putFloat(Keys.HOUSE_LATITUE, houselatitude);
    }

}
