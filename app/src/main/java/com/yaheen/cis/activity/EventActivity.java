package com.yaheen.cis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.MapActivity;
import com.yaheen.cis.adapter.EventImgAdapter;
import com.yaheen.cis.adapter.EventProblemAdapter;
import com.yaheen.cis.adapter.UrgencyAdapter;
import com.yaheen.cis.entity.EventDetailBean;
import com.yaheen.cis.entity.HouseBean;
import com.yaheen.cis.util.HttpUtils;
import com.yaheen.cis.util.img.ImgUploadHelper;
import com.yaheen.cis.util.img.PhotoPagerUtils;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class EventActivity extends MapActivity {

    private TextView tvLocation, tvType, tvDescribe, tvUrgency, tvUsername;

    private TextView tvHOwner, tvHNumber, tvHAreaType, tvHInspectionPoint, tvHAdderss;

    private TextView tvPUser, tvPPosition, tvPCommitment, tvPPhone, tvPTime;

    private TextView tvMUser, tvMType, tvMName, tvMTime, tvMOwner;

    private LinearLayout llBack, llHouse, llParty, llMerchant;

    private ImageView ivUrgency;

    private MapView mapView = null;

    private BaiduMap mBaiduMap;

    private RecyclerView rvProblem, rvImg;

    private EventProblemAdapter problemAdapter;

    private EventImgAdapter imgAdapter;

    //图片链接列表
    private List<EventDetailBean.TbEventBean.FileArrBean> imgUrlList = new ArrayList<>();

    private String questionUrl = "";

    private String mHhouseUrl = "";

    private String recordId, eventId;

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

        initData();
        initView();
        initHouseData();
        initMapView();
        initQuestion();
        initImgUpload();
        getEventInfo();
    }

    private void initData() {
        questionUrl = getBaseUrl() + "/eapi/eventDetail.do";
        mHhouseUrl = getHouseUrl() + "/separationSub/getRangeHouseNumberFromApplets.do";
    }

    private void initView() {
        llBack = findViewById(R.id.back);
        tvType = findViewById(R.id.tv_type);
        ivUrgency = findViewById(R.id.iv_urgency);
        tvUrgency = findViewById(R.id.tv_urgency);
        tvDescribe = findViewById(R.id.tv_describe);
        tvUsername = findViewById(R.id.tv_username);
        tvLocation = findViewById(R.id.tv_location_describe);

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initHouseData() {
        tvHNumber = findViewById(R.id.tv_house_number);
        tvHAdderss = findViewById(R.id.tv_house_address);
        tvHOwner = findViewById(R.id.tv_patrol_username);
        tvHAreaType = findViewById(R.id.tv_house_area_type);
        tvHInspectionPoint = findViewById(R.id.tv_house_inspection_point);

        tvPTime = findViewById(R.id.tv_party_time);
        tvPPhone = findViewById(R.id.tv_party_phone);
        tvPUser = findViewById(R.id.tv_party_username);
        tvPPosition = findViewById(R.id.tv_party_position);
        tvPCommitment = findViewById(R.id.tv_party_commitment);

        tvMName = findViewById(R.id.tv_merchant_name);
        tvMType = findViewById(R.id.tv_merchant_type);
        tvMUser = findViewById(R.id.tv_merchant_user);
        tvMTime = findViewById(R.id.tv_merchant_time);
        tvMOwner = findViewById(R.id.tv_merchant_owner);

        llHouse = findViewById(R.id.ll_house_data);
        llParty = findViewById(R.id.ll_party_data);
        llMerchant = findViewById(R.id.ll_merchant_data);
    }

    private void initQuestion() {
        rvProblem = findViewById(R.id.rv_problem);
        rvProblem.setLayoutManager(new GridLayoutManager(this, 1));
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

        imgAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view instanceof ImageView) {
                    if (((ImageView) view).getDrawable() == null)
                        return;
                    ArrayList<String> urls = new ArrayList<String>();
                    urls.add(imgUrlList.get(position).getImageUrl());
                    new PhotoPagerUtils.Builder(EventActivity.this)
                            .setBigImageUrls(urls)
                            .setBigBitmap(((ImageView) view).getDrawable())
                            .setSavaImage(true)
                            .build();
                }
            }
        });
    }

    private void initMapView() {
        mapView = findViewById(R.id.detail_map_view);

        mBaiduMap = mapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.hideSDKLayer();
        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.ic_map_point);
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker));
    }

    private void getEventInfo() {
        RequestParams requestParams = new RequestParams(questionUrl);
        requestParams.addQueryStringParameter("token", DefaultPrefsUtil.getToken());
        requestParams.addQueryStringParameter("recordId", recordId);
        requestParams.addQueryStringParameter("eventId", eventId);
        requestParams.addQueryStringParameter("role", DefaultPrefsUtil.getRole());

        HttpUtils.getPostHttp(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                EventDetailBean data = gson.fromJson(result, EventDetailBean.class);
                if (data != null && data.isResult()) {
                    tvType.setText(data.getTbEvent().getType());
                    imgUrlList = (data.getTbEvent().getFileArr());
                    tvUsername.setText(data.getTbEvent().getUsername());
                    tvDescribe.setText(data.getTbEvent().getDescribe());
                    emergencyTransfer(data.getTbEvent().getEmergency());
                    imgAdapter.setDatas(data.getTbEvent().getFileArr());
                    problemAdapter.setDatas(data.getTbEvent().getQuestionnaireArr());
                    searchAddress(data.getTbEvent().getLatitude(), data.getTbEvent().getLongitude());
                    setLocationData(data.getTbEvent().getLatitude(), data.getTbEvent().getLongitude());
                    getHouseData(data.getTbEvent().getHouseId());
                } else if (data != null && data.getCode() == 1002) {
                    startActivity(new Intent(EventActivity.this, LoginActivity.class));
                    showToast("该账号被别人登陆了");
                    finish();
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

    private void setLocationData(double lat, double lon) {

        MyLocationData locData = new MyLocationData.Builder().direction(100)
                .latitude(lat).longitude(lon).build();

        // 设置定位数据
        mBaiduMap.setMyLocationData(locData);
        mBaiduMap.showSDKLayer();

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
        if (emergency.equals("1")) {
            ivUrgency.setBackgroundResource(R.drawable.ic_recode);
            tvUrgency.setText(R.string.detail_urgency_record);
        } else if (emergency.equals("2")) {
            ivUrgency.setBackgroundResource(R.drawable.ic_normal);
            tvUrgency.setText(R.string.detail_urgency_normal);
        } else if (emergency.equals("3")) {
            ivUrgency.setBackgroundResource(R.drawable.ic_suspicious);
            tvUrgency.setText(R.string.detail_urgency_suspicious);
        } else if (emergency.equals("4")) {
            ivUrgency.setBackgroundResource(R.drawable.ic_urgent);
            tvUrgency.setText(R.string.detail_urgency_urgent);
        }
    }

    private void getHouseData(String houseId) {

        if (TextUtils.isEmpty(houseId)) {
            return;
        }

        RequestParams params = new RequestParams(mHhouseUrl);
        params.addQueryStringParameter("houseNumberId", houseId);
        HttpUtils.getPostHttp(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                HouseBean data = gson.fromJson(result, HouseBean.class);
                if (data != null && data.isResult() && data.getJson().size() > 0) {
                    showHouseData(data.getJson().get(0));
                } else {
                    llHouse.setVisibility(View.GONE);
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

    /**
     * 显示门牌信息，没有数据的不显示
     */
    public void showHouseData(HouseBean.JsonBean jsonBean) {
        if (jsonBean != null) {

            if (jsonBean.getHouseNumber() != null || jsonBean.getUser() != null) {

                if (jsonBean.getUser().size() > 0 && !TextUtils.isEmpty(jsonBean.getUser().get(0).getName())) {
                    tvHOwner.setText(jsonBean.getUser().get(0).getName());
                    tvHOwner.setVisibility(View.VISIBLE);
                } else {
                    tvHOwner.setVisibility(View.GONE);
                }

                if (jsonBean.getHouseNumber() != null) {

                    if (!TextUtils.isEmpty(jsonBean.getHouseNumber().getPeopleNumber())) {
                        tvHNumber.setText(jsonBean.getHouseNumber().getPeopleNumber());
                        tvHNumber.setVisibility(View.VISIBLE);
                    } else {
                        tvHNumber.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(jsonBean.getHouseNumber().getCommunity())) {
                        if (jsonBean.getHouseNumber().getCommunity().equals("A")) {
                            tvHAreaType.setText(getString(R.string.detail_house_type_a));
                            tvHAreaType.setVisibility(View.VISIBLE);
                        } else if (jsonBean.getHouseNumber().getCommunity().equals("N")) {
                            tvHAreaType.setText(getString(R.string.detail_house_type_n));
                            tvHAreaType.setVisibility(View.VISIBLE);
                        } else {
                            tvHAreaType.setVisibility(View.GONE);
                        }
                    } else {
                        tvHAreaType.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(jsonBean.getHouseNumber().getGridInspectionPoint())) {
                        if (jsonBean.getHouseNumber().getGridInspectionPoint().equals("Y")) {
                            tvHInspectionPoint.setText(getString(R.string.text_yes));
                        } else {
                            tvHInspectionPoint.setText(getString(R.string.text_no));
                        }
                        tvHInspectionPoint.setVisibility(View.VISIBLE);
                    } else {
                        tvHInspectionPoint.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(jsonBean.getHouseNumber().getAddress())) {
                        tvHAdderss.setText(jsonBean.getHouseNumber().getAddress());
                        tvHAdderss.setVisibility(View.VISIBLE);
                    } else {
                        tvHAdderss.setVisibility(View.GONE);
                    }
                }
                llHouse.setVisibility(View.VISIBLE);
            } else {
                llHouse.setVisibility(View.GONE);
            }

            if (jsonBean.getPartyMember() != null && jsonBean.getPartyMember().size() > 0) {

                if (!TextUtils.isEmpty(jsonBean.getPartyMember().get(0).getName())) {
                    tvPUser.setText(jsonBean.getPartyMember().get(0).getName());
                    tvPUser.setVisibility(View.VISIBLE);
                } else {
                    tvPUser.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(jsonBean.getPartyMember().get(0).getPosition())) {
                    tvPPosition.setText(jsonBean.getPartyMember().get(0).getPosition());
                    tvPPosition.setVisibility(View.VISIBLE);
                } else {
                    tvPPosition.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(jsonBean.getPartyMember().get(0).getPromise())) {
                    tvPCommitment.setText(jsonBean.getPartyMember().get(0).getPromise());
                    tvPCommitment.setVisibility(View.VISIBLE);
                } else {
                    tvPCommitment.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(jsonBean.getPartyMember().get(0).getPhone())) {
                    tvPPhone.setText(jsonBean.getPartyMember().get(0).getPhone());
                    tvPPhone.setVisibility(View.VISIBLE);
                } else {
                    tvPPhone.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(jsonBean.getPartyMember().get(0).getTime())) {
                    tvPTime.setText(jsonBean.getPartyMember().get(0).getTime());
                    tvPTime.setVisibility(View.VISIBLE);
                } else {
                    tvPTime.setVisibility(View.GONE);
                }
                llParty.setVisibility(View.VISIBLE);
            } else {
                llParty.setVisibility(View.GONE);
            }

            if (jsonBean.getMerchants() != null) {

                if (!TextUtils.isEmpty(jsonBean.getMerchants().getHouseOwnerName())) {
                    tvMUser.setText(jsonBean.getMerchants().getHouseOwnerName());
                    tvMUser.setVisibility(View.VISIBLE);
                } else {
                    tvMUser.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(jsonBean.getMerchants().getType())) {
                    tvMType.setText(jsonBean.getMerchants().getType());
                    tvMType.setVisibility(View.VISIBLE);
                } else {
                    tvMType.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(jsonBean.getMerchants().getName())) {
                    tvMName.setText(jsonBean.getMerchants().getName());
                    tvMName.setVisibility(View.VISIBLE);
                } else {
                    tvMName.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(jsonBean.getMerchants().getTime())) {
                    tvMTime.setText(jsonBean.getMerchants().getTime());
                    tvMTime.setVisibility(View.VISIBLE);
                } else {
                    tvMTime.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(jsonBean.getMerchants().getUserName())) {
                    tvMOwner.setText(jsonBean.getMerchants().getUserName());
                    tvMOwner.setVisibility(View.VISIBLE);
                } else {
                    tvMOwner.setVisibility(View.GONE);
                }

                String url = jsonBean.getMerchants().getStorephotos();
                llMerchant.setVisibility(View.VISIBLE);
            } else {
                llMerchant.setVisibility(View.GONE);
            }
        } else {
            llHouse.setVisibility(View.GONE);
            llParty.setVisibility(View.GONE);
            llMerchant.setVisibility(View.GONE);
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
