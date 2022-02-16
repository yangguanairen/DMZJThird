package com.sena.dmzjthird.comic.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.comic.adapter.ComicLatestAdapter;
import com.sena.dmzjthird.comic.bean.ComicLatestBean;
import com.sena.dmzjthird.databinding.FragmentComicLatestBinding;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.RetrofitHelper;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class ComicLatestFragment extends Fragment {

    private FragmentComicLatestBinding binding;
    private ComicLatestAdapter adapter;
    private RetrofitService service;

    private int page = 0;

    private boolean isLoaded = false;

    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentComicLatestBinding.inflate(inflater, container, false);
        service = RetrofitHelper.getServer(RetrofitService.BASE_ORIGIN_URL);

        initRecyclerView();
        initRefreshLayout();

        getResponse();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isLoaded) {
            isLoaded = true;
            binding.refreshLayout.setRefreshing(true);
            getResponse();
        }
    }

    private void initRecyclerView() {
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ComicLatestAdapter(getActivity());
        binding.recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {
//            Intent intent = new Intent(getActivity(), ComicInfoActivity.class);
//            intent.putExtra(getString(R.string.intent_comic_id), ((ComicLatestBean) adapter.getData().get(position)).getId());
//            startActivity(intent);
            IntentUtil.goToComicInfoActivity(getActivity(), ((ComicLatestBean) adapter.getData().get(position)).getId());
        });

        adapter.getLoadMoreModule().setOnLoadMoreListener(this::getResponse);
        adapter.getLoadMoreModule().setAutoLoadMore(true);
        adapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
    }

    private void initRefreshLayout() {
        binding.refreshLayout.setOnRefreshListener(() -> {
            page = 0;
            getResponse();
        });
    }

    private void getResponse() {
        service.getLatestComic(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ComicLatestBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<ComicLatestBean> beans) {
                        if (beans.size() < 10) {
                            adapter.getLoadMoreModule().loadMoreEnd();
                        } else {
                            adapter.getLoadMoreModule().loadMoreComplete();
                            page++;
                        }
                        if (page == 0) {
                            adapter.setList(beans);
                        } else {
                            adapter.addData(beans);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (binding.refreshLayout.isRefreshing()) {
                            binding.refreshLayout.setRefreshing(false);
                        }

                        if (e instanceof HttpException) {
                            LogUtil.e("HttpError: " + ((HttpException) e).code());
                        } else {
                            LogUtil.e("OtherError: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (binding.refreshLayout.isRefreshing()) {
                            binding.refreshLayout.setRefreshing(false);
                        }
                    }
                });
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isLoaded = false;
    }
}