package com.sena.dmzjthird.comic.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.comic.adapter.ComicCategoryAdapter;
import com.sena.dmzjthird.comic.bean.ComicClassifyCoverBean;
import com.sena.dmzjthird.databinding.FragmentComicCategoryBinding;
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


public class ComicCategoryFragment extends Fragment {

    private FragmentComicCategoryBinding binding;
    private ComicCategoryAdapter adapter;
    private RetrofitService service;

    private boolean isLoaded = false;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentComicCategoryBinding.inflate(inflater, container, false);
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

    private void lazyLoad() {
        getResponse();
    }

    private void initView() {
        binding.recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        adapter = new ComicCategoryAdapter(getActivity());
        binding.recyclerview.setAdapter(adapter);

        adapter.setOnItemClickListener((adapter, view, position) ->
                IntentUtil.goToComicClassifyActivity(getActivity(), ((ComicClassifyCoverBean.Data) adapter.getData().get(position)).getTag_id()));
    }

    private void getResponse() {
        service.getComicCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ComicClassifyCoverBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ComicClassifyCoverBean bean) {
                        if (bean == null) {
                            // 出错处理
                            return ;
                        }
                        adapter.setList(bean.getData());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (e instanceof HttpException) {
                            LogUtil.d("HttpError: " + ((HttpException) e).code());
                        } else {
                            LogUtil.d("OtherError: " + e.getMessage());
                        }

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

}