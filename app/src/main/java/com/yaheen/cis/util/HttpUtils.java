package com.yaheen.cis.util;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class HttpUtils {

    public static void getPostHttp(RequestParams params, Callback.CommonCallback<String> callback) {

        params.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;");
        params.setConnectTimeout(60 * 1000);
        params.setReadTimeout(60 * 1000);

        x.http().post(params, callback);
    }

    public static void getGetHttp(RequestParams params, Callback.CommonCallback<String> callback) {

        params.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;");
        params.setConnectTimeout(60 * 1000);
        params.setReadTimeout(60 * 1000);

        x.http().get(params, callback);
    }
}
