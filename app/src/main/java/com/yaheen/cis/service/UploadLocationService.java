package com.yaheen.cis.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yaheen.cis.activity.LoginActivity;
import com.yaheen.cis.entity.UploadLocationBean;
import com.yaheen.cis.util.map.BDMapUtils;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;
import com.yaheen.cis.util.time.CountDownTimerUtils;
import com.yaheen.cis.util.time.TimeTransferUtils;

import java.util.Timer;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class UploadLocationService extends Service {

    protected String webSocketUrl = "ws://192.168.199.113:8080/crs/chat";

    private WebSocketConnection mConnection = new WebSocketConnection();

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
                    .setCountDownInterval(10 * 1000)
                    .setTickDelegate(new CountDownTimerUtils.TickDelegate() {
                        @Override
                        public void onTick(long pMillisUntilFinished) {
                            sendLocation();
                        }
                    }).start();
        }

        public void connect() {
            initWebSocket();
        }

        public void sendLocation() {
            sendMsg();
        }
    }

    private void initWebSocket() {
        //注意连接和服务名称要一致
        String wsuri = webSocketUrl;

        if (mConnection == null) {
            mConnection = new WebSocketConnection();
        }

        try {
            mConnection.connect(wsuri, new mWebSocketHandler());
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }

    private class mWebSocketHandler extends WebSocketHandler {
        @Override
        public void onOpen() {
//            Toast.makeText(UploadLocationService.this, "连接服务器成功", Toast.LENGTH_SHORT).show();
            sendMsg();
        }

        @Override
        public void onTextMessage(String text) {
            Gson gson = new Gson();
//            TbChatMsg chatMsg = gson.fromJson(text, TbChatMsg.class);
        }

        @Override
        public void onBinaryMessage(byte[] payload) {
            super.onBinaryMessage(payload);
        }

        @Override
        public void onClose(int code, String reason) {
//            Toast.makeText(UploadLocationService.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendMsg() {
        if (BDMapUtils.getLocation() == null) {
            return;
        }
        if (TextUtils.isEmpty(DefaultPrefsUtil.getPatrolRecordId())) {
            return;
        }
        UploadLocationBean data = new UploadLocationBean();
        data.setLongitude(BDMapUtils.getLocation().getLongitude() + "");
        data.setLatitude(BDMapUtils.getLocation().getLatitude() + "");
        data.setRecordId(DefaultPrefsUtil.getPatrolRecordId());
        String str = gson.toJson(data);
        Log.i("lin", "sendMsg: " + str);
        if (mConnection.isConnected()) {
            mConnection.sendTextMessage(str);
        } else {
            initWebSocket();
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
        manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int time = 10*1000;
        long triggerAtTime = SystemClock.elapsedRealtime()+(time);
        Intent i = new Intent(this,AlarmReceiver.class);
        pi = PendingIntent.getBroadcast(this,0,i,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        sendMsg();
        Log.i("lin", "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("lin", "onDestroy: ");
    }
}
