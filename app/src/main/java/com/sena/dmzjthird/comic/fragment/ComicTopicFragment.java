package com.sena.dmzjthird.comic.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.comic.adapter.ComicTopicAdapter;
import com.sena.dmzjthird.comic.bean.ComicTopicBean;
import com.sena.dmzjthird.databinding.FragmentComicTopicBinding;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.RetrofitHelper;

import org.jetbrains.annotations.NotNull;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;


public class ComicTopicFragment extends Fragment {

    private FragmentComicTopicBinding binding;
    private ComicTopicAdapter adapter;
    private RetrofitService service;

    private int page = 0;
    private boolean isLoaded = false;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentComicTopicBinding.inflate(inflater, container, false);
        service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);


        initView();

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

    private void initView() {
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ComicTopicAdapter(getActivity());
        binding.recyclerview.setAdapter(adapter);

        adapter.setOnItemClickListener((a, view, position) ->
                IntentUtil.goToComicTopicInfoActivity(getActivity(), ((ComicTopicBean.Data) a.getData().get(position)).getId()));

        adapter.getLoadMoreModule().setOnLoadMoreListener(this::getResponse);
        adapter.getLoadMoreModule().setAutoLoadMore(true);
        adapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(true);

        binding.refreshLayout.setOnRefreshListener(() -> {
            page = 0;
            getResponse();
        });
    }

    private void getResponse() {
        service.getComicTopic(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ComicTopicBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ComicTopicBean bean) {
//                        if ((beans == null && page == 0) || beans != null && page == 0 && beans.size() == 0) {
//                            binding.tvNoData.setVisibility(View.VISIBLE);
//                            binding.recyclerViewRank.setVisibility(View.GONE);
//                            Toast.makeText(getApplicationContext(), "没有数据了", Toast.LENGTH_LONG).show();
//                        }
                        binding.refreshLayout.setRefreshing(false);
                        if (page == 0 && bean.getData().size() == 0) {
                            // 出错处理
                            return ;
                        }
                        if (page == 0) {
                            adapter.setList(bean.getData());
                        } else {
                            adapter.addData(bean.getData());
                        }
                        if (bean.getData().size() == 0) {
                            adapter.getLoadMoreModule().loadMoreEnd();
                        } else {
                            adapter.getLoadMoreModule().loadMoreComplete();
                        }
                        page++;

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        binding.refreshLayout.setRefreshing(false);
                        if (e instanceof HttpException) {
                            LogUtil.d("HttpError: " + ((HttpException) e).code() );
                        } else {
                            LogUtil.d("OtherError: " + e.getMessage());
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