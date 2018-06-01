package com.yaheen.cis.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.yaheen.cis.R;
import com.yaheen.cis.util.ProgersssDialog;
import com.yaheen.cis.util.toast.ToastUtils;

public class BaseActivity extends AppCompatActivity {

    private ProgersssDialog progersssDialog;

    private ToastUtils toastUtils = new ToastUtils();

    //标题栏title内容
    private TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setTitleContent(int content) {
        tvContent = findViewById(R.id.tv_title_content);
        if (tvContent != null) {
            tvContent.setText(content);
        }
    }

    public void showLoadingDialog() {
        progersssDialog = new ProgersssDialog(BaseActivity.this);
    }

    public void cancelLoadingDialog() {
        if (progersssDialog != null) {
            progersssDialog.dismiss();
        }
    }

    public void showToast(int string){
        toastUtils.showToast(string,this);
    }

    public void showToast(String string){
        toastUtils.showToast(string,this);
    }
}
