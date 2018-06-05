package com.yaheen.cis.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yaheen.cis.R;
import com.yaheen.cis.util.map.BDMapUtils;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;

public class LoginActivity extends PermissionActivity {

    private LinearLayout llRPsd;

    private CheckBox cbRPsd;

    private TextView tvLogin;

    private EditText etName,etPsd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitleContent(R.string.login_title);

        initView();

        BDMapUtils.startLocation();

        llRPsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void initView() {

        cbRPsd = findViewById(R.id.cb_remember_password);
        llRPsd = findViewById(R.id.ll_remember_password);
        etName = findViewById(R.id.et_username);
        etPsd = findViewById(R.id.et_password);
        tvLogin = findViewById(R.id.tv_login);

        etPsd.setText(DefaultPrefsUtil.getUserPassword());
        etName.setText(DefaultPrefsUtil.getUserName());

    }

    private void login() {
//        String name = etName.getText().toString();
//        String psd = etPsd.getText().toString();
//
//        if(TextUtils.isEmpty(name)){
//            showToast(R.string.login_username_empty);
//            return;
//        }
//
//        if(TextUtils.isEmpty(psd)){
//            showToast(R.string.login_password_empty);
//            return;
//        }
//
//        //不记住密码则保存空字符串
//        if(cbRPsd.isChecked()){
//            DefaultPrefsUtil.setUserPassword(psd);
//        }
//        DefaultPrefsUtil.setUserName(name);

        Intent intent = new Intent(this,TurnActivity.class);
        startActivity(intent);
    }

    private void check() {
        if (cbRPsd.isChecked()) {
            cbRPsd.setChecked(false);
        } else {
            cbRPsd.setChecked(true);
        }
    }
}
