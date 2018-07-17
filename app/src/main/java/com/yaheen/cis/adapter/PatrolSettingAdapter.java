package com.yaheen.cis.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yaheen.cis.R;
import com.yaheen.cis.entity.Status;
import com.yaheen.cis.entity.TypeBean;

import java.util.ArrayList;
import java.util.List;

public class PatrolSettingAdapter extends BaseQuickAdapter<TypeBean.TypeArrBean, BaseViewHolder> {

    private CheckBox checkBox;

    public PatrolSettingAdapter() {
        super(R.layout.item_patrol_setting);
    }

    public void setDatas(@Nullable List<TypeBean.TypeArrBean> data) {
        setNewData(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final TypeBean.TypeArrBean item) {
        if(item.isSelected()){
            helper.setChecked(R.id.cb_setting,true);
        }else {
            helper.setChecked(R.id.cb_setting,false);
        }

        helper.addOnClickListener(R.id.cb_setting);
        helper.setText(R.id.cb_setting, item.getName());
    }

    //已经选择的问题类型
    public TypeBean getTypeBean() {
        TypeBean typeBean = new TypeBean();
        typeBean.setTypeArr(new ArrayList<TypeBean.TypeArrBean>());
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).isSelected()) {
                typeBean.getTypeArr().add(mData.get(i));
            }
        }
        return typeBean;
    }
}
