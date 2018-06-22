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
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.MapActivity;
import com.yaheen.cis.activity.base.PermissionActivity;
import com.yaheen.cis.adapter.DataServer;
import com.yaheen.cis.adapter.RecordMapAdapter;
import com.yaheen.cis.entity.RecordEventBean;
import com.yaheen.cis.util.FreeHandScreenUtil;
import com.yaheen.cis.util.FreeHandSystemUtil;
import com.yaheen.cis.util.map.BDMapUtils;
import com.yaheen.cis.util.map.cluster.Cluster;
import com.yaheen.cis.util.map.cluster.ClusterItem;
import com.yaheen.cis.util.map.cluster.ClusterManager;
import com.yaheen.cis.util.map.MapViewLocationListener;
import com.yaheen.cis.util.map.route.DrivingRouteOverlay;
import com.yaheen.cis.util.map.route.OverlayManager;
import com.yaheen.cis.util.map.route.WalkingRouteOverlay;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class RecordMapActivity extends MapActivity {

    private LinearLayout llRecord,llBack;

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

    private String eventUrl = baseUrl + "/eapi/eventList.do";

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

    private void initMapView() {
        ivFull = findViewById(R.id.iv_full_screen);
        flMapView = findViewById(R.id.fl_map_view);
        mapView = findViewById(R.id.record_map_view);
        mapView.showScaleControl(false);
        mapView.showZoomControls(false);

        mBaiduMap = mapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        mBaiduMap.hideSDKLayer();

        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.ic_map_point);
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker));
//        BDMapUtils.setMapViewListener(new locationListener());
//        setLocationData(BDMapUtils.getLocation());

        // 定义点聚合管理类ClusterManager
        mClusterManager = new ClusterManager<MyItem>(this, mBaiduMap);
        // 添加Marker点
//        addMarkers();
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
                    params.height = (int) FreeHandScreenUtil.dpToPx(RecordMapActivity.this, 300);
                } else {
                    isFull = true;
                    llRecord.setVisibility(View.GONE);
                    int[] size = FreeHandScreenUtil.getScreenSize(RecordMapActivity.this);
                    //全屏设置，屏幕的高度减去状态栏的高度
                    params.height = size[1] - FreeHandScreenUtil.getStatusBarHeight(RecordMapActivity.this);
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
//        mapAdapter.setDatas(DataServer.getSampleData(10));
        rvRecordMap.setAdapter(mapAdapter);

        rvRecordMap.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    Log.i("lin", "onScrollStateChanged: " + firstItemPosition);
                    if (firstItemPosition > 0) {
                        mapView.onPause();
                    } else {
                        mapView.onResume();
                    }
                }
            }
        });

        mapAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //地图移动回定位位置
//                MapStatus ms;
//                ms = new MapStatus.Builder().target(new LatLng(39.914935, 116.403119)).zoom(15).build();
//                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
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
        requestParams.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;");

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                RecordEventBean data = gson.fromJson(result, RecordEventBean.class);
                if (data != null && data.isResult()) {
                    mapAdapter.setDatas(data.getEventList());
                    mapAdapter.notifyDataSetChanged();
                    addMarkers(data.getRecordObject(), data.getEventList());
//                    searchRoute(data.getRecordObject(), data.getEventList());
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

    private View getHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.header_record_map,
                (ViewGroup) rvRecordMap.getParent(), false);

        mapView = view.findViewById(R.id.record_map_view);
        mapView.showScaleControl(false);
        mapView.showZoomControls(false);
        mBaiduMap = mapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, null));
        BDMapUtils.setMapViewListener(new locationListener());
//        setLocationData(BDMapUtils.getLocation());

        return view;
    }

    private void setLocationData(LatLng mLoc) {

        if (mLoc == null) {
            return;
        }

        MyLocationData locData = new MyLocationData.Builder().direction(100)
                .latitude(mLoc.latitude).longitude(mLoc.longitude).build();

        // 设置定位数据
        mBaiduMap.setMyLocationData(locData);

        if (isFirstLoc) {
            isFirstLoc = false;
            LatLng ll = new LatLng(mLoc.latitude, mLoc.longitude);
            MapStatus.Builder builder = new MapStatus.Builder();
            //设置地图层级（4-21）
            builder.target(ll).zoom(17.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
    }

    private class locationListener implements MapViewLocationListener {

        @Override
        public void changeLocation(BDLocation mLoc) {

//            setLocationData(mLoc);
        }
    }

    /**
     * 向地图添加Marker点
     */
    public void addMarkers(RecordEventBean.RecordObjectBean rBean, List<RecordEventBean.EventListBean> eventList) {

        items = new ArrayList<>();
        //画直线
        List<LatLng> points = new ArrayList<LatLng>();

        LatLng stLatLng = new LatLng(
                Float.valueOf(rBean.getStartLatitude()), Float.valueOf(rBean.getStartLongitude()));
        LatLng enLatLng = new LatLng(
                Float.valueOf(rBean.getEndLatitude()), Float.valueOf(rBean.getEndLongitude()));

        String eventId = "";
        String emergency = "";

        for (int i = 0; i < eventList.size() + 2; i++) {
            LatLng latLng;
            if (i == 0) {
                latLng = enLatLng;
                eventId = "";
            } else if (i == eventList.size() + 1) {
                latLng = stLatLng;
                eventId = "";
            } else {
                latLng = new LatLng(
                        Float.valueOf(eventList.get(i - 1).getLatitude()),
                        Float.valueOf(eventList.get(i - 1).getLongitude()));
                eventId = eventList.get(i - 1).getId();
                emergency = eventList.get(i - 1).getEmergency();
            }
            items.add(new MyItem(latLng, eventId, emergency));
            points.add(latLng);
        }


        mClusterManager.addItems(items);

        OverlayOptions ooPolyline = new PolylineOptions().width(10)
                .color(0xAAFF0000).points(points);
        mBaiduMap.addOverlay(ooPolyline);
        setLocationData(stLatLng);
        mBaiduMap.showSDKLayer();
    }

    /**
     * 每个Marker点，包含Marker点坐标以及图标
     */
    public class MyItem implements ClusterItem {
        private final LatLng mPosition;

        private final String eventId;

        private final String emergency;

        public MyItem(LatLng latLng, String eventId, String emergency) {
            mPosition = latLng;
            this.eventId = eventId;
            this.emergency = emergency;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        @Override
        public BitmapDescriptor getBitmapDescriptor() {
            if(emergency.equals("1")){
                return BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_map_recode);
            }else if(emergency.equals("2")){
                return BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_map_normal);
            }else if(emergency.equals("3")){
                return BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_map_suspicious);
            }else if(emergency.equals("4")){
                return BitmapDescriptorFactory
                        .fromResource(R.drawable.ic_map_urgency);
            }
            return BitmapDescriptorFactory
                    .fromResource(R.drawable.ic_map_point);
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
