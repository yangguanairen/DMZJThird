package com.sena.dmzjthird.comic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.comic.bean.ComicComplaintRankBean;
import com.sena.dmzjthird.utils.GlideUtil;
import com.sena.dmzjthird.utils.MyDataStore;
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
 * Time: 19:06
 */
public class ComicRankComplaintAdapter extends BaseQuickAdapter<ComicComplaintRankBean, BaseViewHolder> implements LoadMoreModule {

    private final Context mContext;

    public ComicRankComplaintAdapter(Context context) {
        super(R.layout.item_comic_rank);
        this.mContext = context;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void convert(@NotNull BaseViewHolder holder, ComicComplaintRankBean bean) {

        GlideUtil.loadImageWithCookie(mContext, "https://images.dmzj.com/" + bean.getCover(), holder.getView(R.id.cover));

        holder.setText(R.id.title, bean.getName());
        holder.setText(R.id.author, bean.getAuthors());
        holder.setText(R.id.tag, bean.getTypes());
        holder.setText(R.id.updateTime, TimeUtil.millConvertToDate(bean.getLast_updatetime()*1000));


        holder.getView(R.id.subscribe).setOnClickListener(v -> {
            String uid = MyDataStore.getInstance(mContext).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, "");
            if ("".equals(uid)) {
                Toast.makeText(mContext, mContext.getString(R.string.not_login), Toast.LENGTH_SHORT).show();
                return ;
            }
            RetrofitService service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);
            if (holder.getView(R.id.subscribe).getContentDescription().equals("0")) {
                // 订阅
                service.subscribeComic(bean.getId(), uid, "mh")
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
                service.cancelSubscribeComic(bean.getId(), uid, "mh")
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
