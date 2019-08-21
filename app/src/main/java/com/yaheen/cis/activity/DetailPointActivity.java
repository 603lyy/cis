package com.yaheen.cis.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.gson.JsonObject;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.PermissionActivity;
import com.yaheen.cis.adapter.CommonImgAdapter;
import com.yaheen.cis.adapter.EventImgAdapter;
import com.yaheen.cis.adapter.ImgUploadAdapter;
import com.yaheen.cis.adapter.PatrolTypeAdapter;
import com.yaheen.cis.adapter.ProblemAdapter;
import com.yaheen.cis.adapter.UrgencyAdapter;
import com.yaheen.cis.entity.EventDetailBean;
import com.yaheen.cis.entity.HouseBean;
import com.yaheen.cis.entity.ImgUploadBean;
import com.yaheen.cis.entity.QuestionBean;
import com.yaheen.cis.entity.ReportBean;
import com.yaheen.cis.entity.TypeBean;
import com.yaheen.cis.util.HttpUtils;
import com.yaheen.cis.util.img.ImgUploadHelper;
import com.yaheen.cis.util.img.PhotoPagerUtils;
import com.yaheen.cis.util.img.UpLoadImgListener;
import com.yaheen.cis.util.img.UriUtil;
import com.yaheen.cis.util.nfc.Base64Utils;
import com.yaheen.cis.util.notification.NotificationUtils;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;
import com.yaheen.cis.util.time.CountDownTimerUtils;
import com.yaheen.cis.util.map.BDMapUtils;
import com.yaheen.cis.util.map.MapViewLocationListener;
import com.yaheen.cis.util.upload.UploadLocationUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DetailPointActivity extends PermissionActivity {

    private TextView tvLocation, tvCommit;

    private TextView tvHOwner, tvHNumber, tvHAreaType, tvHInspectionPoint, tvHAdderss;

    private TextView tvPUser, tvPPosition, tvPCommitment, tvPPhone, tvPTime;

    private TextView tvMUser, tvMType, tvMName, tvMTime, tvMOwner;

    private LinearLayout llBack, llHouse, llParty, llMerchant;

    private EditText etDescribe;

    private ImageView ivDelete;

    private MapView mapView = null;

    private BaiduMap mBaiduMap;

    private RecyclerView rvProblem, rvUrgency, rvImg, rvPatrol, rvHouseImg;

    private RefreshLayout refreshLayout;

    private ProblemAdapter problemAdapter;

    private PatrolTypeAdapter typeAdapter;

    private UrgencyAdapter urgencyAdapter;

    private ImgUploadAdapter uploadAdapter;

    private String questionUrl = baseUrl + "/eapi/findQuestionaireByTypeId.do";

    private String uploadImgUrl = baseUrl + "/eapi/uploadPhoto.do";

    private String reportUrl = baseUrl + "/eapi/report.do";

    //水唇镇系统
//    private String mHhouseUrl =  houseUrl + "/merchants/getAllMechats.do";

    //河口镇系统
//    private String mHhouseUrl =  houseUrl + "/merchants/getAllMechats.do";

    //全国
//    private String mHhouseUrl = houseUrl + "/merchants/getAllMechats.do";

    //岳阳
    private String mHhouseUrl = houseUrl + "/separationSub/getRangeHouseNumberFromApplets.do";

    private String typeStr, houseId, recordId;

    //已上传图片的ID的拼接
    private String imgIdStr = "";

    //判断地图是否是第一次定位
    private boolean isFirstLoc = true;

    //被选择的图片路径列表
    private List<Uri> selectUriList = new ArrayList<>();

    //图片上传列表的图片路径列表
    private List<String> adapterPathList = new ArrayList<>();

    //图片上传列表的图片路径列表
    private List<Uri> imgUriList = new ArrayList<>();

    //已上传图片的ID列表
    private List<String> uploadIdList = new ArrayList<>();

    //问题类型实体
    private TypeBean typeData;

    private CommonImgAdapter imgAdapter;

    //图片链接列表
    private List<String> imgUrlList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_point);
        showLoadingDialog();

        recordId = getIntent().getStringExtra("recordId");
        houseId = getIntent().getStringExtra("houseId");
        typeStr = getIntent().getStringExtra("type");
        typeData = gson.fromJson(typeStr, TypeBean.class);

        //开始定位
        BDMapUtils.startLocation();

        initView();
        initPatrol();
        initQuestion();
        initUrgency();
        initMapView();
        initImgUpload();
        initHouseData();
        initHouseImgView();

        getHouseData(houseId);
        getQuestionMsg(typeData.getTypeArr().get(0).getId());

        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendReport();
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
        llBack = findViewById(R.id.back);
        tvCommit = findViewById(R.id.tv_commit);
        etDescribe = findViewById(R.id.et_describe);
        refreshLayout = findViewById(R.id.refresh_layout);
        tvLocation = findViewById(R.id.tv_location_describe);

        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BDMapUtils.getLocation() != null) {
                    tvLocation.setText(BDMapUtils.getLocation().getAddrStr());
                }
            }
        });

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
                    new PhotoPagerUtils.Builder(DetailPointActivity.this)
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
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, null));
        BDMapUtils.setMapViewListener(new LocationListener());
    }

    private void initHouseImgView() {
        rvHouseImg = findViewById(R.id.rv_house_img);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvHouseImg.setLayoutManager(layoutManager);

        imgAdapter = new CommonImgAdapter(this);
        rvHouseImg.setAdapter(imgAdapter);

        imgAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view instanceof ImageView) {
                    if (((ImageView) view).getDrawable() == null)
                        return;
                    ArrayList<String> urls = new ArrayList<String>();
                    urls.add(imgUrlList.get(position));
                    new PhotoPagerUtils.Builder(DetailPointActivity.this)
                            .setBigImageUrls(urls)
                            .setBigBitmap(((ImageView) view).getDrawable())
                            .setSavaImage(true)
                            .build();
                }
            }
        });
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
                            DetailPointActivity.this, imgListener, 9 - uploadIdList.size());
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
                String name = jsonBean.getMerchants().getNameList();
                if (!TextUtils.isEmpty(url)) {
                    while (url.indexOf(",") > 0) {
//                        imgUrlList.add("http://lyl.t.yaheen.com:168/whnsubyueyang/webFile/visit.do?id=" + url.substring(0,url.indexOf(",") + 1) + "&showName=" + name.substring(1, name.indexOf(",") + 1));
                        url = url.substring(url.indexOf(",") + 1);
                        name = name.substring(name.indexOf(",") + 1);
                    }
//                    imgUrlList.add("http://lyl.t.yaheen.com:168/whnsubyueyang/webFile/visit.do?id=" + url + "&showName=" + name);
//                    imgUrlList.add(houseUrl + "/webFile/visit.do?id=" + url);

                    imgAdapter.setDatas(imgUrlList);
                }
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

    private void getQuestionMsg(String typeId) {
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
                    startActivity(new Intent(DetailPointActivity.this, LoginActivity.class));
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
//            imgPath = UriUtil.getPath(DetailPointActivity.this, uri);
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
                                startActivity(new Intent(DetailPointActivity.this, LoginActivity.class));
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
                            ImgUploadHelper.compressImage(DetailPointActivity.this,
                                    UriUtil.getPath(DetailPointActivity.this,
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

//        if (!TextUtils.isEmpty(houseId)) {
//            jsonObject.addProperty("scopeOfOperation", tvPArea.getText().toString());
//            jsonObject.addProperty("householdName", tvPUsername.getText().toString());
//            jsonObject.addProperty("inspectionSite", tvPAddress.getText().toString());
//            jsonObject.addProperty("householdPhone", tvPPhone.getText().toString());
//            jsonObject.addProperty("fireOfficer", tvPLeader.getText().toString());
//            jsonObject.addProperty("businessHours", tvPTime.getText().toString());
//            jsonObject.addProperty("responsiblePerson", tvOwner.getText().toString());
//        }
        RequestParams requestParams = new RequestParams(reportUrl);
        requestParams.addQueryStringParameter("token", DefaultPrefsUtil.getToken());
        requestParams.addQueryStringParameter("role", DefaultPrefsUtil.getRole());
        requestParams.addQueryStringParameter("data", gson.toJson(jsonObject));
        requestParams.addQueryStringParameter("recordId", recordId);
        if (!TextUtils.isEmpty(houseId)) {
            requestParams.addParameter("point", true);
        }


        HttpUtils.getPostHttp(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ReportBean data = gson.fromJson(result, ReportBean.class);
                if (data != null && data.isResult()) {
                    showToast(R.string.detail_commit_success);
                    finish();
                } else if (data != null && data.getCode() == 1002) {
                    startActivity(new Intent(DetailPointActivity.this, LoginActivity.class));
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
            ImgUploadHelper.compressImage(DetailPointActivity.this,
                    UriUtil.getPath(DetailPointActivity.this, list.get(0)), isTakePhoto);
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
        ImgUploadHelper.onActivityResult(this, requestCode, resultCode, data);
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
