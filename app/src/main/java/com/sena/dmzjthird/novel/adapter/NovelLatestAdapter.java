package com.sena.dmzjthird.novel.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.novel.bean.NovelLatestBean;
import com.sena.dmzjthird.utils.TimeUtil;

import java.util.Arrays;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2022/2/26
 * Time: 11:54
 */
public class NovelLatestAdapter extends BaseQuickAdapter<NovelLatestBean, BaseViewHolder> implements LoadMoreModule {

    private final Context mContext;

    public NovelLatestAdapter(Context context) {
        super(R.layout.item_object_latest);
        mContext = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, NovelLatestBean bean) {

        Glide.with(mContext).load(bean.getCover())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                .into((ImageView) holder.getView(R.id.cover));

        holder.setText(R.id.title, bean.getName());
        holder.setText(R.id.author, bean.getAuthors());
        String tagStr = Arrays.toString(bean.getTypes().toArray());
        holder.setText(R.id.tag, tagStr.substring(1, tagStr.length() - 1));
        holder.setText(R.id.latestChapter, bean.getLast_update_chapter_name());
        holder.setText(R.id.updateTime, TimeUtil.millConvertToDate(bean.getLast_update_time() * 1000));

    }
}
