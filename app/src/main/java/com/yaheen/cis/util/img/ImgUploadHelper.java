
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
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ImgUploadHelper {

    private static final String Tag = "ImgUploadHelper";

    private static final int IMAGE_REQUEST_CODE = 0;

    private static final int CAMERA_REQUEST_CODE = 1;

    private static final int RESULT_REQUEST_CODE = 2;

    //相册多选URI列表
    private static List<Uri> mSelected;

    private static UpLoadImgListener imgListener;

    /**
     * 显示用户头像上传对话框
     */
    public static void showUserAvatarUploadDialog(final BaseActivity activity,UpLoadImgListener upLoadImgListener) {
        imgListener = upLoadImgListener;

        View view = activity.getLayoutInflater().inflate(R.layout.dialog_img_upload, null);
        final Dialog dialog = new AlertDialog.Builder(activity).setView(view).show();

        View tv_photo = dialog.findViewById(R.id.tv_photo);
        View tv_camera = dialog.findViewById(R.id.tv_camera);
        tv_photo.setOnClickListener(new OnRepeatClickListener() {
            @Override
            public void onRepeatClick(View v) {
//                try {
//                    Intent intentFromGallery = new Intent();
//                    intentFromGallery.addCategory(Intent.CATEGORY_OPENABLE);
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//                        intentFromGallery.setAction(Intent.ACTION_OPEN_DOCUMENT);
//                    } else {
//                        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
//                    }
//                    intentFromGallery.setType("image/*"); // 设置文件类型
//                    activity.startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                dialog.dismiss();

                Matisse.from(activity)
                        .choose(MimeType.allOf()) // 选择 mime 的类型
                        .countable(true)
                        .maxSelectable(9) // 图片选择的最多数量
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
                Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
//                        Uri.fromFile(new File(UserInfoUtil.getUserAvatarPathTemp())));
                intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
//                        Uri.fromFile(new File(Environment.getExternalStorageDirectory().toString()+"/Yiniu/avatar_temp.jpg")));
                        FileProvider.getUriForFile(activity, "com.yaheen.cis.fileprovider",
                                new File(Environment.getExternalStorageDirectory().toString() +
                                        "/Yiniu/.UserInfo/avatar_temp.jpg"))
                );
                activity.startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
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
                    mSelected = Matisse.obtainResult(data);
                    imgListener.upLoad(mSelected);
                }

                break;
            case CAMERA_REQUEST_CODE:
//                cropImage(activity,
////                        Uri.fromFile(new File(Environment.getExternalStorageDirectory().toString() + "/Yiniu/avatar_temp.jpg"))
//                        FileProvider.getUriForFile(activity, "com.yaheen.cis.fileprovider",
//                                new File(Environment.getExternalStorageDirectory().toString() +
//                                        "/Yiniu/.UserInfo/avatar_temp.jpg"))
//                );
                break;
            case RESULT_REQUEST_CODE:
                if (data != null)
                    doAfterImageSelected(activity, data);
                break;
        }

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