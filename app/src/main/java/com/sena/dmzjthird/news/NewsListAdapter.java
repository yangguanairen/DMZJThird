package com.sena.dmzjthird.news;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.application.NewsListRes;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.utils.GlideUtil;
import com.sena.dmzjthird.utils.TimeUtil;

/**
 * FileName: NewsAdapter
 * Author: JiaoCan
 * Date: 2022/2/25 10:36
 */

public class NewsListAdapter extends BaseQuickAdapter<NewsListRes.NewsListItemResponse, BaseViewHolder> implements LoadMoreModule {

    private final Context mContext;

    public NewsListAdapter(Context context) {
        super(R.layout.item_news);
        mContext = context;
    }


    @Override
    protected void convert(@NonNull BaseViewHolder holder, NewsListRes.NewsListItemResponse data) {

        GlideUtil.loadImageWithCookie(mContext, data.getRowPicUrl(), holder.getView(R.id.cover));


//        Glide.with(mContext).load(data.getRowPicUrl()).into((ImageView) holder.getView(R.id.cover));
        holder.setText(R.id.title, data.getTitle());
        holder.setText(R.id.updateTime, TimeUtil.millConvertToDate(data.getCreateTime() * 1000));
        holder.setText(R.id.likeCount, data.getMoodAmount() + "");
        holder.setText(R.id.commentCount, data.getCommentAmount() + "");


    }
}
