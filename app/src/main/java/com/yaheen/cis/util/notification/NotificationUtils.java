package com.yaheen.cis.util.notification;

import android.app.NotificationManager;
import android.content.Context;

public class NotificationUtils {

    //定义notify的id，避免与其它的notification的处理冲突
    private static final int NOTIFY_ID = 2018627;

    private static final String CHANNEL = "1";

    public static void cancelNofication(Context context) {
        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.cancel(NOTIFY_ID);
        }
    }

}
