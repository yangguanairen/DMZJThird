package com.sena.dmzjthird.novel.adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.novel.bean.NovelSearchBean;
import com.sena.dmzjthird.utils.GlideUtil;
import com.sena.dmzjthird.utils.TimeUtil;

/**
 * FileName: NovelSearchResultAdapter
 * Author: JiaoCan
 * Date: 2022/3/14 13:31
 */

public class NovelSearchResultAdapter extends BaseQuickAdapter<NovelSearchBean, BaseViewHolder> implements LoadMoreModule {

    private final Context mContext;

    public NovelSearchResultAdapter(Context context) {
        super(R.layout.item_object_rank);
        mContext = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, NovelSearchBean bean) {
        GlideUtil.loadImage(mContext, bean.getCover(), holder.getView(R.id.cover));

        holder.setText(R.id.title, bean.getTitle());
        holder.setText(R.id.author, bean.getAuthors());
        holder.setText(R.id.tag, bean.getTypes());
        holder.setText(R.id.updateTime, TimeUtil.millConvertToDate(bean.getAddtime() * 1000));
    }

}
