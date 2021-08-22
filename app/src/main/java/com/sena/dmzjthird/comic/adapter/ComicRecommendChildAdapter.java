package com.sena.dmzjthird.comic.adapter;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.comic.bean.ComicRecommendBean;
import com.sena.dmzjthird.utils.GlideUtil;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/3
 * Time: 22:35
 */
public class ComicRecommendChildAdapter extends BaseQuickAdapter<ComicRecommendBean.Data, BaseViewHolder> {

    private final Context mContext;

    public ComicRecommendChildAdapter(Context context) {
        super(R.layout.item_comic_recommend_child);
        this.mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, ComicRecommendBean.Data data) {

        GlideUtil.loadImageWithCookie(mContext, data.getCover(), holder.getView(R.id.comic_cover));

        holder.setText(R.id.comic_title, data.getTitle());

        if ("".equals(data.getSub_title()) || data.getSub_title() == null) {
            holder.getView(R.id.comic_author).setVisibility(View.GONE);
        } else {
            holder.setText(R.id.comic_author, data.getSub_title());
        }

    }
}
