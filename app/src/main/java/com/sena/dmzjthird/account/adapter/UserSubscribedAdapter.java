package com.sena.dmzjthird.account.adapter;

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
import com.sena.dmzjthird.account.bean.UserSubscribedBean;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/15
 * Time: 12:11
 */
public class UserSubscribedAdapter extends BaseQuickAdapter<UserSubscribedBean, BaseViewHolder> implements LoadMoreModule {

    private final Context mContext;

    public UserSubscribedAdapter(Context context) {
        super(R.layout.item_comic_classify);
        this.mContext = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, UserSubscribedBean bean) {

        Glide.with(mContext)
                .load(bean.getCover())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                .into((ImageView) holder.getView(R.id.cover));

        holder.setText(R.id.title, bean.getName());
        holder.setVisible(R.id.author, false);
        holder.setVisible(R.id.status, false);


    }
}
