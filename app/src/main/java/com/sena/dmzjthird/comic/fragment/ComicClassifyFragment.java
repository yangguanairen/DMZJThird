package com.sena.dmzjthird.comic.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sena.dmzjthird.R;
import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.comic.adapter.ComicClassifyCoverAdapter;
import com.sena.dmzjthird.comic.bean.ComicClassifyCoverBean;
import com.sena.dmzjthird.comic.view.ComicClassifyActivity;
import com.sena.dmzjthird.databinding.FragmentComicClassifyBinding;
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


public class ComicClassifyFragment extends Fragment {

    private FragmentComicClassifyBinding binding;
    private ComicClassifyCoverAdapter adapter;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentComicClassifyBinding.inflate(inflater, container, false);

        binding.recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        adapter = new ComicClassifyCoverAdapter(getActivity());
        binding.recyclerview.setAdapter(adapter);

        adapter.setOnItemClickListener((adapter, view, position) ->
                IntentUtil.goToComicClassifyActivity(getActivity(), ((ComicClassifyCoverBean.Data) adapter.getData().get(position)).getTag_id()));

        binding.refresh.setOnRefreshListener(() -> {
            new Handler().postDelayed(() -> binding.refresh.setRefreshing(false), 5000);
        });

        getResponse();

        return binding.getRoot();
    }

    private void getResponse() {
        RetrofitService service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);
        service.getComicClassifyCover()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ComicClassifyCoverBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ComicClassifyCoverBean bean) {
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