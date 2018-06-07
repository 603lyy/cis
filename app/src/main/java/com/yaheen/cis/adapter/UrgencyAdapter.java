package com.yaheen.cis.adapter;

import android.support.annotation.Nullable;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yaheen.cis.R;
import com.yaheen.cis.entity.Status;

import java.util.List;

public class UrgencyAdapter extends BaseQuickAdapter<Status, BaseViewHolder> {

    public UrgencyAdapter() {
        super(R.layout.item_urgency);
    }

    public void setDatas(@Nullable List<Status> data) {
        setNewData(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Status item) {
    }
}
