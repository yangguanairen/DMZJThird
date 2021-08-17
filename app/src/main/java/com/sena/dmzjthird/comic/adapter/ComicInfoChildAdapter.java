package com.sena.dmzjthird.comic.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.comic.bean.ComicInfoBean;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/13
 * Time: 14:11
 */
public class ComicInfoChildAdapter extends BaseQuickAdapter<ComicInfoBean.Data, BaseViewHolder> {


    public ComicInfoChildAdapter() {
        super(R.layout.item_comic_info_child);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, ComicInfoBean.Data data) {
        holder.setText(R.id.chapterName, data.getChapter_name());
    }
}