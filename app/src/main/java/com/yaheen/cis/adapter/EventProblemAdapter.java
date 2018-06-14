package com.yaheen.cis.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yaheen.cis.R;
import com.yaheen.cis.entity.EventDetailBean;
import com.yaheen.cis.entity.QuestionBean;

import java.util.List;

public class EventProblemAdapter extends BaseQuickAdapter<EventDetailBean.TbEventBean.QuestionnaireArrBean, BaseViewHolder> {

    private CheckBox checkBox;

    public EventProblemAdapter() {
        super(R.layout.item_event_problem);
    }

    public void setDatas(@Nullable List<EventDetailBean.TbEventBean.QuestionnaireArrBean> data) {
        setNewData(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final EventDetailBean.TbEventBean.QuestionnaireArrBean item) {
        helper.setText(R.id.tv_problem, item.getQuestionnaire());

    }

}
