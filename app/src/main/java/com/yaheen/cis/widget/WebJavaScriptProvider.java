
package com.yaheen.cis.widget;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.yaheen.cis.activity.base.BaseActivity;

import org.json.JSONObject;

import java.util.ArrayList;

public class WebJavaScriptProvider {

    private BaseActivity activity;

    private Context context;

    // 外部绑定web页面的title
    private String webTitle;

    // 外部绑定web页面的URL
    private String webUrl;

    // 外部绑定webview
    private WebView webview;

    //BI发送记录
    private ArrayList<String> eventStatistic = new ArrayList<String>();

    private Context getContext() {
        return context;
    }

    public WebJavaScriptProvider(Context ctx, BaseActivity activity) {
        this.activity = activity;
        this.context = ctx;

    }

    /**
     * 绑定网页title以及url
     *
     * @param title
     * @param url
     */
    public void bindTitleAndUrl(String title, String url) {
        this.webTitle = title;
        this.webUrl = url;
    }

    public void bindWebView(WebView webview) {
        this.webview = webview;
    }

    @JavascriptInterface
    public void openUrlWithSameWindow(String jsonStr) {
        try {
            if (jsonStr != null) {
                JSONObject json = new JSONObject(jsonStr);
//                String url = UrlUtil.appendDefaultParamsToUrl(json.getString("url"));
                if (this.webview != null) {
//                    this.webview.loadUrl(url);
                    this.webview.clearHistory();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
