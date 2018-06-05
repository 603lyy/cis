package com.yaheen.cis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yaheen.cis.R;

public class TurnActivity extends BaseActivity {

    private TextView tvPatrol,tvRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn);

        tvPatrol = findViewById(R.id.tv_patrol);
        tvRecord = findViewById(R.id.tv_record);

        tvRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TurnActivity.this,RecordActivity.class);
                startActivity(intent);
            }
        });

        tvPatrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TurnActivity.this,PatrolSettingActivity.class);
                startActivity(intent);
            }
        });
    }
}
