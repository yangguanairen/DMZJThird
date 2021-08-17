package com.sena.dmzjthird.comic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.comic.bean.ComicInfoBean;
import com.sena.dmzjthird.comic.view.ComicViewActivity;

import java.util.Collections;
import java.util.List;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/13
 * Time: 14:10
 */
public class ComicInfoAdapter extends BaseQuickAdapter<ComicInfoBean, BaseViewHolder> {

    private final Context mContext;

    public ComicInfoAdapter(Context context) {
        super(R.layout.item_comic_info);
        this.mContext = context;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void convert(@NonNull BaseViewHolder holder, ComicInfoBean bean) {

        RecyclerView recyclerView = holder.getView(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        ComicInfoChildAdapter adapter = new ComicInfoChildAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setList(bean.getData());

        holder.setText(R.id.classifyName, bean.getTitle());
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            ComicInfoBean.Data data = (ComicInfoBean.Data) adapter1.getData().get(position);
            Intent intent = new Intent(mContext, ComicViewActivity.class);
            intent.putExtra(mContext.getString(R.string.intent_comic_id), data.getComic_id());
            intent.putExtra(mContext.getString(R.string.intent_chapter_id), data.getId());
            intent.putExtra(mContext.getString(R.string.intent_serial_data), bean);
            mContext.startActivity(intent);
        });

        TextView textView = (TextView) holder.getView(R.id.sort);
        textView.setOnClickListener(v -> {
            List<ComicInfoBean.Data> data = adapter.getData();
            Collections.reverse(data);
            adapter.setList(data);
            textView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    mContext.getDrawable(textView.getContentDescription().toString().equals("1")?R.drawable.ic_sort_positive:R.drawable.ic_sort_reverse),  null);
            textView.setContentDescription(textView.getContentDescription().toString().equals("1")?"0":"1");

        });


    }


}
