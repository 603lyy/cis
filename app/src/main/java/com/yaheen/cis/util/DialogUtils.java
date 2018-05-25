package com.yaheen.cis.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.yaheen.cis.util.dialog.DialogCallback;
import com.yaheen.cis.util.dialog.IDialogCancelCallback;

/**
 * Created by linjingsheng on 17/5/1.
 */

public class DialogUtils {

    private static AlertDialog dialog;

    public static void showNormalDialog(Context context, String msg, final DialogCallback callback,
                                        final IDialogCancelCallback cancelCallback, String PositiveButton,
                                        String NegativeButton, String title) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setTitle(title);
        normalDialog.setMessage(msg);
        normalDialog.setPositiveButton(PositiveButton,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        if (callback != null) {
                            callback.callback();
                        }
                    }
                });
        normalDialog.setNegativeButton(NegativeButton,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        if (cancelCallback != null) {
                            cancelCallback.cancelCallback();
                        }
                    }
                });
        // 显示
        normalDialog.show();
    }

    public static void showDialog(Context context, String msg, final DialogCallback callback, final IDialogCancelCallback cancelCallback) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(context);
        normalDialog.setTitle("提醒");
        normalDialog.setMessage(msg);
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        if (callback != null) {
                            callback.callback();
                        }
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        if (cancelCallback != null) {
                            cancelCallback.cancelCallback();
                        }
                    }
                });
        // 显示
        dialog = normalDialog.show();

    }

    public static void closeShowDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
