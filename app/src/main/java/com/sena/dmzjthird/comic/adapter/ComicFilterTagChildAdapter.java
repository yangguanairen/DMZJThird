package com.sena.dmzjthird.comic.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.comic.bean.ComicClassifyFilterBean;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/14
 * Time: 17:03
 */
public class ComicFilterTagChildAdapter extends BaseQuickAdapter<ComicClassifyFilterBean.Items, BaseViewHolder> {

    private String tagId = "0";

    public ComicFilterTagChildAdapter() {
        super(R.layout.item_object_filter_tag_child);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, ComicClassifyFilterBean.Items item) {
        holder.setText(R.id.tag, item.getTag_name());
        if (item.getTag_id().equals(tagId)) {
            holder.setBackgroundResource(R.id.tag, R.drawable.shape_filter_tag);
        } else {
            holder.setBackgroundResource(R.id.tag, 0);
        }
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }
}
