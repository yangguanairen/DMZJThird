package com.sena.dmzjthird.comic.adapter;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.account.MyRetrofitService;
import com.sena.dmzjthird.comic.bean.ComicTopicInfoBean;
import com.sena.dmzjthird.utils.GlideUtil;
import com.sena.dmzjthird.utils.MyDataStore;
import com.sena.dmzjthird.utils.RetrofitHelper;
import com.sena.dmzjthird.utils.XPopUpUtil;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/15
 * Time: 21:06
 */
public class ComicTopicRelatedAdapter extends BaseQuickAdapter<ComicTopicInfoBean.Comics, BaseViewHolder> {

    private final Context mContext;

    public ComicTopicRelatedAdapter(Context context) {
        super(R.layout.item_object_topic_related);
        this.mContext = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, ComicTopicInfoBean.Comics comics) {

        GlideUtil.loadImageWithCookie(mContext, comics.getCover(), holder.getView(R.id.cover));

        holder.setText(R.id.title, comics.getName());
        holder.setText(R.id.tag, comics.getRecommend_brief());
        holder.setText(R.id.description, comics.getRecommend_reason());

//        holder.getView(R.id.subscribe).setOnClickListener(v -> {
//            long uid = MyDataStore.getInstance(mContext).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, 0L);
////            if ("".equals(uid)) {
////                Toast.makeText(mContext, mContext.getString(R.string.not_login), Toast.LENGTH_SHORT).show();
////                return ;
////            }
//            MyRetrofitService service = RetrofitHelper.getMyServer(MyRetrofitService.MY_BASE_URL);
//            service.controlSubscribe(uid, comics.getId(), comics.getCover(), comics.getName(), comics.getRecommend_brief())
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(resultBean -> {
//                        if (resultBean == null) {
//                            XPopUpUtil.showCustomErrorToast(mContext, "请求失败，请稍后重试");
//                            return ;
//                        }
//                        if (resultBean.getCode() == 100) {
//                            XPopUpUtil.showCustomErrorToast(mContext, mContext.getString(R.string.not_login));
//                            return ;
//                        }
//                        holder.setBackgroundResource(R.id.subscribe,
//                                "true".equals(resultBean.getContent()) ? R.drawable.ic_subscribed_black : R.drawable.ic_subscribe_black);
//                    });
//        });

    }
}
