package com.sena.dmzjthird.comic.adapter;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.comic.bean.AuthorInfoBean;
import com.sena.dmzjthird.utils.GlideUtil;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/13
 * Time: 21:27
 */
public class AuthorInfoAdapter extends BaseQuickAdapter<AuthorInfoBean.Data, BaseViewHolder> {

    private final Context mContext;

    public AuthorInfoAdapter(Context context) {
        super(R.layout.item_object_rank);
        this.mContext = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, AuthorInfoBean.Data data) {

        GlideUtil.loadImageWithCookie(mContext, data.getCover(), holder.getView(R.id.cover));

        holder.setText(R.id.title, data.getName());
        holder.setText(R.id.author, data.getStatus());
        ((TextView) holder.getView(R.id.author)).setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        holder.setVisible(R.id.tag, false);
        holder.setVisible(R.id.updateTime, false);


    }
}
