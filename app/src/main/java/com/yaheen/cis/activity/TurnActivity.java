package com.yaheen.cis.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yaheen.cis.BaseApp;
import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.BaseActivity;
import com.yaheen.cis.entity.QuestionBean;
import com.yaheen.cis.entity.TypeBean;
import com.yaheen.cis.service.UploadLocationService;
import com.yaheen.cis.util.DialogUtils;
import com.yaheen.cis.util.dialog.DialogCallback;
import com.yaheen.cis.util.dialog.IDialogCancelCallback;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class TurnActivity extends BaseActivity {

    private String typeUrl = baseUrl + "/eapi/findTypeByUserId.do";

    private String questionUrl = baseUrl + "/eapi/findQuestionaireByTypeId.do";

    private TextView tvPatrol, tvRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn);

        tvPatrol = findViewById(R.id.tv_patrol);
        tvRecord = findViewById(R.id.tv_record);

        tvRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TurnActivity.this, RecordActivity.class);
                startActivity(intent);
            }
        });

        tvPatrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoadingDialog();
                checkRecord();
//                getTypeList();
            }
        });

    }

    /**
     * 判断上次巡查是否结束
     */
    private void checkRecord() {
//        DefaultPrefsUtil.setPatrolqQuestion("");
//        DefaultPrefsUtil.setPatrolStart(0);
//        DefaultPrefsUtil.setPatrolType("");
        String typeStr = DefaultPrefsUtil.getPatrolType();
        String questionStr = DefaultPrefsUtil.getPatrolqQuestion();
        if (TextUtils.isEmpty(typeStr) || TextUtils.isEmpty(questionStr)) {
            Intent intent = new Intent(TurnActivity.this, PatrolSettingActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(TurnActivity.this, DetailActivity.class);
            intent.putExtra("type", typeStr);
            intent.putExtra("question", questionStr);
            startActivity(intent);
        }
    }

    private void getTypeList() {

        RequestParams requestParams = new RequestParams(typeUrl);
        requestParams.addQueryStringParameter("token", DefaultPrefsUtil.getToken());
        requestParams.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;");
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                TypeBean data = gson.fromJson(result, TypeBean.class);
                if (data != null && data.isResult()) {
                    getQuestion(data);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    private void getQuestion(final TypeBean typeBean) {
        RequestParams requestParams = new RequestParams(questionUrl);
        requestParams.addQueryStringParameter("token", DefaultPrefsUtil.getToken());
        requestParams.addQueryStringParameter("typeId", typeBean.getTypeArr().get(0).getId());
        requestParams.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;");

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                QuestionBean data = gson.fromJson(result, QuestionBean.class);
                if (data != null && data.isResult()) {
                    Intent intent = new Intent(TurnActivity.this, DetailActivity.class);
                    intent.putExtra("type", gson.toJson(typeBean));
                    intent.putExtra("question", gson.toJson(data));
                    intent.putExtra("sign", true);
                    startActivity(intent);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

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

    @Override
    protected void onResume() {
        super.onResume();
        cancelLoadingDialog();
    }

    @Override
    public void onBackPressed() {
        DialogUtils.showDialog(TurnActivity.this, "确定要退出该APP吗？", new DialogCallback() {
            @Override
            public void callback() {
                BaseApp.exit();
            }
        }, new IDialogCancelCallback() {
            @Override
            public void cancelCallback() {
            }
        });
    }
}
