package com.sena.dmzjthird.comic.adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.account.MyRetrofitService;
import com.sena.dmzjthird.comic.bean.ComicLatestBean;
import com.sena.dmzjthird.utils.GlideUtil;
import com.sena.dmzjthird.utils.MyDataStore;
import com.sena.dmzjthird.utils.RetrofitHelper;
import com.sena.dmzjthird.utils.TimeUtil;
import com.sena.dmzjthird.utils.XPopUpUtil;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/12
 * Time: 17:13
 */
public class ComicLatestAdapter extends BaseQuickAdapter<ComicLatestBean, BaseViewHolder> implements LoadMoreModule {

    private final Context mContext;

    public ComicLatestAdapter(Context context) {
        super(R.layout.item_comic_rank);
        this.mContext = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, ComicLatestBean bean) {

        GlideUtil.loadImageWithCookie(mContext, "https://images.dmzj.com/"+bean.getCover(), holder.getView(R.id.cover));

        holder.setText(R.id.title, bean.getName());
        holder.setText(R.id.author, bean.getAuthors());
        holder.setText(R.id.tag, bean.getTypes().replace("/", " "));
        holder.setText(R.id.updateTime, TimeUtil.millConvertToDate(bean.getLast_updatetime()*1000));


        holder.getView(R.id.subscribe).setOnClickListener(v -> {


            long uid = MyDataStore.getInstance(mContext).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, 0L);
            // 注释掉，可能增加一条无意义的请求
            // 是否需要，后续在做讨论
//            if (0L == uid) {
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
