package com.yaheen.cis.util.upload;

import android.content.Context;
import android.content.Intent;

import com.yaheen.cis.service.UploadLocationService;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;

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
