package com.sena.dmzjthird.novel.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.databinding.FragmentNovelCategoryBinding;
import com.sena.dmzjthird.novel.adapter.NovelCategoryAdapter;
import com.sena.dmzjthird.novel.bean.NovelCategoryBean;
import com.sena.dmzjthird.novel.view.NovelFilterActivity;
import com.sena.dmzjthird.utils.RetrofitHelper;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NovelCategoryFragment extends Fragment {

    private FragmentNovelCategoryBinding binding;
    private RetrofitService service;
    private NovelCategoryAdapter adapter;

    private boolean isLoaded = false;

    public static NovelCategoryFragment newInstance() {
        return new NovelCategoryFragment();
    }

    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNovelCategoryBinding.inflate(inflater, container, false);
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

        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new NovelCategoryAdapter(getContext());
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((a, view, position) -> {
            NovelCategoryBean bean = (NovelCategoryBean) a.getData().get(position);
            String selectTagName = bean.getTitle();
            Intent intent = new Intent(getContext(), NovelFilterActivity.class);
            intent.putExtra("selectTagName", selectTagName);
            startActivity(intent);
        });

    }

    private void lazyLoad() {
        getResponse();
    }

    private void getResponse() {
        service.getNovelCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<NovelCategoryBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<NovelCategoryBean> beanList) {
                        if (beanList.size() == 0) {
                            // 出错处理
                            return ;
                        }
                        adapter.setList(beanList);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        // 出错处理
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        isLoaded = false;
    }
}