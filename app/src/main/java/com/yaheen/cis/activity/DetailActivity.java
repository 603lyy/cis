package com.yaheen.cis.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.PermissionActivity;
import com.yaheen.cis.adapter.DataServer;
import com.yaheen.cis.adapter.ImgUploadAdapter;
import com.yaheen.cis.adapter.PatrolSettingAdapter;
import com.yaheen.cis.adapter.UrgencyAdapter;
import com.yaheen.cis.util.time.CountDownTimerUtils;
import com.yaheen.cis.util.map.BDMapUtils;
import com.yaheen.cis.util.map.MapViewLocationListener;
import com.yaheen.cis.util.time.TimeTransferUtils;

public class DetailActivity extends PermissionActivity {

    private TextView tvLocation, tvTime;

    private MapView mapView = null;

    private BaiduMap mBaiduMap;

    private RecyclerView rvProblem;

    private RecyclerView rvUrgency;

    private RecyclerView rvImg;

    private PatrolSettingAdapter problemAdapter;

    private UrgencyAdapter urgencyAdapter;

    private ImgUploadAdapter uploadAdapter;

    //判断地图是否是第一次定位
    boolean isFirstLoc = true;

    //记录开始巡查的时间戳,方便计算时间
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        startTime = System.currentTimeMillis();

        initView();
        initUrgency();
        initMapView();
        initImgUpload();
    }

    private void initView() {
        tvTime = findViewById(R.id.tv_time);
        tvLocation = findViewById(R.id.tv_location_describe);

        rvProblem = findViewById(R.id.rv_problem);
        rvProblem.setLayoutManager(new GridLayoutManager(this, 4));

        problemAdapter = new PatrolSettingAdapter();
//        problemAdapter.setDatas(DataServer.getSampleData(10));
        rvProblem.setAdapter(problemAdapter);

        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BDMapUtils.getLocation() != null) {
                    tvLocation.setText(BDMapUtils.getLocation().getAddrStr());
                }
            }
        });

        CountDownTimerUtils.getCountDownTimer()
                .setMillisInFuture(24 * 60 * 60 * 1000)
                .setCountDownInterval(1000)
                .setTickDelegate(new CountDownTimerUtils.TickDelegate() {
                    @Override
                    public void onTick(long pMillisUntilFinished) {
                        long time = System.currentTimeMillis() - startTime - 28800000L;
                        tvTime.setText(TimeTransferUtils.getHMSStrTime(time + ""));
                    }
                }).start();
    }

    private void initUrgency() {
        rvUrgency = findViewById(R.id.rv_urgency);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvUrgency.setLayoutManager(layoutManager);

        urgencyAdapter = new UrgencyAdapter();
        urgencyAdapter.setDatas(DataServer.getSampleData(10));
        rvUrgency.setAdapter(urgencyAdapter);
    }

    private void initImgUpload() {
        rvImg = findViewById(R.id.rv_img);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvImg.setLayoutManager(layoutManager);

        uploadAdapter = new ImgUploadAdapter();
        uploadAdapter.setDatas(DataServer.getSampleData(10));
        rvImg.setAdapter(uploadAdapter);
    }

    private void initMapView() {
        mapView = findViewById(R.id.detail_map_view);

        mBaiduMap = mapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, null));
        BDMapUtils.setMapViewListener(new locationListener());
    }

    private class locationListener implements MapViewLocationListener {

        @Override
        public void changeLocation(BDLocation mLoc) {

            MyLocationData locData = new MyLocationData.Builder().direction(100)
                    .latitude(mLoc.getLatitude()).longitude(mLoc.getLongitude()).build();

            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);

            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(mLoc.getLatitude(), mLoc.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                //设置地图层级（4-21）
                builder.target(ll).zoom(19.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }
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
