package com.yaheen.cis.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yaheen.cis.R;
import com.yaheen.cis.entity.RecordEventBean;
import com.yaheen.cis.entity.Status;
import com.yaheen.cis.util.map.BDMapUtils;

import java.util.List;

public class RecordMapAdapter extends BaseQuickAdapter<RecordEventBean.EventListBean, BaseViewHolder> {

    public RecordMapAdapter() {
        super(R.layout.item_record_map);
    }

    public void setDatas(@Nullable List<RecordEventBean.EventListBean> data) {
        setNewData(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RecordEventBean.EventListBean item) {

        helper.setText(R.id.tv_time, "2018-06-05 13:54");
        helper.setText(R.id.tv_situation, transferSituation(item.getEmergency()));
        if (!item.getEmergency().equals("1")) {
            helper.setText(R.id.tv_describe, item.getDescribe());
        }
    }

    private String transferSituation(String emergency) {
        if (emergency.equals("1")) {
            return "记录";
        } else if (emergency.equals("2")) {
            return "正常";
        } else if (emergency.equals("3")) {
            return "可疑";
        } else if (emergency.equals("4")) {
            return "危险";
        }
        return emergency;
    }
}
