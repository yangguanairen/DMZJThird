package com.sena.dmzjthird.novel.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.novel.bean.NovelFilterTagBean;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2022/2/27
 * Time: 11:21
 */
public class NovelFilterTagChildAdapter extends BaseQuickAdapter<NovelFilterTagBean.NovelFilterItem, BaseViewHolder> {

    private int selectId;

    public NovelFilterTagChildAdapter() {
        super(R.layout.item_object_filter_tag_child);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, NovelFilterTagBean.NovelFilterItem item) {
        holder.setText(R.id.tag, item.getTag_name());
        if (item.getTag_id() == selectId) {
            holder.setBackgroundResource(R.id.tag, R.drawable.shape_filter_tag);
        } else {
            holder.setBackgroundResource(R.id.tag, 0);
        }
    }

    public void setSelectTag(int tagId) {
        selectId = tagId;
    }
}
