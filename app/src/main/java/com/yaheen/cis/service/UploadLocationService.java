package com.yaheen.cis.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yaheen.cis.R;
import com.yaheen.cis.activity.LoginActivity;
import com.yaheen.cis.activity.base.BaseActivity;
import com.yaheen.cis.entity.UploadLocationBean;
import com.yaheen.cis.util.map.BDMapUtils;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;
import com.yaheen.cis.util.time.CountDownTimerUtils;
import com.yaheen.cis.util.time.TimeTransferUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Timer;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class UploadLocationService extends Service {

    //定义notify的id，避免与其它的notification的处理冲突
    private static final int NOTIFY_ID = 840567289;

    private static final String CHANNEL = "1";

//    private String questionUrl = "http://192.168.199.113:8080/crs/eapi/realtimeUpload.do";

    private String questionUrl = "http://lyy.tunnel.echomod.cn/crs/eapi/realtimeUpload.do";

    private NotificationManager mNotificationManager;

    private NotificationCompat.Builder mBuilder;

    private AlarmManager manager;

    private PendingIntent pi;

    private Gson gson = new Gson();

    //记录开始巡查的时间戳,方便计算时间
    private long startTime;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("lin", "onBind: ");
        startTime = System.currentTimeMillis();
        return new MyBinder();
    }

    /**
     * 代理类
     */
    public class MyBinder extends Binder {

        public void startTimer() {
            CountDownTimerUtils.getCountDownTimer()
                    .setMillisInFuture(7 * 24 * 60 * 60 * 1000)
                    .setCountDownInterval(30 * 1000)
                    .setTickDelegate(new CountDownTimerUtils.TickDelegate() {
                        @Override
                        public void onTick(long pMillisUntilFinished) {
                            sendLocation();
                            Log.i("lin", "onTick: ");
                        }
                    }).start();
        }

        public void connect() {
            setNotification();
        }

        public void sendLocation() {
            sendRealLocation();
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("lin", "onUnbind: ");
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        Log.i("lin", "onCreate: ");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        manager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        int time = 30 * 1000;
//        long triggerAtTime = SystemClock.elapsedRealtime() + (time);
//        Intent i = new Intent(this, AlarmReceiver.class);
//        pi = PendingIntent.getBroadcast(this, 0, i, 0);
//        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);

//        sendRealLocation();
        Log.i("lin", "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
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
        mBuilder.setContentTitle("开始下载")
                .setContentText("正在连接服务器")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setOngoing(true)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis());
        mNotificationManager.notify(NOTIFY_ID, mBuilder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("lin", "onDestroy: ");
    }
}
