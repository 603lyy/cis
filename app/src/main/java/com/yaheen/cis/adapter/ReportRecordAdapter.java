package com.yaheen.cis.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yaheen.cis.R;
import com.yaheen.cis.entity.RecordBean;
import com.yaheen.cis.entity.ReportRecordBean;

import java.util.List;

public class ReportRecordAdapter extends BaseQuickAdapter<ReportRecordBean.EventListBean, BaseViewHolder> {

    private String typeStr = "";

    public ReportRecordAdapter() {
        super(R.layout.item_report_record);
    }

    public void setDatas(@Nullable List<ReportRecordBean.EventListBean> data) {
        setNewData(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReportRecordBean.EventListBean item) {

//        for (int i = 0; i < item.getTypeArr().size(); i++) {
//            if (i == 0) {
//                typeStr = item.getTypeArr().get(i);
//            } else {
//                typeStr = typeStr + "、" + item.getTypeArr().get(i);
//            }
//        }
//
        helper.setText(R.id.tv_time, item.getTime());
        helper.setText(R.id.tv_duration, transferStr(R.string.record_emergency, item.getEmergency()));
        helper.setText(R.id.tv_describe, item.getDescribe());
    }

    @SuppressLint("StringFormatInvalid")
    private String transferStr(int id, String string) {
        return String.format(mContext.getResources().getString(id), string);
    }
}
