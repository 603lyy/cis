package com.yaheen.cis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.BaseActivity;
import com.yaheen.cis.adapter.DataServer;
import com.yaheen.cis.adapter.PatrolSettingAdapter;
import com.yaheen.cis.entity.TypeBean;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class PatrolSettingActivity extends BaseActivity {

    RecyclerView rvSetting;

    PatrolSettingAdapter settingAdapter;

    private String url = "http://192.168.199.111:8080/crs/eapi/findTypeByUserId.do";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrol_setting);

        rvSetting = findViewById(R.id.rv_setting);
        rvSetting.setLayoutManager(new GridLayoutManager(this, 3));

        settingAdapter = new PatrolSettingAdapter();
        settingAdapter.addHeaderView(getHeaderView());
        rvSetting.setAdapter(settingAdapter);

        showLoadingDialog();
        getTypeList();
    }

    private View getHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.header_patrol_setting,
                (ViewGroup) rvSetting.getParent(), false);
        ImageView ivStart = view.findViewById(R.id.iv_start);

        ivStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatrolSettingActivity.this, DetailActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void getTypeList() {

        RequestParams requestParams = new RequestParams(url);
        requestParams.addQueryStringParameter("token", DefaultPrefsUtil.getToken());
        requestParams.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;");
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                TypeBean data = gson.fromJson(result, TypeBean.class);
                if (data != null && data.isResult()) {
                    settingAdapter.setDatas(data.getTypeArr());
                    settingAdapter.notifyDataSetChanged();
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
