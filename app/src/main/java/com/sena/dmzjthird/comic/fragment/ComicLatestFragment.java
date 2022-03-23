package com.sena.dmzjthird.comic.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.application.ComicUpdateListRes;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.comic.adapter.ComicLatestAdapter;
import com.sena.dmzjthird.databinding.FragmentComicLatestBinding;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.api.ComicApi;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ComicLatestFragment extends Fragment {

    private FragmentComicLatestBinding binding;
    private ComicLatestAdapter adapter;

    private boolean isLoaded = false;
    private int page = 1;
    // 分类
    // 全部漫画=100, 原创漫画=10, 译制漫画=0
    private final int TYPE_ALL = 100;
    private final int TYPE_ORIGINAL = 1;
    private final int TYPE_TRANSLATION = 0;
    private int mType = TYPE_ALL;

    private TextView currentSelectTag;

    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentComicLatestBinding.inflate(inflater, container, false);

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

    private void initView() {

        currentSelectTag = binding.selectAll;

        initAdapter();

        binding.refreshLayout.setOnRefreshListener(() -> {
            page = 0;
            getResponse();
        });

        binding.selectAll.setOnClickListener(v -> registerTagClick(binding.selectAll, TYPE_ALL));
        binding.selectOriginal.setOnClickListener(v -> registerTagClick(binding.selectOriginal, TYPE_ORIGINAL));
        binding.selectTranslate.setOnClickListener(v -> registerTagClick(binding.selectTranslate, TYPE_TRANSLATION));
    }

    private void initAdapter() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ComicLatestAdapter(getActivity());
        binding.recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((a, view, position) -> {
            ComicUpdateListRes.ComicUpdateListItemResponse data = (ComicUpdateListRes.ComicUpdateListItemResponse) a.getData().get(position);
            String comicId = String.valueOf(data.getComicId());
            IntentUtil.goToComicInfoActivity(getActivity(), comicId);
        });

        adapter.getLoadMoreModule().setOnLoadMoreListener(this::getResponse);
        adapter.getLoadMoreModule().setAutoLoadMore(true);
        adapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
    }

    private void registerTagClick(TextView textView, int type) {
        if (currentSelectTag == textView) return ;
        currentSelectTag = textView;
        mType = type;
        page = 1;
        if (currentSelectTag == binding.selectAll) {
            binding.selectAll.setTextColor(getResources().getColor(R.color.theme_blue, null));
            binding.selectAll.setBackgroundResource(R.drawable.shape_object_update_tag);
        } else {
            binding.selectAll.setTextColor(Color.BLACK);
            binding.selectAll.setBackgroundColor(Color.WHITE);
        }
        if (currentSelectTag == binding.selectOriginal) {
            binding.selectOriginal.setTextColor(getResources().getColor(R.color.theme_blue, null));
            binding.selectOriginal.setBackgroundResource(R.drawable.shape_object_update_tag);
        } else {
            binding.selectOriginal.setTextColor(Color.BLACK);
            binding.selectOriginal.setBackgroundColor(Color.WHITE);
        }
        if (currentSelectTag == binding.selectTranslate) {
            binding.selectTranslate.setTextColor(getResources().getColor(R.color.theme_blue, null));
            binding.selectTranslate.setBackgroundResource(R.drawable.shape_object_update_tag);
        } else {
            binding.selectTranslate.setTextColor(Color.BLACK);
            binding.selectTranslate.setBackgroundColor(Color.WHITE);
        }
        initAdapter();
        getResponse();
    }

    private void getResponse() {

        ComicApi.getComicUpdate(mType, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ComicUpdateListRes.ComicUpdateListItemResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<ComicUpdateListRes.ComicUpdateListItemResponse> dataList) {
                        binding.refreshLayout.setRefreshing(false);
                        if (page == 1 && dataList.size() == 0) {
                            // 出错处理
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
                        // 出错处理
                        onRequestError(true);
                    }

                    @Override
                    public void onComplete() {

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