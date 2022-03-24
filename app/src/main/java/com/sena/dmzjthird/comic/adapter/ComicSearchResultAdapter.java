package com.sena.dmzjthird.comic.adapter;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.comic.bean.ComicSearchResultBean;
import com.sena.dmzjthird.utils.GlideUtil;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/17
 * Time: 21:03
 */
public class ComicSearchResultAdapter extends BaseQuickAdapter<ComicSearchResultBean, BaseViewHolder> implements LoadMoreModule {

    private final Context mContext;

    public ComicSearchResultAdapter(Context context) {
        super(R.layout.item_object_rank);
        this.mContext = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, ComicSearchResultBean bean) {

        GlideUtil.loadImage(mContext, bean.getCover(), holder.getView(R.id.cover));

        holder.setText(R.id.title, bean.getTitle());
        holder.setText(R.id.author, bean.getAuthors());
        holder.setText(R.id.tag, bean.getTypes());
        TextView textView = holder.getView(R.id.updateTime);
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_book, 0, 0, 0);
        textView.setText(bean.getLast_name());

    }
}
