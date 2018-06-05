package com.yaheen.cis.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
    }
}
