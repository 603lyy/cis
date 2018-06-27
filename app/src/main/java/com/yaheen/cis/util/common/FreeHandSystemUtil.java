
package com.yaheen.cis.util.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.yaheen.cis.BaseApp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @desc 本工具类用于操作系统属性相关方法
 */
public class FreeHandSystemUtil {

    /**
     * 判断当前机器是否平板
     *
     * @param context
     * @return
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 判断当前代码是否在UI线程运行
     *
     * @return
     */
    public static boolean isRunInUiThread() {
        Thread currThread = Thread.currentThread();
        if (currThread == BaseApp.getUIThread()) {
            return true;
        }
        return false;
    }

    /**
     * 获取用户识别码(IMSI)
     *
     * @param context
     * @return
     */
    public static String getIMSI(Context context) {
        String imsi = "";
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            imsi = tm.getSubscriberId();
        } catch (Exception e) {
        }
        return TextUtils.isEmpty(imsi) ? "" : imsi;
    }

    /**
     * 1 获取手机串号(IMEI),仅仅只对Android手机有效，平板一般为空
     * 采用此种方法，需要在AndroidManifest.xml中加入一个许可：android
     * .permission.READ_PHONE_STATE，并且用户应当允许安装此应用。作为手机来讲，IMEI是唯一的，它应该类似于
     * 359881030314356（除非你有一个没有量产的手机（水货）它可能有无效的IMEI，如：0000000000000）。
     * 
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        String imei = "";
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            imei = tm.getDeviceId();
        } catch (Exception e) {
        }

        return TextUtils.isEmpty(imei) ? "" : imei;
    }

    /**
     * 2获取设备Pseudo-Unique ID, 这个在任何Android手机中都有效
     * 有一些特殊的情况，一些如平板电脑的设置没有通话功能，或者你不愿加入READ_PHONE_STATE许可。而你仍然想获得唯一序列号之类的东西。
     * 这时你可以通过取出ROM版本
     * 、制造商、CPU型号、以及其他硬件信息来实现这一点。这样计算出来的ID不是唯一的（因为如果两个手机应用了同样的硬件以及Rom
     * 镜像）。但应当明白的是，出现类似情况的可能性基本可以忽略。要实现这一点，你可以使用Build类
     * 
     * @return
     */
    public static String getPseudoUniqueID() {
        String m_szDevIDShort = "35"
                + // we make this look like a valid IMEI
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 + Build.CPU_ABI.length() % 10
                + Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 + Build.HOST.length()
                % 10 + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10
                + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 + Build.TAGS.length()
                % 10 + Build.TYPE.length() % 10 + Build.USER.length() % 10;
        return m_szDevIDShort;
    }

    /**
     * 3获取Wlanmac地址,Wlan不必打开，就可读取些值。
     * 
     * @param context
     * @return
     */
    public static String getWlanMacAddress(Context context) {
        String macAddress = "";
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        if (info != null) {
            macAddress = info.getMacAddress();
        }
        return TextUtils.isEmpty(macAddress) ? "" : macAddress;
    }

    /**
     * 4获取蓝牙mac地址,蓝牙没有必要打开，也能读取
     * 
     * @param context
     * @return
     */
    public static String getBTMacAddress() {
        BluetoothAdapter bluetoothAdapter = null;
        String bt_mac = "";
        try {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter != null) {
                bt_mac = bluetoothAdapter.getAddress();
            }
        } catch (Exception e) {
        }
        return TextUtils.isEmpty(bt_mac) ? "" : bt_mac;
    }

    /**
     * 5The Android ID ,
     * 通常被认为不可信，因为它有时为null。开发文档中说明了：这个ID会改变如果进行了出厂设置。并且，如果某个Andorid手机被Root过的话
     * ，这个ID也可以被任意改变
     * 
     * @param context
     * @return
     */
    public static String getAndroidID(Context context) {
        String androidId = "";
        androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        return TextUtils.isEmpty(androidId) ? "" : androidId;
    }

    /**
     * 获取一个比较安全可靠的唯一ID，利用上述5种ID生成 Combined Device ID
     * 综上所述，我们一共有五种方式取得设备的唯一标识。它们中的一些可能会返回null，或者由于硬件缺失、权限问题等获取失败。
     * 但你总能获得至少一个能用。所以，最好的方法就是通过拼接，或者拼接后的计算出的MD5值来产生一个结果。
     * 
     * @return
     */
    public static String getSafeUUID(Context context) {
        String m_szLongID = getIMEI(context) + getPseudoUniqueID() + getAndroidID(context)
                + getWlanMacAddress(context) + getBTMacAddress();
        // compute md5
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
        // get md5 bytes
        byte p_md5Data[] = m.digest();
        // create a hex string
        String m_szUniqueID = new String();
        for (int i = 0; i < p_md5Data.length; i++) {
            int b = (0xFF & p_md5Data[i]);
            // if it is a single digit, make sure it have 0 in front (proper
            // padding)
            if (b <= 0xF)
                m_szUniqueID += "0";
            // add number to string
            m_szUniqueID += Integer.toHexString(b);
        } // hex string to uppercase
        m_szUniqueID = m_szUniqueID.toUpperCase();
        return m_szUniqueID;
    }

    /**
     * 获取手机型号
     * 
     * @return
     */
    public static String getPhoneModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机厂商
     * 
     * @return
     */
    public static String getPhoneManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取OS信息
     * 
     * @return
     */
    public static String getOSInfo() {
        StringBuilder osInfo = new StringBuilder();
        osInfo.append("Build.ID=").append(Build.ID).append('\r').append('\n')
                .append("Build.DISPLAY=").append(Build.DISPLAY).append('\r').append('\n')
                .append("Build.BOARD=").append(Build.BOARD).append('\r').append('\n')
                .append("Build.BRAND=").append(Build.BRAND).append('\r').append('\n')
                .append("Build.CPU_ABI=").append(Build.CPU_ABI).append('\r').append('\n')
                .append("Build.DEVICE=").append(Build.DEVICE).append('\r').append('\n')
                .append("Build.FINGERPRINT=").append(Build.FINGERPRINT).append('\r').append('\n')
                .append("Build.HOST=").append(Build.HOST).append('\r').append('\n')
                .append("Build.MANUFACTURER=").append(Build.MANUFACTURER).append('\r').append('\n')
                .append("Build.MODEL=").append(Build.MODEL).append('\r').append('\n')
                .append("Build.PRODUCT=").append(Build.PRODUCT).append('\r').append('\n')
                .append("Build.TAGS=").append(Build.TAGS).append('\r').append('\n')
                .append("Build.TIME=").append(Build.TIME).append('\r').append('\n')
                .append("Build.TYPE=").append(Build.TYPE).append('\r').append('\n')
                .append("Build.USER=").append(Build.USER).append('\r').append('\n').append('\r')
                .append('\n');
        return osInfo.toString();
    }

    /**
     * 获取系统版本，如"1.0" or "3.4b5".
     * 
     * @return　SDK版本号
     */
    public static String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取SDK版本号数字
     * 
     * @return
     */
    public static int getSDKInt() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取SDK版本号字串（官方已废弃）
     * 
     * @return
     */
    @Deprecated
    public static String getSDK() {
        return Build.VERSION.SDK;
    }

    /**
     * 获取本机手机号码
     * 
     * @param context
     * @return
     */
    public static String getPhoneNumber(Context context) {
        // 创建电话管理
        TelephonyManager tm = (TelephonyManager)
        // 与手机建立连接
        context.getSystemService(Context.TELEPHONY_SERVICE);
        // 获取手机号码
        return tm.getLine1Number();
    }

    /**
     * 获取状态栏高度
     * 
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject)
                        .toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    /**
     * 判断本应用是否存活
     * 如果需要判断本应用是否在后台还是前台用getRunningTask
     * */
    public static boolean isAppAlive(Context mContext,String packageName){
        boolean isAPPRunning = false;
        // 获取activity管理对象
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        // 获取所有正在运行的app
        List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = activityManager.getRunningAppProcesses();
        // 遍历，进程名即包名
        for(ActivityManager.RunningAppProcessInfo appInfo : appProcessInfoList){
            if(packageName.equals(appInfo.processName)){
                isAPPRunning = true;
                break;
            }
        }
        return isAPPRunning;
    }

}
