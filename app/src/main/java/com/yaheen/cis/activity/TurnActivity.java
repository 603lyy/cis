package com.yaheen.cis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yaheen.cis.BaseApp;
import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.BaseActivity;
import com.yaheen.cis.util.DialogUtils;
import com.yaheen.cis.util.dialog.DialogCallback;
import com.yaheen.cis.util.dialog.IDialogCancelCallback;

public class TurnActivity extends BaseActivity {

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
                Intent intent = new Intent(TurnActivity.this, PatrolSettingActivity.class);
                startActivity(intent);
            }
        });
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
