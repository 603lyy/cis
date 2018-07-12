package com.yaheen.cis.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.PermissionActivity;
import com.yaheen.cis.adapter.PatrolTypeAdapter;
import com.yaheen.cis.adapter.RecordAdapter;
import com.yaheen.cis.adapter.ReportRecordAdapter;
import com.yaheen.cis.adapter.ReportUrgencyAdapter;
import com.yaheen.cis.adapter.UrgencyAdapter;
import com.yaheen.cis.entity.RecordBean;
import com.yaheen.cis.entity.ReportRecordBean;
import com.yaheen.cis.util.HttpUtils;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

public class ReportRecordActivity extends PermissionActivity {

    private String recordUrl = baseUrl + "/eapi/findEventListByEmergency.do";

    private RecyclerView rvUrgency, rvRecord;

    private LinearLayout llBack;

    private CheckBox cbTrue, cbFalse;

    private ReportUrgencyAdapter urgencyAdapter;

    private ReportRecordAdapter recordAdapter;

    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_record);

        cbFalse = findViewById(R.id.cb_report_not_handle);
        cbTrue = findViewById(R.id.cb_report_is_handle);
        rvUrgency = findViewById(R.id.rv_urgency);
        rvRecord = findViewById(R.id.rv_record);
        llBack = findViewById(R.id.back);

        initView();
        initUrgency();
        initRecordList();
        getRecordList();
    }

    private void initView() {

        if (DefaultPrefsUtil.getRole().equals("LEADER")) {
            cbTrue.setChecked(false);
        } else {
            cbTrue.setChecked(true);
        }

        cbTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRecordList();
            }
        });

        cbFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRecordList();
            }
        });

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initUrgency() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvUrgency.setLayoutManager(layoutManager);

        urgencyAdapter = new ReportUrgencyAdapter();
        rvUrgency.setAdapter(urgencyAdapter);

        urgencyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (urgencyAdapter.getData().get(position).isSelect()) {
                    urgencyAdapter.getData().get(position).setSelect(false);
                    if (TextUtils.isEmpty(urgencyAdapter.getUrgencyStr())) {
                        urgencyAdapter.getData().get(position).setSelect(true);
                    } else {
                        urgencyAdapter.notifyDataSetChanged();
                        getRecordList();
                    }
                } else {
                    urgencyAdapter.getData().get(position).setSelect(true);
                    urgencyAdapter.notifyDataSetChanged();
                    getRecordList();
                }
            }
        });
    }

    private void initRecordList() {

        recordAdapter = new ReportRecordAdapter();
        rvRecord.setAdapter(recordAdapter);
        rvRecord.setLayoutManager(new LinearLayoutManager(this));

        recordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });

        recordAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_detail) {
                    showLoadingDialog();
                    Intent intent = new Intent(ReportRecordActivity.this, HandleDetailActivity.class);
                    intent.putExtra("eventId", recordAdapter.getData().get(position).getId());
                    if (recordAdapter.getData().get(position).getFlag().equals("N")) {
                        intent.putExtra("handle", false);
                    }
                    startActivity(intent);
                } else if (view.getId() == R.id.tv_state && DefaultPrefsUtil.getRole().equals("LEADER")) {
                    if (DefaultPrefsUtil.getRole().equals("LEADER")
                            && recordAdapter.getData().get(position).getFlag().equals("N")) {
                        showLoadingDialog();
                        Intent intent = new Intent(ReportRecordActivity.this, HandleActivity.class);
                        intent.putExtra("eventId", recordAdapter.getData().get(position).getId());
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void getRecordList() {

        String flag;

        if (TextUtils.isEmpty(urgencyAdapter.getUrgencyStr())) {
            showToast(R.string.record_emergency_empty);
            return;
        }

        if (!cbTrue.isChecked() && !cbFalse.isChecked()) {
            return;
        } else if (cbFalse.isChecked() && cbTrue.isChecked()) {
            flag = "";
        } else if (!cbTrue.isChecked()) {
            flag = "N";
        } else {
            flag = "Y";
        }

        showLoadingDialog();

        RequestParams requestParams = new RequestParams(recordUrl);
        requestParams.addQueryStringParameter("icons", urgencyAdapter.getUrgencyStr());
        requestParams.addQueryStringParameter("token", DefaultPrefsUtil.getToken());
        requestParams.addQueryStringParameter("flag", flag);

        HttpUtils.getPostHttp(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ReportRecordBean data = gson.fromJson(result, ReportRecordBean.class);
                if (data != null && data.isResult()) {
                    recordAdapter.setDatas(data.getEventList());
                    recordAdapter.notifyDataSetChanged();
                } else if (data != null && data.getCode() == 1002) {
                    startActivity(new Intent(ReportRecordActivity.this, LoginActivity.class));
                    finish();
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
        if (isFirst) {
            isFirst = false;
        } else {
            cancelLoadingDialog();
        }
    }
}
