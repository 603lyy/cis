package com.yaheen.cis.adapter;

import android.support.annotation.Nullable;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yaheen.cis.R;
import com.yaheen.cis.entity.Status;
import com.yaheen.cis.entity.UrgencyBean;

import java.util.ArrayList;
import java.util.List;

public class UrgencyAdapter extends BaseQuickAdapter<UrgencyBean, BaseViewHolder> {

    public UrgencyAdapter() {
        super(R.layout.item_urgency);
        List<UrgencyBean> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            UrgencyBean bean = new UrgencyBean();
            bean.setId(i + "");
            bean.setSelect(false);
            list.add(bean);
        }
        setNewData(list);
    }

    public void setDatas(@Nullable List<UrgencyBean> data) {
        setNewData(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UrgencyBean item) {
        if (helper.getAdapterPosition() == 0) {
            if (item.isSelect()) {
                helper.setBackgroundRes(R.id.iv_urgency, R.drawable.ic_recode);
            } else {
                helper.setBackgroundRes(R.id.iv_urgency, R.drawable.ic_urgency_normal);
            }
            helper.setText(R.id.tv_urgency, R.string.detail_urgency_record);
        } else if (helper.getAdapterPosition() == 1) {
            if (item.isSelect()) {
                helper.setBackgroundRes(R.id.iv_urgency, R.drawable.ic_normal);
            } else {
                helper.setBackgroundRes(R.id.iv_urgency, R.drawable.ic_urgency_normal);
            }
            helper.setText(R.id.tv_urgency, R.string.detail_urgency_normal);
        } else if (helper.getAdapterPosition() == 2) {
            if (item.isSelect()) {
                helper.setBackgroundRes(R.id.iv_urgency, R.drawable.ic_suspicious);
            } else {
                helper.setBackgroundRes(R.id.iv_urgency, R.drawable.ic_urgency_normal);
            }
            helper.setText(R.id.tv_urgency, R.string.detail_urgency_suspicious);
        } else if (helper.getAdapterPosition() == 3) {
            if (item.isSelect()) {
                helper.setBackgroundRes(R.id.iv_urgency, R.drawable.ic_urgent);
            } else {
                helper.setBackgroundRes(R.id.iv_urgency, R.drawable.ic_urgency_normal);
            }
            helper.setText(R.id.tv_urgency, R.string.detail_urgency_urgent);
        }

    }

    public String geUrgencyId() {
        String id = "";
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).isSelect()) {
                id = mData.get(i).getId();
            }
        }
        return id;
    }

    public void resetData() {
        for (int i = 0; i < mData.size(); i++) {
            mData.get(i).setSelect(false);
        }
    }
}
