package com.yaheen.cis.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yaheen.cis.R;
import com.yaheen.cis.entity.Status;
import com.yaheen.cis.entity.TypeBean;

import java.util.List;

public class PatrolSettingAdapter extends BaseQuickAdapter<TypeBean.TypeArrBean, BaseViewHolder> {

    private CheckBox checkBox;

    public PatrolSettingAdapter() {
        super(R.layout.item_patrol_setting);
    }

    public void setDatas(@Nullable List<TypeBean.TypeArrBean> data) {
        setNewData(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TypeBean.TypeArrBean item) {

        checkBox = helper.getView(R.id.cb_setting);
        helper.setText(R.id.cb_setting,item.getName());

        switch (helper.getLayoutPosition() % 2) {
            case 0:
                checkBox.setChecked(true);
                break;
            case 1:
                checkBox.setChecked(false);
                break;
        }
    }
}
