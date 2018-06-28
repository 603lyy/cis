package com.yaheen.cis.service;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.yaheen.cis.IAIDLUpload;
import com.yaheen.cis.R;
import com.yaheen.cis.util.map.BDMapUtils;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;
import com.yaheen.cis.util.time.CountDownTimerUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;


public class UploadLocationService extends Service {

    //定义notify的id，避免与其它的notification的处理冲突
    private static final int NOTIFY_ID = 2018627;

    private static final String CHANNEL = "1";

    private String questionUrl = "http://192.168.199.113:8080/crs/eapi/realtimeUpload.do";
//
//    private String questionUrl = "http://myj.tunnel.echomod.cn/crs/eapi/realtimeUpload.do";

    private NotificationManager mNotificationManager;

    private NotificationCompat.Builder mBuilder;

    private IAIDLUpload iaidlUpload = null;

    private CountDownTimerUtils timerUtils;

    //记录开始巡查的时间戳,方便计算时间
    private long startTime;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        startTime = System.currentTimeMillis();
        return new MBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("lin", "onUnbind: ");
        return super.onUnbind(intent);
    }

    class MBinder extends IAIDLUpload.Stub {

        @Override
        public void stopApp() throws RemoteException {

        }
    }

    @Override
    public void onCreate() {
        if (DefaultPrefsUtil.getIsStop()) {
            return;
        }
        startCountTime();
        setNotification();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!DefaultPrefsUtil.getIsStop()) {
            connect();
        } else {
            cancelConnect();
        }
        return START_STICKY;
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iaidlUpload = IAIDLUpload.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (!DefaultPrefsUtil.getIsStop()) {
                connect();
            }
        }
    };

    private void startCountTime() {
        timerUtils = CountDownTimerUtils.getCountDownTimer()
                .setMillisInFuture(7 * 24 * 60 * 60 * 1000)
                .setCountDownInterval(30 * 1000)
                .setTickDelegate(new CountDownTimerUtils.TickDelegate() {
                    @Override
                    public void onTick(long pMillisUntilFinished) {
                        if (DefaultPrefsUtil.getIsStop()) {
                            try {
                                iaidlUpload.stopApp();
                                cancelConnect();
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                        sendRealLocation();
                        Log.i("lin", "onTick: ");
                    }
                });
        timerUtils.start();
    }

    private void sendRealLocation() {

        if (TextUtils.isEmpty(DefaultPrefsUtil.getPatrolRecordId())) {
            return;
        }

        if (BDMapUtils.getLocation() == null) {
            BDMapUtils.startLocation();
            return;
        }

        RequestParams requestParams = new RequestParams(questionUrl);
        requestParams.addQueryStringParameter("longitude",
                BDMapUtils.getLocation().getLongitude() + "");
        requestParams.addQueryStringParameter("latitude",
                BDMapUtils.getLocation().getLatitude() + "");
        requestParams.addQueryStringParameter("token", DefaultPrefsUtil.getToken());
        requestParams.addQueryStringParameter("recordId", DefaultPrefsUtil.getPatrolRecordId());
        requestParams.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;");
        requestParams.setConnectTimeout(60 * 1000);
        requestParams.setReadTimeout(60 * 1000);

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 创建通知栏
     */
    private void setNotification() {

        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("1",
                    "Channel1", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            mNotificationManager.createNotificationChannel(channel);
        }

        mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL);
        mBuilder.setVibrate(new long[]{0, 0, 0, 0})
                .setContentText("定时上传当前坐标")
                .setSmallIcon(R.mipmap.ic_logo)
                .setContentTitle("实时定位")
                .setAutoCancel(true)
                .setOngoing(true);
        mNotificationManager.notify(NOTIFY_ID, mBuilder.build());
    }

    private void connect() {
        // 重新启动
        startService(new Intent(UploadLocationService.this, GuardService.class));
        // 重新绑定
        bindService(new Intent(UploadLocationService.this, GuardService.class),
                mServiceConnection, Context.BIND_IMPORTANT);
    }

    private void cancelConnect() {
        stopService(new Intent(UploadLocationService.this, GuardService.class));
        unbindService(mServiceConnection);
        timerUtils.cancel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNotificationManager.cancel(NOTIFY_ID);
    }

}
