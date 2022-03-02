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
import com.sena.dmzjthird.comic.bean.ComicLatestBean;
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
    private int mType = 100;

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

        binding.selectAll.setOnClickListener(v -> registerTagClick(binding.selectAll, 100));
        binding.selectOriginal.setOnClickListener(v -> registerTagClick(binding.selectOriginal, 1));
        binding.selectTranslate.setOnClickListener(v -> registerTagClick(binding.selectTranslate, 0));
    }

    private void initAdapter() {
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ComicLatestAdapter(getActivity());
        binding.recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener((a, view, position) -> {
//            Intent intent = new Intent(getActivity(), ComicInfoActivity.class);
//            intent.putExtra(getString(R.string.intent_comic_id), ((ComicLatestBean) adapter.getData().get(position)).getId());
//            startActivity(intent);

            IntentUtil.goToComicInfoActivity(getActivity(), ((ComicLatestBean) a.getData().get(position)).getId());
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
            binding.selectAll.setTextColor(getContext().getColor(R.color.theme_blue));
            binding.selectAll.setBackgroundResource(R.drawable.shape_object_update_tag);
        } else {
            binding.selectAll.setTextColor(Color.BLACK);
            binding.selectAll.setBackgroundColor(Color.WHITE);
        }
        if (currentSelectTag == binding.selectOriginal) {
            binding.selectOriginal.setTextColor(getContext().getColor(R.color.theme_blue));
            binding.selectOriginal.setBackgroundResource(R.drawable.shape_object_update_tag);
        } else {
            binding.selectOriginal.setTextColor(Color.BLACK);
            binding.selectOriginal.setBackgroundColor(Color.WHITE);
        }
        if (currentSelectTag == binding.selectTranslate) {
            binding.selectTranslate.setTextColor(getContext().getColor(R.color.theme_blue));
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
                            return ;
                        }
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
                    }

                    @Override
                    public void onComplete() {

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