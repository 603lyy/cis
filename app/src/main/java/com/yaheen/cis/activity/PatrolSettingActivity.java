package com.yaheen.cis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yaheen.cis.R;
import com.yaheen.cis.adapter.DataServer;
import com.yaheen.cis.adapter.PatrolSettingAdapter;

public class PatrolSettingActivity extends BaseActivity {

    RecyclerView rvSetting;

    PatrolSettingAdapter settingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrol_setting);

        rvSetting = findViewById(R.id.rv_setting);
        rvSetting.setLayoutManager(new GridLayoutManager(this, 3));

        settingAdapter = new PatrolSettingAdapter();
        settingAdapter.addHeaderView(getHeaderView());
        settingAdapter.setDatas(DataServer.getSampleData(10));
        rvSetting.setAdapter(settingAdapter);
    }

    private View getHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.header_patrol_setting,
                (ViewGroup) rvSetting.getParent(), false);
        ImageView ivStart = view.findViewById(R.id.iv_start);

        ivStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatrolSettingActivity.this,DetailActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
