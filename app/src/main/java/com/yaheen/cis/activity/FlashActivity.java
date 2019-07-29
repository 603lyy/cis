package com.yaheen.cis.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.PermissionActivity;
import com.yaheen.cis.util.HttpUtils;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;
import com.yaheen.cis.util.sharepreferences.SharedPreferencesUtils;
import com.yaheen.cis.util.time.CountDownTimerUtils;
import com.yaheen.cis.util.time.TimeTransferUtils;

public class FlashActivity extends PermissionActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);

        DefaultPrefsUtil.setPatrolType("");
        CountDownTimerUtils.getCountDownTimer()
                .setMillisInFuture(3 * 1000)
                .setFinishDelegate(new CountDownTimerUtils.FinishDelegate() {
                    @Override
                    public void onFinish() {
                        Intent intent = new Intent(FlashActivity.this,UrlSettingActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .start();
    }
}
