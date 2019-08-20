package com.yaheen.cis.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yaheen.cis.R;
import com.yaheen.cis.entity.FieldHouseBean;

import java.util.List;

public class FieldHouseAdapter extends BaseQuickAdapter<FieldHouseBean.JsonBean, BaseViewHolder> {

    public FieldHouseAdapter() {
        super(R.layout.item_field_hosue);
    }

    public void setDatas(@Nullable List<FieldHouseBean.JsonBean> data) {
        setNewData(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FieldHouseBean.JsonBean item) {

        helper.setText(R.id.tv_address, item.getAddress());
        if (item.getUsername().indexOf(",") > 0) {
            helper.setText(R.id.tv_username, item.getUsername().substring(0, item.getUsername().indexOf(",")));
        } else {
            helper.setText(R.id.tv_username, item.getUsername());
        }
    }
}
