package com.sena.dmzjthird.novel.adapter;

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
import com.sena.dmzjthird.novel.bean.NovelFilterBean;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2022/2/27
 * Time: 11:03
 */
public class NovelFilterAdapter extends BaseQuickAdapter<NovelFilterBean, BaseViewHolder> implements LoadMoreModule {

    private final Context mContext;

    public NovelFilterAdapter(Context context) {
        super(R.layout.item_object_filter);
        mContext = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, NovelFilterBean bean) {

        Glide.with(mContext).load(bean.getCover())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                .into((ImageView) holder.getView(R.id.cover));

        holder.setText(R.id.title, bean.getName());
        holder.setText(R.id.author, bean.getAuthors());
        holder.setGone(R.id.status, true);


    }
}
