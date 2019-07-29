package com.yaheen.cis.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yaheen.cis.R;
import com.yaheen.cis.entity.TypeArrBean;
import com.yaheen.cis.entity.TypeBean;

import java.util.List;

public class ReportTypeAdapter extends BaseQuickAdapter<TypeArrBean, BaseViewHolder> {

    private int changeTime = 0;

    public ReportTypeAdapter() {
        super(R.layout.item_record_type);
    }

    public void setDatas(@Nullable List<TypeArrBean> data) {
        setNewData(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TypeArrBean item) {
        helper.addOnClickListener(R.id.cb_type);
        helper.setText(R.id.cb_type, item.getName());

        if (changeTime < getData().size()) {
            item.setSelected(true);
            changeTime++;
        }

        if (item.isSelected()) {
            helper.setChecked(R.id.cb_type, true);
        } else {
            helper.setChecked(R.id.cb_type, false);
        }

    }

    //返回选中问题的ID字符串
    public String getTypeIdStr() {
        String str = "";
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).isSelected()) {
                if (TextUtils.isEmpty(str)) {
                    str = mData.get(i).getId();
                } else {
                    str = str + "," + mData.get(i).getId();
                }
            }
        }
        return str;
    }
}
