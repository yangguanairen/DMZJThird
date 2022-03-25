package com.sena.dmzjthird.comic.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.comic.adapter.ComicRecommendAdapter;
import com.sena.dmzjthird.comic.bean.ComicRecommendLikeBean;
import com.sena.dmzjthird.comic.bean.ComicRecommendNewBean;
import com.sena.dmzjthird.custom.autoBanner.AutoBannerData;
import com.sena.dmzjthird.databinding.FragmentComicRecommendBinding;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.RetrofitHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class ComicRecommendFragment extends Fragment {

    /**
     * 46: 大图推荐-child1
     * 47: 近期必看-Child1
     * 93: 游戏专区-child1
     * 48: 火热专题-child1
     * 50: 猜你喜欢-child2
     * 51: 大师作品-child1
     * 52: 国漫精彩-child1
     * 53: 美漫事件-child1
     * 54: 热门连载-child1
     * 55: 条漫专区-child1
     * 56: 最新上架-child2
     */

    private FragmentComicRecommendBinding binding;
    private RetrofitService service;

    private ComicRecommendAdapter adapter;
    private final List<ComicRecommendNewBean> list = new ArrayList<>();

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentComicRecommendBinding.inflate(inflater, container, false);
        service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);

        binding.progress.spin();

        initView();
        getResponse();

        return binding.getRoot();
    }

    private void initView() {

        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ComicRecommendAdapter(getContext());
        binding.recyclerview.setAdapter(adapter);

        binding.refreshLayout.setOnRefreshListener(() -> {
            list.clear();
            getResponse();
        });
    }

    private void getResponse() {

        service.getComicRecommend()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ComicRecommendNewBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<ComicRecommendNewBean> beanList) {
                        binding.refreshLayout.setRefreshing(false);
                        if (beanList.size() == 0) {
                            // 出错处理
                            return ;
                        }
                        List<AutoBannerData> bannerDataList = new ArrayList<>();
                        for (ComicRecommendNewBean.ComicRecommendItem item: beanList.get(0).getData()) {
                            AutoBannerData bannerData = new AutoBannerData();
                            bannerData.setTitle(item.getTitle());
                            bannerData.setCoverUrl(item.getCover());
                            // 区分处理
                            bannerDataList.add(bannerData);
                        }
                        binding.banner.setDataList(bannerDataList);

                        for (int i = 1; i < beanList.size(); i++) {
                            setRecommendList(beanList.get(i));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        // 出错处理
                        binding.refreshLayout.setRefreshing(false);
                        binding.progress.stopSpinning();
                        binding.progress.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        service.getComicRecommendLike()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ComicRecommendLikeBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ComicRecommendLikeBean bean) {
                        setRecommendList(bean.convertToRecommendBean());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        // 出错处理
                        binding.progress.stopSpinning();
                        binding.progress.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    // 多线程，使用同步
    // 集成所有请求结果，集中发送给adapter
    private synchronized void setRecommendList(ComicRecommendNewBean bean) {
        list.add(bean);
        LogUtil.e("首页Bean Size: " + list.size());
        if (list.size() != 9) return ;
        list.sort((o1, o2) -> {
            Integer i1 = o1.getSort();
            Integer i2 = o2.getSort();
            return i1.compareTo(i2);
        });
        adapter.setList(list);
        binding.banner.setVisibility(View.VISIBLE);
        binding.progress.stopSpinning();
        binding.progress.setVisibility(View.GONE);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }




}