package com.yaheen.cis.util.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yaheen.cis.activity.base.SinglePixelActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ScreenManager {

    private static final String TAG = "lin";
    private Context mContext;
    private static ScreenManager mSreenManager;
    // 使用弱引用，防止内存泄漏
    private WeakReference<Activity> mActivityRef;

    private List<WeakReference<Activity>> weakReferenceList = new ArrayList<>();


    private ScreenManager(Context mContext) {
        this.mContext = mContext;
    }


    // 单例模式
    public static ScreenManager getScreenManagerInstance(Context context) {
        if (mSreenManager == null) {
            mSreenManager = new ScreenManager(context);
        }
        return mSreenManager;
    }


    // 获得SinglePixelActivity的引用
    public void setSingleActivity(Activity mActivity) {
        mActivityRef = new WeakReference<>(mActivity);
    }

    public void setActivities(Activity mActivity){
        weakReferenceList.add(new WeakReference<>(mActivity));
    }


    // 启动SinglePixelActivity
    public void startActivity() {
        Log.i(TAG, "准备启动SinglePixelActivity...");
        Intent intent = new Intent(mContext, SinglePixelActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    // 结束SinglePixelActivity
    public void finishActivity() {
        Log.i(TAG, "准备结束SinglePixelActivity...");
        if (mActivityRef != null) {
            Activity mActivity = mActivityRef.get();
            if (mActivity != null) {
                mActivity.finish();
            }
        }
    }

    public void finishActivities() {
        WeakReference<Activity> mActivityRef;
        for (int i = 0; i < weakReferenceList.size(); i++) {
            mActivityRef = weakReferenceList.get(i);
            if (mActivityRef != null) {
                Activity mActivity = mActivityRef.get();
                if (mActivity != null) {
                    mActivity.finish();
                }
            }
        }
    }
}
