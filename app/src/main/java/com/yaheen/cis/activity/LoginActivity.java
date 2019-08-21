package com.yaheen.cis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yaheen.cis.BaseApp;
import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.PermissionActivity;
import com.yaheen.cis.entity.GetVerBean;
import com.yaheen.cis.entity.LoginBean;
import com.yaheen.cis.entity.TypeBean;
import com.yaheen.cis.util.DialogUtils;
import com.yaheen.cis.util.HttpUtils;
import com.yaheen.cis.util.common.CommonUtils;
import com.yaheen.cis.util.common.FreeHandScreenUtil;
import com.yaheen.cis.util.common.FreeHandSystemUtil;
import com.yaheen.cis.util.common.ScreenManager;
import com.yaheen.cis.util.dialog.DialogCallback;
import com.yaheen.cis.util.dialog.IDialogCancelCallback;
import com.yaheen.cis.util.nfc.AESUtils;
import com.yaheen.cis.util.nfc.Base64Utils;
import com.yaheen.cis.util.nfc.RSAUtils;
import com.yaheen.cis.util.notification.NotificationUtils;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;
import com.yaheen.cis.util.time.CountDownTimerUtils;
import com.yaheen.cis.util.upload.UploadLocationUtils;
import com.yaheen.cis.util.version.VersionUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class LoginActivity extends PermissionActivity {

    private CheckBox cbRPsd;

    private ImageView ivLogin;

    private LinearLayout llRPsd, llVer;

    private TextView tvLogin, tvChange, tvGetVer;

    private EditText etName, etPsd, etIp, etPhone, etVerification;

    private CountDownTimerUtils countDownTimerUtils;

    private String url = baseUrl + "/eapi/wlogin.do";

    private String getVerUrl = baseUrl + "/eapi/getVerifyCode.do";

    private String key = "TkxwSEowNVBUT2hoN3lYTQ==";

    //用户获取的验证码
    private String ver = "";

    //是否手机号登录
    private boolean isPhone = false, isCount = false;

    //账号是否在巡查
    private String loginNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitleContent(R.string.login_title);
        initView();

        VersionUtils.checkVersion(LoginActivity.this);

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
            }
        });
    }

    private void initView() {

        etVerification = findViewById(R.id.et_verification);
        cbRPsd = findViewById(R.id.cb_remember_password);
        llRPsd = findViewById(R.id.ll_remember_password);
        llVer = findViewById(R.id.ll_verification);
        tvGetVer = findViewById(R.id.tv_get_ver);
        etName = findViewById(R.id.et_username);
        tvChange = findViewById(R.id.tv_change);
        etPsd = findViewById(R.id.et_password);
        ivLogin = findViewById(R.id.iv_login);
        tvLogin = findViewById(R.id.tv_login);
        etPhone = findViewById(R.id.et_phone);
        etIp = findViewById(R.id.et_ip);

        etPsd.setText(DefaultPrefsUtil.getUserPassword());
        etName.setText(DefaultPrefsUtil.getUserName());

        changeView();

        tvChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPhone.setEnabled(true);
                changePhoneView();
            }
        });

        tvGetVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开始计时
                setCountTime();
            }
        });

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                loginNumber = "";
            }
        });

    }

    public void changePhoneView() {
        if (isPhone) {
            isPhone = false;
            llVer.setVisibility(View.GONE);
            etPhone.setVisibility(View.GONE);
            etPsd.setVisibility(View.VISIBLE);
            cbRPsd.setVisibility(View.VISIBLE);
            etName.setVisibility(View.VISIBLE);
            tvChange.setText(R.string.login_phone);
        } else {
            isPhone = true;
            etPsd.setVisibility(View.GONE);
            cbRPsd.setVisibility(View.GONE);
            etName.setVisibility(View.GONE);
            llVer.setVisibility(View.VISIBLE);
            etPhone.setVisibility(View.VISIBLE);
            tvChange.setText(R.string.login_account);
        }
    }


    /**
     * 判断上次巡查是否结束
     */
    private void checkRecord() {
        String typeStr = DefaultPrefsUtil.getPatrolType();
        if (isPhone) {
            loginPhone();
        } else {
            loginUserName();
        }
    }

    private void setCountTime() {
        String phone = etPhone.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            showToast(R.string.login_phone_empty);
            return;
        }

        if (!CommonUtils.isPhoneNumber(phone)) {
            showToast(R.string.login_phone_not_right);
            return;
        }

        if (countDownTimerUtils != null && isCount) {
            return;
        }

        //开始计时并请求验证码
        getVerification(phone);
        isCount = true;

        countDownTimerUtils = CountDownTimerUtils.getCountDownTimer()
                .setMillisInFuture(61 * 1000)
                .setCountDownInterval(1000)
                .setTickDelegate(new CountDownTimerUtils.TickDelegate() {
                    @Override
                    public void onTick(long pMillisUntilFinished) {
                        tvGetVer.setText("重新发送(" + (int) pMillisUntilFinished / 1000 + "秒)");
                    }
                })
                .setFinishDelegate(new CountDownTimerUtils.FinishDelegate() {
                    @Override
                    public void onFinish() {
                        tvGetVer.setText(R.string.login_get_verification);
                        if (countDownTimerUtils != null) {
                            countDownTimerUtils.cancel();
                            countDownTimerUtils = null;
                            isCount = false;
                        }
                    }
                });
        countDownTimerUtils.start();
    }

    private void getVerification(String phone) {

        if (TextUtils.isEmpty(phone)) {
            showToast(R.string.login_phone_empty);
            return;
        }

        ver = "";

        RequestParams requestParams = new RequestParams(getVerUrl);
        requestParams.addQueryStringParameter("mobile", phone);
        requestParams.addQueryStringParameter("number", loginNumber);

        HttpUtils.getPostHttp(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                GetVerBean data = gson.fromJson(result, GetVerBean.class);
                if (data != null && data.isResult()) {
                    ver = data.getCode();
                    if (TextUtils.isEmpty(loginNumber) && data.getNumber() != null) {
                        DialogUtils.showDialog(LoginActivity.this, "此账号正在巡查，是否继续登录", new DialogCallback() {
                            @Override
                            public void callback() {
                                loginNumber = "1001";
                            }
                        }, null);
                    }
                } else {
                    showToast(R.string.login_get_verification_fail);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(R.string.login_get_verification_fail);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    private void loginPhone() {

        final String phone = etPhone.getText().toString();
        final String ver = etVerification.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            showToast(R.string.login_phone_empty);
            return;
        } else if (!CommonUtils.isPhoneNumber(phone)) {
            showToast(R.string.login_phone_not_right);
            return;
        }

        if (TextUtils.isEmpty(ver)) {
            showToast(R.string.login_verification_empty);
            return;
        } else if (!this.ver.equals(ver)) {
            showToast(R.string.login_verification_not_right);
            return;
        }

        showLoadingDialog();

        RequestParams requestParams = new RequestParams(url);
        requestParams.addQueryStringParameter("mobile", phone);
        requestParams.addQueryStringParameter("loginFlag", "M");
        requestParams.addQueryStringParameter("devFlag", "APP");
        requestParams.addQueryStringParameter("hardwareId", FreeHandSystemUtil.getSafeUUID(getApplicationContext()));
        if (loginNumber.equals("1001")) {
            requestParams.addQueryStringParameter("number", loginNumber);
        }

        HttpUtils.getPostHttp(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                LoginBean data = gson.fromJson(result, LoginBean.class);
                if (data != null) {
                    if (data.isResult()) {
                        DefaultPrefsUtil.setRole(data.getRole());
                        DefaultPrefsUtil.setToken(data.getToken());
                        DefaultPrefsUtil.setPhone(data.getMobile());
//                        DefaultPrefsUtil.setUserName(data.getUsername());
                        DefaultPrefsUtil.setUnitName(data.getUnitName());
                        DefaultPrefsUtil.setHouseLatitude(data.getLatitude());
                        DefaultPrefsUtil.setHouseLongitude(data.getLongitude());

                        Intent intent = new Intent(LoginActivity.this, TurnActivity.class);
                        //账号正在巡查，返回巡查信息
                        if (data.getTypeArr() != null && data.getTypeArr().size() > 0 && !TextUtils.isEmpty(data.getRecordId())) {
                            TypeBean typeBean = new TypeBean();
                            typeBean.setTypeArr(data.getTypeArr());
                            typeBean.setRecordId(data.getRecordId());
                            typeBean.setRecordStartTime(data.getRecordStartTime());
//                            intent.putExtra("type", gson.toJson(typeBean));
                            DefaultPrefsUtil.setPatrolType(gson.toJson(typeBean));
                        }
                        startActivity(intent);
                    } else {
                        loginNumber = "";
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

    private void loginUserName() {

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
        requestParams.addQueryStringParameter("number", "");
        requestParams.addQueryStringParameter("devFlag", "APP");
        requestParams.addQueryStringParameter("loginFlag", "N");
        requestParams.addQueryStringParameter("password", Base64Utils.encode(AESUtils.encrypt(psd, key)));
        requestParams.addQueryStringParameter("hardwareId", FreeHandSystemUtil.getSafeUUID(getApplicationContext()));

        HttpUtils.getPostHttp(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                final LoginBean data = gson.fromJson(result, LoginBean.class);
                if (data != null) {
                    if (data.isResult()) {
                        //不记住密码则保存空字符串
                        if (cbRPsd.isChecked()) {
                            DefaultPrefsUtil.setUserPassword(psd);
                        } else {
                            DefaultPrefsUtil.setUserPassword("");
                        }
                        DefaultPrefsUtil.setRole(data.getRole());
                        DefaultPrefsUtil.setToken(data.getToken());
                        DefaultPrefsUtil.setPhone(data.getMobile());
                        DefaultPrefsUtil.setUserName(data.getUsername());
                        DefaultPrefsUtil.setUnitName(data.getUnitName());
                        DefaultPrefsUtil.setHouseLatitude(data.getLatitude());
                        DefaultPrefsUtil.setHouseLongitude(data.getLongitude());

                        Intent intent = new Intent(LoginActivity.this, TurnActivity.class);
                        //账号正在巡查，返回巡查信息
                        if (data.getTypeArr() != null && data.getTypeArr().size() > 0 && !TextUtils.isEmpty(data.getRecordId())) {
                            TypeBean typeBean = new TypeBean();
                            typeBean.setTypeArr(data.getTypeArr());
                            typeBean.setRecordId(data.getRecordId());
                            typeBean.setRecordStartTime(data.getRecordStartTime());
//                            intent.putExtra("type", gson.toJson(typeBean));
                            DefaultPrefsUtil.setPatrolType(gson.toJson(typeBean));
                        }
                        startActivity(intent);
                    } else if (data.getCode() == 1001) {
                        DialogUtils.showDialog(LoginActivity.this, "此账号正在巡查，是否继续登录", new DialogCallback() {
                            @Override
                            public void callback() {
                                etPhone.setText(data.getMobile());
                                etPhone.setEnabled(false);
                                loginNumber = "1001";
                                changePhoneView();
                            }
                        }, null);
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

    private void changeView() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ivLogin.getLayoutParams();
        params.height = (int) (FreeHandScreenUtil.getScreenWidthAveragePart(getApplicationContext(), 1) / 8 * 5);
        ivLogin.setLayoutParams(params);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
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
        if (countDownTimerUtils != null) {
            countDownTimerUtils.cancel();
            countDownTimerUtils = null;
            isCount = false;
        }
    }
}
