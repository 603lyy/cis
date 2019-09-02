
package com.yaheen.cis.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.FetchActivity;
import com.yaheen.cis.activity.base.PermissionActivity;
import com.yaheen.cis.adapter.ImgUploadAdapter;
import com.yaheen.cis.entity.CommonBean;
import com.yaheen.cis.entity.ImgUploadBean;
import com.yaheen.cis.util.HttpUtils;
import com.yaheen.cis.util.img.ImgUploadHelper;
import com.yaheen.cis.util.img.PhotoPagerUtils;
import com.yaheen.cis.util.img.UpLoadImgListener;
import com.yaheen.cis.util.img.UriUtil;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;
import com.yaheen.cis.util.time.TimeTransferUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HandleActivity extends FetchActivity {

    private String commitUrl = "";

    private String uploadImgUrl = "";

    private EditText etDes, etUser, etTime;

    private TextView tvCommit;

    private ImageView ivDelete;

    private LinearLayout llBack;

    private RecyclerView rvImg;

    private ImgUploadAdapter uploadAdapter;

    //被选择的图片路径列表
    private List<Uri> selectUriList = new ArrayList<>();

    //图片上传列表的图片路径列表
    private List<String> adapterPathList = new ArrayList<>();

    //图片上传列表的图片路径列表
    private List<Uri> imgUriList = new ArrayList<>();

    //已上传图片的ID列表
    private List<String> uploadIdList = new ArrayList<>();

    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle);

        eventId = getIntent().getStringExtra("eventId");

        initData();
        initImgUpload();

        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commitEvent();
            }
        });

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initData() {
        commitUrl = getBaseUrl() + "/eapi/handleEvent.do";
        uploadImgUrl = getBaseUrl() + "/eapi/uploadDealFile.do";

        llBack = findViewById(R.id.back);
        etTime = findViewById(R.id.et_time);
        etDes = findViewById(R.id.et_describe);
        etUser = findViewById(R.id.et_username);
        tvCommit = findViewById(R.id.tv_commit);

        etUser.setText(DefaultPrefsUtil.getCurrentUserName());
        etTime.setText(TimeTransferUtils.getYMDHMSStrTime2(System.currentTimeMillis()+""));
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
                    new PhotoPagerUtils.Builder(HandleActivity.this)
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
                if (uploadIdList.size() < 4) {
                    ImgUploadHelper.showUserAvatarUploadDialog(
                            HandleActivity.this, imgListener, 4 - uploadIdList.size());
                } else {
                    showToast(R.string.handle_img_limit);
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

    private void upLoadImg(final Uri uri, final String imgPath, final boolean isTakePhoto) {

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
                                startActivity(new Intent(HandleActivity.this, LoginActivity.class));
                                finish();
                            }

                            if (data.isResult()) {
                                imgUriList.add(uri);
                                uploadIdList.add(data.getFileId());
                                adapterPathList.add(imgPath);
                                uploadAdapter.setDatas(adapterPathList);
                                uploadAdapter.notifyDataSetChanged();
                            }

                            if (uploadIdList != null && uploadIdList.size() > 0) {
                                ivDelete.setVisibility(View.VISIBLE);
                            } else {
                                ivDelete.setVisibility(View.GONE);
                            }
                        }

                        //图片路径不为空，继续上传剩余图片
                        if (selectUriList.size() > 0) {
                            ImgUploadHelper.compressImage(HandleActivity.this,
                                    UriUtil.getPath(HandleActivity.this,
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

    private UpLoadImgListener imgListener = new UpLoadImgListener() {
        @Override
        public void upLoad(List<Uri> list, boolean isTakePhoto) {
            showLoadingDialog();

            if (list.size() <= 0) {
                return;
            }
            selectUriList = list;
            ImgUploadHelper.compressImage(HandleActivity.this,
                    UriUtil.getPath(HandleActivity.this, list.get(0)), isTakePhoto);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImgUploadHelper.onActivityResult(this, requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void compress(Uri uri, String imgPath, boolean isTakePhoto) {
        showLoadingDialog();
        upLoadImg(uri, imgPath, isTakePhoto);
    }

    /**
     * 提交处理
     */
    private void commitEvent() {

        if (TextUtils.isEmpty(etDes.getText().toString())) {
            showToast(R.string.handle_describe_empty);
            return;
        } else if (TextUtils.isEmpty(etTime.getText().toString())) {
            showToast(R.string.handle_time_empty);
            return;
        } else if (TextUtils.isEmpty(etUser.getText().toString())) {
            showToast(R.string.handle_user_empty);
            return;
        }

        String imgIdStr = "";

        for (int i = 0; i < uploadIdList.size(); i++) {
            if (i == 0) {
                imgIdStr = uploadIdList.get(i);
            } else {
                imgIdStr = imgIdStr + "," + uploadIdList.get(i);
            }
        }

        if (TextUtils.isEmpty(imgIdStr)) {
            showToast(R.string.handle_img_url_empty);
            return;
        }

        showLoadingDialog();

        RequestParams requestParams = new RequestParams(commitUrl);
        requestParams.addQueryStringParameter("eventId", eventId);
        requestParams.addQueryStringParameter("fileIds", imgIdStr);
        requestParams.addQueryStringParameter("token", DefaultPrefsUtil.getToken());
        requestParams.addQueryStringParameter("content", etDes.getText().toString());
        requestParams.addQueryStringParameter("dealUser", etUser.getText().toString());
        requestParams.addQueryStringParameter("dealDate", etTime.getText().toString());

        HttpUtils.getPostHttp(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CommonBean data = gson.fromJson(result, CommonBean.class);
                if (data != null && data.isResult()) {
                    startActivity(new Intent(HandleActivity.this, ReportRecordActivity.class));
                    finish();
                } else if (data != null && data.getCode() == 1002) {
                    startActivity(new Intent(HandleActivity.this, LoginActivity.class));
                    finish();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(R.string.handle_commit_fail);
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
}
