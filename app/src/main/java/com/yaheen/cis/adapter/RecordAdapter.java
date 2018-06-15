package com.yaheen.cis.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yaheen.cis.R;
import com.yaheen.cis.entity.RecordBean;
import com.yaheen.cis.entity.Status;

import java.util.List;

public class RecordAdapter extends BaseQuickAdapter<RecordBean.RecordArrBean, BaseViewHolder> {

    private String typeStr = "";

    public RecordAdapter() {
        super(R.layout.item_record);
    }

    public void setDatas(@Nullable List<RecordBean.RecordArrBean> data) {
        setNewData(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RecordBean.RecordArrBean item) {

        for (int i = 0; i < item.getTypeArr().size(); i++) {
            if (i == 0) {
                typeStr = item.getTypeArr().get(i);
            } else {
                typeStr = typeStr + "、" + item.getTypeArr().get(i);
            }
        }

        helper.setText(R.id.tv_time, transferStr(R.string.record_patrol_time, item.getStartTime()));
        helper.setText(R.id.tv_duration, transferStr(R.string.record_patrol_duration, item.getTimeDiffrence()));
        helper.setText(R.id.tv_type, transferStr(R.string.record_patrol_type, typeStr));
    }

    @SuppressLint("StringFormatInvalid")
    private String transferStr(int id, String string) {
        return String.format(mContext.getResources().getString(id), string);
    }
}
