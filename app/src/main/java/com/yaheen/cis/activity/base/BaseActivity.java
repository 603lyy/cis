package com.yaheen.cis.activity.base;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yaheen.cis.R;
import com.yaheen.cis.util.ProgersssDialog;
import com.yaheen.cis.util.img.CompressImg;
import com.yaheen.cis.util.toast.ToastUtils;

public class BaseActivity extends AppCompatActivity implements CompressImg {

    private ProgersssDialog progersssDialog;

    private ToastUtils toastUtils = new ToastUtils();

    //标题栏title内容
    private TextView tvContent;

    private LinearLayout llBack;

    protected Gson gson = new Gson();

    protected String baseUrl = "http://myj.tunnel.echomod.cn/crs";

//    protected String baseUrl = "http://192.168.199.113:8080/crs";

    protected String webSocketUrl = "ws://192.168.199.113:8080/crs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBar();
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
        progersssDialog = new ProgersssDialog(BaseActivity.this);
    }

    public void cancelLoadingDialog() {
        if (progersssDialog != null) {
            progersssDialog.dismiss();
        }
    }

    public void showToast(int string) {
        toastUtils.showToast(string, this);
    }

    public void showToast(String string) {
        toastUtils.showToast(string, this);
    }

    @Override
    public void compress(Uri uri, String imgPath, boolean isTakePhoto) {

    }
}
