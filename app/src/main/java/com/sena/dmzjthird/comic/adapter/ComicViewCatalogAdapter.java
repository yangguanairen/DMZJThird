package com.sena.dmzjthird.comic.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.comic.bean.ComicInfoBean;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/17
 * Time: 13:33
 */
public class ComicViewCatalogAdapter extends BaseQuickAdapter<ComicInfoBean.Data, BaseViewHolder> {

    private final Context mContext;
    private String currentChapterId;

    public ComicViewCatalogAdapter(Context context) {
        super(R.layout.item_comic_info_child);
        this.mContext = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, ComicInfoBean.Data data) {
        TextView textView = holder.getView(R.id.chapterName);
        textView.setText(data.getChapter_name());
        if (currentChapterId.equals(data.getId())) {
            textView.setTextColor(mContext.getColor(R.color.theme_blue));
        } else {
            textView.setTextColor(Color.WHITE);
        }
        textView.setBackgroundColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER_VERTICAL);
    }

    public void setCurrentChapterId(String id) {
        currentChapterId = id;
    }


}
