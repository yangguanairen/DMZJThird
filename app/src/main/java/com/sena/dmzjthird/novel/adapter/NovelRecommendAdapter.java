package com.sena.dmzjthird.novel.adapter;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.novel.bean.NovelRecommendBean;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2022/2/26
 * Time: 20:06
 */
public class NovelRecommendAdapter extends BaseQuickAdapter<NovelRecommendBean, BaseViewHolder> {

    private final Context mContext;

    public NovelRecommendAdapter(Context context) {
        super(R.layout.item_object_recommend);
        mContext = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, NovelRecommendBean bean) {

        RecyclerView recyclerView = holder.getView(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        NovelRecommendChildAdapter adapter = new NovelRecommendChildAdapter(mContext);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((a, view, position) -> {
            NovelRecommendBean.NovelRecommendData data = (NovelRecommendBean.NovelRecommendData) a.getData().get(position);
            String objId = data.getObj_id();
            // 跳转轻小说详情页

        });
        adapter.setList(bean.getData());

        holder.setText(R.id.title, bean.getTitle());
        // 最新更新
        if (bean.getCategory_id() == 58) {
            holder.setImageResource(R.id.icon, R.drawable.ic_right);
            holder.getView(R.id.icon).setOnClickListener(v -> {
                LocalBroadcastManager manager = LocalBroadcastManager.getInstance(mContext);
                Intent intent = new Intent("goToNovelLatest");
                manager.sendBroadcast(intent);
            });
        }
    }
}
