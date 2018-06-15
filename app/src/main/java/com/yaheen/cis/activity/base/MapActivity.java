package com.yaheen.cis.activity.base;

import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.yaheen.cis.entity.RecordEventBean;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends PermissionActivity implements OnGetGeoCoderResultListener,
        OnGetRoutePlanResultListener {

    // 搜索模块
    private GeoCoder mSearch = null;

    //路线规划模块
    private RoutePlanSearch planSearch = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);

        // 初始化搜索模块，注册事件监听
        planSearch = RoutePlanSearch.newInstance();
        planSearch.setOnGetRoutePlanResultListener(this);
    }

    /**
     * 发起搜索
     */
    public void searchAddress(String lat, String lon) {
        LatLng ptCenter = new LatLng((Float.valueOf(lat)), (Float.valueOf(lon)));
        // 反Geo搜索
        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                .location(ptCenter).newVersion(1));
//        else if(v.getId()==R.id.geocode)
//
//    {
//        EditText editCity = (EditText) findViewById(R.id.city);
//        EditText editGeoCodeKey = (EditText) findViewById(R.id.geocodekey);
//        // Geo搜索
//        mSearch.geocode(new GeoCodeOption().city(
//                editCity.getText().toString()).address(editGeoCodeKey.getText().toString()));
//    }
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
//        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//            Toast.makeText(MapActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
//                    .show();
//            return;
//        }
//        String strInfo = String.format("纬度：%f 经度：%f",
//                result.getLocation().latitude, result.getLocation().longitude);
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {

        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MapActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
    }

    public void searchRoute(RecordEventBean.RecordObjectBean rBean, List<RecordEventBean.EventListBean> eventList) {

        LatLng stLatLng = new LatLng(Float.valueOf(rBean.getStartLatitude()), Float.valueOf(rBean.getStartLongitude()));
        LatLng enLatLng = new LatLng(Float.valueOf(rBean.getEndLatitude()), Float.valueOf(rBean.getEndLongitude()));

        PlanNode stNode = PlanNode.withLocation(stLatLng);
        PlanNode enNode = PlanNode.withLocation(enLatLng);
        List<PlanNode> planNodes = new ArrayList<>();
        for (int i = 0; i < eventList.size(); i++) {
            LatLng psLatLng = new LatLng(Float.valueOf(eventList.get(i).getLatitude()), Float.valueOf(eventList.get(i).getLongitude()));
            PlanNode passNode = PlanNode.withLocation(psLatLng);
            planNodes.add(passNode);
        }
        planSearch.drivingSearch((new DrivingRoutePlanOption())
                .from(stNode).to(enNode).passBy(planNodes));
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSearch.destroy();
        planSearch.destroy();
    }
}
