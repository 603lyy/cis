package com.yaheen.cis.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.PermissionActivity;
import com.yaheen.cis.entity.CommonBean;
import com.yaheen.cis.entity.ReportBean;
import com.yaheen.cis.entity.SubUserBean;
import com.yaheen.cis.util.HttpUtils;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

public class Handle2Activity extends PermissionActivity {

    private String commitUrl = "";

    private String reportUrl = "";

    private String subUserUrl = "";

    private String trustUrl = "";

    private EditText etDes;

    private TextView tvCommit, tvSelect;

    private LinearLayout llBack, llSelect;

    private String eventId;

    private int selectIndex = 0;

    private List<SubUserBean.UserListBean> subUserList = new ArrayList<>();

    private boolean isTracking;

    private OptionsPickerView pvCustomOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle2);

        llBack = findViewById(R.id.back);
        etDes = findViewById(R.id.et_describe);
        tvSelect = findViewById(R.id.tv_select);
        tvCommit = findViewById(R.id.tv_commit);
        llSelect = findViewById(R.id.ll_select);

        eventId = getIntent().getStringExtra("eventId");
        isTracking = getIntent().getBooleanExtra("isTracking", false);

        initData();
        initCustomOptionPicker();
        getSubUsers();

        if (isTracking) {
            etDes.setHint(R.string.handle_trust_hint);
            llSelect.setVisibility(View.VISIBLE);
        } else {
            etDes.setHint(R.string.handle_send_report_hint);
            llSelect.setVisibility(View.GONE);
        }

        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTracking) {
                    trustReport();
                } else {
                    sendReport();
                }
            }
        });

        tvSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvCustomOptions.show();
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
        trustUrl = getBaseUrl() + "/eapi/submitDelegateTasks.do";
        subUserUrl = getBaseUrl() + "/eapi/findSubUser.do";
        commitUrl = getBaseUrl() + "/eapi/handleEvent.do";
        reportUrl = getBaseUrl() + "/eapi/report.do";
    }

    private void initCustomOptionPicker() {//条件选择器初始化，自定义布局
        /**
         * @description
         *
         * 注意事项：
         * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
         * 具体可参考demo 里面的两个自定义layout布局。
         */
        pvCustomOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                tvSelect.setText(subUserList.get(options1).getUsername());
                selectIndex = options1;
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        TextView tvCancel = (TextView) v.findViewById(R.id.tv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.returnData();
                                pvCustomOptions.dismiss();
                            }
                        });

                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomOptions.dismiss();
                            }
                        });

                    }
                })
                .isDialog(true)
                .setOutSideCancelable(false)
                .build();
    }

    private void getSubUsers() {

        RequestParams requestParams = new RequestParams(subUserUrl);
        requestParams.addQueryStringParameter("token", DefaultPrefsUtil.getToken());
        requestParams.addQueryStringParameter("role", DefaultPrefsUtil.getRole());
        requestParams.addQueryStringParameter("eventId", eventId);

        HttpUtils.getPostHttp(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                SubUserBean data = gson.fromJson(result, SubUserBean.class);
                subUserList = data.getUserList();

                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < subUserList.size(); i++) {
                    list.add(subUserList.get(i).getUsername());
                }
                pvCustomOptions.setPicker(list);
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
                    startActivity(new Intent(Handle2Activity.this, ReportRecordActivity.class));
                    showToast(R.string.detail_commit_success);
                    finish();
                } else if (data != null && data.getCode() == 1002) {
                    startActivity(new Intent(Handle2Activity.this, LoginActivity.class));
                    showToast("该账号被别人登陆了");
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

    private void trustReport() {

        showLoadingDialog();

        RequestParams requestParams = new RequestParams(trustUrl);
        requestParams.addQueryStringParameter("eventId", eventId);
        requestParams.addQueryStringParameter("token", DefaultPrefsUtil.getToken());
        requestParams.addQueryStringParameter("describe", etDes.getText().toString());
        requestParams.addQueryStringParameter("subUserId", subUserList.get(selectIndex).getId());


        HttpUtils.getPostHttp(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ReportBean data = gson.fromJson(result, ReportBean.class);
                if (data != null && data.isResult()) {
                    startActivity(new Intent(Handle2Activity.this, ReportRecordActivity.class));
                    showToast(R.string.handle_trust_success);
                    finish();
                } else if (data != null && data.getCode() == 1002) {
                    startActivity(new Intent(Handle2Activity.this, LoginActivity.class));
                    showToast("该账号被别人登陆了");
                    finish();
                } else {
                    showToast(R.string.handle_trust_fail);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(R.string.handle_trust_fail);
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
                    showToast("该账号被别人登陆了");
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
