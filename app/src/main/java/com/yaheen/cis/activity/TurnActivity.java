package com.yaheen.cis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yaheen.cis.BaseApp;
import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.BaseActivity;
import com.yaheen.cis.entity.QuestionBean;
import com.yaheen.cis.entity.TypeBean;
import com.yaheen.cis.util.DialogUtils;
import com.yaheen.cis.util.dialog.DialogCallback;
import com.yaheen.cis.util.dialog.IDialogCancelCallback;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;

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
                showLoadingDialog();
                checkRecord();
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
        if (TextUtils.isEmpty(DefaultPrefsUtil.getPatrolType()) ||
                TextUtils.isEmpty(DefaultPrefsUtil.getPatrolqQuestion())) {
            Intent intent = new Intent(TurnActivity.this, PatrolSettingActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(TurnActivity.this, DetailActivity.class);
            intent.putExtra("type", DefaultPrefsUtil.getPatrolType());
            intent.putExtra("question", DefaultPrefsUtil.getPatrolqQuestion());
            startActivity(intent);
        }
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
