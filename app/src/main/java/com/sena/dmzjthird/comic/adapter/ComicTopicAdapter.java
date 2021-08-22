package com.sena.dmzjthird.comic.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.comic.bean.ComicTopicBean;
import com.sena.dmzjthird.utils.GlideUtil;
import com.sena.dmzjthird.utils.TimeUtil;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/11
 * Time: 12:49
 */
public class ComicTopicAdapter extends BaseQuickAdapter<ComicTopicBean.Data, BaseViewHolder> implements LoadMoreModule {

    private final Context mContext;

    public ComicTopicAdapter(Context context) {
        super(R.layout.item_comic_topic);
        this.mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, ComicTopicBean.Data data) {


        Glide.with(mContext)
                .load(GlideUtil.addCookie(data.getSmall_cover()))
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(5)))
                .into((ImageView) holder.getView(R.id.cover));

        holder.setText(R.id.title, data.getTitle());
        holder.setText(R.id.updateTime, TimeUtil.millConvertToDate(data.getCreate_time()*1000));

    }
}
