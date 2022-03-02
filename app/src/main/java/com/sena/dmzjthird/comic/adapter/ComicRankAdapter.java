package com.sena.dmzjthird.comic.adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.application.ComicRankListRes;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.utils.GlideUtil;
import com.sena.dmzjthird.utils.TimeUtil;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2022/3/2
 * Time: 20:04
 */
public class ComicRankAdapter extends BaseQuickAdapter<ComicRankListRes.ComicRankListItemResponse, BaseViewHolder> implements LoadMoreModule {

    private final Context mContext;

    public ComicRankAdapter(Context context) {
        super(R.layout.item_object_rank);
        mContext = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, ComicRankListRes.ComicRankListItemResponse data) {

        GlideUtil.loadImageWithCookie(mContext, data.getCover(), holder.getView(R.id.cover));
        holder.setText(R.id.title, data.getTitle());
        holder.setText(R.id.author, data.getAuthors());
        holder.setText(R.id.tag, data.getTypes().replace("/", "  "));
        holder.setText(R.id.updateTime, TimeUtil.millConvertToDate(data.getLastUpdatetime() * 1000));



    }
}
