package com.sena.dmzjthird.comic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.application.ComicDetailInfo;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.utils.IntentUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/13
 * Time: 14:10
 */
public class ComicInfoAdapter extends BaseQuickAdapter<ComicDetailInfo.ComicDetailChapterResponse, BaseViewHolder> {

    private final Context mContext;

    private final String mComicId;

    private boolean isReverse = true;

    public ComicInfoAdapter(Context context, String comicId) {
        super(R.layout.item_comic_info);
        mComicId = comicId;
        mContext = context;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void convert(@NonNull BaseViewHolder holder, ComicDetailInfo.ComicDetailChapterResponse data) {

        RecyclerView recyclerView = holder.getView(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        ComicInfoChildAdapter adapter = new ComicInfoChildAdapter();
        recyclerView.setAdapter(adapter);
        ArrayList<String> chapterNames = new ArrayList<>();
        ArrayList<String> chapterIds = new ArrayList<>();
        for (int i = 0; i < data.getDataCount(); i++) {
            chapterNames.add(data.getData(i).getChapterTitle());
            chapterIds.add(data.getData(i).getChapterId() + "");
        }
        adapter.setList(chapterNames);

        holder.setText(R.id.classifyName, data.getTitle());
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            String selectedId = String.valueOf(data.getData(position).getChapterId());
            IntentUtil.goToComicViewActivity(mContext, mComicId, selectedId);
        });

        TextView tvSort = (TextView) holder.getView(R.id.sort);
        tvSort.setOnClickListener(v -> {
            Collections.reverse(chapterNames);
            Collections.reverse(chapterIds);
            adapter.setList(chapterNames);
            tvSort.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    mContext.getDrawable(isReverse ? R.drawable.ic_sort_positive : R.drawable.ic_sort_reverse),  null);
            isReverse = !isReverse;
        });


    }


}
