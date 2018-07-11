package com.yaheen.cis.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.PermissionActivity;
import com.yaheen.cis.util.HttpUtils;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

public class HandleActivity extends PermissionActivity {

    private String commitUrl = baseUrl + "";

    private EditText etDes;

    private TextView tvCommit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle);

        etDes = findViewById(R.id.et_describe);
        tvCommit = findViewById(R.id.tv_commit);

        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commitEvent();
            }
        });
    }

    private void commitEvent() {

        showLoadingDialog();

        RequestParams requestParams = new RequestParams(commitUrl);
        requestParams.addQueryStringParameter("token", DefaultPrefsUtil.getToken());

        HttpUtils.getPostHttp(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

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
