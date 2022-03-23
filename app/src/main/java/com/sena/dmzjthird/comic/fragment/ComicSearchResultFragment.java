package com.sena.dmzjthird.comic.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.comic.adapter.ComicSearchResultAdapter;
import com.sena.dmzjthird.comic.bean.ComicSearchResultBean;
import com.sena.dmzjthird.databinding.FragmentComicSearchResultBinding;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.RetrofitHelper;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ComicSearchResultFragment extends Fragment {

    private static final String ARG_QUERY = "arg_query";
    private String query;

    private FragmentComicSearchResultBinding binding;
    private RetrofitService service;
    private ComicSearchResultAdapter adapter;
    private int page = 0;

    public static ComicSearchResultFragment newInstance(String query) {
        ComicSearchResultFragment fragment = new ComicSearchResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUERY, query);
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentComicSearchResultBinding.inflate(inflater, container, false);
        service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);

        initView();
        getResponse();

        return binding.getRoot();
    }

    private void initView() {
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ComicSearchResultAdapter(getActivity());
        binding.recyclerview.setAdapter(adapter);

        adapter.setOnItemClickListener((a, view, position) -> {
            ComicSearchResultBean bean = (ComicSearchResultBean) a.getData().get(position);
            String comicId = bean.getId();
            IntentUtil.goToComicInfoActivity(getActivity(), comicId);
        });
        adapter.getLoadMoreModule().setOnLoadMoreListener(this::getResponse);
        adapter.getLoadMoreModule().setAutoLoadMore(true);
        adapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
    }

    private void getResponse() {
        service.getComicSearchResult(query, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ComicSearchResultBean>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<ComicSearchResultBean> beans) {

                        if (page == 0 && beans.size() == 0) {
                            onErrorAppear(true);
                            return ;
                        }
                        onErrorAppear(false);
                        if (beans.size() == 0) {
                            adapter.getLoadMoreModule().loadMoreEnd();
                        } else {
                            adapter.getLoadMoreModule().loadMoreComplete();
                        }
                        if (page == 0) {
                            adapter.setList(beans);
                        } else {
                            adapter.addData(beans);
                        }
                        page++;

                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        LogUtil.internetError(e);
                        onErrorAppear(true);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void onErrorAppear(boolean isError) {
        binding.recyclerview.setVisibility(isError ? View.INVISIBLE : View.VISIBLE);
        binding.error.noData.setVisibility(isError ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}