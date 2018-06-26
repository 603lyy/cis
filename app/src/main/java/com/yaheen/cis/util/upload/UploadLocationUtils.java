package com.yaheen.cis.util.upload;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

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
//        startService(intent);
        context.bindService(intent, conn, BIND_AUTO_CREATE);
    }
}
