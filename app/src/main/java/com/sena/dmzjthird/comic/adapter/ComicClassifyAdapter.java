package com.sena.dmzjthird.comic.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
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
public class ComicClassifyAdapter extends BaseQuickAdapter<ComicClassifyBean, BaseViewHolder> implements LoadMoreModule {

    private final Context mContext;

    public ComicClassifyAdapter(Context context) {
        super(R.layout.item_comic_classify);
        this.mContext = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, ComicClassifyBean bean) {

        Glide.with(mContext).load(GlideUtil.addCookie(bean.getCover()))
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                .into((ImageView) holder.getView(R.id.cover));

        holder.setText(R.id.title, bean.getTitle());
        holder.setText(R.id.author, bean.getAuthors());
        holder.setText(R.id.status, bean.getStatus());


    }
}
