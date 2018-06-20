package com.yaheen.cis.activity.base;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.yaheen.cis.R;
import com.yaheen.cis.entity.PhotoPagerBean;
import com.yaheen.cis.util.PermissionUtil;
import com.yaheen.cis.util.img.ImgPathUtil;
import com.yaheen.cis.util.img.ImageUtils;
import com.yaheen.cis.util.img.PhotoPagerUtils;
import com.yaheen.cis.util.img.fresco.FrescoImageLoader;
import com.yaheen.cis.widget.scalephotoview.OnViewTapListener;
import com.yaheen.cis.widget.scalephotoview.PhotoDraweeView;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 *
 */
public class PhotoPagerActivity extends PhotoPagerBaseActivity implements View.OnLongClickListener {

    private static final String TAG = PhotoPagerActivity.class.getSimpleName();
    private static final int REQUEST_CODE = 100;
    private static final String STATE_POSITION = "STATE_POSITION";

    protected PhotoPagerBean photoPagerBean;
    private OnViewTapListener onViewTapListener;
    private View.OnClickListener onClickListener;

    private String saveImageLocalPath;
    private boolean saveImage;
    protected int currentPosition;
    //-end

    private PhotoDraweeView scalePhotoView;
    private int screenWith, screenHeight;

    private ImageRequest request;

    protected Bundle getBundle() {
        return getIntent().getBundleExtra(PhotoPagerUtils.EXTRA_USER_BUNDLE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOpenToolBar(false);
        Bundle bundle = getIntent().getBundleExtra(PhotoPagerUtils.EXTRA_PAGER_BUNDLE);
        photoPagerBean = bundle.getParcelable(PhotoPagerUtils.EXTRA_PAGER_BEAN);
        if (photoPagerBean == null) {
            onBackPressed();
            return;
        }
        saveImage = photoPagerBean.isSaveImage();
        saveImageLocalPath = photoPagerBean.getSaveImageLocalPath();

        setContentView(R.layout.activity_photo_detail_pager);

        if (savedInstanceState != null) {
            photoPagerBean.setPagePosition(savedInstanceState.getInt(STATE_POSITION));
        }
        //图片单击的回调
        onViewTapListener = new OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                onSingleClick();
            }
        };

        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSingleClick();
            }
        };

        screenWith = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;

        initView();
    }

    private void initView() {
        scalePhotoView = findViewById(R.id.scalePhotoView);
        scalePhotoView.setOnViewTapListener(onViewTapListener);
        scalePhotoView.setMaxHeight(scalePhotoView.getWidth());
        GenericDraweeHierarchy hierarchy = scalePhotoView.getHierarchy();
        hierarchy.setActualImageFocusPoint(new PointF(0.5f, 0.5f)); // 居中显示
        hierarchy.setPlaceholderImage(R.drawable.ic_loading_3, ScalingUtils.ScaleType.CENTER);
        hierarchy.setFailureImage(getResources().getDrawable(R.drawable.failure_image));
        hierarchy.setRetryImage(getResources().getDrawable(R.drawable.failure_image), ScalingUtils.ScaleType.FIT_CENTER);
        String bigImgUrl = photoPagerBean.getBigImgUrls().get(0);
        final Uri uri = Uri.parse(bigImgUrl);
        request = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(false)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setTapToRetryEnabled(true)//点击重试
                .setImageRequest(request)//原图，大图
                .setControllerListener(new BaseControllerListener<ImageInfo>() {
                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                        super.onFinalImageSet(id, imageInfo, animatable);
                        if (imageInfo == null) {
                            return;
                        }
                        scalePhotoView.update(imageInfo.getWidth(), imageInfo.getHeight());
                    }
                })
                .setOldController(scalePhotoView.getController())
                .build();
        scalePhotoView.setController(controller);
        scalePhotoView.setOnLongClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * 图片单击回调
     */
    protected boolean onSingleClick() {
        onBackPressed();
        return false;
    }

    /**
     * //图片长按回调
     */
    @Override
    public boolean onLongClick(View view) {
        saveImageDialog();
        return false;
    }

    /**
     * 保存图片到图库
     */
    protected void saveImage() {
        //以下操作会回调这两个方法:#startPermissionSDSuccess(), #startPermissionSDFaild()
        PermissionGen.needPermission(PhotoPagerActivity.this, REQUEST_CODE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    //获取读写sd卡权限成功回调
    @PermissionSuccess(requestCode = REQUEST_CODE)
    private void startPermissionSDSuccess() {
        //保存图片到本地
        String bigImgUrl = photoPagerBean.getBigImgUrls().get(0);
        String fileName = bigImgUrl.substring(bigImgUrl.lastIndexOf("/") + 1, bigImgUrl.length());
        fileName = fileName.substring(0, fileName.indexOf("&"));
        if (!fileName.contains(".jpg") && !fileName.contains(".png") && !fileName.contains(".jpeg") && !fileName.contains(".gif") && !fileName.contains(".webp")) {
            //防止有些图片没有后缀名
            fileName = fileName + ".jpg";
        }
        String filePath = (ImgPathUtil.getBigBitmapCachePath()) + "cis/" + fileName;
        if (TextUtils.isEmpty(filePath) && saveImageLocalPath != null) {
            filePath = saveImageLocalPath + "/cis/" + fileName;
        }
        boolean state = saveImage(filePath);
        String tips = state ? getString(R.string.save_image_success) : getString(R.string.saved_fail);
        Toast.makeText(PhotoPagerActivity.this, tips, Toast.LENGTH_SHORT).show();
    }

    @PermissionFail(requestCode = REQUEST_CODE)
    private void startPermissionSDFaild() {
        new android.app.AlertDialog.Builder(this)
                .setMessage(getString(R.string.permission_tip_SD))
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PermissionUtil.startSystemSettingActivity(PhotoPagerActivity.this);
            }
        }).setCancelable(false).show();
    }

    private void saveImageDialog() {
        new AlertDialog.Builder(this)
                .setItems(new String[]{getString(R.string.save_big_image)},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveImage();
                            }
                        }).show();
    }

    /**
     * 保存图片到本地sd卡
     */
    private boolean saveImage(String filePath) {
        boolean state = false;
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        if (request == null) {
            return false;
        }
        try {
            byte[] b = FrescoImageLoader.getByte(this, request.getSourceUri());
            if (b != null) {
                state = ImageUtils.saveImageToGallery(filePath, b, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return state;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onViewTapListener = null;
        onClickListener = null;
        photoPagerBean = null;
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, R.anim.image_pager_exit_animation);
        super.onBackPressed();
    }

}
