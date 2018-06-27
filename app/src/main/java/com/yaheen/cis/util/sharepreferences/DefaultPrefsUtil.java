package com.yaheen.cis.util.sharepreferences;

/**
 * Created by Administrator on 17-10-20.
 */

public class DefaultPrefsUtil {

    public static void setIsStop(boolean isStop) {
        DefaultPrefs.getInstance().setIsStop(isStop);
    }

    public static boolean getIsStop() {
        return DefaultPrefs.getInstance().getIsStop();
    }

    public static void setCourseCode(String courseCode) {
        DefaultPrefs.getInstance().setCourseCode(courseCode);
    }

    public static String getCourseCode() {
        return DefaultPrefs.getInstance().getCourseCode();
    }

    public static void setUUID(String uuid) {
        DefaultPrefs.getInstance().setUUID(uuid);
    }

    public static String getUUID() {
        return DefaultPrefs.getInstance().getUUID();
    }

    public static void setToken(String token) {
        DefaultPrefs.getInstance().setToken(token);
    }

    public static String getToken() {
        return DefaultPrefs.getInstance().getToken();
    }

    public static void setTeacherName(String teacherName) {
        DefaultPrefs.getInstance().setTeacherName(teacherName);
    }

    public static String getTeacherName() {
        return DefaultPrefs.getInstance().getTeacherName();
    }

    public static void setUserName(String userName) {
        DefaultPrefs.getInstance().setUserName(userName);
    }

    public static String getUserName() {
        return DefaultPrefs.getInstance().getUserName();
    }

    public static void setUserPassword(String userPassword) {
        DefaultPrefs.getInstance().setPassword(userPassword);
    }

    public static String getUserPassword() {
        return DefaultPrefs.getInstance().getPassword();
    }

    public static void setLoginTime(String loginTime) {
        DefaultPrefs.getInstance().setLoginTime(loginTime);
    }

    public static String getLoginTime() {
        return DefaultPrefs.getInstance().getLoginTime();
    }

    public static void setCourseUid(String courseName, String courseUid) {
        DefaultPrefs.getInstance().setCourseUid(courseName, courseUid);
    }

    public static String getCourseUid(String courseName) {
        return DefaultPrefs.getInstance().getCourseUid(courseName);
    }

    public static void setMachineCode(String machineCode) {
        DefaultPrefs.getInstance().setMachineCode(machineCode);
    }

    public static String getMachineCode() {
        return DefaultPrefs.getInstance().getMachineCode();
    }

    public static void setIpUrl(String ipUrl) {
        DefaultPrefs.getInstance().setIpUrl(ipUrl);
    }

    public static String getIpUrl() {
        return DefaultPrefs.getInstance().getIpUrl();
    }

    public static void setVersion(int version) {
        DefaultPrefs.getInstance().setVersion(version);
    }

    public static int getVersion() {
        return DefaultPrefs.getInstance().getVersion();
    }

    public static void setPatrolType(String patrolType) {
        DefaultPrefs.getInstance().setPatrolType(patrolType);
    }

    public static String getPatrolType() {
        return DefaultPrefs.getInstance().getPatrolType();
    }

    public static void setPatrolqQuestion(String patrolqQuestion) {
        DefaultPrefs.getInstance().setPatrolQuestion(patrolqQuestion);
    }

    public static String getPatrolqQuestion() {
        return DefaultPrefs.getInstance().getPatrolQuestion();
    }

    public static void setPatrolStart(long patrolStart) {
        DefaultPrefs.getInstance().setPatrolStart(patrolStart);
    }

    public static long getPatrolStart() {
        return DefaultPrefs.getInstance().getPatrolStart();
    }

    public static void setPatrolRecordId(String patrolRecordId) {
        DefaultPrefs.getInstance().setPatrolRecordId(patrolRecordId);
    }

    public static String getPatrolRecordId() {
        return DefaultPrefs.getInstance().getPatrolRecordId();
    }
}
