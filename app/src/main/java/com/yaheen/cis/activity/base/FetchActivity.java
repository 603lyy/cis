package com.yaheen.cis.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.yaheen.cis.R;
import com.yaheen.cis.activity.DetailPointActivity;
import com.yaheen.cis.activity.LoginActivity;
import com.yaheen.cis.activity.TurnActivity;
import com.yaheen.cis.entity.CheckBean;
import com.yaheen.cis.entity.CommonBean;
import com.yaheen.cis.entity.TypeBean;
import com.yaheen.cis.util.HttpUtils;
import com.yaheen.cis.util.nfc.Base64Utils;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

public class FetchActivity extends PermissionActivity {

    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;

    private String typeUrl = baseUrl + "/eapi/findTypeByUserId.do";

    private String checkUrl = "http://shortlink.cn/eai/getShortLinkCompleteInformation.do";

    //水唇镇
//    private String checkIdUrl =  houseUrl + "/houseNumbers/getGridInspectionPoint.do";

    //河口镇
//    private String checkIdUrl =  houseUrl + "/houseNumbers/getGridInspectionPoint.do";

    //全国
    private String checkIdUrl =  houseUrl + "/houseNumbers/getGridInspectionPoint.do";

    private String houseId;

    protected boolean isFetch = false;

    public void openFetch() {
        showLoadingDialog();
        Intent intent = new Intent(getApplication(), CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    //请求门牌系统，判断是否是巡查点
    private void checkId(CheckBean.EntityBean data) {

        houseId = data.getLink().substring(data.getLink().lastIndexOf("=") + 1);

        if (TextUtils.isEmpty(houseId)) {
            cancelLoadingDialog();
            return;
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("houseNumberId", houseId);

        RequestParams params = new RequestParams(checkIdUrl);
        params.addQueryStringParameter("json", Base64Utils.encode(jsonObject.toString().getBytes()));

        HttpUtils.getPostHttp(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CommonBean data = gson.fromJson(result, CommonBean.class);
                if (data != null) {
                    if (data.isResult()) {
                        getTypeList();
                    } else if (data.getMsg() == null) {
                        houseId = "";
                        getTypeList();
                    } else {
                        showToast(R.string.turn_id_empty);
                        cancelLoadingDialog();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                cancelLoadingDialog();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void check(String slink) {

        if (TextUtils.isEmpty(slink)) {
            Toast.makeText(this, R.string.short_link_empty, Toast.LENGTH_SHORT).show();
            cancelLoadingDialog();
            return;
        }
        slink = slink.substring(slink.lastIndexOf("/") + 1);

        RequestParams params = new RequestParams(checkUrl);
        params.addQueryStringParameter("key", "7zbQUBNY0XkEcUoushaJD7UcKyWkc91q");
        params.addQueryStringParameter("shortLinkCode", slink);

        HttpUtils.getPostHttp(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                CheckBean checkBean = gson.fromJson(result, CheckBean.class);
                if (checkBean != null && checkBean.isResult()) {
                    checkId(checkBean.getEntity());
                } else {
                    Toast.makeText(FetchActivity.this, R.string.scan_not, Toast.LENGTH_SHORT).show();
                    cancelLoadingDialog();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(FetchActivity.this, R.string.scan_fail, Toast.LENGTH_SHORT).show();
                cancelLoadingDialog();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    private void getTypeList() {

        RequestParams requestParams = new RequestParams(typeUrl);
        requestParams.addQueryStringParameter("token", DefaultPrefsUtil.getToken());

        HttpUtils.getPostHttp(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                TypeBean data = gson.fromJson(result, TypeBean.class);
                if (data != null && data.isResult()) {
//                    Intent intent = new Intent(FetchActivity.this, DetailPointActivity.class);
//                    intent.putExtra("type", gson.toJson(data));
//                    intent.putExtra("sign", true);
//                    intent.putExtra("houseId", houseId);
//                    startActivity(intent);
                    getHouseId(result, houseId);
                } else if (data != null && data.getCode() == 1002) {
                    startActivity(new Intent(FetchActivity.this, LoginActivity.class));
                    finish();
                } else {
                    cancelLoadingDialog();
                    showToast(R.string.scan_fail);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(R.string.scan_fail);
                cancelLoadingDialog();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    if (result != null) {
                        showLoadingDialog();
                        isFetch = true;
                        check(result);
                    } else {
                        cancelLoadingDialog();
                        Toast.makeText(this, "解析二维码失败", Toast.LENGTH_LONG).show();
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    cancelLoadingDialog();
                    Toast.makeText(this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            cancelLoadingDialog();
        }
    }

    public void getHouseId(String type, String houseId) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFetch) {
            cancelLoadingDialog();
        }
        isFetch = false;
    }
}
