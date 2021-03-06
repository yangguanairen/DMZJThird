package com.sena.dmzjthird.novel.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.databinding.FragmentNovelLatestBinding;
import com.sena.dmzjthird.novel.adapter.NovelLatestAdapter;
import com.sena.dmzjthird.novel.bean.NovelLatestBean;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.RetrofitHelper;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class NovelLatestFragment extends Fragment {

    private FragmentNovelLatestBinding binding;
    private RetrofitService service;
    private NovelLatestAdapter adapter;

    private boolean isLoaded = false;
    private int page = 0;

    public static NovelLatestFragment newInstance() {
        return new NovelLatestFragment();
    }

    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentNovelLatestBinding.inflate(inflater, container, false);

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
        adapter = new NovelLatestAdapter(getContext());
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((a, view, position) -> {
            NovelLatestBean bean = (NovelLatestBean) a.getData().get(position);
            String novelId = bean.getId();
            // ?????????????????????
            IntentUtil.goToNovelInfoActivity(getContext(), novelId);
        });
        adapter.getLoadMoreModule().setOnLoadMoreListener(this::getResponse);
        adapter.getLoadMoreModule().setAutoLoadMore(true);
        adapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);

        binding.refreshLayout.setOnRefreshListener(() -> {
            page = 0;
            getResponse();
        });
    }

    private void lazyLoad() {
        binding.refreshLayout.setRefreshing(true);
        getResponse();
    }

    private void getResponse() {
        service.getNovelLatest(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<NovelLatestBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<NovelLatestBean> beanList) {

                        if (page == 0 && beanList.isEmpty()) {
                            // ????????????
                            onRequestError(true);
                            return ;
                        }
                        onRequestError(false);

                        if (page == 0) {
                            adapter.setList(beanList);
                        } else {
                            adapter.addData(beanList);
                        }
                        if (beanList.isEmpty()) {
                            adapter.getLoadMoreModule().loadMoreEnd();
                        } else {
                            adapter.getLoadMoreModule().loadMoreComplete();
                        }

                        page++;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        // ????????????
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
        binding = null;
        isLoaded = false;
    }
}