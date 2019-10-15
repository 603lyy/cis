package com.yaheen.cis.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieSyncManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.google.gson.JsonObject;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.BaseActivity;
import com.yaheen.cis.activity.base.FetchActivity;
import com.yaheen.cis.adapter.ImgUploadAdapter;
import com.yaheen.cis.adapter.PatrolTypeAdapter;
import com.yaheen.cis.adapter.ProblemAdapter;
import com.yaheen.cis.adapter.UrgencyAdapter;
import com.yaheen.cis.entity.HouseBean;
import com.yaheen.cis.entity.ImgUploadBean;
import com.yaheen.cis.entity.QuestionBean;
import com.yaheen.cis.entity.ReportBean;
import com.yaheen.cis.entity.TypeBean;
import com.yaheen.cis.entity.UploadLocationListBean;
import com.yaheen.cis.util.DialogUtils;
import com.yaheen.cis.util.HttpUtils;
import com.yaheen.cis.util.common.FreeHandScreenUtil;
import com.yaheen.cis.util.dialog.DialogCallback;
import com.yaheen.cis.util.dialog.IDialogCancelCallback;
import com.yaheen.cis.util.img.ImgUploadHelper;
import com.yaheen.cis.util.img.PhotoPagerUtils;
import com.yaheen.cis.util.img.UpLoadImgListener;
import com.yaheen.cis.util.img.UriUtil;
import com.yaheen.cis.util.img.WebViewImgUploadHelper;
import com.yaheen.cis.util.nfc.Base64Utils;
import com.yaheen.cis.util.notification.NotificationUtils;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;
import com.yaheen.cis.util.time.CountDownTimerUtils;
import com.yaheen.cis.util.map.BDMapUtils;
import com.yaheen.cis.util.map.MapViewLocationListener;
import com.yaheen.cis.util.time.TimeTransferUtils;
import com.yaheen.cis.util.upload.UploadLocationUtils;
import com.yaheen.cis.widget.webview.WebJavaScriptProvider;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class DetailActivity extends FetchActivity {

    private TextView tvLocation, tvTime, tvCommit, tvFinish, tvFetch;

    private TextView tvPTime, tvPAddress, tvPUsername, tvPPhone, tvPArea, tvPLeader, tvOwner;

    private EditText etDescribe;

    private ImageView ivDelete;

    private MapView mapView = null;

    private BaiduMap mBaiduMap;

    private RecyclerView rvProblem, rvUrgency, rvImg, rvPatrol;

    private LinearLayout llDetailTitle;

    private LinearLayout llProblem, llDescribe, llUrgency, llImg, llMap, llWebView;

    private RefreshLayout refreshLayout;

    private View llTitle, llHouse;

    private WebView mWebView;

    private ProblemAdapter problemAdapter;

    private PatrolTypeAdapter typeAdapter;

    private UrgencyAdapter urgencyAdapter;

    private ImgUploadAdapter uploadAdapter;

    private String questionUrl = "";

    private String uploadImgUrl = "";

    private String reportUrl = "";

    private String endUrl = "";

    private String typeStr, recordId, houseId;

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

    private ValueCallback<Uri[]> mUploadMsgs;

    private ValueCallback<Uri> mUploadMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        llDetailTitle = findViewById(R.id.ll_detail_title);
        refreshLayout = findViewById(R.id.refresh_layout);
        llHouse = findViewById(R.id.ll_house_data);
        llTitle = findViewById(R.id.ll_title_bar);
        tvCommit = findViewById(R.id.tv_commit);
        tvFinish = findViewById(R.id.tv_finish);
        showLoadingDialog();

        houseId = getIntent().getStringExtra("houseId");
        typeStr = getIntent().getStringExtra("type");

        typeData = gson.fromJson(typeStr, TypeBean.class);
        if (TimeTransferUtils.getHMSTime(typeData.getRecordStartTime()) != null) {
            startTime = Long.valueOf(TimeTransferUtils.getHMSTime(typeData.getRecordStartTime()));
        } else {
            startTime = System.currentTimeMillis();
        }
        recordId = typeData.getRecordId();

        //开始定位
        BDMapUtils.startLocation();

        if (!TextUtils.isEmpty(recordId)) {
            DefaultPrefsUtil.setPatrolRecordId(recordId);
        }

        if (!isSign) {
            //开始时间为零，即开始新的巡查
//            if (startTime == 0) {
//                startTime = System.currentTimeMillis();
//                DefaultPrefsUtil.setPatrolStart(startTime);
//            }
            UploadLocationUtils.startUpload(getApplicationContext());
        }

        initData();
        initView();
        initPatrol();
        initQuestion();
        initUrgency();
        initMapView();
        initImgUpload();
        initWebViewSetting();

        getQuestionMsg(typeData.getTypeArr().get(0).getId());

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

    private void initData() {
        questionUrl = getBaseUrl() + "/eapi/findQuestionaireByTypeId.do";
        uploadImgUrl = getBaseUrl() + "/eapi/uploadPhoto.do";
        reportUrl = getBaseUrl() + "/eapi/report.do";
        endUrl = getBaseUrl() + "/eapi/endPatrol.do";
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mBaiduMap.setMyLocationEnabled(true);
        mapView.onResume();
    }

    private void initView() {
        llImg = findViewById(R.id.ll_img);
        llMap = findViewById(R.id.ll_map);
        tvTime = findViewById(R.id.tv_time);
        tvFetch = findViewById(R.id.tv_fetch);
        llProblem = findViewById(R.id.ll_problem);
        llUrgency = findViewById(R.id.ll_urgency);
        llWebView = findViewById(R.id.ll_web_view);
        etDescribe = findViewById(R.id.et_describe);
        llDescribe = findViewById(R.id.ll_describe);
        tvLocation = findViewById(R.id.tv_location_describe);

        mWebView = findViewById(R.id.web_view);

        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BDMapUtils.getLocation() != null) {
                    tvLocation.setText(BDMapUtils.getLocation().getAddrStr());
                }
            }
        });

        tvFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFetch();
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
            llHouse.setVisibility(View.GONE);
            llTitle.setVisibility(View.GONE);
            llDetailTitle.setVisibility(View.VISIBLE);
        } else {
            llHouse.setVisibility(View.VISIBLE);
            llTitle.setVisibility(View.VISIBLE);
            llDetailTitle.setVisibility(View.GONE);
        }
    }

    private void initHouseData() {
//        tvPUsername = findViewById(R.id.tv_house_username);
//        tvPAddress = findViewById(R.id.tv_patrol_address);
//        tvPLeader = findViewById(R.id.tv_house_leader);
//        tvPPhone = findViewById(R.id.tv_house_phone);
//        tvPTime = findViewById(R.id.tv_patrol_time);
//        tvOwner = findViewById(R.id.tv_house_owner);
//        tvPArea = findViewById(R.id.tv_house_area);
    }

    private void initPatrol() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvPatrol = findViewById(R.id.rv_patrol);
        rvPatrol.setLayoutManager(new GridLayoutManager(this, 3));

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
                    if (TextUtils.isEmpty(typeAdapter.getData().get(position).getLink())) {
                        getQuestionMsg(typeAdapter.getData().get(position).getId());
                    }
                    checkShowWebView(typeAdapter.getData().get(position).getLink());
                }
                typeAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 判断显示网页控件
     */
    @SuppressLint("ClickableViewAccessibility")
    private void checkShowWebView(String link) {
        if (!TextUtils.isEmpty(link)) {
            cancelLoadingDialog();
            llImg.setVisibility(View.GONE);
            llMap.setVisibility(View.GONE);
            tvCommit.setVisibility(View.GONE);
            llProblem.setVisibility(View.GONE);
            llUrgency.setVisibility(View.GONE);
            llDescribe.setVisibility(View.GONE);
            llWebView.setVisibility(View.VISIBLE);
            refreshLayout.setEnableRefresh(false);

            int[] viewLoc = new int[2];
            llProblem.getLocationOnScreen(viewLoc);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llWebView.getLayoutParams();
            int[] screenSize = FreeHandScreenUtil.getScreenSize(getApplicationContext());
            params.height = (int) (screenSize[1] - viewLoc[1]);

            initWebViewSetting();
            mWebView.loadUrl(link + "?whnUrl=" + getHouseUrl()
                    + "&userName=" + DefaultPrefsUtil.getCurrentUserName() + "&userId=" + DefaultPrefsUtil.getUserId()
                    + "&houseNumberId=" + "&role=" + DefaultPrefsUtil.getRole());
        } else {
            llImg.setVisibility(View.VISIBLE);
            llMap.setVisibility(View.VISIBLE);
            llWebView.setVisibility(View.GONE);
            tvCommit.setVisibility(View.VISIBLE);
            llUrgency.setVisibility(View.VISIBLE);
            llProblem.setVisibility(View.VISIBLE);
            llDescribe.setVisibility(View.VISIBLE);
            refreshLayout.setEnableRefresh(true);
        }
    }

    /**
     * init WebView
     */
    private void initWebViewSetting() {
        WebSettings webSetting = mWebView.getSettings();
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);

        //手机屏幕适配
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setUseWideViewPort(true);

        //禁止放大
        webSetting.setBuiltInZoomControls(false);
        webSetting.setSupportZoom(false);
        webSetting.setDisplayZoomControls(false);

        //启用数据库
        webSetting.setDatabaseEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);//支持JavaScriptEnabled
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        //启用地理定位
        webSetting.setGeolocationEnabled(true);
        //设置定位的数据库路径
        webSetting.setGeolocationDatabasePath(dir);
        //最重要的方法，一定要设置，这就是出不来的主要原因
        webSetting.setDomStorageEnabled(true);
        //设置可以访问文件
        webSetting.setAllowFileAccess(true);
        webSetting.setJavaScriptEnabled(true);

        mWebView.setWebChromeClient(webChromeClient);
        mWebView.setWebViewClient(webViewClient);

        mWebView.addJavascriptInterface(new FetchProvider(this, this), "android");
    }

    class FetchProvider extends WebJavaScriptProvider {

        public FetchProvider(Context ctx, BaseActivity activity) {
            super(ctx, activity);
        }

        @JavascriptInterface
        public void back() {
            finish();
        }

    }

    private void initQuestion() {
        rvProblem = findViewById(R.id.rv_problem);
        rvProblem.setLayoutManager(new GridLayoutManager(this, 3));

        problemAdapter = new ProblemAdapter();
        if (!TextUtils.isEmpty(typeStr) && qData != null) {
            problemAdapter.setDatas(qData.getQuestionaireArr());
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
                imgUriList.clear();
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

        if (typeId.equals("-1")) {
            return;
        }

        RequestParams requestParams = new RequestParams(questionUrl);
        requestParams.addQueryStringParameter("typeId", typeId);
        requestParams.addQueryStringParameter("token", DefaultPrefsUtil.getToken());

        HttpUtils.getPostHttp(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                QuestionBean data = gson.fromJson(result, QuestionBean.class);
                if (data != null && data.isResult()) {
                    problemAdapter.setDatas(data.getQuestionaireArr());
                    clearData();
                } else if (data != null && data.getCode() == 1002) {
                    startActivity(new Intent(DetailActivity.this, LoginActivity.class));
                    finish();
                } else {
                    showToast(R.string.get_question_empty);
                    problemAdapter.setDatas(null);
//                    finish();
                }
                problemAdapter.notifyDataSetChanged();
                refreshLayout.finishRefresh(true);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(R.string.get_question_empty);
                problemAdapter.notifyDataSetChanged();
                refreshLayout.finishRefresh(true);
                problemAdapter.setDatas(null);
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
        params.setMultipart(true);

        HttpUtils.getPostHttp(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        ImgUploadBean data = gson.fromJson(result, ImgUploadBean.class);
                        //删除已请求上传图片的路径
                        if (selectUriList.size() > 0) {
                            selectUriList.remove(0);
                        }
                        if (data != null) {

                            if (data.getCode() == 1002) {
                                startActivity(new Intent(DetailActivity.this, LoginActivity.class));
                                finish();
                            }

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
                }
        );
    }

    private void sendReport() {

        if (BDMapUtils.getLocation() == null) {
            showToast(R.string.map_init_ing);
            return;
        }

        if (BDMapUtils.getLocation().getLatitude() < 1 || BDMapUtils.getLocation().getLongitude() < 1) {
            showToast(R.string.map_init_fail);
            cancelLoadingDialog();
            return;
        }

        if (TextUtils.isEmpty(problemAdapter.getQuestionStr())) {
            showToast(R.string.detail_question_empty);
            return;
        }

//        if (TextUtils.isEmpty(etDescribe.getText())) {
//            showToast(R.string.detail_describe_empty);
//            return;
//        }

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
        jsonObject.addProperty("area", BDMapUtils.getLocation().getAddrStr());
        jsonObject.addProperty("webFileids", s);
        jsonObject.addProperty("houseId", houseId);

//        if (isSign && !TextUtils.isEmpty(houseId)) {
//            jsonObject.addProperty("scopeOfOperation", tvPArea.getText().toString());
//            jsonObject.addProperty("householdName", tvPUsername.getText().toString());
//            jsonObject.addProperty("inspectionSite", tvPAddress.getText().toString());
//            jsonObject.addProperty("householdPhone", tvPPhone.getText().toString());
//            jsonObject.addProperty("fireOfficer", tvPLeader.getText().toString());
//            jsonObject.addProperty("businessHours", tvPTime.getText().toString());
//        }

        RequestParams requestParams = new RequestParams(reportUrl);
        requestParams.addQueryStringParameter("token", DefaultPrefsUtil.getToken());
        requestParams.addQueryStringParameter("role", DefaultPrefsUtil.getRole());
        requestParams.addQueryStringParameter("data", gson.toJson(jsonObject));
        requestParams.addQueryStringParameter("recordId", recordId);
        if (isSign && !TextUtils.isEmpty(houseId)) {
            requestParams.addParameter("point", true);
        }


        HttpUtils.getPostHttp(requestParams, new Callback.CommonCallback<String>() {
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
                } else if (data != null && data.getCode() == 1002) {
                    startActivity(new Intent(DetailActivity.this, LoginActivity.class));
                    finish();
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

        if (BDMapUtils.getLocation().getLatitude() < 1 || BDMapUtils.getLocation().getLongitude() < 1) {
            showToast(R.string.map_init_fail);
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

        HttpUtils.getPostHttp(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ReportBean data = gson.fromJson(result, ReportBean.class);
                if (data != null && data.isResult()) {
                    showToast(R.string.detail_finish_success);
                    finishPatrolSetting();
                    finish();
                } else if (data != null && data.getCode() == 1002) {
                    startActivity(new Intent(DetailActivity.this, LoginActivity.class));
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

    private WebChromeClient webChromeClient = new WebChromeClient() {

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                cancelLoadingDialog();
            }
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, true);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            mUploadMsg = null;
            mUploadMsgs = null;
            mUploadMsgs = filePathCallback;

            WebViewImgUploadHelper.showImgUploadDialog(DetailActivity.this, imgListener,
                    9 - uploadIdList.size(), mUploadMsgs, mUploadMsg);

            return true;
        }

        // For Android 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUploadMsg = null;
            mUploadMsgs = null;
            mUploadMsg = uploadMsg;

            WebViewImgUploadHelper.showImgUploadDialog(DetailActivity.this, imgListener,
                    9 - uploadIdList.size(), mUploadMsgs, mUploadMsg);
        }

        // For Android > 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType, String capture) {
            mUploadMsg = null;
            mUploadMsgs = null;
            mUploadMsg = uploadMsg;

            WebViewImgUploadHelper.showImgUploadDialog(DetailActivity.this, imgListener,
                    9 - uploadIdList.size(), mUploadMsgs, mUploadMsg);
        }

        // Andorid 3.0 +
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            mUploadMsg = null;
            mUploadMsgs = null;
            mUploadMsg = uploadMsg;

            WebViewImgUploadHelper.showImgUploadDialog(DetailActivity.this, imgListener,
                    9 - uploadIdList.size(), mUploadMsgs, mUploadMsg);
        }
    };

    private WebViewClient webViewClient = new WebViewClient() {

//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//            return super.shouldOverrideUrlLoading(view, request);
//        }

    };

    private class LocationListener implements MapViewLocationListener {

        @Override
        public void changeLocation(BDLocation mLoc) {

            MyLocationData locData = new MyLocationData.Builder().direction(100)
                    .latitude(mLoc.getLatitude()).longitude(mLoc.getLongitude()).build();

            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            tvLocation.setText(mLoc.getAddrStr());

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
                ms = new MapStatus.Builder()
                        .target(new LatLng(mLoc.getLatitude(), mLoc.getLongitude()))
                        .zoom(mBaiduMap.getMapStatus().zoom).build();
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
                tvLocation.setText(mLoc.getAddrStr());
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

    @Override
    public void getHouseId(String type, String houseId) {
        super.getHouseId(type, houseId);
        Intent intent = new Intent(DetailActivity.this, DetailPointActivity.class);
        intent.putExtra("recordId", recordId);
        intent.putExtra("sign", true);
        intent.putExtra("houseId", houseId);
        intent.putExtra("type", type);
        startActivity(intent);
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

    private void finishPatrolSetting() {
        //关闭定时上传坐标服务
        UploadLocationUtils.stopUpload();
        //清空本地记录
        DefaultPrefsUtil.setPatrolqQuestion("");
        DefaultPrefsUtil.setPatrolRecordId("");
        DefaultPrefsUtil.setPatrolStart(0);
        DefaultPrefsUtil.setPatrolType("");
        //停止坐标获取
        BDMapUtils.stopLocation();
        //关闭notification
        NotificationUtils.cancelNofication(getApplicationContext());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (llWebView.getVisibility() == View.GONE) {
            ImgUploadHelper.onActivityResult(this, requestCode, resultCode, data);
        } else {
            WebViewImgUploadHelper.onWebViewActivityResult(this, mUploadMsgs, mUploadMsg, requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        CountDownTimerUtils.getCountDownTimer().cancel();
        mapView.onDestroy();
        mapView = null;
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
