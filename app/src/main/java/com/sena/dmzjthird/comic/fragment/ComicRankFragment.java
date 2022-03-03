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
    private int page = 0;

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
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ComicRankAdapter(getContext());
        binding.recyclerview.setAdapter(adapter);

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
            page = 0;
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
                        if (page == 0 && dataList.isEmpty()) {
                            // 出错处理
                            return ;
                        }

                        if (page == 0) {
                            adapter.setList(dataList);
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
                        // 出错处理
                        binding.refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    private void initDialog() {
        String[] classifyName = {"全部分类"};
        String[] sortName = {"人气排行", "吐槽排行", "订阅排行"};
        String[] timeName = {"日排行", "周排行", "月排行", "总排行"};
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

                    page = 0;
                    getResponse();
                })
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        isLoaded = false;
    }
}