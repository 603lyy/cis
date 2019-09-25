package com.yaheen.cis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
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
import com.yaheen.cis.adapter.RecordMapAdapter;
import com.yaheen.cis.entity.RecordEventBean;
import com.yaheen.cis.util.HttpUtils;
import com.yaheen.cis.util.common.FreeHandScreenUtil;
import com.yaheen.cis.util.map.BDMapUtils;
import com.yaheen.cis.util.map.cluster.Cluster;
import com.yaheen.cis.util.map.cluster.ClusterItem;
import com.yaheen.cis.util.map.cluster.ClusterManager;
import com.yaheen.cis.util.map.MapViewLocationListener;
import com.yaheen.cis.util.map.route.DrivingRouteOverlay;
import com.yaheen.cis.util.map.route.OverlayManager;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class RecordMapActivity extends MapActivity {

    private LinearLayout llRecord, llBack;

    private FrameLayout flMapView;

    private RecyclerView rvRecordMap;

    private RecordMapAdapter mapAdapter;

    private MapView mapView = null;

    private ImageView ivFull;

    private BaiduMap mBaiduMap;

    private ClusterManager<MyItem> mClusterManager;

    private RouteLine route = null;

    private OverlayManager routeOverlay = null;

    private List<MyItem> items;

    private String eventUrl = "";

    private String recordId;

    //判断地图是否是第一次定位
    private boolean isFirstLoc = true;

    private boolean isFull = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_map);

        llBack = findViewById(R.id.back);

        recordId = getIntent().getStringExtra("recordId");
        showLoadingDialog();

        initData();
        initMapView();
        initRecordView();
        getRecordEventList();

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initData() {
         eventUrl = getBaseUrl() + "/eapi/eventList.do";
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
        mClusterManager = new ClusterManager<MyItem>(this, mBaiduMap);
        // 设置地图监听，当地图状态发生改变时，进行点聚合运算
        mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
        // 设置maker点击时的响应
        mBaiduMap.setOnMarkerClickListener(mClusterManager);

        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {
            @Override
            public boolean onClusterClick(Cluster<MyItem> cluster) {
                Toast.makeText(RecordMapActivity.this,
                        "有" + cluster.getSize() + "个点", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
            @Override
            public boolean onClusterItemClick(MyItem item) {
                if (!TextUtils.isEmpty(item.eventId)) {
                    Intent intent = new Intent(RecordMapActivity.this, EventActivity.class);
                    intent.putExtra("recordId", recordId);
                    intent.putExtra("eventId", item.eventId);
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
                    ivFull.setBackgroundResource(R.drawable.ic_full_screen);
                    params.height = (int) FreeHandScreenUtil.dpToPx(RecordMapActivity.this, 300);
                } else {
                    isFull = true;
                    llRecord.setVisibility(View.GONE);
                    ivFull.setBackgroundResource(R.drawable.ic_full_screen_2);
                    int[] size = FreeHandScreenUtil.getScreenSize(RecordMapActivity.this);
                    //全屏设置，屏幕的高度减去状态栏的高度
                    params.height = (int) (size[1]
                            - FreeHandScreenUtil.getStatusBarHeight(RecordMapActivity.this)
                            - FreeHandScreenUtil.dpToPx(RecordMapActivity.this, 45));
                }
                flMapView.setLayoutParams(params);//将设置好的布局参数应用到控件中
            }
        });
    }

    private void initRecordView() {
        llRecord = findViewById(R.id.ll_record_list);
        rvRecordMap = findViewById(R.id.rv_record_map);
        rvRecordMap.setLayoutManager(new LinearLayoutManager(this));

        mapAdapter = new RecordMapAdapter();
        rvRecordMap.setAdapter(mapAdapter);

        mapAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(RecordMapActivity.this, EventActivity.class);
                intent.putExtra("recordId", recordId);
                intent.putExtra("eventId", mapAdapter.getData().get(position).getId());
                startActivity(intent);

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
                    mapAdapter.setDatas(data.getEventList());
                    mapAdapter.notifyDataSetChanged();
                    addMarkers(data.getRecordObject(), data.getEventList(), data.getCoordinateList());
                } else if (data != null && data.getCode() == 1002) {
                    startActivity(new Intent(RecordMapActivity.this, LoginActivity.class));
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

        if (isFirstLoc) {
            isFirstLoc = false;
            LatLng ll = new LatLng(mLoc.latitude, mLoc.longitude);
            MapStatus.Builder builder = new MapStatus.Builder();
            //设置地图层级（4-21）
            builder.target(ll).zoom(17.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
    }

    /**
     * 向地图添加Marker点
     */
    public void addMarkers(RecordEventBean.RecordObjectBean rBean,
                           List<RecordEventBean.EventListBean> eventList,
                           List<RecordEventBean.CoordinateListBean> coordinateList) {

        items = new ArrayList<>();
        //画直线
        List<LatLng> points = new ArrayList<LatLng>();

        LatLng stLatLng = new LatLng(rBean.getStartLatitude(), rBean.getStartLongitude());
        LatLng enLatLng = new LatLng(rBean.getEndLatitude(), rBean.getEndLongitude());

        String eventId, emergency, titleStr;

        //事件上报点+起终点
        for (int i = 0; i < eventList.size() + 2; i++) {
            LatLng latLng;
            if (i == 0) {
                latLng = enLatLng;
                emergency = "";
                eventId = "";
                titleStr = "start";
            } else if (i == eventList.size() + 1) {
                latLng = stLatLng;
                emergency = "";
                eventId = "";
                titleStr = "end";
            } else {
                latLng = new LatLng(eventList.get(i - 1).getLatitude(),
                        eventList.get(i - 1).getLongitude());
                emergency = eventList.get(i - 1).getEmergency();
                eventId = eventList.get(i - 1).getId();
                titleStr = "";
            }
            items.add(new MyItem(latLng, eventId, emergency, titleStr));
        }

        //画轨迹线
        for (int i = 0; i < coordinateList.size(); i++) {
            LatLng latLng;
            if (i == 0) {
                latLng = enLatLng;
            } else if (i == coordinateList.size() - 1) {
                latLng = stLatLng;
            } else {
                latLng = new LatLng(coordinateList.get(i).getLatitude(),
                        coordinateList.get(i).getLongitude());
            }
            points.add(latLng);
        }

        mClusterManager.addItems(items);
        OverlayOptions ooPolyline = new PolylineOptions().width(10)
                .color(0xAAFF0000).points(points);
        mBaiduMap.addOverlay(ooPolyline);
        setLocationData(stLatLng);
    }

    /**
     * 每个Marker点，包含Marker点坐标以及图标
     */
    public class MyItem implements ClusterItem {
        private final LatLng mPosition;

        private final String eventId;

        private final String emergency;

        private final String title;

        public MyItem(LatLng latLng, String eventId, String emergency, String title) {
            mPosition = latLng;
            this.eventId = eventId;
            this.emergency = emergency;
            this.title = title;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        @Override
        public BitmapDescriptor getBitmapDescriptor() {
            if (emergency.equals("1")) {
                return BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_map_recode);
            } else if (emergency.equals("2")) {
                return BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_map_normal);
            } else if (emergency.equals("3")) {
                return BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_map_suspicious);
            } else if (emergency.equals("4")) {
                return BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_map_urgency);
            }
//            View view = getLayoutInflater().inflate(R.layout.item_map_marker,null);
//            return BitmapDescriptorFactory.fromView(view);
            return BitmapDescriptorFactory
                    .fromResource(R.drawable.ic_map_point_2);
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
