package com.sena.dmzjthird.novel.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.custom.autoBanner.AutoBannerData;
import com.sena.dmzjthird.databinding.FragmentNovelRecommendBinding;
import com.sena.dmzjthird.novel.adapter.NovelRecommendAdapter;
import com.sena.dmzjthird.novel.bean.NovelRecommendBean;
import com.sena.dmzjthird.utils.RetrofitHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NovelRecommendFragment extends Fragment {

    private FragmentNovelRecommendBinding binding;
    private RetrofitService service;
    private NovelRecommendAdapter adapter;

    private boolean isLoaded = false;

    public static NovelRecommendFragment newInstance() {
       return new NovelRecommendFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentNovelRecommendBinding.inflate(inflater, container, false);
        service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);

        initView();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isLoaded) return ;
        isLoaded = true;
        lazyLoad();
    }

    private void initView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NovelRecommendAdapter(getContext());
        binding.recyclerView.setAdapter(adapter);

        binding.refreshLayout.setOnRefreshListener(this::getResponse);
    }

    private void lazyLoad() {
        binding.refreshLayout.setRefreshing(true);
        getResponse();
    }

    private void getResponse() {
        service.getNovelRecommend()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<NovelRecommendBean>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<NovelRecommendBean> beanList) {

                        if (beanList.size() == 0) {
                            // 出错处理
                            Toast.makeText(getContext(), "轻小说首页请求失败!", Toast.LENGTH_SHORT).show();
                            return ;
                        }

                        List<AutoBannerData> bannerDataList = new ArrayList<>();
                        for (NovelRecommendBean.NovelRecommendData data: beanList.get(0).getData()) {
                            AutoBannerData bannerData = new AutoBannerData();
                            bannerData.setObjectId(data.getObj_id());
                            bannerData.setTitle(data.getTitle());
                            bannerData.setCoverUrl(data.getCover());
                            bannerData.setType(1);
                            bannerDataList.add(bannerData);
                        }
                        binding.banner.setDataList(bannerDataList);
                        adapter.setList(beanList.subList(1, beanList.size()));
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        // 出错处理
                        binding.refreshLayout.setRefreshing(false);
                        Toast.makeText(getContext(), "轻小说首页请求失败!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        binding.refreshLayout.setRefreshing(false);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        isLoaded = false;
    }
}