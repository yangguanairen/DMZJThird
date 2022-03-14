package com.sena.dmzjthird.news;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.application.NewsListRes;
import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.custom.autoBanner.AutoBannerData;
import com.sena.dmzjthird.databinding.FragmentNewsLatestBinding;
import com.sena.dmzjthird.news.bean.NewsBannerBean;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.RetrofitHelper;
import com.sena.dmzjthird.utils.api.NewsApi;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NewsLatestFragment extends Fragment {

    private FragmentNewsLatestBinding binding;
    private RetrofitService service;
    private NewsListAdapter adapter;

    private int page = 1;

    private boolean isLoaded = false;

    public static NewsLatestFragment newInstance() {
        return new NewsLatestFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewsLatestBinding.inflate(inflater, container, false);
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

    private void lazyLoad() {
        binding.refreshLayout.setRefreshing(true);
        initBanner();
        getResponse();
    }

    private void initView() {

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewsListAdapter(getContext());
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((a, view, position) -> {
            NewsListRes.NewsListItemResponse data = (NewsListRes.NewsListItemResponse) a.getData().get(position);
            String pageUrl = data.getPageUrl();
            // WebView加载链接
            LogUtil.e("新闻网页链接: " + pageUrl);
            IntentUtil.goToWebViewActivity(getContext(), data.getArticleId() + "", data.getTitle(), data.getRowPicUrl(), data.getPageUrl());
        });

        adapter.getLoadMoreModule().setOnLoadMoreListener(this::getResponse);
        adapter.getLoadMoreModule().setAutoLoadMore(true);
        adapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);

        binding.refreshLayout.setOnRefreshListener(() -> {
            page = 1;
            getResponse();
        });

    }

    private void initBanner() {

        service.getNewsBanner()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NewsBannerBean>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull NewsBannerBean bean) {
                        if (bean.getData().size() == 0 || bean.getCode() != 0) {
                            // 出错处理
                            LogUtil.e("新闻轮播图加载失败!!");
                            return ;
                        }

                        List<AutoBannerData> bannerList = new ArrayList<>();
                        for (NewsBannerBean.NewsBannerData b: bean.getData()) {
                            AutoBannerData data = new AutoBannerData();
                            data.setObjectId(b.getObject_id());
                            data.setTitle(b.getTitle());
                            data.setCoverUrl(b.getPic_url());
                            data.setType(2);
                            data.setPageUrl(b.getObject_url());
                            bannerList.add(data);
                        }

                        binding.banner.setDataList(bannerList);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        // 出错处理

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private void getResponse() {

        NewsApi.getNewsList(0, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<NewsListRes.NewsListItemResponse>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<NewsListRes.NewsListItemResponse> dataList) {
                        binding.refreshLayout.setRefreshing(false);

                        if (page == 1 && dataList.size() == 0) {
                            // 出错处理
                            return ;
                        }

                        if (page == 1) {
                            adapter.setList(dataList);
                        } else {
                            adapter.addData(dataList);
                        }
                        if (dataList.size() == 0) {
                            adapter.getLoadMoreModule().loadMoreEnd();
                        } else {
                            adapter.getLoadMoreModule().loadMoreComplete();
                        }
                        page++;
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        binding.refreshLayout.setRefreshing(false);
                        // 出错处理
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void openUrlInWebView(String url) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        isLoaded = false;
    }

}