package com.sena.dmzjthird.comic.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.comic.bean.ComicRecommendNewBean;
import com.sena.dmzjthird.utils.GlideUtil;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/3
 * Time: 22:35
 */
public class ComicRecommendChildAdapter extends BaseQuickAdapter<ComicRecommendNewBean.ComicRecommendItem, BaseViewHolder> {

    private final Context mContext;

    public ComicRecommendChildAdapter(Context context, int layoutId) {
        super(layoutId);
        this.mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, ComicRecommendNewBean.ComicRecommendItem item) {

        GlideUtil.loadImageWithCookie(mContext, item.getCover(), holder.getView(R.id.cover));

        holder.setText(R.id.title, item.getTitle());

        // item_object_recommend_child2没有subTitle
        // 故，需要try-catch包裹
        try {
            if ("".equals(item.getSub_title()) || item.getSub_title() == null) {
                holder.setGone(R.id.subTitle, true);
            } else {
                holder.setText(R.id.subTitle, item.getSub_title());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
