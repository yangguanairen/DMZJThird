package com.sena.dmzjthird.comic.adapter;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.comic.bean.ComicRelatedBean;
import com.sena.dmzjthird.comic.view.AuthorInfoActivity;
import com.sena.dmzjthird.comic.view.ComicInfoActivity;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/13
 * Time: 20:16
 */
public class ComicRelatedAdapter extends BaseQuickAdapter<ComicRelatedBean.Author_Comics, BaseViewHolder> {

    private final Context mContext;

    public ComicRelatedAdapter(Context context) {
        super(R.layout.item_comic_recommend);
        this.mContext = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, ComicRelatedBean.Author_Comics data) {

        RecyclerView recyclerView = holder.getView(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        ComicRelatedChildAdapter adapter = new ComicRelatedChildAdapter(mContext);
        recyclerView.setAdapter(adapter);
        adapter.setList(data.getData());

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            Intent intent = new Intent(mContext, ComicInfoActivity.class);
            intent.putExtra(mContext.getString(R.string.intent_comic_id), ((ComicRelatedBean.Data) adapter1.getData().get(position)).getId());
            mContext.startActivity(intent);
        });

        if (data.getAuthor_name().equals("同类题材")) {
            holder.setText(R.id.title, data.getAuthor_name()+"作品");
            holder.setVisible(R.id.refresh, false);
        } else {
            holder.setText(R.id.title, data.getAuthor_name()+"的其他作品");
            holder.setBackgroundResource(R.id.refresh, R.drawable.ic_right);

        }
        holder.getView(R.id.refresh).setOnClickListener(v -> {
            Intent intent = new Intent(mContext, AuthorInfoActivity.class);
            intent.putExtra(mContext.getString(R.string.intent_author_id), data.getAuthor_id());
            mContext.startActivity(intent);
        });
    }
}
