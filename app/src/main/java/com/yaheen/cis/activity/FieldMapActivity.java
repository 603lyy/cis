package com.yaheen.cis.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.MapActivity;
import com.yaheen.cis.adapter.FieldHouseAdapter;
import com.yaheen.cis.adapter.RecordMapAdapter;
import com.yaheen.cis.entity.FieldHouseBean;
import com.yaheen.cis.entity.RecordEventBean;
import com.yaheen.cis.util.HttpUtils;
import com.yaheen.cis.util.common.FreeHandScreenUtil;
import com.yaheen.cis.util.map.cluster.Cluster;
import com.yaheen.cis.util.map.cluster.ClusterItem;
import com.yaheen.cis.util.map.cluster.ClusterManager;
import com.yaheen.cis.util.map.route.DrivingRouteOverlay;
import com.yaheen.cis.util.map.route.OverlayManager;
import com.yaheen.cis.util.nfc.Base64Utils;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

public class FieldMapActivity extends MapActivity {

    private LinearLayout llRecord, llBack;

    private FrameLayout flMapView;

    private RecyclerView rvRecordMap;

//    private RecordMapAdapter mapAdapter;

    private FieldHouseAdapter mAdapter;

    private MapView mapView = null;

    private ImageView ivFull;

    private EditText etSearch;

    private TextView tvSearch;

    private BaiduMap mBaiduMap;

    private ClusterManager<FieldMapActivity.MyItem> mClusterManager;

    private RouteLine route = null;

    private OverlayManager routeOverlay = null;

    private List<FieldMapActivity.MyItem> items;

    private String eventUrl = houseUrl + "/separationSub/getRetrievalHouseNumberFromGudie.do";

    private String houseDataUrl = houseUrl + "/separationSub/getRetrievalHouseNumberFromGudie.do";

    private String recordId;

    //地图显示的层级
    private float mapLevel = 18;

    //判断地图是否是第一次定位
    private boolean isFirstLoc = true;

    private boolean isFull = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_map);

        llBack = findViewById(R.id.back);

        recordId = getIntent().getStringExtra("recordId");
        showLoadingDialog();

        initMapView();
        initRecordView();
//        getRecordEventList();
        getUnitLocationData();

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initMapView() {
        ivFull = findViewById(R.id.iv_full_screen);
        flMapView = findViewById(R.id.fl_map_view);
        mapView = findViewById(R.id.record_map_view);
        mapView.showScaleControl(false);
        mapView.showZoomControls(false);

        mBaiduMap = mapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);

        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.ic_map_point);
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker));

        // 定义点聚合管理类ClusterManager
        mClusterManager = new ClusterManager<FieldMapActivity.MyItem>(this, mBaiduMap);
        // 设置地图监听，当地图状态发生改变时，进行点聚合运算
        mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
        // 设置maker点击时的响应
        mBaiduMap.setOnMarkerClickListener(mClusterManager);

        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<FieldMapActivity.MyItem>() {
            @Override
            public boolean onClusterClick(Cluster<FieldMapActivity.MyItem> cluster) {
                Toast.makeText(FieldMapActivity.this,
                        "有" + cluster.getSize() + "个点", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<FieldMapActivity.MyItem>() {
            @Override
            public boolean onClusterItemClick(FieldMapActivity.MyItem item) {
                if (!TextUtils.isEmpty(item.houseId)) {
                    Intent intent = new Intent(FieldMapActivity.this, FieldDetailActivity.class);
                    intent.putExtra("houseId", item.houseId);
                    startActivity(intent);
                }
                return false;
            }
        });

        ivFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) flMapView.getLayoutParams();
                if (isFull) {
                    isFull = false;
                    llRecord.setVisibility(View.VISIBLE);
                    params.height = (int) FreeHandScreenUtil.dpToPx(FieldMapActivity.this, 300);
                } else {
                    isFull = true;
                    llRecord.setVisibility(View.GONE);
                    int[] size = FreeHandScreenUtil.getScreenSize(FieldMapActivity.this);
                    //全屏设置，屏幕的高度减去状态栏的高度
                    params.height = (int) (size[1]
                            - FreeHandScreenUtil.getStatusBarHeight(FieldMapActivity.this)
                            - FreeHandScreenUtil.dpToPx(FieldMapActivity.this, 45));
                }
                flMapView.setLayoutParams(params);//将设置好的布局参数应用到控件中
            }
        });
    }

    private void initRecordView() {
        etSearch = findViewById(R.id.et_search);
        tvSearch = findViewById(R.id.tv_search);
        llRecord = findViewById(R.id.ll_record_list);
        rvRecordMap = findViewById(R.id.rv_record_map);
        rvRecordMap.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new FieldHouseAdapter();
        rvRecordMap.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(FieldMapActivity.this, FieldDetailActivity.class);
                intent.putExtra("houseId", mAdapter.getData().get(position).getId());
                startActivity(intent);

            }
        });

        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchHouseData();
            }
        });

    }

    private void searchHouseData() {
        showLoadingDialog();
        RequestParams requestParams = new RequestParams(houseDataUrl);
        requestParams.addQueryStringParameter("searchContent", Base64Utils.encode(etSearch.getText().toString().getBytes()));

        HttpUtils.getPostHttp(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                FieldHouseBean data = null;
                try{
                    data = gson.fromJson(result, FieldHouseBean.class);
                }catch (Exception e){
                    showToast("搜索失败");
                }
                if (data != null && data.isResult()) {
                    mAdapter.setDatas(data.getJson());
                    mAdapter.notifyDataSetChanged();
                    addMarkers(data.getJson());
                    mBaiduMap.hideInfoWindow();
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

    private void getUnitLocationData() {

        RequestParams requestParams = new RequestParams(houseDataUrl);
        requestParams.addQueryStringParameter("unitStr", Base64Utils.encode(DefaultPrefsUtil.getUnitName().getBytes()));

        HttpUtils.getPostHttp(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                FieldHouseBean data = gson.fromJson(result, FieldHouseBean.class);
//                if (data != null && data.isResult()) {
//                    mAdapter.setDatas(data.getJson());
//                    mAdapter.notifyDataSetChanged();
//                    addMarkers(data.getJson());
//                    mBaiduMap.hideInfoWindow();
//                }
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

    private void getRecordEventList() {
        RequestParams requestParams = new RequestParams(eventUrl);
        requestParams.addQueryStringParameter("recordId", recordId);
        requestParams.addQueryStringParameter("token", DefaultPrefsUtil.getToken());

        HttpUtils.getPostHttp(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                RecordEventBean data = gson.fromJson(result, RecordEventBean.class);
                if (data != null && data.isResult()) {
//                    mapAdapter.setDatas(data.getEventList());
//                    mapAdapter.notifyDataSetChanged();
//                    addMarkers(data.getRecordObject(), data.getEventList(), data.getCoordinateList());
                } else if (data != null && data.getCode() == 1002) {
                    startActivity(new Intent(FieldMapActivity.this, LoginActivity.class));
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

    private void setLocationData(LatLng mLoc) {

        if (mLoc == null) {
            return;
        }

        MyLocationData locData = new MyLocationData.Builder().direction(100)
                .latitude(mLoc.latitude).longitude(mLoc.longitude).build();

        // 设置定位数据
        mBaiduMap.setMyLocationData(locData);
        mBaiduMap.showSDKLayer();

//        if (isFirstLoc) {
        isFirstLoc = false;
        LatLng ll = new LatLng(mLoc.latitude, mLoc.longitude);
        MapStatus.Builder builder = new MapStatus.Builder();
        //设置地图层级（4-21）
        if (mapLevel != 19) {
            mapLevel++;
        } else {
            mapLevel = 18;
        }
        builder.target(ll).zoom(mapLevel);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
//        }
    }

//    /**
//     * 向地图添加Marker点
//     */
//    public void addMarkers(RecordEventBean.RecordObjectBean rBean,
//                           List<RecordEventBean.EventListBean> eventList,
//                           List<RecordEventBean.CoordinateListBean> coordinateList) {
//
//        items = new ArrayList<>();
//        //画直线
//        List<LatLng> points = new ArrayList<LatLng>();
//
//        LatLng stLatLng = new LatLng(rBean.getStartLatitude(), rBean.getStartLongitude());
//        LatLng enLatLng = new LatLng(rBean.getEndLatitude(), rBean.getEndLongitude());
//
//        String eventId, titleStr;
//
//        //事件上报点+起终点
//        for (int i = 0; i < eventList.size() + 2; i++) {
//            LatLng latLng;
//            if (i == 0) {
//                latLng = enLatLng;
//                eventId = "";
//                titleStr = "start";
//            } else if (i == eventList.size() + 1) {
//                latLng = stLatLng;
//                eventId = "";
//                titleStr = "end";
//            } else {
//                latLng = new LatLng(eventList.get(i - 1).getLatitude(),
//                        eventList.get(i - 1).getLongitude());
//                eventId = eventList.get(i - 1).getId();
//                titleStr = "";
//            }
//            items.add(new MyItem(latLng, eventId, titleStr));
//        }
//
//        //画轨迹线
////        for (int i = 0; i < coordinateList.size(); i++) {
////            LatLng latLng;
////            if (i == 0) {
////                latLng = enLatLng;
////            } else if (i == coordinateList.size() - 1) {
////                latLng = stLatLng;
////            } else {
////                latLng = new LatLng(coordinateList.get(i).getLatitude(),
////                        coordinateList.get(i).getLongitude());
////            }
////            points.add(latLng);
////        }
//
//        mClusterManager.addItems(items);
////        OverlayOptions ooPolyline = new PolylineOptions().width(10)
////                .color(0xAAFF0000).points(points);
////        mBaiduMap.addOverlay(ooPolyline);
//        setLocationData(stLatLng);
//    }

    /**
     * 向地图添加Marker点
     */
    public void addMarkers(List<FieldHouseBean.JsonBean> houseList) {

        items = new ArrayList<>();

        String houseId, titleStr;

        LatLng stLatLng = null;

        //事件上报点+起终点
        for (int i = 0; i < houseList.size(); i++) {
            if (houseList.get(i).getLatitude() == null || houseList.get(i).getLongitude() == null) {
                continue;
            }

            //初始化第一个点坐标，作为地图中心点
            if (items.size() == 0) {
                stLatLng = new LatLng(Double.valueOf(houseList.get(i).getLatitude()), Double.valueOf(houseList.get(i).getLongitude()));
            }
            LatLng latLng = new LatLng(Double.valueOf(houseList.get(i).getLatitude()),
                    Double.valueOf(houseList.get(i).getLongitude()));
            houseId = houseList.get(i).getId();
            titleStr = "";
            items.add(new MyItem(latLng, houseId, titleStr));
        }


        mClusterManager.clearItems();
        mClusterManager.addItems(items);
        if (stLatLng != null) {
            setLocationData(stLatLng);
        }
    }

    /**
     * 每个Marker点，包含Marker点坐标以及图标
     */
    public class MyItem implements ClusterItem {
        private final LatLng mPosition;

        private final String houseId;

        private final String title;

        public MyItem(LatLng latLng, String houseId, String title) {
            mPosition = latLng;
            this.houseId = houseId;
            this.title = title;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        @Override
        public BitmapDescriptor getBitmapDescriptor() {
//            if (emergency.equals("1")) {
//                return BitmapDescriptorFactory
//                        .fromResource(R.drawable.ic_map_recode);
//            } else if (emergency.equals("2")) {
//                return BitmapDescriptorFactory
//                        .fromResource(R.drawable.ic_map_normal);
//            } else if (emergency.equals("3")) {
//                return BitmapDescriptorFactory
//                        .fromResource(R.drawable.ic_map_suspicious);
//            } else if (emergency.equals("4")) {
            return BitmapDescriptorFactory
                    .fromResource(R.drawable.ic_map_urgency);
//            }
//            View view = getLayoutInflater().inflate(R.layout.item_map_marker,null);
//            return BitmapDescriptorFactory.fromView(view);
//            return BitmapDescriptorFactory
//                    .fromResource(R.drawable.ic_map_point_2);
        }
    }

    @Override
    public void searchRoute(RecordEventBean.RecordObjectBean rBean, List<RecordEventBean.EventListBean> eventList) {
        super.searchRoute(rBean, eventList);
    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        super.onGetDrivingRouteResult(result);

        if (result.getRouteLines().size() > 0) {
            route = result.getRouteLines().get(0);
            DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);
            routeOverlay = overlay;
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
            mBaiduMap.setMyLocationEnabled(false);
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
