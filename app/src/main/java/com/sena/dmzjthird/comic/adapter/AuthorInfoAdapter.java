package com.sena.dmzjthird.comic.adapter;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.comic.bean.AuthorInfoBean;
import com.sena.dmzjthird.utils.GlideUtil;
import com.sena.dmzjthird.utils.PreferenceHelper;
import com.sena.dmzjthird.utils.RetrofitHelper;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/13
 * Time: 21:27
 */
public class AuthorInfoAdapter extends BaseQuickAdapter<AuthorInfoBean.Data, BaseViewHolder> {

    private Context mContext;

    public AuthorInfoAdapter(Context context) {
        super(R.layout.item_comic_rank);
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


        holder.getView(R.id.subscribe).setOnClickListener(v -> {
            if (PreferenceHelper.findStringByKey(mContext, PreferenceHelper.USER_UID) == null) {
                Toast.makeText(mContext, mContext.getString(R.string.not_login), Toast.LENGTH_SHORT).show();
                return;
            }
            RetrofitService service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);
            if (holder.getView(R.id.subscribe).getContentDescription().equals("0")) {
                // 订阅
                service.subscribeComic(data.getId(), PreferenceHelper.findStringByKey(mContext, PreferenceHelper.USER_UID), "mh")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(bean1 -> {
                            if (bean1.getCode() != 0) {
                                Toast.makeText(mContext, mContext.getString(R.string.subscribe_fail), Toast.LENGTH_SHORT).show();
                            } else {
                                holder.setBackgroundResource(R.id.subscribe, R.drawable.ic_subscribed_black);
                                Toast.makeText(mContext, mContext.getString(R.string.subscribe_success), Toast.LENGTH_SHORT).show();
                            }
                        });
                holder.getView(R.id.subscribe).setContentDescription("1");
            } else {
                // 取消订阅
                service.cancelSubscribeComic(data.getId(), PreferenceHelper.findStringByKey(mContext, PreferenceHelper.USER_UID), "mh")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(bean1 -> {
                            if (bean1.getCode() != 0) {
                                Toast.makeText(mContext, mContext.getString(R.string.cancel_subscribe_fail), Toast.LENGTH_SHORT).show();
                            } else {
                                holder.setBackgroundResource(R.id.subscribe, R.drawable.ic_subscribe_black);
                                Toast.makeText(mContext, mContext.getString(R.string.cancel_subscribe_success), Toast.LENGTH_SHORT).show();
                            }
                        });
                holder.getView(R.id.subscribe).setContentDescription("0");
            }
        });


    }
}
