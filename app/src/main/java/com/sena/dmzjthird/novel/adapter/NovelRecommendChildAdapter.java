package com.sena.dmzjthird.novel.adapter;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.novel.bean.NovelRecommendBean;
import com.sena.dmzjthird.utils.GlideUtil;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2022/2/26
 * Time: 20:08
 */
public class NovelRecommendChildAdapter extends BaseQuickAdapter<NovelRecommendBean.NovelRecommendData, BaseViewHolder> {

    private final Context mContext;

    public NovelRecommendChildAdapter(Context context) {
        super(R.layout.item_object_recommend_child);
        mContext = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, NovelRecommendBean.NovelRecommendData data) {

        GlideUtil.loadImage(mContext, data.getCover(), holder.getView(R.id.cover));
        holder.setText(R.id.title, data.getTitle());

        if (TextUtils.isEmpty(data.getSub_title())) {
            holder.setGone(R.id.subTitle, true);
        } else {
            holder.setText(R.id.subTitle, data.getSub_title());
        }

    }
}
