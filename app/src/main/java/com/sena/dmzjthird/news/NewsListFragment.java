package com.sena.dmzjthird.news;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.application.NewsListRes;
import com.sena.dmzjthird.databinding.FragmentNewsListBinding;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.api.NewsApi;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


/**
 * 0 最新
 * 1 动画情报
 * 2 漫画情报
 * 3 轻小说情报
 * 8 美图欣赏
 * 7 游戏资讯
 * 4 动漫周边
 * 5 声优情报
 * 9 漫展情报
 * 6 音乐咨询
 * 10 大杂烩
 */

public class NewsListFragment extends Fragment {

    private static final String ARG_SORT_ID = "sort_id";

    private int mSortId;
    private int page = 1;
    private boolean isLoaded = false;

    private FragmentNewsListBinding binding;
    private NewsListAdapter adapter;

    public static NewsListFragment newInstance(int sortId) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SORT_ID, sortId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSortId = getArguments().getInt(ARG_SORT_ID);
        }
    }

    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewsListBinding.inflate(inflater, container, false);

        initView();

        return binding.getRoot();
    }

    private void initView() {

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewsListAdapter(getContext());
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((a, view, position) -> {
            NewsListRes.NewsListItemResponse data = (NewsListRes.NewsListItemResponse) a.getData().get(position);
            String pageUrl = data.getPageUrl();
            // WebView加载
            LogUtil.e("新闻网页链接: " + pageUrl);
            IntentUtil.goToWebViewActivity(getContext(), data.getArticleId()+"", data.getTitle(), data.getRowPicUrl(), data.getPageUrl());
        });

        adapter.getLoadMoreModule().setOnLoadMoreListener(this::getResponse);
        adapter.getLoadMoreModule().setAutoLoadMore(true);
        adapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);

        binding.refreshLayout.setOnRefreshListener(() -> {
            page = 1;
            getResponse();
        });
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
        getResponse();
    }

    private void getResponse() {
        NewsApi.getNewsList(mSortId, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<NewsListRes.NewsListItemResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<NewsListRes.NewsListItemResponse> dataList) {
                        if (page == 1 && dataList.size() == 0) {
                            onRequestError(true);
                            return ;
                        }
                        onRequestError(false);

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
                    public void onError(@NonNull Throwable e) {
                        binding.refreshLayout.setRefreshing(false);
                        onRequestError(true);
                    }

                    @Override
                    public void onComplete() {
                        binding.refreshLayout.setRefreshing(false);
                    }
                });
    }

    private void onRequestError(boolean isError) {
        binding.error.noData.setVisibility(isError ? View.VISIBLE : View.INVISIBLE);
        binding.recyclerView.setVisibility(isError ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isLoaded = false;
        binding = null;
    }

}