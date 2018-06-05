package com.yaheen.cis.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yaheen.cis.R;
import com.yaheen.cis.adapter.DataServer;
import com.yaheen.cis.adapter.RecordAdapter;

public class RecordActivity extends Activity {

    private RecyclerView rvRecord;

    private RecordAdapter recordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        rvRecord = findViewById(R.id.rv_record);
        rvRecord.setLayoutManager(new LinearLayoutManager(this));

        recordAdapter = new RecordAdapter();
        recordAdapter.setDatas(DataServer.getSampleData(10));
        rvRecord.setAdapter(recordAdapter);

        recordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(RecordActivity.this, RecordMapActivity.class);
                startActivity(intent);
            }
        });
    }
}
