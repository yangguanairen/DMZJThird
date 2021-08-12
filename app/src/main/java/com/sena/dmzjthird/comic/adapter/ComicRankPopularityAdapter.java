package com.sena.dmzjthird.comic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.comic.bean.ComicComplaintRankBean;
import com.sena.dmzjthird.comic.bean.ComicPopularityRankBean;
import com.sena.dmzjthird.comic.bean.ComicSubscribeRankBean;
import com.sena.dmzjthird.comic.view.ComicInfoActivity;
import com.sena.dmzjthird.utils.GlideUtil;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.PreferenceHelper;
import com.sena.dmzjthird.utils.RetrofitHelper;
import com.sena.dmzjthird.utils.TimeUtil;

import org.jetbrains.annotations.NotNull;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/11
 * Time: 14:11
 */
public class ComicRankPopularityAdapter extends BaseQuickAdapter<ComicPopularityRankBean, BaseViewHolder> implements LoadMoreModule {

    private final Context mContext;

    public ComicRankPopularityAdapter(Context context) {
        super(R.layout.item_comic_rank);
        this.mContext = context;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void convert(@NotNull BaseViewHolder holder, ComicPopularityRankBean bean) {
        Glide.with(mContext)
                .load(GlideUtil.addCookie("https://images.dmzj.com/" + bean.getCover()))
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                .into((ImageView) holder.getView(R.id.cover));

        holder.setText(R.id.title, bean.getName());
        holder.setText(R.id.author, bean.getAuthors());
        holder.setText(R.id.tag, bean.getTypes());
        holder.setText(R.id.updateTime, TimeUtil.millConvertToDate(bean.getLast_updatetime()*1000));

        holder.getView(R.id.cons).setOnClickListener(v -> {
            Intent intent = new Intent(mContext, ComicInfoActivity.class);
            intent.putExtra(mContext.getString(R.string.intent_comic_id), bean.getId());
            mContext.startActivity(intent);
        });

        holder.getView(R.id.subscribe).setOnClickListener(v -> {
            if (PreferenceHelper.findStringByKey(mContext, PreferenceHelper.USER_UID) == null) {
                Toast.makeText(mContext, mContext.getString(R.string.not_login), Toast.LENGTH_SHORT).show();
                return ;
            }
            RetrofitService service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);
            if (holder.getView(R.id.subscribe).getContentDescription().equals("0")) {
                // 订阅
                service.subscribeComic(bean.getId(), PreferenceHelper.findStringByKey(mContext, PreferenceHelper.USER_UID), "mh")
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
                service.cancelSubscribeComic(bean.getId(), PreferenceHelper.findStringByKey(mContext, PreferenceHelper.USER_UID), "mh")
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
