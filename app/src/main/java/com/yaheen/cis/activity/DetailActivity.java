package com.yaheen.cis.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yaheen.cis.BaseApp;
import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.PermissionActivity;
import com.yaheen.cis.adapter.DataServer;
import com.yaheen.cis.adapter.ImgUploadAdapter;
import com.yaheen.cis.adapter.PatrolTypeAdapter;
import com.yaheen.cis.adapter.ProblemAdapter;
import com.yaheen.cis.adapter.UrgencyAdapter;
import com.yaheen.cis.entity.ImgUploadBean;
import com.yaheen.cis.entity.QuestionBean;
import com.yaheen.cis.entity.ReportBean;
import com.yaheen.cis.entity.TypeBean;
import com.yaheen.cis.entity.UploadLocationListBean;
import com.yaheen.cis.util.DialogUtils;
import com.yaheen.cis.util.dialog.DialogCallback;
import com.yaheen.cis.util.dialog.IDialogCancelCallback;
import com.yaheen.cis.util.img.ImgUploadHelper;
import com.yaheen.cis.util.img.PhotoPagerUtils;
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

    private TextView tvLocation, tvTime, tvCommit, tvFinish;

    private EditText etDescribe;

    private ImageView ivDelete;

    private MapView mapView = null;

    private BaiduMap mBaiduMap;

    private RecyclerView rvProblem, rvUrgency, rvImg, rvPatrol;

    private ProblemAdapter problemAdapter;

    private PatrolTypeAdapter typeAdapter;

    private UrgencyAdapter urgencyAdapter;

    private ImgUploadAdapter uploadAdapter;

    private String questionUrl = baseUrl + "/eapi/findQuestionaireByTypeId.do";

    private String uploadImgUrl = baseUrl + "/eapi/uploadPhoto.do";

    private String reportUrl = baseUrl + "/eapi/report.do";

    private String endUrl = baseUrl + "/eapi/endPatrol.do";

    private String typeStr, questionStr, recordId;

    //已上传图片的ID的拼接
    private String imgIdStr = "";

    //判断地图是否是第一次定位
    private boolean isFirstLoc = true;

    //记录开始巡查的时间戳,方便计算时间
    private long startTime;

    //被选择的图片路径列表
    private List<Uri> selectUriList = new ArrayList<>();

    //图片上传列表的图片路径列表
    private List<String> adapterPathList = new ArrayList<>();

    //图片上传列表的图片路径列表
    private List<Uri> imgUriList = new ArrayList<>();

    //已上传图片的ID列表
    private List<String> uploadIdList = new ArrayList<>();

    //定时上传的坐标点列表
    private List<UploadLocationListBean.LocationBean> locationList = new ArrayList<>();

    //问题类型实体
    private TypeBean typeData;

    //问题实体
    private QuestionBean qData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        tvCommit = findViewById(R.id.tv_commit);
        tvFinish = findViewById(R.id.tv_finish);

        startTime = System.currentTimeMillis();
        typeStr = getIntent().getStringExtra("type");
        recordId = getIntent().getStringExtra("recordId");
        questionStr = getIntent().getStringExtra("question");
        qData = gson.fromJson(questionStr, QuestionBean.class);
        typeData = gson.fromJson(typeStr, TypeBean.class);

        //记录ID不可为空
        if (TextUtils.isEmpty(recordId)) {
            finish();
        }

        initView();
        initPatrol();
        initQuestion();
        initUrgency();
        initMapView();
        initImgUpload();

        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendReport();
            }
        });

        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initView() {
        tvTime = findViewById(R.id.tv_time);
        etDescribe = findViewById(R.id.et_describe);
        tvLocation = findViewById(R.id.tv_location_describe);

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

    private void initPatrol() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvPatrol = findViewById(R.id.rv_patrol);
        rvPatrol.setLayoutManager(layoutManager);

        typeAdapter = new PatrolTypeAdapter();
        typeAdapter.setDatas(typeData.getTypeArr());
        rvPatrol.setAdapter(typeAdapter);

        typeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                //不是被选中的巡查类型，改为选中状态,其他改为未选中状态
                if (!typeAdapter.getData().get(position).isSelected()) {
                    for (int i = 0; i < typeAdapter.getData().size(); i++) {
                        if (i == position) {
                            typeAdapter.getData().get(i).setSelected(true);
                        } else {
                            typeAdapter.getData().get(i).setSelected(false);
                        }
                    }
                    showLoadingDialog();
                    getQuestionMsg(typeAdapter.getData().get(position).getId());
                }
                typeAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initQuestion() {
        rvProblem = findViewById(R.id.rv_problem);
        rvProblem.setLayoutManager(new GridLayoutManager(this, 4));

        problemAdapter = new ProblemAdapter();
        if (!TextUtils.isEmpty(typeStr)) {
            problemAdapter.setDatas(qData.getTypeArr());
        }
        rvProblem.setAdapter(problemAdapter);
    }

    private void initUrgency() {
        rvUrgency = findViewById(R.id.rv_urgency);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvUrgency.setLayoutManager(layoutManager);

        urgencyAdapter = new UrgencyAdapter();
        rvUrgency.setAdapter(urgencyAdapter);

        urgencyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!urgencyAdapter.getData().get(position).isSelect()) {
                    for (int i = 0; i < urgencyAdapter.getData().size(); i++) {
                        if (i == position) {
                            urgencyAdapter.getData().get(i).setSelect(true);
                        } else {
                            urgencyAdapter.getData().get(i).setSelect(false);
                        }
                    }
                }
                urgencyAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initImgUpload() {
        rvImg = findViewById(R.id.rv_img);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        rvImg.setLayoutManager(layoutManager);

        uploadAdapter = new ImgUploadAdapter(this);
        uploadAdapter.addFooterView(getImgFooterView());
        rvImg.setAdapter(uploadAdapter);

        uploadAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view instanceof ImageView) {
                    ArrayList<String> urls = new ArrayList<String>();
                    urls.add(imgUriList.get(position) + "");
                    new PhotoPagerUtils.Builder(DetailActivity.this)
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
        tvLocation.setText(BDMapUtils.getLocation().getAddrStr());

        mBaiduMap = mapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, null));
        BDMapUtils.setMapViewListener(new LocationListener());
    }

    private View getImgFooterView() {
        View view = getLayoutInflater().inflate(R.layout.footer_img_upload,
                (ViewGroup) rvImg.getParent(), false);
        ImageView ivAdd = view.findViewById(R.id.iv_add);
        ivDelete = view.findViewById(R.id.iv_delete);

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImgUploadHelper.showUserAvatarUploadDialog(DetailActivity.this, imgListener);
            }
        });

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadIdList.clear();
                selectUriList.clear();
                adapterPathList.clear();
                uploadAdapter.setDatas(adapterPathList);
                ivDelete.setVisibility(View.GONE);
            }
        });
        return view;
    }

    private void getQuestionMsg(String typeId) {
        RequestParams requestParams = new RequestParams(questionUrl);
        requestParams.addQueryStringParameter("token", DefaultPrefsUtil.getToken());
        requestParams.addQueryStringParameter("typeId", typeId);
        requestParams.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;");

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                QuestionBean data = gson.fromJson(result, QuestionBean.class);
                if (data != null && data.isResult()) {
                    problemAdapter.setDatas(data.getTypeArr());
                    problemAdapter.notifyDataSetChanged();
                    clearData();
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

    private void upLoadImg(final Uri uri, final boolean isTakePhoto) {

        final String imgPath;

        if (!isTakePhoto) {
            imgPath = UriUtil.getPath(DetailActivity.this, uri);
        } else {
            imgPath = ImgUploadHelper.getPhotoPath();
        }

        if (TextUtils.isEmpty(imgPath)) {
            return;
        }

        RequestParams params = new RequestParams(uploadImgUrl);
        params.addQueryStringParameter("token", DefaultPrefsUtil.getToken());
        params.addBodyParameter("originImage", new File(imgPath));
        params.setMultipart(true);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ImgUploadBean data = gson.fromJson(result, ImgUploadBean.class);
                if (data != null) {
                    //删除已请求上传图片的路径
                    selectUriList.remove(uri);

                    if (data.isResult()) {
                        imgUriList.add(uri);
                        uploadIdList.add(data.getFileId());
                        adapterPathList.add(imgPath);
                        uploadAdapter.setDatas(adapterPathList);
                        uploadAdapter.notifyDataSetChanged();
                        if (TextUtils.isEmpty(imgIdStr)) {
                            imgIdStr = data.getFileId();
                        } else {
                            imgIdStr = imgIdStr + "," + data.getFileId();
                        }
                    }

                    if (uploadIdList != null && uploadIdList.size() > 0) {
                        ivDelete.setVisibility(View.VISIBLE);
                    } else {
                        ivDelete.setVisibility(View.GONE);
                    }

                    //图片路径不为空，继续上传剩余图片
                    if (selectUriList.size() > 0) {
                        upLoadImg(selectUriList.get(0), isTakePhoto);
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

    private void sendReport() {

        if (TextUtils.isEmpty(urgencyAdapter.geUrgencyId())) {
            showToast(R.string.detail_urgency_empty);
            return;
        }

//        if (TextUtils.isEmpty(problemAdapter.getQuestionStr())) {
//            showToast(R.string.detail_urgency_empty);
//            return;
//        }

        if (TextUtils.isEmpty(etDescribe.getText())) {
            showToast(R.string.detail_describe_empty);
            return;
        }

        showLoadingDialog();

        String s = "";

        for (int i = 0; i < uploadIdList.size(); i++) {
            if (i == 0) {
                s = uploadIdList.get(i);
            } else {
                s = s + "," + uploadIdList.get(i);
            }
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("typeId", typeData.getTypeArr().get(0).getId());
        jsonObject.addProperty("questionaireIds", problemAdapter.getQuestionStr());
        jsonObject.addProperty("emergency", urgencyAdapter.geUrgencyId());
        jsonObject.addProperty("describe", etDescribe.getText().toString());
        jsonObject.addProperty("longitude", BDMapUtils.getLocation().getLongitude());
        jsonObject.addProperty("latitude", BDMapUtils.getLocation().getLatitude());
        jsonObject.addProperty("webFileids", s);


        RequestParams requestParams = new RequestParams(reportUrl);
        requestParams.addQueryStringParameter("token", DefaultPrefsUtil.getToken());
        requestParams.addQueryStringParameter("recordId", recordId);
        requestParams.addQueryStringParameter("data", gson.toJson(jsonObject));
        requestParams.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;");

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ReportBean data = gson.fromJson(result, ReportBean.class);
                if (data != null && data.isResult()) {
                    showToast(R.string.detail_commit_success);
                    clearData();
                } else {
                    showToast(R.string.detail_commit_fail);
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

    private void finishPatrol() {
        showLoadingDialog();

        UploadLocationListBean.LocationBean bean = new UploadLocationListBean.LocationBean();
        bean.setLatitude(BDMapUtils.getLocation().getLatitude() + "");
        bean.setLongitude(BDMapUtils.getLocation().getLongitude() + "");
        locationList.add(bean);

        RequestParams requestParams = new RequestParams(endUrl);
        requestParams.addQueryStringParameter("recordId", recordId);
        requestParams.addQueryStringParameter("data", gson.toJson(locationList));
        requestParams.addQueryStringParameter("token", DefaultPrefsUtil.getToken());
        requestParams.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;");

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ReportBean data = gson.fromJson(result, ReportBean.class);
                if (data != null && data.isResult()) {
                    showToast(R.string.detail_finish_success);
                    finish();
                } else {
                    showToast(R.string.detail_finish_fail);
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

    private class LocationListener implements MapViewLocationListener {

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
        public void upLoad(List<Uri> list, boolean isTakePhoto) {
            showLoadingDialog();

            if (list.size() <= 0) {
                return;
            }
            selectUriList = list;
            upLoadImg(list.get(0), isTakePhoto);
        }
    };

    //上报成功后重置数据
    private void clearData() {
        imgUriList.clear();
        uploadIdList.clear();
        selectUriList.clear();
        etDescribe.setText("");
        adapterPathList.clear();
        problemAdapter.resetData();
        uploadAdapter.setDatas(null);
        problemAdapter.notifyDataSetChanged();
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

    @Override
    public void onBackPressed() {

        DialogUtils.showDialog(DetailActivity.this, "是否要结束本次巡查？", new DialogCallback() {
            @Override
            public void callback() {
                finishPatrol();
            }
        }, new IDialogCancelCallback() {
            @Override
            public void cancelCallback() {
            }
        });
    }
}
