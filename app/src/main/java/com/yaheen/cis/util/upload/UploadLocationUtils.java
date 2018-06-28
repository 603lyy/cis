package com.yaheen.cis.util.upload;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;

import com.yaheen.cis.service.GuardService;
import com.yaheen.cis.service.JobWakeUpService;
import com.yaheen.cis.service.UploadLocationService;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;

import static android.content.Context.BIND_AUTO_CREATE;

public class UploadLocationUtils {

    public static void startUpload(Context context) {
        DefaultPrefsUtil.setIsStop(false);
//        开启后台服务上传坐标
        Intent intent = new Intent(context, UploadLocationService.class);
        context.startService(intent);
//        context.bindService(intent, conn, BIND_AUTO_CREATE);

//        // 这里必须判断，否则5.0以下手机肯定崩溃
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            context.startService(new Intent(context, JobWakeUpService.class));
//        }

    }

    public static void stopUpload() {
        DefaultPrefsUtil.setIsStop(true);
    }
}
