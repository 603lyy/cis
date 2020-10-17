package com.yaheen.cis.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yaheen.cis.R;
import com.yaheen.cis.entity.EventDetailBean;
import com.yaheen.cis.entity.ReportRecordBean;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;

import java.util.List;

public class FlowRecordAdapter extends BaseQuickAdapter<EventDetailBean.DetailsListBean, BaseViewHolder> {

    public FlowRecordAdapter() {
        super(R.layout.item_flow_record);
    }

    public void setDatas(@Nullable List<EventDetailBean.DetailsListBean> data) {
        setNewData(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EventDetailBean.DetailsListBean item) {


        helper.setText(R.id.tv_title, item.getRepresent());
        helper.setText(R.id.tv_time, item.getOperationDate());
        helper.setText(R.id.tv_content, item.getDescribe());

    }

    @SuppressLint("StringFormatInvalid")
    private String transferStr(int id, int string) {
        return String.format(mContext.getResources().getString(id), mContext.getResources().getString(string));
    }

    @SuppressLint("StringFormatInvalid")
    private String transferStr(int id, String string) {
        return String.format(mContext.getResources().getString(id), string);
    }
}
