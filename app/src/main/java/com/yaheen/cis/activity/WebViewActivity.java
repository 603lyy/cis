package com.yaheen.cis.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.BaseActivity;
import com.yaheen.cis.activity.base.FetchActivity;
import com.yaheen.cis.util.img.WebViewImgUploadHelper;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;
import com.yaheen.cis.widget.webview.WebJavaScriptProvider;

import java.util.ArrayList;
import java.util.List;

public class WebViewActivity extends FetchActivity {

    private WebView mWebView;

    //被选择的图片路径列表
    private List<Uri> selectUriList = new ArrayList<>();

    //已上传图片的ID列表
    private List<String> uploadIdList = new ArrayList<>();

    private ValueCallback<Uri[]> mUploadMsgs;

    private ValueCallback<Uri> mUploadMsg;

    //第一次进入该页面
    private boolean isFirstIn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        initView();
        initWebViewSetting();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isFirstIn) {
            isFirstIn = false;
            showLoadingDialog();

            mWebView.loadUrl("http://temporary.zl.yafrm.com/contact/user/contact.html?" + "whnUrl=" + getHouseUrl()
                    + "&userName=" + DefaultPrefsUtil.getCurrentUserName() + "&userId=" + DefaultPrefsUtil.getUserId()
                    + "&houseNumberId=" + "&role=" + DefaultPrefsUtil.getRole());
        }
    }

    private void initView() {
        llBack = findViewById(R.id.back);
        mWebView = findViewById(R.id.web_view);

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

            WebViewImgUploadHelper.showImgUploadDialog(WebViewActivity.this, null,
                    9 - uploadIdList.size(), mUploadMsgs, mUploadMsg);

            return true;
        }

        // For Android 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUploadMsg = null;
            mUploadMsgs = null;
            mUploadMsg = uploadMsg;

            WebViewImgUploadHelper.showImgUploadDialog(WebViewActivity.this, null,
                    9 - uploadIdList.size(), mUploadMsgs, mUploadMsg);
        }

        // For Android > 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType, String capture) {
            mUploadMsg = null;
            mUploadMsgs = null;
            mUploadMsg = uploadMsg;

            WebViewImgUploadHelper.showImgUploadDialog(WebViewActivity.this, null,
                    9 - uploadIdList.size(), mUploadMsgs, mUploadMsg);
        }

        // Andorid 3.0 +
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            mUploadMsg = null;
            mUploadMsgs = null;
            mUploadMsg = uploadMsg;

            WebViewImgUploadHelper.showImgUploadDialog(WebViewActivity.this, null,
                    9 - uploadIdList.size(), mUploadMsgs, mUploadMsg);
        }
    };

    private WebViewClient webViewClient = new WebViewClient() {

//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//            return super.shouldOverrideUrlLoading(view, request);
//        }

    };

}
