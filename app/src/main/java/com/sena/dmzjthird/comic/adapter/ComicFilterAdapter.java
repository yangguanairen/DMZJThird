package com.sena.dmzjthird.comic.adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.comic.bean.ComicClassifyBean;
import com.sena.dmzjthird.utils.GlideUtil;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/14
 * Time: 11:56
 */
public class ComicFilterAdapter extends BaseQuickAdapter<ComicClassifyBean, BaseViewHolder> implements LoadMoreModule {

    private final Context mContext;

    public ComicFilterAdapter(Context context) {
        super(R.layout.item_object_filter);
        this.mContext = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, ComicClassifyBean bean) {

        GlideUtil.loadImageWithCookie(mContext, bean.getCover(), holder.getView(R.id.cover));

        holder.setText(R.id.title, bean.getTitle());
        holder.setText(R.id.author, bean.getAuthors());
        holder.setText(R.id.status, bean.getStatus());


    }
}
