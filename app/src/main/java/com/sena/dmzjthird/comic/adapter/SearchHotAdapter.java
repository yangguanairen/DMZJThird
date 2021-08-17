package com.sena.dmzjthird.comic.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.comic.bean.SearchHotBean;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/17
 * Time: 18:30
 */
public class SearchHotAdapter extends BaseQuickAdapter<SearchHotBean, BaseViewHolder> {

    public SearchHotAdapter() {
        super(R.layout.item_search_hot);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, SearchHotBean bean) {
        holder.setText(R.id.title, bean.getName());
    }
}
