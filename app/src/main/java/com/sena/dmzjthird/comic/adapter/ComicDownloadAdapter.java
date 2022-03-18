package com.sena.dmzjthird.comic.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.application.ComicDetailRes;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.account.MyRetrofitService;
import com.sena.dmzjthird.utils.RetrofitHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * FileName: ComicDownloadAdapter
 * Author: JiaoCan
 * Date: 2022/3/17 8:25
 */

public class ComicDownloadAdapter extends BaseQuickAdapter<ComicDetailRes.ComicDetailChapterResponse, BaseViewHolder> {

    private final Context mContext;

    private final Set<Integer> selectIdSet; // 最大容量15，防止Service开启过多，内存崩溃

    public ComicDownloadAdapter(Context context) {
        super(R.layout.item_comic_info);
        mContext = context;
        selectIdSet = new HashSet<>();
    }



    @Override
    protected void convert(@NonNull BaseViewHolder holder, ComicDetailRes.ComicDetailChapterResponse data) {

        RecyclerView recyclerView = holder.getView(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        ComicDownloadChildAdapter adapter = new ComicDownloadChildAdapter();
        recyclerView.setAdapter(adapter);


        List<Integer> chapterIdList = new ArrayList<>();
        List<String> chapterNameList = new ArrayList<>();
        for (int i = 0; i < data.getDataCount(); i++) {
            ComicDetailRes.ComicDetailChapterInfoResponse item = data.getData(i);
            chapterIdList.add(item.getChapterId());
            chapterNameList.add(item.getChapterTitle());
        }

        adapter.setOnItemClickListener((a, view, position) -> {

            if (selectIdSet.size() >= 15) {
                Toast.makeText(mContext, "一次最多选择15个章节!!", Toast.LENGTH_SHORT).show();
                return ;
            }

            int chapterId  = chapterIdList.get(position);
            boolean tag = view.getTag() == null ? false : (boolean) view.getTag();
            if (!tag) {
                view.setBackgroundResource(R.drawable.shape_comic_info_catalog_item_select);
                selectIdSet.add(chapterId);
            } else {
                view.setBackgroundResource(R.drawable.shape_comic_info_catalog_item);
                selectIdSet.remove(chapterId);
            }
            view.setTag(!tag);

        });

        adapter.setList(chapterNameList);

    }

    public Set<Integer> getSelectIdSet() {
        return selectIdSet;
    }


}
