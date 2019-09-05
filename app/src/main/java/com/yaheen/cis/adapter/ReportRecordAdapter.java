package com.yaheen.cis.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yaheen.cis.R;
import com.yaheen.cis.entity.RecordBean;
import com.yaheen.cis.entity.ReportRecordBean;
import com.yaheen.cis.util.sharepreferences.DefaultPrefsUtil;

import java.util.List;

public class ReportRecordAdapter extends BaseQuickAdapter<ReportRecordBean.EventListBean, BaseViewHolder> {

    public ReportRecordAdapter() {
        super(R.layout.item_report_record);
    }

    public void setDatas(@Nullable List<ReportRecordBean.EventListBean> data) {
        setNewData(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReportRecordBean.EventListBean item) {

        helper.addOnClickListener(R.id.tv_state);
        helper.addOnClickListener(R.id.tv_detail);

        helper.setText(R.id.tv_time, item.getTime());
        helper.setText(R.id.tv_describe, item.getDescribe());
        helper.setText(R.id.tv_name, transferStr(R.string.record_patrol_name, item.getUsername()));

        if (item.getFlag().equals("Y")) {
            helper.setText(R.id.tv_state, R.string.report_is_handle);
            helper.setBackgroundRes(R.id.tv_state, R.drawable.btn_gray_round);
        }
//        else if (item.getStatus().equals("VILLAGELEADER") && DefaultPrefsUtil.getRole().equals("VILLAGELEADER")) {
//            helper.setText(R.id.tv_state, R.string.report_is_report);
//            helper.setBackgroundRes(R.id.tv_state, R.drawable.btn_gray_round);
//        }
        else {
            helper.setText(R.id.tv_state, R.string.report_not_handle);
            helper.setBackgroundRes(R.id.tv_state, R.drawable.btn_yellow_round);
        }

        if (item.getEmergency().equals("4")) {
            helper.setText(R.id.tv_duration, transferStr(R.string.record_emergency, R.string.detail_urgency_urgent));
        } else if (item.getEmergency().equals("3")) {
            helper.setText(R.id.tv_duration, transferStr(R.string.record_emergency, R.string.detail_urgency_suspicious));
        } else if (item.getEmergency().equals("2")) {
            helper.setText(R.id.tv_duration, transferStr(R.string.record_emergency, R.string.detail_urgency_normal));
        } else {
            helper.setText(R.id.tv_duration, transferStr(R.string.record_emergency, R.string.detail_urgency_record));
        }
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
