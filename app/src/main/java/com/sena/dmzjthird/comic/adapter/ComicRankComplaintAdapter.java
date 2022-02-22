package com.sena.dmzjthird.comic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.account.MyRetrofitService;
import com.sena.dmzjthird.comic.bean.ComicComplaintRankBean;
import com.sena.dmzjthird.utils.GlideUtil;
import com.sena.dmzjthird.utils.MyDataStore;
import com.sena.dmzjthird.utils.RetrofitHelper;
import com.sena.dmzjthird.utils.TimeUtil;
import com.sena.dmzjthird.utils.XPopUpUtil;

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
            long uid = MyDataStore.getInstance(mContext).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, 0L);
//            if ("".equals(uid)) {
//                Toast.makeText(mContext, mContext.getString(R.string.not_login), Toast.LENGTH_SHORT).show();
//                return ;
//            }
            MyRetrofitService service = RetrofitHelper.getMyServer(MyRetrofitService.MY_BASE_URL);
            service.controlSubscribe(uid, bean.getId(), bean.getCover(), bean.getName(), bean.getAuthors())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(resultBean -> {
                        if (resultBean == null) {
                            XPopUpUtil.showCustomErrorToast(mContext, "请求失败，请稍后重试");
                            return ;
                        }
                        if (resultBean.getCode() == 100) {
                            XPopUpUtil.showCustomErrorToast(mContext, mContext.getString(R.string.not_login));
                            return ;
                        }
                        holder.setBackgroundResource(R.id.subscribe,
                                "true".equals(resultBean.getContent()) ? R.drawable.ic_subscribed_black : R.drawable.ic_subscribe_black);


                    });
        });
    }
}
