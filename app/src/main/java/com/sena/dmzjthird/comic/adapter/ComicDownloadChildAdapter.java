package com.sena.dmzjthird.comic.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;

/**
 * FileName: ComicDownloadChildAdapter
 * Author: JiaoCan
 * Date: 2022/3/17 17:17
 */

public class ComicDownloadChildAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ComicDownloadChildAdapter() {
        super(R.layout.item_comic_info_child);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, String s) {
        holder.setText(R.id.chapterName, s);
    }
}
