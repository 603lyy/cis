package com.yaheen.cis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.MapActivity;
import com.yaheen.cis.adapter.EventImgAdapter;
import com.yaheen.cis.adapter.EventProblemAdapter;
import com.yaheen.cis.adapter.UrgencyAdapter;
import com.yaheen.cis.entity.EventDetailBean;
import com.yaheen.cis.util.img.ImgUploadHelper;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class EventActivity extends MapActivity {

    private final int REQUEST_CODE_CHOOSE = 1001;

    private TextView tvLocation, tvType, tvDescribe, tvUrgency;

    private ImageView ivUrgency;

    private MapView mapView = null;

    private BaiduMap mBaiduMap;

    private RecyclerView rvProblem, rvImg;

    private EventProblemAdapter problemAdapter;

    private EventImgAdapter imgAdapter;

    private String questionUrl = "http://192.168.199.108:8080/crs/eapi/eventDetail.do";

    private String  recordId, eventId;

    //判断地图是否是第一次定位
    private boolean isFirstLoc = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        showLoadingDialog();
        eventId = getIntent().getStringExtra("eventId");
        recordId = getIntent().getStringExtra("recordId");

        //记录ID不可为空
        if (TextUtils.isEmpty(recordId)) {
            finish();
        }

        initView();
        initMapView();
        initQuestion();
        initImgUpload();
        getEventInfo();
    }

    private void initView() {
        tvType = findViewById(R.id.tv_type);
        ivUrgency = findViewById(R.id.iv_urgency);
        tvUrgency = findViewById(R.id.tv_urgency);
        tvDescribe = findViewById(R.id.tv_describe);
        tvLocation = findViewById(R.id.tv_location_describe);
    }

    private void initQuestion() {
        rvProblem = findViewById(R.id.rv_problem);
        rvProblem.setLayoutManager(new GridLayoutManager(this, 4));
        problemAdapter = new EventProblemAdapter();
        rvProblem.setAdapter(problemAdapter);
    }

    private void initImgUpload() {
        rvImg = findViewById(R.id.rv_img);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvImg.setLayoutManager(layoutManager);

        imgAdapter = new EventImgAdapter(this);
        rvImg.setAdapter(imgAdapter);
    }

    private void initMapView() {
        mapView = findViewById(R.id.detail_map_view);

        mBaiduMap = mapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, null));
    }

    private void getEventInfo() {
        RequestParams requestParams = new RequestParams(questionUrl);
        requestParams.addQueryStringParameter("token", DefaultPrefsUtil.getToken());
        requestParams.addQueryStringParameter("recordId", recordId);
        requestParams.addQueryStringParameter("eventId", eventId);
        requestParams.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;");

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                EventDetailBean data = gson.fromJson(result, EventDetailBean.class);
                if (data != null && data.isResult()) {
                    tvType.setText(data.getTbEvent().getType());
                    tvDescribe.setText(data.getTbEvent().getDescribe());
                    emergencyTransfer(data.getTbEvent().getEmergency());
                    imgAdapter.setDatas(data.getTbEvent().getFileArr());
                    problemAdapter.setDatas(data.getTbEvent().getQuestionnaireArr());
                    searchAddress(data.getTbEvent().getLatitude(), data.getTbEvent().getLongitude());
                    setLocationData(Float.valueOf(data.getTbEvent().getLatitude()),
                            Float.valueOf(data.getTbEvent().getLongitude()));
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

    private void setLocationData(float lat, float lon) {

        MyLocationData locData = new MyLocationData.Builder().direction(100)
                .latitude(lat).longitude(lon).build();

        // 设置定位数据
        mBaiduMap.setMyLocationData(locData);

        if (isFirstLoc) {
            isFirstLoc = false;
            LatLng ll = new LatLng(lat, lon);
            MapStatus.Builder builder = new MapStatus.Builder();
            //设置地图层级（4-21）
            builder.target(ll).zoom(17.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
    }

    private void emergencyTransfer(String emergency) {
        if (emergency.equals("0")) {
            ivUrgency.setBackgroundResource(R.drawable.ic_recode);
            tvUrgency.setText(R.string.detail_urgency_record);
        } else if (emergency.equals("1")) {
            ivUrgency.setBackgroundResource(R.drawable.ic_normal);
            tvUrgency.setText(R.string.detail_urgency_normal);
        } else if (emergency.equals("2")) {
            ivUrgency.setBackgroundResource(R.drawable.ic_suspicious);
            tvUrgency.setText(R.string.detail_urgency_suspicious);
        } else if (emergency.equals("3")) {
            ivUrgency.setBackgroundResource(R.drawable.ic_urgent);
            tvUrgency.setText(R.string.detail_urgency_urgent);
        }
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        super.onGetReverseGeoCodeResult(result);
        tvLocation.setText(result.getAddress());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImgUploadHelper.onActivityResult(this, requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
        mapView = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mBaiduMap.setMyLocationEnabled(true);
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mBaiduMap.setMyLocationEnabled(false);
        mapView.onPause();
    }
}
