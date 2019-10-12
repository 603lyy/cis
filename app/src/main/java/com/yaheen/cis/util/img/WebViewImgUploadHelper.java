package com.yaheen.cis.util.img;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;

import com.yaheen.cis.BuildConfig;
import com.yaheen.cis.R;
import com.yaheen.cis.activity.base.BaseActivity;
import com.yaheen.cis.util.listener.OnRepeatClickListener;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static android.app.Activity.RESULT_OK;

public class WebViewImgUploadHelper {

    private static final String Tag = "ImgUploadHelper";

    private static final int IMAGE_REQUEST_CODE = 0;

    private static final int CAMERA_REQUEST_CODE = 1;

    private static final int RESULT_REQUEST_CODE = 2;

    //旧版本打开图片选择
    private static final int REQUEST_SELECT_FILE_CODE = 3;

    private static String basePath = Environment.getExternalStorageDirectory().toString();

    private static String patrolTempImgPath = "";

    public static String getPhotoPath() {
        return patrolTempImgPath;
    }

    /**
     * 显示图片上传对话框
     */
    public static void showImgUploadDialog(final BaseActivity activity, UpLoadImgListener upLoadImgListener,
                                           final int length, final ValueCallback<Uri[]> mUploadMsgs, final ValueCallback<Uri> mUploadMsg) {
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_img_upload, null);
        final Dialog dialog = new AlertDialog.Builder(activity).setView(view).show();
        View tv_photo = dialog.findViewById(R.id.tv_photo);
        View tv_camera = dialog.findViewById(R.id.tv_camera);

        tv_photo.setOnClickListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                Matisse.from(activity)
                        .choose(MimeType.allOf()) // 选择 mime 的类型
                        .countable(true)
                        .maxSelectable(length) // 图片选择的最多数量
                        .gridExpectedSize(activity.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f) // 缩略图的比例
                        .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                        .forResult(IMAGE_REQUEST_CODE); // 设置作为标记的请求码
                dialog.dismiss();
            }
        });

        tv_camera.setOnClickListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                patrolTempImgPath = ImgPathUtil.getBigBitmapCachePath() + System.currentTimeMillis() + ".jpg";
                Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intentFromCapture.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
                        getUriForFileProvider(activity, patrolTempImgPath));
                activity.startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
                dialog.dismiss();
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if (mUploadMsgs != null) {
                    mUploadMsgs.onReceiveValue(null);
                }

                if (mUploadMsg != null) {
                    mUploadMsg.onReceiveValue(null);
                }

            }
        });
    }

    /**
     * 供外部调用webview的onActivityResult方法
     */
    public static void onWebViewActivityResult(BaseActivity activity, ValueCallback<Uri[]> mUploadMsgs, ValueCallback<Uri> mUploadMsg, int requestCode, int resultCode,
                                               Intent data) {

        if (Activity.RESULT_CANCELED == resultCode) {
            if (mUploadMsgs != null) {
                mUploadMsgs.onReceiveValue(null);
            }

            if (mUploadMsg != null) {
                mUploadMsg.onReceiveValue(null);
            }
            return;
        }
        switch (requestCode) {
            case IMAGE_REQUEST_CODE:
                if (mUploadMsgs != null) {
                    mUploadMsgs.onReceiveValue(new Uri[]{Matisse.obtainResult(data).get(0)});
                }

                if (mUploadMsg != null) {
                    mUploadMsg.onReceiveValue(Matisse.obtainResult(data).get(0));
                }
                break;
            case CAMERA_REQUEST_CODE:
                compressWebViewImage(activity, patrolTempImgPath, mUploadMsgs, mUploadMsg);
                break;
        }

    }

    public static Uri getUriForFileProvider(BaseActivity activity, String path) {
        File outputImage = new File(path);
        return FileProvider.getUriForFile(activity.getBaseContext(), BuildConfig.APPLICATION_ID + ".provider", outputImage);
    }

    /**
     * webview调用压缩图片
     */
    public static void compressWebViewImage(final BaseActivity activity, String imgPath, final ValueCallback<Uri[]> mUploadMsgs, final ValueCallback<Uri> mUploadMsg) {
        Luban.with(activity)
                .load(imgPath)
                .ignoreBy(100)
                .setTargetDir(ImgPathUtil.getBigBitmapCachePath())
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        if (mUploadMsgs != null) {
                            mUploadMsgs.onReceiveValue(new Uri[]{getUriForFileProvider(activity, file.getAbsolutePath())});
                        }

                        if (mUploadMsg != null) {
                            mUploadMsg.onReceiveValue(getUriForFileProvider(activity, file.getAbsolutePath()));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }).launch();
    }
}
