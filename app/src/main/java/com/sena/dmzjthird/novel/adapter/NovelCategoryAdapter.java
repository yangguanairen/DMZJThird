package com.sena.dmzjthird.novel.adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.novel.bean.NovelCategoryBean;
import com.sena.dmzjthird.utils.GlideUtil;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2022/2/26
 * Time: 12:33
 */
public class NovelCategoryAdapter extends BaseQuickAdapter<NovelCategoryBean, BaseViewHolder> {

    private final Context mContext;

    public NovelCategoryAdapter(Context context) {
        super(R.layout.item_object_category);
        mContext = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, NovelCategoryBean bean) {

        GlideUtil.loadImageWithCookie(mContext, bean.getCover(), holder.getView(R.id.cover));
        holder.setText(R.id.title, bean.getTitle());
    }
}
