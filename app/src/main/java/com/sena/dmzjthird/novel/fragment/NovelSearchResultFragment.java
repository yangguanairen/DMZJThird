package com.sena.dmzjthird.novel.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.databinding.FragmentNovelSearchResultBinding;
import com.sena.dmzjthird.novel.adapter.NovelSearchResultAdapter;
import com.sena.dmzjthird.novel.bean.NovelSearchBean;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.RetrofitHelper;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NovelSearchResultFragment extends Fragment {

    private static final String ARG_QUERY = "query";
    private String query;
    private int page = 0;

    private FragmentNovelSearchResultBinding binding;
    private RetrofitService service;
    private NovelSearchResultAdapter adapter;

    public static NovelSearchResultFragment newInstance(String s) {
        NovelSearchResultFragment fragment = new NovelSearchResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUERY, s);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            query = getArguments().getString(ARG_QUERY);
        }
    }

    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNovelSearchResultBinding.inflate(inflater, container, false);
        service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);

        initView();

        getResponse();

        return binding.getRoot();
    }

    private void initView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NovelSearchResultAdapter(getContext());
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((a, view, position) -> {
            NovelSearchBean bean = (NovelSearchBean) a.getData().get(position);
            String novelId = bean.getId();
            IntentUtil.goToNovelInfoActivity(getContext(), novelId);
        });
        adapter.getLoadMoreModule().setOnLoadMoreListener(this::getResponse);
        adapter.getLoadMoreModule().setAutoLoadMore(true);
        adapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
    }

    private void getResponse() {
        service.getNovelSearch(query, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<NovelSearchBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<NovelSearchBean> beanList) {
                        if (page == 0 && beanList.isEmpty()) {
                            // 出错处理
                            onErrorAppear(true);
                            return ;
                        }
                        onErrorAppear(false);

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
                        // 出错处理
                        onErrorAppear(true);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void onErrorAppear(boolean isError) {
        binding.recyclerView.setVisibility(isError ? View.INVISIBLE : View.VISIBLE);
        binding.noData.setVisibility(isError ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}