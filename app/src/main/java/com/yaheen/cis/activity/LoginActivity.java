package com.yaheen.cis.activity;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yaheen.cis.BaseApp;
import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.PermissionActivity;
import com.yaheen.cis.entity.LoginBean;
import com.yaheen.cis.util.DialogUtils;
import com.yaheen.cis.util.HttpUtils;
import com.yaheen.cis.util.common.FreeHandSystemUtil;
import com.yaheen.cis.util.common.ScreenManager;
import com.yaheen.cis.util.dialog.DialogCallback;
import com.yaheen.cis.util.dialog.IDialogCancelCallback;
import com.yaheen.cis.util.nfc.AESUtils;
import com.yaheen.cis.util.nfc.Base64Utils;
import com.yaheen.cis.util.nfc.RSAUtils;
import com.yaheen.cis.util.notification.NotificationUtils;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;
import com.yaheen.cis.util.upload.UploadLocationUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class LoginActivity extends PermissionActivity {

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
                checkRecord();
//                login();
//                read();
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

    /**
     * 判断上次巡查是否结束
     */
    private void checkRecord() {
        String typeStr = DefaultPrefsUtil.getPatrolType();
        if (!TextUtils.isEmpty(typeStr) && !etName.getText().toString().equals(DefaultPrefsUtil.getUserName())) {
            showToast(R.string.cancel_record);
        } else {
            login();
        }
    }

    private void login() {

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

        showLoadingDialog();

        RequestParams requestParams = new RequestParams(url);
        requestParams.addQueryStringParameter("username", name);
        requestParams.addQueryStringParameter("password", Base64Utils.encode(AESUtils.encrypt(psd, key)));
        requestParams.addQueryStringParameter("hardwareId", FreeHandSystemUtil.getSafeUUID(getApplicationContext()));

        HttpUtils.getPostHttp(requestParams, new Callback.CommonCallback<String>() {
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
//                        finish();
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

    private void read() {
        String in, str = "";

        //读取的内容会随着文件的改变而改变
        try {
            //读取的是字节流
            InputStream is = getResources().getAssets().open("encrypt.key");
            //UTF-8编码的指定是很重要的
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader bfr = new BufferedReader(isr);
            while ((in = bfr.readLine()) != null) {
                str = str + in;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCGKXHz1Kr0PZs6t/usPwvhPbXWhHr4p+4gHMl+IjMH6kjHXEus+TIBRyV3NemCDkQQ447MdQ/DDCTyw4S8xfmUpzLqdaex1+coDq9y5IxVRju9WegKlnGrDppzDd18DeFgScFPRShjQcbIiztFy4/rDeWvLPqtrf8hwGDs+PoBIwIDAQAB";

//        String cSrc = "59M030Of64C6Um51aF2yY3j94SLt5u467EF0O3E1LUuQGBB56pGRQ634t55ikTxQMtueEXOHJDuUs141ui1q1yEj";
        // 私钥加密
//        String enString = RSAUtils.encryptByPrivateKeyToString(cSrc, privateKey);
        // 公钥解密
//        String DeString = RSAUtils.decryptByPublicKeyToString(enString, publicKey);

        // 公钥解密
        String DeString = RSAUtils.decryptByPublicKeyToString(str, publicKey);
        Log.i("lin", "read: " + DeString);
    }

    @Override
    public void onBackPressed() {
        DialogUtils.showDialog(LoginActivity.this, "确定要退出该APP吗？", new DialogCallback() {
            @Override
            public void callback() {
                ScreenManager.getScreenManagerInstance(LoginActivity.this).finishActivities();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
