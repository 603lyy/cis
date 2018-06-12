package com.yaheen.cis.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yaheen.cis.R;
import com.yaheen.cis.entity.RecordBean;
import com.yaheen.cis.entity.Status;

import java.util.List;

public class RecordAdapter extends BaseQuickAdapter<RecordBean.RecordArrBean, BaseViewHolder> {

    public RecordAdapter() {
        super(R.layout.item_record);
    }

    public void setDatas(@Nullable List<RecordBean.RecordArrBean> data) {
        setNewData(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RecordBean.RecordArrBean item) {

        helper.setText(R.id.tv_describe,item.getStartTime());
    }
}
