package com.yaheen.cis.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yaheen.cis.R;
import com.yaheen.cis.entity.Status;
import com.yaheen.cis.entity.TypeArrBean;
import com.yaheen.cis.entity.TypeBean;

import java.util.List;

public class PatrolTypeAdapter extends BaseQuickAdapter<TypeArrBean, BaseViewHolder> {

    private CheckBox checkBox;

    private int changeTime = 0;

    public PatrolTypeAdapter() {
        super(R.layout.item_patrol_type);
    }

    public void setDatas(@Nullable List<TypeArrBean> data) {
//        if (data != null) {
//            TypeArrBean lastItem = new TypeArrBean();
//            lastItem.setName("外链");
//            lastItem.setId("-1");
//            data.add(lastItem);
//        }
        setNewData(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TypeArrBean item) {
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

        if (item.getName().equals("“两违”行为")) {
            helper.setBackgroundRes(R.id.iv_type, R.drawable.ic_land);
        } else if (item.getName().equals("禁毒")) {
            helper.setBackgroundRes(R.id.iv_type, R.drawable.ic_drug);
        } else if (item.getName().equals("环保")) {
            helper.setBackgroundRes(R.id.iv_type, R.drawable.ic_protection);
        } else if (item.getName().equals("环境卫生")) {
            helper.setBackgroundRes(R.id.iv_type, R.drawable.ic_weisheng);
        }
//        else if (item.getName().equals("其他")) {
//            helper.setBackgroundRes(R.id.iv_type, R.drawable.ic_other);
//        }
        else {
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
