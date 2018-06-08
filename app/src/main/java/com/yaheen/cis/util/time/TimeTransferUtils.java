package com.yaheen.cis.util.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeTransferUtils {

    private static long mDate = 1530374400000L;

    //字符串转时间戳
    public static String getHMSTime(String timeString) {
        String timeStamp = null;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date d;
        try {
            d = sdf.parse(timeString);
            long l = d.getTime();
            timeStamp = String.valueOf(l);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }

    //时间戳转字符串
    public static String getHMSStrTime(String timeStamp) {
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        long l = Long.valueOf(timeStamp);
        timeString = sdf.format(new Date(l));//单位秒
        return timeString;
    }

    public static boolean isTimeOver() {
        if (System.currentTimeMillis() < mDate) {
            return false;
        }
        return true;
    }

}
