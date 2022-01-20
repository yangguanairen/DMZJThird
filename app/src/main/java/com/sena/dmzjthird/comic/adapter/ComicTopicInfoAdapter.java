package com.sena.dmzjthird.comic.adapter;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.comic.bean.ComicTopicInfoBean;
import com.sena.dmzjthird.utils.GlideUtil;
import com.sena.dmzjthird.utils.MyDataStore;
import com.sena.dmzjthird.utils.PreferenceHelper;
import com.sena.dmzjthird.utils.RetrofitHelper;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/15
 * Time: 21:06
 */
public class ComicTopicInfoAdapter extends BaseQuickAdapter<ComicTopicInfoBean.Comics, BaseViewHolder> {

    private final Context mContext;

    public ComicTopicInfoAdapter(Context context) {
        super(R.layout.item_comic_rank);
        this.mContext = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, ComicTopicInfoBean.Comics comics) {

        GlideUtil.loadImageWithCookie(mContext, comics.getCover(), holder.getView(R.id.cover));

        holder.setText(R.id.title, comics.getName());
        holder.setText(R.id.author, comics.getRecommend_brief());
        holder.setText(R.id.tag, comics.getRecommend_reason());

        ((TextView) holder.getView(R.id.author)).setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        ((TextView) holder.getView(R.id.tag)).setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        holder.setVisible(R.id.updateTime, false);

        holder.getView(R.id.subscribe).setOnClickListener(v -> {
            String uid = MyDataStore.getInstance(mContext).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, "");
            if ("".equals(uid)) {
                Toast.makeText(mContext, mContext.getString(R.string.not_login), Toast.LENGTH_SHORT).show();
                return ;
            }
            RetrofitService service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);
            if (holder.getView(R.id.subscribe).getContentDescription().equals("0")) {
                // 订阅
                service.subscribeComic(comics.getId(), uid, "mh")
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
                service.cancelSubscribeComic(comics.getId(), uid, "mh")
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
