package com.yaheen.cis.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yaheen.cis.BaseApp;
import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.BaseActivity;
import com.yaheen.cis.activity.base.PermissionActivity;
import com.yaheen.cis.util.DialogUtils;
import com.yaheen.cis.util.HttpUtils;
import com.yaheen.cis.util.common.ScreenManager;
import com.yaheen.cis.util.dialog.DialogCallback;
import com.yaheen.cis.util.dialog.IDialogCancelCallback;
import com.yaheen.cis.util.notification.NotificationUtils;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;
import com.yaheen.cis.util.upload.UploadLocationUtils;

public class UrlSettingActivity extends PermissionActivity {

    private TextView tvConfirm;
    private EditText etBaseUrl, etHouseUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url_setting);

        initView();
    }

    private void initView() {
        tvConfirm = findViewById(R.id.tv_confirm);
        etBaseUrl = findViewById(R.id.et_base_url);
        etHouseUrl = findViewById(R.id.et_house_url);

        etBaseUrl.setText(DefaultPrefsUtil.getBaseUrl());
        etHouseUrl.setText(DefaultPrefsUtil.getHouseUrl());

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etBaseUrl.getText().toString())) {
                    showToast("域名不可为空");
                    return;
                }
                if (TextUtils.isEmpty(etHouseUrl.getText().toString())) {
                    showToast("门牌域名不可为空");
                    return;
                }

                DefaultPrefsUtil.setBaseUrl(etBaseUrl.getText().toString());
                DefaultPrefsUtil.setHouseUrl(etHouseUrl.getText().toString());

                HttpUtils.setHouseUrl();
                HttpUtils.setBaseUrl();

                Intent intent = new Intent(UrlSettingActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DialogUtils.showDialog(UrlSettingActivity.this, "确定要退出该APP吗？", new DialogCallback() {
            @Override
            public void callback() {
                ScreenManager.getScreenManagerInstance(UrlSettingActivity.this).finishActivities();
                NotificationUtils.cancelNofication(getApplicationContext());
                UploadLocationUtils.stopUpload();
                BaseApp.exit();
            }
        }, new IDialogCancelCallback() {
            @Override
            public void cancelCallback() {
            }
        });
    }
}
