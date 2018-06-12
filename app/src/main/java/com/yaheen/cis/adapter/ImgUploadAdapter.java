package com.yaheen.cis.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yaheen.cis.R;
import com.yaheen.cis.entity.Status;

import java.util.List;

public class ImgUploadAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private Context context;

    public ImgUploadAdapter(Context context) {
        super(R.layout.item_upload_img);
        this.context = context;
    }

    public void setDatas(@Nullable List<String> data) {
        setNewData(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String path) {

        Glide.with(context)
                .load(path)
                .centerCrop()
                .into((ImageView) helper.getView(R.id.iv_img));
    }
}
