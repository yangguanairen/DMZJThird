package com.sena.dmzjthird.comic.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.application.ComicDetailRes;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.comic.bean.ComicInfoBean;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/17
 * Time: 13:33
 */
public class ComicViewCatalogAdapter extends BaseQuickAdapter<ComicDetailRes.ComicDetailChapterInfoResponse, BaseViewHolder> {

    private final Context mContext;
    private int currentChapterId;

    public ComicViewCatalogAdapter(Context context) {
        super(R.layout.item_object_view_catalog);
        this.mContext = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, ComicDetailRes.ComicDetailChapterInfoResponse data) {
        holder.setText(R.id.chapterName, data.getChapterTitle());
        if (data.getChapterId() == currentChapterId) {
            holder.setTextColor(R.id.chapterName, mContext.getColor(R.color.theme_blue));
        } else {
            holder.setTextColor(R.id.chapterName, Color.WHITE);
        }
    }

    public void setCurrentChapterId(int chapterId) {
        currentChapterId = chapterId;
        notifyDataSetChanged();
    }


}
