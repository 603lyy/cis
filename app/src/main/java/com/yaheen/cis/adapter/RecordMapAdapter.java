package com.yaheen.cis.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yaheen.cis.R;
import com.yaheen.cis.entity.Status;

import java.util.List;

public class RecordMapAdapter extends BaseQuickAdapter<Status, BaseViewHolder> {

    public RecordMapAdapter() {
        super(R.layout.item_record_map);
    }

    public void setDatas(@Nullable List<Status> data) {
        setNewData(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Status item) {

        helper.setText(R.id.tv_describe,"2018-06-05 13:54");
    }
}
