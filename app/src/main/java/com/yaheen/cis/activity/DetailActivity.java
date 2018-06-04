package com.yaheen.cis.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.yaheen.cis.R;
import com.yaheen.cis.adapter.DataServer;
import com.yaheen.cis.adapter.PatrolSettingAdapter;
import com.yaheen.cis.util.map.BDMapUtils;

public class DetailActivity extends PermissionActivity {

    private TextView tvLocation;

    RecyclerView rvProblem;

    PatrolSettingAdapter problemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initView();
    }

    private void initView() {

        tvLocation = findViewById(R.id.tv_location_describe);

        rvProblem = findViewById(R.id.rv_problem);
        rvProblem.setLayoutManager(new GridLayoutManager(this,4));

        problemAdapter = new PatrolSettingAdapter();
        problemAdapter.setDatas(DataServer.getSampleData(10));
        rvProblem.setAdapter(problemAdapter);

        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BDMapUtils.getLocation()!=null){
                    tvLocation.setText(BDMapUtils.getLocation().getAddrStr());
                }
            }
        });
    }
}
