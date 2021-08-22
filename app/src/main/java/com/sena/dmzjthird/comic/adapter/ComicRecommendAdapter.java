package com.sena.dmzjthird.comic.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.account.UserSubscribedActivity;
import com.sena.dmzjthird.comic.bean.ComicRecommendBean;
import com.sena.dmzjthird.comic.bean.ComicRecommendChildBean2;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.RetrofitHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/3
 * Time: 22:34
 */
public class ComicRecommendAdapter extends BaseQuickAdapter<ComicRecommendBean, BaseViewHolder> {

    private final Context mContext;
    public static final String BROADCAST_INTENT = "broadcast_intent";

    public ComicRecommendAdapter(Context context) {
        super(R.layout.item_comic_recommend);
        this.mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, ComicRecommendBean bean) {



        String title = bean.getTitle();
        holder.setText(R.id.title, title);
        int id = bean.getCategory_id();
        if (id == 47 || id == 51 || id == 53 || id == 55) {
//            holder.setVisible(R.id.refresh, false);
        } else if (id == 48 || id == 49 || id == 56) {
            holder.setBackgroundResource(R.id.refresh, R.drawable.ic_right);
            holder.getView(R.id.refresh).setVisibility(View.VISIBLE);
        } else {
            holder.setBackgroundResource(R.id.refresh, R.drawable.ic_refresh);
            holder.getView(R.id.refresh).setVisibility(View.VISIBLE);
        }

        RecyclerView recyclerView = holder.getView(R.id.recyclerview);
        ComicRecommendChildAdapter adapter = new ComicRecommendChildAdapter(mContext);
        recyclerView.setAdapter(adapter);

        if (id == 48 || id == 53 || id == 55) {
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        }

        adapter.setList(bean.getData());

        // 添加监听器
        holder.getView(R.id.refresh).setOnClickListener(v -> {
            // 我的订阅
            if (id == 49) {
                mContext.startActivity(new Intent(mContext, UserSubscribedActivity.class));
            }
            // 火热专题
            if (id == 48) {
                mContext.sendBroadcast(new Intent(BROADCAST_INTENT));
            }
            // 猜你喜欢
            if (id == 50) {
                RetrofitService service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);
                service.getComicRecommend2(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(bean1 -> {
                            List<ComicRecommendBean.Data> tmp = new ArrayList<>();
                            for (ComicRecommendChildBean2.Data1.Data data : bean1.getData().getData()) {
                                tmp.add(new ComicRecommendBean.Data(data.getCover(), data.getTitle(), data.getAuthors(),
                                        0, null, data.getId(), data.getStatus()));
                            }
                            adapter.setList(tmp);
                        });

            }
            // 国漫也精彩|热门连载
            if (id == 52 || id == 54) {
                RetrofitService service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);
                service.getComicRecommend1(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(bean1 -> {
                            adapter.setList(bean1.getData().getData());
                        });
            }
        });

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            String obj_id = ((ComicRecommendBean.Data) adapter1.getData().get(position)).getObj_id();
            if (id == 48) {
                IntentUtil.goToComicTopicInfoActivity(mContext, obj_id);
            } else if (id == 51) {
                IntentUtil.goToAuthorInfoActivity(mContext, obj_id);
            } else {
                IntentUtil.goToComicInfoActivity(mContext, obj_id);
            }

        });


    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

}
