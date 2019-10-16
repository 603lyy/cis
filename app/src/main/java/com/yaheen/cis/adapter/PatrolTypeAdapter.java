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
        setNewData(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TypeArrBean item) {
        checkBox = helper.getView(R.id.cb_type);
        helper.addOnClickListener(R.id.cb_type);
//        helper.setText(R.id.cb_type, item.getName());
        helper.setText(R.id.tv_type, item.getName());

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

        if (item.getName().equals("厕所")) {
            helper.setBackgroundRes(R.id.iv_type, R.drawable.ic_cesuo);
        } else if (item.getName().equals("房前屋后水沟")) {
            helper.setBackgroundRes(R.id.iv_type, R.drawable.ic_shuigou);
        }  else if (item.getName().equals("环境卫生")) {
            helper.setBackgroundRes(R.id.iv_type, R.drawable.ic_huanjing);
        }   else if (item.getName().equals("环境卫生保洁费1元，1元以上/人/月")) {
            helper.setBackgroundRes(R.id.iv_type, R.drawable.ic_baojie);
        }else if (item.getName().equals("乱堆乱放")) {
            helper.setBackgroundRes(R.id.iv_type, R.drawable.ic_luandui);
        }  else if (item.getName().equals("禽畜养殖污染")) {
            helper.setBackgroundRes(R.id.iv_type, R.drawable.ic_qinchu);
        }   else if (item.getName().equals("“四房”拆除")) {
            helper.setBackgroundRes(R.id.iv_type, R.drawable.ic_sifang);
        }else if (item.getName().equals("围栏")) {
            helper.setBackgroundRes(R.id.iv_type, R.drawable.ic_weilan);
        }
        else {
            helper.setBackgroundRes(R.id.iv_type, R.drawable.ic_huanjing);
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
