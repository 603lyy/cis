package com.yaheen.cis.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.PermissionActivity;
import com.yaheen.cis.adapter.ImgUploadAdapter;
import com.yaheen.cis.adapter.PatrolTypeAdapter;
import com.yaheen.cis.adapter.ProblemAdapter;
import com.yaheen.cis.adapter.UrgencyAdapter;
import com.yaheen.cis.entity.ImgUploadBean;
import com.yaheen.cis.entity.QuestionBean;
import com.yaheen.cis.entity.ReportBean;
import com.yaheen.cis.entity.TypeBean;
import com.yaheen.cis.entity.UploadLocationListBean;
import com.yaheen.cis.service.UploadLocationService;
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

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class DetailActivity extends PermissionActivity {

    private TextView tvLocation, tvTime, tvCommit, tvFinish;

    private EditText etDescribe;

    private ImageView ivDelete;

    private MapView mapView = null;

    private BaiduMap mBaiduMap;

    private RecyclerView rvProblem, rvUrgency, rvImg, rvPatrol;

    private LinearLayout llDetailTitle;

    private RefreshLayout refreshLayout;

    private View llTitle;

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

    //判断是否签到按钮进入，是则取消巡查功能，只提供上报功能
    private boolean isSign = false;

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

        llDetailTitle = findViewById(R.id.ll_detail_title);
        refreshLayout = findViewById(R.id.refresh_layout);
        llTitle = findViewById(R.id.ll_title_bar);
        tvCommit = findViewById(R.id.tv_commit);
        tvFinish = findViewById(R.id.tv_finish);
        showLoadingDialog();

        isSign = getIntent().getBooleanExtra("sign", false);
        questionStr = getIntent().getStringExtra("question");
        typeStr = getIntent().getStringExtra("type");

//        qData = gson.fromJson(questionStr, QuestionBean.class);
        typeData = gson.fromJson(typeStr, TypeBean.class);
        startTime = DefaultPrefsUtil.getPatrolStart();
        recordId = typeData.getRecordId();

        //开始定位
        BDMapUtils.startLocation();

        //开始时间为零，即开始新的巡查
        if (startTime == 0) {
            startTime = System.currentTimeMillis();
            DefaultPrefsUtil.setPatrolStart(startTime);
        }

        if (!TextUtils.isEmpty(recordId)) {
            DefaultPrefsUtil.setPatrolRecordId(recordId);
        }

//        开启后台服务上传坐标（待定）
//        Intent intent = new Intent(getApplicationContext(), UploadLocationService.class);
//        startService(intent);
//        bindService(intent, conn, BIND_AUTO_CREATE);

        initView();
        initPatrol();
        initQuestion();
        initUrgency();
        initMapView();
        initImgUpload();
        if (qData == null) {
            getQuestionMsg(typeData.getTypeArr().get(0).getId());
        }

        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendReport();
            }
        });

        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });

        refreshLayout.setDragRate(0.3f);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshlayout) {
                getQuestionMsg(typeAdapter.getTypeId());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mBaiduMap.setMyLocationEnabled(true);
        mapView.onResume();
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

        if (!isSign) {
            CountDownTimerUtils.getCountDownTimer()
                    .setMillisInFuture(7 * 24 * 60 * 60 * 1000)
                    .setCountDownInterval(1000)
                    .setTickDelegate(new CountDownTimerUtils.TickDelegate() {
                        @Override
                        public void onTick(long pMillisUntilFinished) {
                            long time = System.currentTimeMillis() - startTime - 28800000L;
                            tvTime.setText(TimeTransferUtils.getHMSStrTime(time + ""));
                        }
                    }).start();
            llTitle.setVisibility(View.GONE);
            llDetailTitle.setVisibility(View.VISIBLE);
        } else {
            llTitle.setVisibility(View.VISIBLE);
            llDetailTitle.setVisibility(View.GONE);
        }
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
        rvProblem.setLayoutManager(new GridLayoutManager(this, 3));

        problemAdapter = new ProblemAdapter();
        if (!TextUtils.isEmpty(typeStr) && qData != null) {
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

        mBaiduMap = mapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
//        mBaiduMap.hideSDKLayer();
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
                if (uploadIdList.size() < 9) {
                    ImgUploadHelper.showUserAvatarUploadDialog(
                            DetailActivity.this, imgListener, 9 - uploadIdList.size());
                } else {
                    showToast(R.string.detail_img_limit);
                }
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
        requestParams.addQueryStringParameter("typeId", typeId);
        requestParams.addQueryStringParameter("token", DefaultPrefsUtil.getToken());
        requestParams.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;");
        requestParams.setConnectTimeout(60 * 1000);
        requestParams.setReadTimeout(60 * 1000);

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                QuestionBean data = gson.fromJson(result, QuestionBean.class);
                if (data != null && data.isResult()) {
                    problemAdapter.setDatas(data.getTypeArr());
                    problemAdapter.notifyDataSetChanged();
                    refreshLayout.finishRefresh(true);
                    clearData();
                } else {
                    showToast(R.string.get_question_empty);
//                    finish();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(R.string.get_question_empty);
//                finish();
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

    private void upLoadImg(final Uri uri, final String imgPath, final boolean isTakePhoto) {

//        final String imgPath;
//
//        if (!isTakePhoto) {
//            imgPath = UriUtil.getPath(DetailActivity.this, uri);
//        } else {
//            imgPath = path;
//        }

        if (TextUtils.isEmpty(imgPath)) {
            return;
        }

        RequestParams params = new RequestParams(uploadImgUrl);
        params.addQueryStringParameter("token", DefaultPrefsUtil.getToken());
        params.addBodyParameter("originImage", new File(imgPath));
        params.setConnectTimeout(60 * 1000);
        params.setReadTimeout(60 * 1000);
        params.setMultipart(true);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ImgUploadBean data = gson.fromJson(result, ImgUploadBean.class);
                //删除已请求上传图片的路径
                if (selectUriList.size() > 0) {
                    selectUriList.remove(0);
                }
                if (data != null) {
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
                }

                //图片路径不为空，继续上传剩余图片
                if (selectUriList.size() > 0) {
                    ImgUploadHelper.compressImage(DetailActivity.this,
                            UriUtil.getPath(DetailActivity.this,
                                    selectUriList.get(0)), isTakePhoto);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(R.string.upload_image_fail);
                cancelLoadingDialog();
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

        if (BDMapUtils.getLocation() == null) {
            showToast(R.string.map_init_ing);
            return;
        }

        if (TextUtils.isEmpty(problemAdapter.getQuestionStr())) {
            showToast(R.string.detail_question_empty);
            return;
        }

        if (TextUtils.isEmpty(etDescribe.getText())) {
            showToast(R.string.detail_describe_empty);
            return;
        }

        if (TextUtils.isEmpty(urgencyAdapter.geUrgencyId())) {
            showToast(R.string.detail_urgency_empty);
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
        jsonObject.addProperty("typeId", typeAdapter.getTypeId());
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
                    if (isSign) {
                        finish();
                    } else {
                        clearData();
                    }
                } else {
                    showToast(R.string.detail_commit_fail);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(R.string.detail_commit_fail);
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

        if (BDMapUtils.getLocation() == null) {
            showToast(R.string.map_init_ing);
            return;
        }
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
                    DefaultPrefsUtil.setPatrolqQuestion("");
                    DefaultPrefsUtil.setPatrolStart(0);
                    DefaultPrefsUtil.setPatrolType("");
                    BDMapUtils.stopLocation();
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
            tvLocation.setText(mLoc.getAddrStr());
//            mBaiduMap.showSDKLayer();

            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(mLoc.getLatitude(), mLoc.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                //设置地图层级（4-21）
                builder.target(ll).zoom(19.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            } else {
//            地图移动回定位位置
                MapStatus ms;
                ms = new MapStatus.Builder().target(new LatLng(mLoc.getLatitude(), mLoc.getLongitude())).zoom(19.0f).build();
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
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
            ImgUploadHelper.compressImage(DetailActivity.this,
                    UriUtil.getPath(DetailActivity.this, list.get(0)), isTakePhoto);
        }
    };

    @Override
    public void compress(Uri uri, String imgPath, boolean isTakePhoto) {
        showLoadingDialog();
        upLoadImg(uri, imgPath, isTakePhoto);
    }

    //上报成功后重置数据
    private void clearData() {
        etDescribe.setText("");
        ivDelete.setVisibility(View.GONE);

        imgUriList.clear();
        uploadIdList.clear();
        selectUriList.clear();
        adapterPathList.clear();

        problemAdapter.resetData();
        urgencyAdapter.resetData();
        uploadAdapter.setDatas(null);
        urgencyAdapter.notifyDataSetChanged();
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
        CountDownTimerUtils.getCountDownTimer().cancel();
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
        super.onBackPressed();
    }
}
