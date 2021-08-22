package com.sena.dmzjthird.comic.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.comic.bean.ComicClassifyCoverBean;
import com.sena.dmzjthird.utils.GlideUtil;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/11
 * Time: 12:10
 */
public class ComicClassifyCoverAdapter extends BaseQuickAdapter<ComicClassifyCoverBean.Data, BaseViewHolder> {

    private final Context mContext;

    public ComicClassifyCoverAdapter(Context context) {
        super(R.layout.item_comic_classify_cover);
        this.mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, ComicClassifyCoverBean.Data data) {

        holder.setText(R.id.title, data.getTitle());

        GlideUtil.loadImageWithCookie(mContext, data.getCover(), holder.getView(R.id.cover));

    }
}
