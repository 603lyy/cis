package com.yaheen.cis.activity.base;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yaheen.cis.R;
import com.yaheen.cis.util.ProgersssDialog;
import com.yaheen.cis.util.img.CompressImg;
import com.yaheen.cis.util.toast.ToastUtils;

public class BaseActivity extends Activity implements CompressImg {

    private ProgersssDialog progersssDialog;

    private ToastUtils toastUtils = new ToastUtils();

    //标题栏title内容
    private TextView tvContent;

    private LinearLayout llBack;

    protected Gson gson = new Gson();

//    protected String baseUrl = "http://lyy.tunnel.echomod.cn/crs";

//    protected String baseUrl = "http://192.168.199.113:8080/crs";

//    protected String baseUrl = "http://tlep2.yaheen.com";

    protected String baseUrl = "http://47.106.72.58:9180";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        initToolBar();
    }

    private void initToolBar() {
        tvContent = findViewById(R.id.tv_title_content);
    }

    protected void setTitleContent(int content) {
        tvContent = findViewById(R.id.tv_title_content);
        if (tvContent != null) {
            tvContent.setText(content);
        }
    }

    protected void setBackVisible(boolean visible) {
        llBack = findViewById(R.id.back);
        if (llBack != null) {
            if (visible) {
                llBack.setVisibility(View.VISIBLE);
            } else {
                llBack.setVisibility(View.GONE);
            }
        }
    }

    public void showLoadingDialog() {
        if (progersssDialog != null && progersssDialog.isShowing()) {
            return;
        }
        progersssDialog = new ProgersssDialog(BaseActivity.this);
    }

    public void cancelLoadingDialog() {
        if (progersssDialog != null) {
            progersssDialog.dismiss();
        }
    }

    public boolean isDialogShowing() {
        if (progersssDialog != null && progersssDialog.isShowing()) {
            return true;
        } else {
            return false;
        }
    }

    public void showToast(int string) {
        toastUtils.showToast(string, this);
    }

    public void showToast(String string) {
        toastUtils.showToast(string, this);
    }

    public void setIp(String ip) {
        if (TextUtils.isEmpty(ip)) {
            return;
        }
        baseUrl = "http://" + ip + "8080/crs";
    }

    @Override
    public void compress(Uri uri, String imgPath, boolean isTakePhoto) {

    }
}
