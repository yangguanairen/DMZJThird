package com.sena.dmzjthird.comic.adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.comic.bean.ComicRelatedBean;
import com.sena.dmzjthird.utils.GlideUtil;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/13
 * Time: 20:16
 */
public class ComicRelatedChildAdapter extends BaseQuickAdapter<ComicRelatedBean.Data, BaseViewHolder> {

    private final Context mContext;

    public ComicRelatedChildAdapter(Context context) {
        super(R.layout.item_comic_recommend_child);
        this.mContext = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, ComicRelatedBean.Data data) {

        GlideUtil.loadImageWithCookie(mContext, data.getCover(), holder.getView(R.id.comic_cover));

        holder.setText(R.id.comic_title, data.getName());
        holder.setText(R.id.comic_author, data.getStatus());
    }
}
