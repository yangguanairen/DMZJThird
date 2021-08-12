package com.sena.dmzjthird.comic.adapter;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.comic.bean.ComicLatestBean;
import com.sena.dmzjthird.utils.GlideUtil;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.TimeUtil;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/12
 * Time: 17:13
 */
public class ComicLatestAdapter extends BaseQuickAdapter<ComicLatestBean, BaseViewHolder> implements LoadMoreModule {

    private Context mContext;

    public ComicLatestAdapter(Context context) {
        super(R.layout.item_comic_latest);
        this.mContext = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, ComicLatestBean bean) {

        Glide.with(mContext).load(GlideUtil.addCookie("https://images.dmzj.com/"+bean.getCover()))
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                .into((ImageView) holder.getView(R.id.cover));

        holder.setText(R.id.title, bean.getName());
        holder.setText(R.id.author, bean.getAuthors());
        holder.setText(R.id.tag, bean.getTypes().replace("/", " "));
        holder.setText(R.id.updateTime, TimeUtil.millConvertToDate(bean.getLast_updatetime()*1000));
        holder.setText(R.id.chapterName, bean.getLast_update_chapter_name());

        holder.getView(R.id.linear).setOnClickListener(v -> {
//            Intent intent = new Intent(mContext, ComicViewActivity.class);
//            intent.putExtra(mContext.getString(R.string.intent_comic_id), bean.getId());
//            intent.putExtra(mContext.getString(R.string.intent_chapter_id), bean.getLast_update_chapter_id());
//            mContext.startActivity(intent);
        });


    }
}
