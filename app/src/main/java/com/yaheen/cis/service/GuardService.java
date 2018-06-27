package com.yaheen.cis.service;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.yaheen.cis.util.time.CountDownTimerUtils;

public class GuardService extends Service {

    private int GuardId = 1;


    /**
     * 返回 IBinder驱动
     * Stub: 存根
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    /**
     * 代理类
     */
    public class MyBinder extends Binder {

        public void connect() {
        }

        public void sendLocation() {
        }

        public void disConnect() {
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 提高进程优先级 ，就会在通知栏中出现自己的应用，如果不想提高优先级，可以把这个注释
//        startForeground(GuardId, new Notification());

        // 让GuardService绑定MessageService 并建立连接
        bindService(new Intent(this, UploadLocationService.class), mServiceConnection, Context.BIND_IMPORTANT);
        return START_STICKY;
    }


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//            UploadLocationService.MyBinder myBinder = (UploadLocationService.MyBinder) service;
//            myBinder.connect();
//            myBinder.startTimer();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 断开连接，就需要重新启动，然后重新绑定

            // 重新启动
            startService(new Intent(GuardService.this, UploadLocationService.class));
            // 重新绑定
            bindService(new Intent(GuardService.this, UploadLocationService.class), mServiceConnection, Context.BIND_IMPORTANT);
        }
    };
}
