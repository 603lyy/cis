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

import static android.content.Context.BIND_AUTO_CREATE;

public class UploadLocationUtils {

    private static UploadLocationService.MyBinder myBinder;

    private static ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //拿到后台服务代理对象
            myBinder = (UploadLocationService.MyBinder) service;
            //调用后台服务的方法
            myBinder.connect();
            myBinder.startTimer();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    public static void startUpload(Context context) {
//        开启后台服务上传坐标
        Intent intent = new Intent(context, UploadLocationService.class);
        context.startService(intent);
//        context.bindService(intent, conn, BIND_AUTO_CREATE);

        //开启双进程保护service
//        context.startService(new Intent(context, GuardService.class));

//        // 这里必须判断，否则5.0以下手机肯定崩溃
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            context.startService(new Intent(context, JobWakeUpService.class));
//        }

    }

    public static void stopUpload(Context context) {
        context.stopService(new Intent(context, UploadLocationService.class));
        context.stopService(new Intent(context, GuardService.class));
    }
}
