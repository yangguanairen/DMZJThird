package com.sena.dmzjthird.comic.adapter;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.comic.bean.ComicRecommendBean;
import com.sena.dmzjthird.utils.LogUtil;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/3
 * Time: 22:34
 */
public class ComicRecommendAdapter extends BaseQuickAdapter<ComicRecommendBean, BaseViewHolder> {

    private Context mContext;

    public ComicRecommendAdapter(Context context) {
        super(R.layout.item_comic_recommend);
        this.mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, ComicRecommendBean bean) {

        RecyclerView recyclerView = holder.getView(R.id.recyclerview);
        ComicRecommendChildAdapter adapter = new ComicRecommendChildAdapter(mContext);
        recyclerView.setAdapter(adapter);

        String title = bean.getTitle();
        holder.setText(R.id.title, title);
        int id = bean.getCategory_id();
        if (id == 47 || id == 51 || id == 53 || id == 55) {
            holder.getView(R.id.refresh).setVisibility(View.GONE);
        } else if (id == 48 || id == 56) {
            holder.getView(R.id.refresh).setBackgroundResource(R.drawable.ic_right);
        } else {
            holder.getView(R.id.refresh).setBackgroundResource(R.drawable.ic_refresh);
        }

        if (id == 48 || id == 53 || id == 55) {
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        }

        adapter.setList(bean.getData());

    }
}
