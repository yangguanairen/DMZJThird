package com.sena.dmzjthird.novel.adapter;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.application.NovelChapterRes;
import com.sena.dmzjthird.R;

/**
 * FileName: NovelViewCatalogAdapter
 * Author: JiaoCan
 * Date: 2022/3/10 13:40
 */

public class NovelViewCatalogAdapter extends BaseQuickAdapter<NovelChapterRes.NovelChapterItemResponse, BaseViewHolder> {

    private final Context mContext;
    private int selectChapterId;

    public NovelViewCatalogAdapter(Context context) {
        super(R.layout.item_object_view_catalog);
        mContext = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, NovelChapterRes.NovelChapterItemResponse data) {

        holder.setText(R.id.chapterName, data.getChapterName());
        if (selectChapterId == data.getChapterId()) {
            holder.setTextColor(R.id.chapterName, mContext.getColor(R.color.theme_blue));
        } else {
            holder.setTextColor(R.id.chapterName, Color.WHITE);
        }

    }

    public void setSelectId(int chapterId) {
        selectChapterId = chapterId;
        notifyDataSetChanged();
    }
}
