
package com.yaheen.cis.util.listener;

import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;

public abstract class OnRepeatClickListener implements OnClickListener {
    private final int msg_do_onclick_event = 0;

    // 默认时间间隔
    private final int IntervalTime_Defaut = ViewConfiguration.getJumpTapTimeout();

    // 时间间隔
    private int intervalTime = IntervalTime_Defaut;

    private boolean isLocked = false;

    public void setIntervalTime(int time) {
        this.intervalTime = time;
    }

    public abstract void onRepeatClick(View v);

    @Override
    public void onClick(View v) {
        if (!isLocked) {
            isLocked = true;
            onRepeatClick(v);
            handler.sendEmptyMessageDelayed(msg_do_onclick_event, intervalTime);
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == msg_do_onclick_event) {
                isLocked = false;
            }
        };
    };

}
