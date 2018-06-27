package com.yaheen.cis.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yaheen.cis.BaseApp;
import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.PermissionActivity;
import com.yaheen.cis.entity.LoginBean;
import com.yaheen.cis.service.UploadLocationService;
import com.yaheen.cis.util.DialogUtils;
import com.yaheen.cis.util.dialog.DialogCallback;
import com.yaheen.cis.util.dialog.IDialogCancelCallback;
import com.yaheen.cis.util.map.BDMapUtils;
import com.yaheen.cis.util.nfc.AESUtils;
import com.yaheen.cis.util.nfc.Base64;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;
import com.yaheen.cis.util.upload.UploadLocationUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class LoginActivity extends PermissionActivity {

    //定义notify的id，避免与其它的notification的处理冲突
    private static final int NOTIFY_ID = 840567289;

    private static final String CHANNEL = "1";

    private NotificationManager mNotificationManager;

    private NotificationCompat.Builder mBuilder;

    private LinearLayout llRPsd;

    private CheckBox cbRPsd;

    private TextView tvLogin;

    private EditText etName, etPsd, etIp;

    private String url = baseUrl + "/eapi/login.do";

    private String key = "X2Am6tVLnwMMX8kVgdDk5w==";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitleContent(R.string.login_title);

        initView();

        llRPsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoadingDialog();
                login();
//                setNotification();
            }
        });
    }

    private void initView() {

        cbRPsd = findViewById(R.id.cb_remember_password);
        llRPsd = findViewById(R.id.ll_remember_password);
        etName = findViewById(R.id.et_username);
        etPsd = findViewById(R.id.et_password);
        tvLogin = findViewById(R.id.tv_login);
        etIp = findViewById(R.id.et_ip);

        etPsd.setText(DefaultPrefsUtil.getUserPassword());
        etName.setText(DefaultPrefsUtil.getUserName());

    }

    private void login() {

//        setIp(etIp.getText().toString());

        DefaultPrefsUtil.setIpUrl(etIp.getText().toString());

        final String name = etName.getText().toString();
        final String psd = etPsd.getText().toString();

        if (TextUtils.isEmpty(name)) {
            showToast(R.string.login_username_empty);
            return;
        }

        if (TextUtils.isEmpty(psd)) {
            showToast(R.string.login_password_empty);
            return;
        }

        RequestParams requestParams = new RequestParams(url);
        requestParams.addQueryStringParameter("username", name);
        requestParams.addQueryStringParameter("password", Base64.encode(AESUtils.encrypt(psd, key)));
        requestParams.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;");
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                LoginBean data = gson.fromJson(result, LoginBean.class);
                if (data != null) {
                    if (data.isResult()) {
                        //不记住密码则保存空字符串
                        if (cbRPsd.isChecked()) {
                            DefaultPrefsUtil.setUserPassword(psd);
                        } else {
                            DefaultPrefsUtil.setUserPassword("");
                        }
                        DefaultPrefsUtil.setUserName(name);
                        DefaultPrefsUtil.setToken(data.getToken());

                        Intent intent = new Intent(LoginActivity.this, TurnActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        showToast(data.getMsg());
                    }
                } else {
                    showToast(R.string.login_fail);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(R.string.login_fail);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                cancelLoadingDialog();
            }
        });
    }

    private void check() {
        if (cbRPsd.isChecked()) {
            cbRPsd.setChecked(false);
        } else {
            cbRPsd.setChecked(true);
        }
    }

    @Override
    public void onBackPressed() {
        DialogUtils.showDialog(LoginActivity.this, "确定要退出该APP吗？", new DialogCallback() {
            @Override
            public void callback() {
                UploadLocationUtils.stopUpload(getApplicationContext());
                BaseApp.exit();
            }
        }, new IDialogCancelCallback() {
            @Override
            public void cancelCallback() {
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
