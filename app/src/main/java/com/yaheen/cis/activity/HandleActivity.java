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
import com.yaheen.cis.activity.base.PermissionActivity;
import com.yaheen.cis.adapter.ImgUploadAdapter;
import com.yaheen.cis.entity.CommonBean;
import com.yaheen.cis.util.HttpUtils;
import com.yaheen.cis.util.img.ImgUploadHelper;
import com.yaheen.cis.util.img.PhotoPagerUtils;
import com.yaheen.cis.util.img.UpLoadImgListener;
import com.yaheen.cis.util.img.UriUtil;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

public class HandleActivity extends PermissionActivity {

    private String commitUrl = baseUrl + "/eapi/handleEvent.do";

    private EditText etDes;

    private TextView tvCommit;

    private ImageView ivDelete;

    private LinearLayout llBack;

    private RecyclerView   rvImg;

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

        initImgUpload();
        llBack = findViewById(R.id.back);
        etDes = findViewById(R.id.et_describe);
        tvCommit = findViewById(R.id.tv_commit);

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
                if (uploadIdList.size() < 9) {
                    ImgUploadHelper.showUserAvatarUploadDialog(
                            HandleActivity.this, imgListener, 9 - uploadIdList.size());
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

    private void commitEvent() {

        if (TextUtils.isEmpty(etDes.getText().toString())) {
            showToast(R.string.handle_describe_empty);
            return;
        }

        showLoadingDialog();

        RequestParams requestParams = new RequestParams(commitUrl);
        requestParams.addQueryStringParameter("token", DefaultPrefsUtil.getToken());
        requestParams.addQueryStringParameter("eventId", eventId);
        requestParams.addQueryStringParameter("content", etDes.getText().toString());

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
