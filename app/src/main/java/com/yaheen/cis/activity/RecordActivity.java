package com.yaheen.cis.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.PermissionActivity;
import com.yaheen.cis.adapter.DataServer;
import com.yaheen.cis.adapter.RecordAdapter;
import com.yaheen.cis.entity.RecordBean;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class RecordActivity extends PermissionActivity {

    private RecyclerView rvRecord;

    private TextView tvTime,tvDuration,tvType,tvDescribe,tvDetail;

    private RecordAdapter recordAdapter;

    private String recordUrl = "http://192.168.199.119:8080/crs/eapi/recordList.do";

    private String recordId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        showLoadingDialog();
        setBackVisible(false);

        tvTime = findViewById(R.id.tv_time);
        tvType = findViewById(R.id.tv_type);
        tvDescribe = findViewById(R.id.tv_describe);
        tvDuration = findViewById(R.id.tv_duration);
        rvRecord = findViewById(R.id.rv_record);
        rvRecord.setLayoutManager(new LinearLayoutManager(this));

        recordAdapter = new RecordAdapter();
        rvRecord.setAdapter(recordAdapter);

        recordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(RecordActivity.this, RecordMapActivity.class);
                recordId = recordAdapter.getData().get(position).getId();
                intent.putExtra("recordId", recordId);
                startActivity(intent);
            }
        });

        getRecordList();
    }

    private void getRecordList() {
        RequestParams requestParams = new RequestParams(recordUrl);
        requestParams.addQueryStringParameter("token", DefaultPrefsUtil.getToken());
        requestParams.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;");

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                RecordBean data = gson.fromJson(result, RecordBean.class);
                if (data != null && data.isResult()) {
                    recordAdapter.setDatas(data.getRecordArr());
                    recordAdapter.notifyDataSetChanged();
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
