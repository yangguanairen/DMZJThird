package com.sena.dmzjthird.comic.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.comic.adapter.ComicRankComplaintAdapter;
import com.sena.dmzjthird.comic.adapter.ComicRankPopularityAdapter;
import com.sena.dmzjthird.comic.adapter.ComicRankSubscribeAdapter;
import com.sena.dmzjthird.comic.bean.ComicComplaintRankBean;
import com.sena.dmzjthird.comic.bean.ComicPopularityRankBean;
import com.sena.dmzjthird.comic.bean.ComicSubscribeRankBean;
import com.sena.dmzjthird.comic.view.ComicInfoActivity;
import com.sena.dmzjthird.databinding.FragmentComicRankBinding;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.RetrofitHelper;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;


public class ComicRankFragment extends Fragment {

    private FragmentComicRankBinding binding;

    private RetrofitService service;
    private BaseQuickAdapter adapter;
    private int classify = 0;
    private int sort = 0;
    private int time = 0;
    private int page = 0;

    private CompositeDisposable disposable;

    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentComicRankBinding.inflate(inflater, container, false);

        service = RetrofitHelper.getServer(RetrofitService.BASE_ORIGIN_URL);
        disposable = new CompositeDisposable();

        initAdapter();

        initDialog();

        initRefresh();


        return binding.getRoot();
    }

    private void initAdapter() {
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (sort == 1) {
            adapter = new ComicRankComplaintAdapter(getActivity());
        } else if (sort == 2) {
            adapter = new ComicRankSubscribeAdapter(getActivity());
        } else {
            adapter = new ComicRankPopularityAdapter(getActivity());
        }
        binding.recyclerview.setAdapter(adapter);

//        adapter.setOnItemClickListener((adapter, view, position) -> {
//            Intent intent = new Intent(getActivity(), ComicInfoActivity.class);
//            String comicId;
//            if (sort == 1) {
//                comicId = ((ComicComplaintRankBean) adapter.getData().get(position)).getId();
//            } else if (sort == 2) {
//                comicId = ((ComicSubscribeRankBean) adapter.getData().get(position)).getId();
//            } else {
//                comicId = ((ComicPopularityRankBean) adapter.getData().get(position)).getId();
//            }
//            intent.putExtra(getString(R.string.intent_comic_id), comicId);
//            LogUtil.e("ComicRankFragment->ComicInfoActivity");
//            startActivity(intent);
//        });
//
//        adapter.setOnItemLongClickListener(new OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(@androidx.annotation.NonNull BaseQuickAdapter adapter, @androidx.annotation.NonNull View view, int position) {
//                LogUtil.e("Long Click");
//                return false;
//            }
//        });

        adapter.getLoadMoreModule().setOnLoadMoreListener(this::getResponse);
        adapter.getLoadMoreModule().setAutoLoadMore(false);
        adapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);

        page = 0;
        getResponse();
    }

    private void getResponse() {
        if (sort == 1) {
            if (time == 3) {
                time = 2;
            }
            setAdapterData(service.getComplaintRankComic(time, page));
        } else if (sort == 2) {
            setAdapterData(service.getSubscribeRankComic(time, page));
        } else {
            setAdapterData(service.getPopularityRankComic(time, page));
        }
    }

    private <T> void setAdapterData(Observable<List<T>> observable) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<T>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<T> beans) {
                        if (page == 0) {
                            adapter.setList(beans);
                        } else {
                            adapter.addData(beans);
                        }
                        if (beans.size() < 10) {
                            adapter.getLoadMoreModule().loadMoreEnd();
                        } else {
                            adapter.getLoadMoreModule().loadMoreComplete();
                            page++;
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (e instanceof HttpException) {
                            LogUtil.e("HttpError: " + ((HttpException) e).code());
                        } else {
                            LogUtil.e("OtherError: " + e.getMessage());
                        }
                        disposable.clear();
                    }

                    @Override
                    public void onComplete() {
                        disposable.clear();
                    }
                });
    }

    private void initDialog() {
        String[] classifyName = {"全部分类"};
        String[] sortName = {"人气排行", "吐槽排行", "订阅排行"};
        String[] timeName = {"日排行", "周排行", "月排行", "总排行"};
        binding.rankSort.setOnClickListener(v -> createDialog(sortName, 0));
        binding.rankTime.setOnClickListener(v -> createDialog(timeName, 1));
        binding.rankClassify.setOnClickListener(v -> createDialog(classifyName, 2));
    }

    private void createDialog(String[] data, int flag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        int checkedItem;
        if (flag == 0) {
            checkedItem = sort;
        } else if (flag == 1) {
            checkedItem = time;
        } else {
            checkedItem = classify;
        }

        builder.setSingleChoiceItems(data, checkedItem, (dialog, which) -> {
            if (flag == 0) {
                sort = which;
                binding.rankSort.setText(data[which]);
            } else if (flag == 1) {
                time = which;
                binding.rankTime.setText(data[which]);
            } else {

            }
            initAdapter();
            dialog.dismiss();
            System.gc();
        });
        builder.create().show();

    }

    private void initRefresh() {
        binding.refresh.setOnRefreshListener(() -> {
            initAdapter();
            new Handler().postDelayed(() -> binding.refresh.setRefreshing(false), 3000);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
        disposable = null;
        binding = null;
    }
}