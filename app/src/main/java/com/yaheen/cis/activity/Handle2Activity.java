package com.yaheen.cis.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.PermissionActivity;
import com.yaheen.cis.entity.CommonBean;
import com.yaheen.cis.entity.ReportBean;
import com.yaheen.cis.util.HttpUtils;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

public class Handle2Activity extends PermissionActivity {

    private String commitUrl = "";

    private String reportUrl = "";

    private EditText etDes;

    private TextView tvCommit;

    private LinearLayout llBack;

    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle2);

        llBack = findViewById(R.id.back);
        etDes = findViewById(R.id.et_describe);
        tvCommit = findViewById(R.id.tv_commit);

        eventId = getIntent().getStringExtra("eventId");

        initData();

        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendReport();
            }
        });

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initData() {
        commitUrl = getBaseUrl() + "/eapi/handleEvent.do";

        reportUrl = getBaseUrl() + "/eapi/report.do";
    }

    /**
     * 上报事件
     */
    private void sendReport() {

        if (TextUtils.isEmpty(etDes.getText().toString())) {
            showToast(R.string.handle_describe_empty);
            return;
        }

        showLoadingDialog();

        RequestParams requestParams = new RequestParams(reportUrl);
        requestParams.addQueryStringParameter("token", DefaultPrefsUtil.getToken());
        requestParams.addQueryStringParameter("role", DefaultPrefsUtil.getRole());
        requestParams.addQueryStringParameter("eventId", eventId);
        requestParams.addQueryStringParameter("suggest", etDes.getText().toString());


        HttpUtils.getPostHttp(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ReportBean data = gson.fromJson(result, ReportBean.class);
                if (data != null && data.isResult()) {
                    showToast(R.string.detail_commit_success);
                    finish();
                } else if (data != null && data.getCode() == 1002) {
                    startActivity(new Intent(Handle2Activity.this, LoginActivity.class));
                    finish();
                } else {
                    showToast(R.string.detail_commit_fail);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(R.string.detail_commit_fail);
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

    private void commitEvent() {

        if (TextUtils.isEmpty(etDes.getText().toString())) {
            showToast(R.string.handle_describe_empty);
            return;
        }

        showLoadingDialog();

        RequestParams requestParams = new RequestParams(commitUrl);
        requestParams.addQueryStringParameter("token", DefaultPrefsUtil.getToken());
        requestParams.addQueryStringParameter("eventId", eventId);
        requestParams.addQueryStringParameter("content", etDes.getText().toString());

        HttpUtils.getPostHttp(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CommonBean data = gson.fromJson(result, CommonBean.class);
                if (data != null && data.isResult()) {
                    startActivity(new Intent(Handle2Activity.this, ReportRecordActivity.class));
                    finish();
                } else if (data != null && data.getCode() == 1002) {
                    startActivity(new Intent(Handle2Activity.this, LoginActivity.class));
                    finish();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(R.string.handle_commit_fail);
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
