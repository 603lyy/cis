package com.yaheen.cis.adapter;

import android.support.annotation.Nullable;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yaheen.cis.R;
import com.yaheen.cis.entity.QuestionBean;
import com.yaheen.cis.entity.TypeBean;

import java.util.List;

public class ProblemAdapter extends BaseQuickAdapter<QuestionBean.TypeArrBean, BaseViewHolder> {

    private CheckBox checkBox;

    public ProblemAdapter() {
        super(R.layout.item_problem);
    }

    public void setDatas(@Nullable List<QuestionBean.TypeArrBean> data) {
        setNewData(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, QuestionBean.TypeArrBean item) {
        checkBox = helper.getView(R.id.cb_problem);
        helper.setText(R.id.cb_problem, item.getName());
    }
}
