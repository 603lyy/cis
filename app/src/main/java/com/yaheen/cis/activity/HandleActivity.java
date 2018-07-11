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
import com.yaheen.cis.util.HttpUtils;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

public class HandleActivity extends PermissionActivity {

    private String commitUrl = baseUrl + "/eapi/handleEvent.do";

    private EditText etDes;

    private TextView tvCommit;

    private LinearLayout llBack;

    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle);

        llBack = findViewById(R.id.back);
        etDes = findViewById(R.id.et_describe);
        tvCommit = findViewById(R.id.tv_commit);

        eventId = getIntent().getStringExtra("eventId");

        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commitEvent();
            }
        });

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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
                    startActivity(new Intent(HandleActivity.this, ReportRecordActivity.class));
                    finish();
                } else if (data != null && data.getCode() == 1002) {
                    startActivity(new Intent(HandleActivity.this, LoginActivity.class));
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
