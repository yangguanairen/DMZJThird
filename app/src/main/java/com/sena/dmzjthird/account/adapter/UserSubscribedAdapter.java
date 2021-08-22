package com.sena.dmzjthird.account.adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.account.bean.UserSubscribedBean;
import com.sena.dmzjthird.utils.GlideUtil;

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

        GlideUtil.loadImage(mContext, bean.getCover(), holder.getView(R.id.cover));

        holder.setText(R.id.title, bean.getName());
        holder.setVisible(R.id.author, false);
        holder.setVisible(R.id.status, false);


    }
}
