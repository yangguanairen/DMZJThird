package com.sena.dmzjthird.comic.adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.application.ComicUpdateListRes;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.utils.GlideUtil;
import com.sena.dmzjthird.utils.TimeUtil;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/12
 * Time: 17:13
 */
public class ComicLatestAdapter extends BaseQuickAdapter<ComicUpdateListRes.ComicUpdateListItemResponse, BaseViewHolder> implements LoadMoreModule {

    private final Context mContext;

    public ComicLatestAdapter(Context context) {
        super(R.layout.item_object_latest);
        this.mContext = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, ComicUpdateListRes.ComicUpdateListItemResponse data) {

        GlideUtil.loadImageWithCookie(mContext, data.getCover(), holder.getView(R.id.cover));

        holder.setText(R.id.title, data.getTitle());
        holder.setText(R.id.author, data.getAuthors());
        holder.setText(R.id.tag, data.getTypes().replace("/", " "));
        holder.setText(R.id.latestChapter, data.getLastUpdateChapterName());
        holder.setText(R.id.updateTime, TimeUtil.millConvertToDate(data.getLastUpdatetime() * 1000));

    }
}
