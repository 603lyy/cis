package com.yaheen.cis.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yaheen.cis.R;

import java.util.List;

public class CommonImgAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private Context context;

    public CommonImgAdapter(Context context) {
        super(R.layout.item_common_img);
        this.context = context;
    }

    public void setDatas(@Nullable List<String> data) {
        setNewData(data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final String url) {

        Glide.with(context)
                .load(url)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .centerCrop()
                .into((ImageView) helper.getView(R.id.iv_img));

        helper.addOnClickListener(R.id.iv_img);
    }
}
