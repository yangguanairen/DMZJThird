package com.sena.dmzjthird.comic.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.application.ComicRankListRes;
import com.lxj.xpopup.XPopup;
import com.sena.dmzjthird.comic.adapter.ComicRankAdapter;
import com.sena.dmzjthird.databinding.FragmentComicRankBinding;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.api.ComicApi;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class ComicRankFragment extends Fragment {

    private FragmentComicRankBinding binding;

    private ComicRankAdapter adapter;
    private int classify = 0;
    private int sort = 0;
    private int time = 0;
    private int page = 1;

    private boolean isLoaded;

    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentComicRankBinding.inflate(inflater, container, false);

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
        getResponse();
    }

    private void initAdapter() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ComicRankAdapter(getContext());
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((a, view, position) -> {
            ComicRankListRes.ComicRankListItemResponse data = (ComicRankListRes.ComicRankListItemResponse) a.getData().get(position);
            String comicId = String.valueOf(data.getComicId());

            IntentUtil.goToComicInfoActivity(getActivity(), comicId);
        });

        adapter.getLoadMoreModule().setOnLoadMoreListener(this::getResponse);
        adapter.getLoadMoreModule().setAutoLoadMore(true);
        adapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
    }

    private void initView() {

        initAdapter();

        binding.refreshLayout.setOnRefreshListener(() -> {
            page = 1;
            getResponse();
        });

        initDialog();
    }

    private void getResponse() {

        ComicApi.getComicRank(classify, sort, time, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ComicRankListRes.ComicRankListItemResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<ComicRankListRes.ComicRankListItemResponse> dataList) {
                        binding.refreshLayout.setRefreshing(false);
                        if (page == 1 && dataList.isEmpty()) {
                            // ????????????
                            onRequestError(true);
                            return ;
                        }
                        onRequestError(false);

                        if (page == 1) {
                            adapter.setList(dataList);
                            // tag???????????????????????????
                            binding.recyclerView.scrollToPosition(0);
                        } else {
                            adapter.addData(dataList);
                        }

                        if (dataList.isEmpty()) {
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

                    }
                });

    }


    private void initDialog() {
        String[] classifyName = {"????????????"};
        String[] sortName = {"????????????", "????????????", "????????????"};
        String[] timeName = {"?????????", "?????????", "?????????", "?????????"};
        binding.rankClassify.setOnClickListener(v -> showDialog(classifyName, classify, 1));
        binding.rankSort.setOnClickListener(v -> showDialog(sortName, sort, 2));
        binding.rankTime.setOnClickListener(v -> showDialog(timeName, time, 3));
    }

    private void showDialog(String[] data, int currentPosition, int flag) {
        new XPopup.Builder(getContext())
                .borderRadius(10)
                .asCenterList("", data, new int[]{}, currentPosition, (position, text) -> {

                    switch (flag) {
                        case 1:
                            if (classify == position) return ;
                            classify = position;
                            binding.rankClassify.setText(text);
                            break;
                        case 2:
                            if (sort == position) return ;
                            sort = position;
                            binding.rankSort.setText(text);
                            break;
                        case 3:
                            if (time == position) return ;
                            time = position;
                            binding.rankTime.setText(text);
                            break;
                    }

                    page = 1;
                    getResponse();
                })
                .show();
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