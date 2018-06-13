package com.yaheen.cis.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.MapActivity;
import com.yaheen.cis.activity.base.PermissionActivity;
import com.yaheen.cis.adapter.DataServer;
import com.yaheen.cis.adapter.RecordMapAdapter;
import com.yaheen.cis.entity.RecordEventBean;
import com.yaheen.cis.util.map.BDMapUtils;
import com.yaheen.cis.util.map.cluster.Cluster;
import com.yaheen.cis.util.map.cluster.ClusterItem;
import com.yaheen.cis.util.map.cluster.ClusterManager;
import com.yaheen.cis.util.map.MapViewLocationListener;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class RecordMapActivity extends PermissionActivity {

    private RecyclerView rvRecordMap;

    private RecordMapAdapter mapAdapter;

    private MapView mapView = null;

    private BaiduMap mBaiduMap;

    private ClusterManager<MyItem> mClusterManager;

    private List<MyItem> items;

    private String eventUrl = "http://192.168.199.119:8080/crs/eapi/eventList.do";

    private String recordId;

    //判断地图是否是第一次定位
    private boolean isFirstLoc = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_map);

        recordId = getIntent().getStringExtra("recordId");
        showLoadingDialog();

        initMapView();
        initRecordView();
        getRecordEventList();

        mapAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //地图移动回定位位置
                MapStatus ms;
                ms = new MapStatus.Builder().target(new LatLng(39.914935, 116.403119)).zoom(15).build();
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
            }
        });
    }

    private void initMapView() {
        mapView = findViewById(R.id.record_map_view);
        mapView.showScaleControl(false);
        mapView.showZoomControls(false);
        mBaiduMap = mapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, null));
        BDMapUtils.setMapViewListener(new locationListener());
        setLocationData(BDMapUtils.getLocation());

        // 定义点聚合管理类ClusterManager
        mClusterManager = new ClusterManager<MyItem>(this, mBaiduMap);
        // 添加Marker点
        addMarkers();
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
                Toast.makeText(RecordMapActivity.this,
                        "点击单个Item", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void initRecordView() {
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

    }

    private void getRecordEventList() {
        RequestParams requestParams = new RequestParams(eventUrl);
        requestParams.addQueryStringParameter("recordId", recordId);
        requestParams.addQueryStringParameter("token", DefaultPrefsUtil.getToken());
        requestParams.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;");

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                RecordEventBean data = gson.fromJson(result,RecordEventBean.class);
                if(data!=null&&data.isResult()){
                    mapAdapter.setDatas(data.getEventList());
                    mapAdapter.notifyDataSetChanged();
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
        setLocationData(BDMapUtils.getLocation());

        return view;
    }

    private void setLocationData(BDLocation mLoc) {

        if (mLoc == null) {
            return;
        }

        MyLocationData locData = new MyLocationData.Builder().direction(100)
                .latitude(39.914935).longitude(116.403119).build();

        // 设置定位数据
        mBaiduMap.setMyLocationData(locData);

        if (isFirstLoc) {
            isFirstLoc = false;
            LatLng ll = new LatLng(39.914935, 116.403119);
            MapStatus.Builder builder = new MapStatus.Builder();
            //设置地图层级（4-21）
            builder.target(ll).zoom(17.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
    }

    private class locationListener implements MapViewLocationListener {

        @Override
        public void changeLocation(BDLocation mLoc) {

            setLocationData(mLoc);

//            MyLocationData locData = new MyLocationData.Builder().direction(100)
//                    .latitude(mLoc.getLatitude()).longitude(mLoc.getLongitude()).build();
//
//            // 设置定位数据
//            mBaiduMap.setMyLocationData(locData);
//
//            if (isFirstLoc) {
//                isFirstLoc = false;
//                LatLng ll = new LatLng(mLoc.getLatitude(), mLoc.getLongitude());
//                MapStatus.Builder builder = new MapStatus.Builder();
//                //设置地图层级（4-21）
//                builder.target(ll).zoom(19.0f);
//                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
//            }
        }
    }

    /**
     * 向地图添加Marker点
     */
    public void addMarkers() {
        // 添加Marker点
        LatLng llA = new LatLng(39.963175, 116.400244);
        LatLng llB = new LatLng(39.942821, 116.369199);
        LatLng llC = new LatLng(39.939723, 116.425541);
        LatLng llD = new LatLng(39.906965, 116.401394);
        LatLng llE = new LatLng(39.956965, 116.331394);
        LatLng llF = new LatLng(39.886965, 116.441394);
        LatLng llG = new LatLng(39.996965, 116.411394);

        items = new ArrayList<MyItem>();
        items.add(new MyItem(llA));
        items.add(new MyItem(llB));
        items.add(new MyItem(llC));
        items.add(new MyItem(llD));
        items.add(new MyItem(llE));
        items.add(new MyItem(llF));
        items.add(new MyItem(llG));

        mClusterManager.addItems(items);

        //画直线
        List<LatLng> points = new ArrayList<LatLng>();
        points.add(llA);
        points.add(llB);
        points.add(llC);
        points.add(llD);
        points.add(llE);
        points.add(llF);
        points.add(llG);
        OverlayOptions ooPolyline = new PolylineOptions().width(10)
                .color(0xAAFF0000).points(points);
        mBaiduMap.addOverlay(ooPolyline);
    }

    /**
     * 每个Marker点，包含Marker点坐标以及图标
     */
    public class MyItem implements ClusterItem {
        private final LatLng mPosition;

        public MyItem(LatLng latLng) {
            mPosition = latLng;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        @Override
        public BitmapDescriptor getBitmapDescriptor() {
            return BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_gcoding);
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
