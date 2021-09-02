package com.sena.dmzjthird.comic.adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.module.UpFetchModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.utils.GlideUtil;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/9/1
 * Time: 16:16
 */
public class ComicViewAdapter extends BaseQuickAdapter<String, BaseViewHolder> implements LoadMoreModule, UpFetchModule {

    private final Context mContext;

    public ComicViewAdapter(Context context) {
        super(R.layout.item_comic_view);
        this.mContext = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, String url) {

        holder.setIsRecyclable(false);
        GlideUtil.loadImageWithCookie(mContext, url, holder.getView(R.id.image));
    }
}
