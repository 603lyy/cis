package com.yaheen.cis.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.PermissionActivity;
import com.yaheen.cis.util.version.VersionUtils;

public class SettingActivity extends PermissionActivity {

    LinearLayout llChangePsd, llCheckVersion, llBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    private void initView() {
        llBack = findViewById(R.id.back);
        llChangePsd = findViewById(R.id.ll_change_password);
        llCheckVersion = findViewById(R.id.ll_check_version);

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        llCheckVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VersionUtils.checkVersion(SettingActivity.this, true);
            }
        });

        llChangePsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ChangePsdActivity.class);
                startActivity(intent);
            }
        });
    }
}
