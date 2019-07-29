package com.yaheen.cis.activity;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.yaheen.cis.BaseApp;
import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.PermissionActivity;
import com.yaheen.cis.entity.CheckBean;
import com.yaheen.cis.entity.CommonBean;
import com.yaheen.cis.entity.QuestionBean;
import com.yaheen.cis.entity.TypeBean;
import com.yaheen.cis.util.DialogUtils;
import com.yaheen.cis.util.HttpUtils;
import com.yaheen.cis.util.common.FreeHandScreenUtil;
import com.yaheen.cis.util.common.ScreenManager;
import com.yaheen.cis.util.dialog.DialogCallback;
import com.yaheen.cis.util.dialog.IDialogCancelCallback;
import com.yaheen.cis.util.nfc.Base64Utils;
import com.yaheen.cis.util.notification.NotificationUtils;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;
import com.yaheen.cis.util.upload.UploadLocationUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

public class TurnActivity extends PermissionActivity {

    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;

    private String typeUrl = baseUrl + "/eapi/findTypeByUserId.do";

    private String checkUrl = "http://shortlink.cn/eai/getShortLinkCompleteInformation.do";

    private String checkIdUrl = "http://whn.020szsq.com:8088/houseNumbers/getGridInspectionPoint.do";

    private TextView tvPatrol, tvRecord, tvUpload;

    private ImageView ivTurn;

    private TextView tvFetch;

    private LinearLayout llBack;

    private String houseId;

    //巡查问题类型
//    private String typeStr = "";

    //成功扫码则为true，否则为false
    private boolean isFetch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn_2);

        llBack = findViewById(R.id.back);
        ivTurn = findViewById(R.id.iv_turn);
        tvPatrol = findViewById(R.id.tv_patrol);
        tvRecord = findViewById(R.id.tv_record);
        tvUpload = findViewById(R.id.tv_upload);

//        typeStr = getIntent().getStringExtra("type");

        changeView();

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
            }
        });

        tvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TurnActivity.this, ReportRecordActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * 判断上次巡查是否结束
     */
    private void checkRecord() {
        String typeStr = DefaultPrefsUtil.getPatrolType();
        if (TextUtils.isEmpty(typeStr)) {
            Intent intent = new Intent(TurnActivity.this, PatrolSettingActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(TurnActivity.this, DetailActivity.class);
            intent.putExtra("type", typeStr);
            startActivity(intent);
        }
//        String typeStr = DefaultPrefsUtil.getPatrolType();
//        String questionStr = DefaultPrefsUtil.getPatrolqQuestion();
//        if (TextUtils.isEmpty(typeStr)) {
//            Intent intent = new Intent(TurnActivity.this, PatrolSettingActivity.class);
//            startActivity(intent);
//        } else {
//            Intent intent = new Intent(TurnActivity.this, DetailActivity.class);
//            intent.putExtra("type", typeStr);
//            intent.putExtra("question", questionStr);
//            startActivity(intent);
//        }
    }

    private void getTypeList() {

        RequestParams requestParams = new RequestParams(typeUrl);
        requestParams.addQueryStringParameter("token", DefaultPrefsUtil.getToken());

        HttpUtils.getPostHttp(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                TypeBean data = gson.fromJson(result, TypeBean.class);
                if (data != null && data.isResult()) {
//                    getQuestion(data);
                    Intent intent = new Intent(TurnActivity.this, DetailPointActivity.class);
                    intent.putExtra("type", gson.toJson(data));
                    intent.putExtra("sign", true);
                    intent.putExtra("houseId", houseId);
                    startActivity(intent);
                } else if (data != null && data.getCode() == 1002) {
                    startActivity(new Intent(TurnActivity.this, LoginActivity.class));
                    finish();
                } else {
                    cancelLoadingDialog();
                    showToast(R.string.scan_fail);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(R.string.scan_fail);
                cancelLoadingDialog();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    //请求门牌系统，判断是否是巡查点
    private void checkId(CheckBean.EntityBean data) {

        houseId = data.getLink().substring(data.getLink().lastIndexOf("=") + 1);

        if (TextUtils.isEmpty(houseId)) {
            cancelLoadingDialog();
            return;
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("houseNumberId", houseId);

        RequestParams params = new RequestParams(checkIdUrl);
        params.addQueryStringParameter("json", Base64Utils.encode(jsonObject.toString().getBytes()));

        HttpUtils.getPostHttp(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CommonBean data = gson.fromJson(result, CommonBean.class);
                if (data != null) {
                    if (data.isResult()) {
                        getTypeList();
                    } else if (data.getMsg() == null) {
//                        showToast(R.string.not_id);
                        houseId = "";
                        getTypeList();
//                        cancelLoadingDialog();
                    } else {
                        showToast(R.string.turn_id_empty);
                        cancelLoadingDialog();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                cancelLoadingDialog();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void check(String slink) {

        if (TextUtils.isEmpty(slink)) {
            Toast.makeText(this, R.string.short_link_empty, Toast.LENGTH_SHORT).show();
            cancelLoadingDialog();
            return;
        }
        slink = slink.substring(slink.lastIndexOf("/") + 1);

        RequestParams params = new RequestParams(checkUrl);
        params.addQueryStringParameter("key", "7zbQUBNY0XkEcUoushaJD7UcKyWkc91q");
        params.addQueryStringParameter("shortLinkCode", slink);

        HttpUtils.getPostHttp(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CheckBean checkBean = gson.fromJson(result, CheckBean.class);
                if (checkBean != null && checkBean.isResult()) {
                    checkId(checkBean.getEntity());
                } else {
                    Toast.makeText(TurnActivity.this, R.string.scan_not, Toast.LENGTH_SHORT).show();
                    cancelLoadingDialog();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(TurnActivity.this, R.string.scan_fail, Toast.LENGTH_SHORT).show();
                cancelLoadingDialog();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    private void changeView() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ivTurn.getLayoutParams();
        params.height = (int) (FreeHandScreenUtil.getScreenWidthAveragePart(getApplicationContext(), 1) / 8 * 5);
        ivTurn.setLayoutParams(params);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    if (result != null) {
                        showLoadingDialog();
                        isFetch = true;
                        check(result);
                    } else {
                        cancelLoadingDialog();
                        Toast.makeText(this, "解析二维码失败", Toast.LENGTH_LONG).show();
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    cancelLoadingDialog();
                    Toast.makeText(this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            cancelLoadingDialog();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFetch) {
            cancelLoadingDialog();
        }
        isFetch = false;
    }

    @Override
    public void onBackPressed() {
        DialogUtils.showDialog(TurnActivity.this, "确定要退出该APP吗？", new DialogCallback() {
            @Override
            public void callback() {
                ScreenManager.getScreenManagerInstance(TurnActivity.this).finishActivities();
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
