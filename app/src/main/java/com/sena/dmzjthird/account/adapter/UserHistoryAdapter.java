package com.sena.dmzjthird.account.adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.account.bean.UserHistoryBean;
import com.sena.dmzjthird.utils.GlideUtil;
import com.sena.dmzjthird.utils.TimeUtil;

/**
 * FileName: UserHistoryAdapter
 * Author: JiaoCan
 * Date: 2022/3/4 14:23
 */

public class UserHistoryAdapter extends BaseQuickAdapter<UserHistoryBean, BaseViewHolder> {

    private final Context mContext;

    public UserHistoryAdapter(Context context) {
        super(R.layout.item_object_topic_related);
        mContext = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, UserHistoryBean bean) {

        GlideUtil.loadImageWithCookie(mContext, bean.getObjectCover(), holder.getView(R.id.cover));
        holder.setText(R.id.title, bean.getObjectName());
        holder.setText(R.id.tag, bean.getChapterName() == null ? "未读" : "上次观看至" + bean.getChapterName());
        holder.setText(R.id.description, TimeUtil.millConvertToDate(bean.getaTime()));


    }
}
