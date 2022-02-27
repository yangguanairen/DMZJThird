package com.sena.dmzjthird.novel.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.novel.bean.NovelFilterTagBean;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2022/2/27
 * Time: 11:14
 */
public class NovelFilterTagAdapter extends BaseQuickAdapter<NovelFilterTagBean, BaseViewHolder> {

    private final Context mContext;

    private final Callbacks callbacks;

    private int mSort;
    private int mTheme;
    private int mStatus;

    public NovelFilterTagAdapter(Context context, int sort, int theme, int status) {
        super(R.layout.item_object_filter_tag);
        mContext = context;
        callbacks = (Callbacks) context;
        mSort = sort;
        mTheme = theme;
        mStatus = status;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, NovelFilterTagBean bean) {

        String title = bean.getTitle();
        holder.setText(R.id.title, title);

        RecyclerView recyclerView = holder.getView(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        NovelFilterTagChildAdapter adapter = new NovelFilterTagChildAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((a, view, position) -> {

            NovelFilterTagBean.NovelFilterItem item = (NovelFilterTagBean.NovelFilterItem) a.getData().get(position);
            int selectedId = item.getTag_id();

            if ("排序".equals(title)) {
                mSort = selectedId;
            } else if ("题材".equals(title)) {
                mTheme = selectedId;
            } else if ("连载进度".equals(title)) {
                mStatus = selectedId;
            }
            adapter.setSelectTag(selectedId);
            adapter.setList(bean.getItems());
            callbacks.onFilterItemChange(mTheme, mStatus, mSort);
        });

        if ("排序".equals(title)) {
            adapter.setSelectTag(mSort);
        } else if ("题材".equals(title)) {
            adapter.setSelectTag(mTheme);
        } else if ("连载进度".equals(title)) {
            adapter.setSelectTag(mStatus);
        }

        adapter.setList(bean.getItems());
    }

    public interface Callbacks {
        void onFilterItemChange(int theme, int status, int sort);
    }
}
