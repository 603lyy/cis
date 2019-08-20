package com.yaheen.cis.activity.base;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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
import com.yaheen.cis.util.HttpUtils;
import com.yaheen.cis.util.ProgersssDialog;
import com.yaheen.cis.util.img.CompressImg;
import com.yaheen.cis.util.toast.ToastUtils;

public class BaseActivity extends Activity implements CompressImg {

    private ProgersssDialog progersssDialog;

    private ToastUtils toastUtils = new ToastUtils();

    //标题栏title内容
    private TextView tvContent;

    public LinearLayout llBack, llRightBtn;

    protected Gson gson = new Gson();

    //水唇镇系统
    protected String baseUrl = "https://shuichun.zl.yafrm.com";
//    protected String baseUrl = "http://crs.t.yaheen.com:168/crs_sc";

    //河口镇系统
//    protected String baseUrl = "http://47.106.72.58:9280";

    //广东系统
//    protected String baseUrl = "http://47.106.72.58:10080";

    //岳阳版
//    protected String baseUrl = HttpUtils.baseUrl;

    //    //水唇镇系统
    protected String houseUrl = "https://shuichun.whn.yafrm.com";

    //河口镇系统
//    protected String houseUrl = "https://lhhk.020szsq.com";

    //广东系统
//    protected String houseUrl = "https://npapp.yaheen.com:8090/demonstration";

    //岳阳版
//    protected String houseUrl = HttpUtils.houseUrl;

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

    protected void setRightBtnVisible(boolean visible) {
        llRightBtn = findViewById(R.id.right_btn);
        if (llRightBtn != null) {
            if (visible) {
                llRightBtn.setVisibility(View.VISIBLE);
            } else {
                llRightBtn.setVisibility(View.GONE);
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

    public void copyToClipboard(String text, String toast) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("text", text);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);

        showToast(toast);
    }

    @Override
    public void compress(Uri uri, String imgPath, boolean isTakePhoto) {

    }
}
