package com.yaheen.cis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.BaseActivity;
import com.yaheen.cis.adapter.DataServer;
import com.yaheen.cis.adapter.PatrolSettingAdapter;
import com.yaheen.cis.entity.QuestionBean;
import com.yaheen.cis.entity.StartPatrolBean;
import com.yaheen.cis.entity.TypeBean;
import com.yaheen.cis.util.HttpUtils;
import com.yaheen.cis.util.map.BDMapUtils;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class PatrolSettingActivity extends BaseActivity {

    private RecyclerView rvSetting;

    private PatrolSettingAdapter settingAdapter;

    private String typeUrl = baseUrl + "/eapi/findTypeByUserId.do";

    private String startUrl = baseUrl + "/eapi/startPatrol.do";

    private String questionUrl = baseUrl + "/eapi/findQuestionaireByTypeId.do";

    private String recordId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrol_setting);

        //开始定位
        BDMapUtils.startLocation();

        showLoadingDialog();
        initSettingView();
        getTypeList();
    }

    private void initSettingView() {
        rvSetting = findViewById(R.id.rv_setting);
        rvSetting.setLayoutManager(new GridLayoutManager(this, 3));

        settingAdapter = new PatrolSettingAdapter();
        settingAdapter.addHeaderView(getHeaderView());
        rvSetting.setAdapter(settingAdapter);
    }

    private View getHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.header_patrol_setting,
                (ViewGroup) rvSetting.getParent(), false);
        ImageView ivStart = view.findViewById(R.id.iv_start);

        ivStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoadingDialog();
                startPatrol();
            }
        });
        return view;
    }

    private void getTypeList() {

        RequestParams requestParams = new RequestParams(typeUrl);
        requestParams.addQueryStringParameter("token", DefaultPrefsUtil.getToken());
        HttpUtils.getPostHttp(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                TypeBean data = gson.fromJson(result, TypeBean.class);
                if (data != null && data.isResult()) {
                    data.setRecordId(recordId);
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

    private void startPatrol() {

        if (BDMapUtils.getLocation() == null) {
            showToast(R.string.map_init_ing);
            cancelLoadingDialog();
            return;
        }

        if (BDMapUtils.getLocation().getLatitude() < 1 || BDMapUtils.getLocation().getLongitude() < 1) {
            showToast(R.string.map_init_fail);
            cancelLoadingDialog();
            return;
        }

        if (settingAdapter.getTypeBean().getTypeArr().size() == 0) {
            showToast(R.string.setting_start_select);
            cancelLoadingDialog();
            return;
        }

        String typeId = "";
        for (int i = 0; i < settingAdapter.getTypeBean().getTypeArr().size(); i++) {
            typeId = typeId + settingAdapter.getTypeBean().getTypeArr().get(i).getId() + ",";
        }

        BDLocation location = BDMapUtils.getLocation();
        RequestParams requestParams = new RequestParams(startUrl);
        requestParams.addQueryStringParameter("typeId", typeId);
        requestParams.addQueryStringParameter("token", DefaultPrefsUtil.getToken());
        requestParams.addQueryStringParameter("latitude", location.getLatitude() + "");
        requestParams.addQueryStringParameter("longitude", location.getLongitude() + "");

        HttpUtils.getPostHttp(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                StartPatrolBean data = gson.fromJson(result, StartPatrolBean.class);
                TypeBean typeBean = settingAdapter.getTypeBean();
                typeBean.setRecordId(recordId);
                if (data != null && data.isResult()) {
                    recordId = data.getRecordId();
                    typeBean.setRecordId(data.getRecordId());
                    String typeStr = gson.toJson(typeBean);
                    DefaultPrefsUtil.setPatrolType(typeStr);
                    DefaultPrefsUtil.setPatrolRecordId(recordId);

                    Intent intent = new Intent(PatrolSettingActivity.this, DetailActivity.class);
                    intent.putExtra("type", typeStr);
                    startActivity(intent);
                    finish();
                } else {
                    showToast(R.string.setting_start_fail);
                    cancelLoadingDialog();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                cancelLoadingDialog();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
