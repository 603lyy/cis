package com.yaheen.cis.util.img;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by Awen <Awentljs@gmail.com>
 */
public class ImgPathUtil {

    /**
     * 裁剪头像
     *
     * @return
     */
    public static String getClipPhotoPath() {
        return getPath();
    }

    /**
     * 保存大图到本地的路径地址
     *
     * @return String
     */
    public static String getBigBitmapCachePath() {
        return getPath();
    }

    private static String getPath() {
        String path = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory().getPath() + File.separator + "cis/";
        }
        if (TextUtils.isEmpty(path)) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "cis/";
        }
//        //地址如下:path/appname/
//        String app_root_name = Awen.getContext().getString(R.string.app_root_name);
//        path = path + File.separator + app_root_name + File.separator ;
        exitesFolder(path);
        return path;
    }

    /**
     * 判断文件夹是否存在,不存在则创建
     *
     * @param path
     */
    public static void exitesFolder(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
