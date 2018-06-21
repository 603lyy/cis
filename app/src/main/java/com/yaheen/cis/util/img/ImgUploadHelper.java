
package com.yaheen.cis.util.img;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;

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
import static android.content.Context.CONTEXT_IGNORE_SECURITY;

public class ImgUploadHelper {

    private static final String Tag = "ImgUploadHelper";

    private static final int IMAGE_REQUEST_CODE = 0;

    private static final int CAMERA_REQUEST_CODE = 1;

    private static final int RESULT_REQUEST_CODE = 2;

    private static String basePath = Environment.getExternalStorageDirectory().toString();

    private static String patrolTempImgPath = "";

    //相册多选URI列表
    private static List<Uri> mSelected = new ArrayList<>();

    private static UpLoadImgListener imgListener;

    public static String getPhotoPath() {
        return patrolTempImgPath;
    }

    /**
     * 显示用户头像上传对话框
     */
    public static void showUserAvatarUploadDialog(final BaseActivity activity, UpLoadImgListener upLoadImgListener) {
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_img_upload, null);
        final Dialog dialog = new AlertDialog.Builder(activity).setView(view).show();
        View tv_photo = dialog.findViewById(R.id.tv_photo);
        View tv_camera = dialog.findViewById(R.id.tv_camera);

        imgListener = upLoadImgListener;

        tv_photo.setOnClickListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
                Matisse.from(activity)
                        .choose(MimeType.allOf()) // 选择 mime 的类型
                        .countable(true)
                        .maxSelectable(9) // 图片选择的最多数量
                        .gridExpectedSize(activity.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f) // 缩略图的比例
                        .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                        .forResult(IMAGE_REQUEST_CODE); // 设置作为标记的请求码
                activity.showLoadingDialog();
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
                activity.showLoadingDialog();
                dialog.dismiss();
            }
        });
    }

    /**
     * 供外部调用onActivityResult方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public static void onActivityResult(BaseActivity activity, int requestCode, int resultCode,
                                        Intent data) {

        if (Activity.RESULT_CANCELED == resultCode)
            return;
        switch (requestCode) {
            case IMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    mSelected.clear();
                    mSelected = Matisse.obtainResult(data);
                    imgListener.upLoad(mSelected, false);
                }

                break;
            case CAMERA_REQUEST_CODE:
                compressImage(activity, patrolTempImgPath, true);
                break;
            case RESULT_REQUEST_CODE:
                if (data != null)
                    doAfterImageSelected(activity, data);
                break;
        }

    }

    public static Uri getUriForFileProvider(BaseActivity activity, String path) {
        File outputImage = new File(path);
        return FileProvider.getUriForFile(activity.getBaseContext(), "com.yaheen.cis.provider", outputImage);
    }

    public static void compressImage(final BaseActivity activity, String imgPath, final boolean isTakePhoto) {
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
                        activity.compress(getUriForFileProvider(activity, file.getAbsolutePath()),
                                file.getAbsolutePath(), isTakePhoto);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }).launch();
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public static void cropImage(BaseActivity activity, Uri uri) {
//        String filePath = uri.getPath();
        String filePath = Environment.getExternalStorageDirectory().toString() +
                "/Yiniu/.UserInfo/avatar_temp.jpg";
        if (filePath == null) {
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(FileProvider.getUriForFile(activity, "com.yaheen.cis.fileprovider",
                new File(filePath)), "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高\
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 400);
        intent.putExtra("output",
//                Uri.fromFile(new File(Environment.getExternalStorageDirectory().toString() + "/Yiniu/avatar_temp.jpg"))
                FileProvider.getUriForFile(activity, "com.yaheen.cis.fileprovider",
                        new File(Environment.getExternalStorageDirectory().toString() +
                                "/Yiniu/.UserInfo/avatar_temp.jpg"))
        );
        intent.putExtra("return-data", false);
        activity.startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    /**
     * 保存裁剪之后的图片数据
     */
    private static void doAfterImageSelected(final BaseActivity activity, Intent data) {
//        Bundle extras = data.getExtras();
//        UserInfoObserver.getInstance().updateUserAvatar();
//        UserAvatarUploadProtocol protocol = new UserAvatarUploadProtocol();
//        HashMap<String, String> requestParams = new HashMap<String, String>();
//        requestParams.put("token", UserInfoUtil.getUserToken());
//        requestParams.put("userId", UserInfoUtil.getUserId());
//        protocol.execute(BaseApp.getInstance(), requestParams,
//                new OnSuccessListener<UserIntegralChangeResponse>() {
//
//                    @Override
//                    public void onSuccessResponse(UserIntegralChangeResponse response) {
//                        if (response != null && response.isSuccess() && response.data != null) {
//                            ToastUtil.toast(fragment.getString(R.string.user_personal_success_text));
//                            fragment.sendBroadcast(YiniuAction.Action_Update_MyInfo_Data);
//                        }
//                    }
//                }, null);
    }
}
