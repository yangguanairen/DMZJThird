package com.sena.dmzjthird.comic.adapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.comic.bean.ComicRelatedBean;
import com.sena.dmzjthird.utils.IntentUtil;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/13
 * Time: 20:16
 */
public class ComicRelatedAdapter extends BaseQuickAdapter<ComicRelatedBean.Author_Comics, BaseViewHolder> {

    private final Context mContext;

    public ComicRelatedAdapter(Context context) {
        super(R.layout.item_object_recommend);
        this.mContext = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, ComicRelatedBean.Author_Comics data) {

        RecyclerView recyclerView = holder.getView(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        ComicRelatedChildAdapter adapter = new ComicRelatedChildAdapter(mContext);
        recyclerView.setAdapter(adapter);
        adapter.setList(data.getData());

        adapter.setOnItemClickListener((a, view, position) ->
                IntentUtil.goToComicInfoActivity(mContext, ((ComicRelatedBean.Data) a.getData().get(position)).getId()));

        if (data.getAuthor_name().equals("同类题材")) {
            holder.setText(R.id.title, data.getAuthor_name()+"作品");

        } else {
            holder.setText(R.id.title, data.getAuthor_name()+"的其他作品");
            holder.setBackgroundResource(R.id.icon, R.drawable.ic_right);
            holder.getView(R.id.icon).setOnClickListener(v ->
                    IntentUtil.goToAuthorInfoActivity(mContext, data.getAuthor_id()));
        }

    }
}
