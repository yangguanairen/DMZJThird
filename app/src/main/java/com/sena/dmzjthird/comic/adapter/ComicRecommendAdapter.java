package com.sena.dmzjthird.comic.adapter;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.comic.bean.ComicRecommendLikeBean;
import com.sena.dmzjthird.comic.bean.ComicRecommendNewBean;
import com.sena.dmzjthird.comic.bean.ComicRecommendOtherBean;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.RetrofitHelper;

import org.jetbrains.annotations.NotNull;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/3
 * Time: 22:34
 */
public class ComicRecommendAdapter extends BaseQuickAdapter<ComicRecommendNewBean, BaseViewHolder> {

    private final Context mContext;
    public static final String ACTION_COMIC_UPDATE = "action_comic_update";
    public static final String ACTION_COMIC_TOPIC = "action_comic_topic";


    private final RetrofitService service;

    public ComicRecommendAdapter(Context context) {
        super(R.layout.item_object_recommend);
        mContext = context;
        service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, ComicRecommendNewBean bean) {

        String title = bean.getTitle();
        holder.setText(R.id.title, title);

        RecyclerView recyclerView = holder.getView(R.id.recyclerView);
        ComicRecommendChildAdapter adapter= null;
        ImageView ivIcon = holder.getView(R.id.icon);
        int categoryId = bean.getCategory_id();
        switch (categoryId) {
            case 47: // 近期必看
            case 51: // 大师作品
                recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
                adapter = new ComicRecommendChildAdapter(mContext, R.layout.item_object_recommend_child);
                break;
            case 48: // 火热专题
                recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
                adapter = new ComicRecommendChildAdapter(mContext, R.layout.item_object_recommend_child2);
                ivIcon.setImageResource(R.drawable.ic_right);
                ivIcon.setOnClickListener(v -> {
                    LocalBroadcastManager manager = LocalBroadcastManager.getInstance(mContext);
                    manager.sendBroadcast(new Intent(ACTION_COMIC_TOPIC));
                });
                break;
            case 50: // 猜你喜欢
                adapter = initYouLike(recyclerView, ivIcon);
                break;
            case 52:
                adapter = initGuoman(recyclerView, ivIcon);
                break;
            case 53: // 美漫事件
            case 55: // 条漫专区
                recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
                adapter = new ComicRecommendChildAdapter(mContext, R.layout.item_object_recommend_child2);
                break;
            case 54: // 热门连载
                adapter = initHot(recyclerView, ivIcon);
                break;
            case 56: // 最新上架
                recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
                adapter = new ComicRecommendChildAdapter(mContext, R.layout.item_object_recommend_child);
                ivIcon.setImageResource(R.drawable.ic_right);
                ivIcon.setOnClickListener(v -> {
                    LocalBroadcastManager manager = LocalBroadcastManager.getInstance(mContext);
                    manager.sendBroadcast(new Intent(ACTION_COMIC_UPDATE));
                });
                break;
        }
        if (adapter == null) {
            adapter = new ComicRecommendChildAdapter(mContext, R.layout.item_object_recommend_child);
        }
        recyclerView.setAdapter(adapter);
        adapter.setList(bean.getData());


        adapter.setOnItemClickListener((a, view, position) -> {
            String obj_id = ((ComicRecommendNewBean.ComicRecommendItem) a.getData().get(position)).getObj_id();
            if (categoryId == 48) {
                IntentUtil.goToComicTopicInfoActivity(mContext, obj_id);
            } else if (categoryId == 51) {
                IntentUtil.goToAuthorInfoActivity(mContext, obj_id);
            } else {
                IntentUtil.goToComicInfoActivity(mContext, obj_id);
            }

        });
    }

    private ComicRecommendChildAdapter initYouLike(RecyclerView recyclerView, ImageView ivIcon) {
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        ComicRecommendChildAdapter adapter = new ComicRecommendChildAdapter(mContext, R.layout.item_object_recommend_child);
        ivIcon.setImageResource(R.drawable.ic_refresh);
        ivIcon.setOnClickListener(v -> service.getComicRecommendLike()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ComicRecommendLikeBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ComicRecommendLikeBean comicRecommendLikeBean) {
                        adapter.setList(comicRecommendLikeBean.convertToRecommendItem());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(mContext, "获取漫画首页猜你喜欢失败!!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
        return adapter;
    }

    private ComicRecommendChildAdapter initGuoman(RecyclerView recyclerView, ImageView ivIcon) {
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        ComicRecommendChildAdapter adapter = new ComicRecommendChildAdapter(mContext, R.layout.item_object_recommend_child);
        ivIcon.setImageResource(R.drawable.ic_refresh);
        ivIcon.setOnClickListener(v -> service.getComicRecommendGuoman()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ComicRecommendOtherBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ComicRecommendOtherBean comicRecommendOtherBean) {
                        adapter.setList(comicRecommendOtherBean.getData().getData());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(mContext, "获取漫画首页国漫也精彩失败!!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
        return adapter;
    }

    private ComicRecommendChildAdapter initHot(RecyclerView recyclerView, ImageView ivIcon) {
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        ComicRecommendChildAdapter adapter = new ComicRecommendChildAdapter(mContext, R.layout.item_object_recommend_child);
        ivIcon.setImageResource(R.drawable.ic_refresh);
        ivIcon.setOnClickListener(v -> service.getComicRecommendHot()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ComicRecommendOtherBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ComicRecommendOtherBean comicRecommendOtherBean) {
                        adapter.setList(comicRecommendOtherBean.getData().getData());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(mContext, "获取漫画首页热门连载失败!!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
        return adapter;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
