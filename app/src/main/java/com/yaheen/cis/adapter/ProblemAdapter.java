package com.yaheen.cis.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yaheen.cis.R;
import com.yaheen.cis.entity.QuestionBean;
import com.yaheen.cis.entity.TypeBean;

import java.util.ArrayList;
import java.util.List;

public class ProblemAdapter extends BaseQuickAdapter<QuestionBean.QuestionaireArrBean, BaseViewHolder> {

    private CheckBox checkBox;

    public ProblemAdapter() {
        super(R.layout.item_problem);
    }

    public void setDatas(@Nullable List<QuestionBean.QuestionaireArrBean> data) {
        setNewData(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final QuestionBean.QuestionaireArrBean item) {
        checkBox = helper.getView(R.id.cb_problem);
        helper.setText(R.id.cb_problem, item.getName());

        if(item.isSelected()){
            checkBox.setChecked(true);
        }else {
            checkBox.setChecked(false);
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //已经是选中状态
                if (item.isSelected()) {
                    item.setSelected(false);
                } else {
                    item.setSelected(true);
                }
            }
        });
    }

    //返回选中问题的ID字符串
    public String getQuestionStr() {
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

    public void resetData() {
        for (int i = 0; i < mData.size(); i++) {
            mData.get(i).setSelected(false);
        }
    }
}
