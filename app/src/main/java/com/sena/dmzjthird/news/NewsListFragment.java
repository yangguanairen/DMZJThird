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

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
        binding.refreshLayout.setRefreshing(true);
        lazyLoad();
    }

    private void lazyLoad() {
        getResponse();
    }

    private void getResponse() {
        NewsApi.getNewsList(mSortId, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsList -> {
                    if (binding.refreshLayout.isRefreshing()) {
                        binding.refreshLayout.setRefreshing(false);
                    }
                    if (newsList == null) {
                        // 出错处理
                        adapter.getLoadMoreModule().loadMoreEnd();
                        return ;
                    }

                    if (page == 1) {
                        adapter.setList(newsList);
                    } else {
                        adapter.addData(newsList);
                    }
                    adapter.getLoadMoreModule().loadMoreComplete();
                    page++;
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isLoaded = false;
        binding = null;
    }

}