package com.yaheen.cis.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.PermissionActivity;
import com.yaheen.cis.adapter.PatrolTypeAdapter;
import com.yaheen.cis.adapter.RecordAdapter;
import com.yaheen.cis.adapter.ReportRecordAdapter;
import com.yaheen.cis.adapter.ReportUrgencyAdapter;
import com.yaheen.cis.adapter.UrgencyAdapter;
import com.yaheen.cis.entity.RecordBean;
import com.yaheen.cis.util.HttpUtils;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

public class ReportRecordActivity extends PermissionActivity {

    private String recordUrl = baseUrl + "/eapi/recordList.do";

    private RecyclerView rvUrgency, rvRecord;

    private ReportUrgencyAdapter urgencyAdapter;

    private ReportRecordAdapter recordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_record);

        rvUrgency = findViewById(R.id.rv_urgency);
        rvUrgency = findViewById(R.id.rv_urgency);
        rvRecord = findViewById(R.id.rv_record);

        initUrgency();
        initRecordList();

        getRecordList();
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
                }else {
                    urgencyAdapter.getData().get(position).setSelect(true);
                }
                urgencyAdapter.notifyDataSetChanged();
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
//                Intent intent = new Intent(ReportRecordActivity.this, RecordMapActivity.class);
//                recordId = recordAdapter.getData().get(position).getId();
//                intent.putExtra("recordId", recordId);
//                startActivity(intent);
            }
        });
    }

    private void getRecordList() {
        RequestParams requestParams = new RequestParams(recordUrl);
        requestParams.addQueryStringParameter("token", DefaultPrefsUtil.getToken());

        HttpUtils.getPostHttp(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                RecordBean data = gson.fromJson(result, RecordBean.class);
                if (data != null && data.isResult()) {
                    recordAdapter.setDatas(data.getRecordArr());
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
}
