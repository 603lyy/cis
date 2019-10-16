package com.yaheen.cis.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.PermissionActivity;
import com.yaheen.cis.entity.CommonBean;
import com.yaheen.cis.entity.GetVerBean;
import com.yaheen.cis.entity.LoginBean;
import com.yaheen.cis.entity.TypeBean;
import com.yaheen.cis.util.DialogUtils;
import com.yaheen.cis.util.HttpUtils;
import com.yaheen.cis.util.common.CommonUtils;
import com.yaheen.cis.util.common.FreeHandSystemUtil;
import com.yaheen.cis.util.dialog.DialogCallback;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;
import com.yaheen.cis.util.time.CountDownTimerUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

public class ChangePsdActivity extends PermissionActivity {

    private TextView tvGetVer, tvConfirm;

    private EditText etPsd, etPsdConfirm, etPhone, etVerification;

    private CountDownTimerUtils countDownTimerUtils;

    private boolean isPhone = false, isCount = false;

    private String getVerUrl = "", changePsdUrl = "";

    //用户获取的验证码
    private String ver = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_psd2);

        initData();
        initView();
    }

    private void initData() {
        changePsdUrl = getBaseUrl() + "/eapi/resetPassword.do";
        getVerUrl = getBaseUrl() + "/eapi/getVerifyCode.do";
    }

    private void initView() {

        etPsdConfirm = findViewById(R.id.et_password_confirm);
        etVerification = findViewById(R.id.et_verification);
        tvConfirm = findViewById(R.id.tv_confirm);
        tvGetVer = findViewById(R.id.tv_get_ver);
        etPsd = findViewById(R.id.et_password);
        etPhone = findViewById(R.id.et_phone);
        llBack = findViewById(R.id.back);

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tvGetVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开始计时
                setCountTime();
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

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
        requestParams.addQueryStringParameter("flag", "reset");
//        requestParams.addQueryStringParameter("number", loginNumber);

        HttpUtils.getPostHttp(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                GetVerBean data = gson.fromJson(result, GetVerBean.class);
                if (data != null && data.isResult()) {
                    ver = data.getCode();
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

    private void changePassword() {

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

        if (TextUtils.isEmpty(etPsd.getText())) {
            showToast(R.string.login_password_empty);
            return;
        } else if (TextUtils.isEmpty(etPsdConfirm.getText())) {
            showToast(R.string.confirm_password_empty);
            return;
        } else if (!etPsd.getText().toString().equals(etPsdConfirm.getText().toString())) {
            showToast(R.string.password_not_Inconsistent);
            return;
        }

        showLoadingDialog();

        RequestParams requestParams = new RequestParams(changePsdUrl);
        requestParams.addQueryStringParameter("mobile", phone);
        requestParams.addQueryStringParameter("password", etPsd.getText().toString());
        requestParams.addQueryStringParameter("ensurePwd", etPsdConfirm.getText().toString());

        HttpUtils.getPostHttp(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CommonBean data = gson.fromJson(result, CommonBean.class);
                if (data != null) {
                    if (data.isResult()) {
                        finish();
                    }
                    showToast(data.getMsg());
                } else {
                    showToast(R.string.change_password_fail);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(R.string.change_password_fail);
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
}
