package com.sena.dmzjthird.comic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.application.ComicDetailRes;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.utils.IntentUtil;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/13
 * Time: 14:10
 */
public class ComicInfoAdapter extends BaseQuickAdapter<ComicDetailRes.ComicDetailChapterResponse, BaseViewHolder> {

    private final Context mContext;

    private final String mComicId;
    private final String mCover;
    private final String mComicName;

    private boolean isReverse = true;

    public ComicInfoAdapter(Context context, String comicId, String cover, String comicName) {
        super(R.layout.item_comic_info);
        mContext = context;
        mComicId = comicId;
        mCover = cover;
        mComicName = comicName;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void convert(@NonNull BaseViewHolder holder, ComicDetailRes.ComicDetailChapterResponse data) {

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
        adapter.setOnItemClickListener((a, view, position) -> {
            String selectChapterId = String.valueOf(data.getData(position).getChapterId());
            String selectChapterName = data.getData(position).getChapterTitle();
            IntentUtil.goToComicViewActivity(mContext, mComicId, mCover, mComicName, selectChapterId, selectChapterName);
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
