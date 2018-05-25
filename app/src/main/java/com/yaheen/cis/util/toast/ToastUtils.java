package com.yaheen.cis.util.toast;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtils {

    private Toast toast;

    public void showToast(int sting, Context context){
        toast =  Toast.makeText(context, sting, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    public void showToast(String sting, Context context){
        toast =  Toast.makeText(context, sting, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

}
