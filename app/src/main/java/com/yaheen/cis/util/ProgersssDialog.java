package com.yaheen.cis.util;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yaheen.cis.R;

public class ProgersssDialog extends Dialog {

    private Context context;
    private ImageView img;
    private ProgressBar progressBar;
    private TextView txt;

    public ProgersssDialog(Context context) {
        super(context, R.style.progress_dialog);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.progress_dialog, null);
//        img=(ImageView) view.findViewById(R.id.progress_dialog_img);
        progressBar = view.findViewById(R.id.progress_bar);
        txt = view.findViewById(R.id.progress_dialog_txt);
//        txt.setText("加载中...");
        setContentView(view);
        show();
    }


    public void diss() {
        dismiss();
    }

    public void setMsg(String msg) {
        txt.setText(msg);
    }

    public void setMsg(int msgId) {
        txt.setText(msgId);
    }

}