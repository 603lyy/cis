package com.yaheen.cis.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.PermissionActivity;
import com.yaheen.cis.adapter.DataServer;
import com.yaheen.cis.adapter.ImgUploadAdapter;
import com.yaheen.cis.adapter.PatrolSettingAdapter;
import com.yaheen.cis.adapter.UrgencyAdapter;
import com.yaheen.cis.entity.ImgUploadBean;
import com.yaheen.cis.entity.QuestionBean;
import com.yaheen.cis.util.img.ImgUploadHelper;
import com.yaheen.cis.util.img.UpLoadImgListener;
import com.yaheen.cis.util.img.UriUtil;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;
import com.yaheen.cis.util.time.CountDownTimerUtils;
import com.yaheen.cis.util.map.BDMapUtils;
import com.yaheen.cis.util.map.MapViewLocationListener;
import com.yaheen.cis.util.time.TimeTransferUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends PermissionActivity {

    private final int REQUEST_CODE_CHOOSE = 1001;

    private Gson gson = new Gson();

    private TextView tvLocation, tvTime;

    private MapView mapView = null;

    private BaiduMap mBaiduMap;

    private RecyclerView rvProblem;

    private RecyclerView rvUrgency;

    private RecyclerView rvImg;

    private PatrolSettingAdapter problemAdapter;

    private UrgencyAdapter urgencyAdapter;

    private ImgUploadAdapter uploadAdapter;

    private String questionUrl = "http://192.168.199.118:8080/crs/eapi/findQuestionaireByTypeId.do";

    private String recordId;

    //判断地图是否是第一次定位
    private boolean isFirstLoc = true;

    //记录开始巡查的时间戳,方便计算时间
    private long startTime;

    //被选择的图片路径列表
    private List<Uri> selectUriList;

    //图片上传列表的数据列表
    private List<String> adapterPathList = new ArrayList<>();

    //已上传图片的ID的拼接
    private String imgIdStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        startTime = System.currentTimeMillis();

        recordId = getIntent().getStringExtra("recordId");

        initView();
        initUrgency();
        initMapView();
        initImgUpload();
        getQuestionMsg();
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
//                if (BDMapUtils.getLocation() != null) {
//                    tvLocation.setText(BDMapUtils.getLocation().getAddrStr());
//                }

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

        uploadAdapter = new ImgUploadAdapter(this);
        uploadAdapter.addFooterView(getImgFooterView());
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

    private View getImgFooterView() {
        View view = getLayoutInflater().inflate(R.layout.footer_img_upload,
                (ViewGroup) rvImg.getParent(), false);
        ImageView ivAdd = view.findViewById(R.id.iv_add);

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImgUploadHelper.showUserAvatarUploadDialog(DetailActivity.this, imgListener);
            }
        });
        return view;
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

    private UpLoadImgListener imgListener = new UpLoadImgListener() {
        @Override
        public void upLoad(List<Uri> list) {
            showLoadingDialog();

            if (list.size() <= 0) {
                return;
            }
            selectUriList = list;
            upLoadImg(list.get(0));
        }
    };

    private void getQuestionMsg() {
        RequestParams requestParams = new RequestParams(questionUrl);
        requestParams.addQueryStringParameter("token", DefaultPrefsUtil.getToken());
        requestParams.addQueryStringParameter("typeId", "402847f26390db7b016390df8bd90001");
        requestParams.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;");

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                QuestionBean data = gson.fromJson(result, QuestionBean.class);
                if (data != null && data.isResult()) {
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

            }
        });
    }

    private void upLoadImg(final Uri uri) {

        final String imgPath = UriUtil.getPath(DetailActivity.this, uri);

        if (TextUtils.isEmpty(imgPath)) {
            return;
        }

        RequestParams params = new RequestParams("http://192.168.199.118:8080/crs/eapi/uploadPhoto.do" +
                "?token=" + DefaultPrefsUtil.getToken());
        params.setMultipart(true);
        params.addBodyParameter("originImage", new File(imgPath));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ImgUploadBean data = gson.fromJson(result, ImgUploadBean.class);
                if (data != null) {
                    //删除已请求上传图片的路径
                    selectUriList.remove(uri);

                    if (data.isResult()) {
                        adapterPathList.add(imgPath);
                        uploadAdapter.setDatas(adapterPathList);
                        uploadAdapter.notifyDataSetChanged();
                        if (TextUtils.isEmpty(imgIdStr)) {
                            imgIdStr = data.getFileId();
                        } else {
                            imgIdStr = imgIdStr + "," + data.getFileId();
                        }
                    }

                    //图片路径不为空，继续上传剩余图片
                    if (selectUriList.size() > 0) {
                        upLoadImg(selectUriList.get(0));
                    }
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
                if (selectUriList.size() <= 0) {
                    cancelLoadingDialog();
                }
            }
        });
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
