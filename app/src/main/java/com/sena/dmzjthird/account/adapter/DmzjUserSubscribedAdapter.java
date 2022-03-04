package com.sena.dmzjthird.account.adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.account.bean.DmzjUserSubscribedBean;
import com.sena.dmzjthird.utils.GlideUtil;

/**
 * FileName: DmzjUserSubscribedAdapter
 * Author: JiaoCan
 * Date: 2022/3/4 13:53
 */

public class DmzjUserSubscribedAdapter extends BaseQuickAdapter<DmzjUserSubscribedBean, BaseViewHolder> implements LoadMoreModule {

    private final Context mContext;

    public DmzjUserSubscribedAdapter(Context context) {
        super(R.layout.item_object_filter);
        mContext = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, DmzjUserSubscribedBean bean) {

        GlideUtil.loadImageWithCookie(mContext, bean.getSub_img(), holder.getView(R.id.cover));
        holder.setText(R.id.title, bean.getName());
        holder.setText(R.id.subTitle, bean.getStatus());
    }
}
