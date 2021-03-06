package com.yaheen.cis.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yaheen.cis.R;
import com.yaheen.cis.entity.Status;
import com.yaheen.cis.entity.TypeBean;

import java.util.List;

public class PatrolTypeAdapter extends BaseQuickAdapter<TypeBean.TypeArrBean, BaseViewHolder> {

    private CheckBox checkBox;

    private int changeTime = 0;

    public PatrolTypeAdapter() {
        super(R.layout.item_patrol_type);
    }

    public void setDatas(@Nullable List<TypeBean.TypeArrBean> data) {
        setNewData(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TypeBean.TypeArrBean item) {
        checkBox = helper.getView(R.id.cb_type);
        helper.addOnClickListener(R.id.cb_type);
        helper.setText(R.id.cb_type, item.getName());

        if (changeTime == 0) {
            item.setSelected(true);
            changeTime++;
        } else if (changeTime < getData().size()) {
            item.setSelected(false);
            changeTime++;
        }

        if (item.isSelected()) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }

        if (item.getName().equals("国土")) {
            helper.setBackgroundRes(R.id.iv_type, R.drawable.ic_land);
        } else if (item.getName().equals("禁毒")) {
            helper.setBackgroundRes(R.id.iv_type, R.drawable.ic_drug);
        } else {
            helper.setBackgroundRes(R.id.iv_type, R.drawable.ic_fire);
        }
    }

    //返回选中问题的ID字符串
    public String getTypeId() {
        String str = "";

        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).isSelected()) {
                return mData.get(i).getId();
            }
        }
        return str;
    }
}
