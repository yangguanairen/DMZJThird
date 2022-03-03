package com.sena.dmzjthird.comic.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sena.dmzjthird.R;
import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.comic.adapter.ComicRelatedAdapter;
import com.sena.dmzjthird.comic.bean.ComicRelatedBean;
import com.sena.dmzjthird.databinding.FragmentComicRelatedBinding;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.RetrofitHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;


public class ComicRelatedFragment extends Fragment {

    private static final String COMIC_ID = "comic_id";

    private FragmentComicRelatedBinding binding;
    private String comicId;
    private ComicRelatedAdapter adapter;

    private boolean isLoaded = false;

    public static ComicRelatedFragment newInstance(String id) {

        Bundle args = new Bundle();
        args.putString(COMIC_ID, id);

        ComicRelatedFragment fragment = new ComicRelatedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            comicId = getArguments().getString(COMIC_ID);
        }
    }


    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentComicRelatedBinding.inflate(inflater, container, false);

        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ComicRelatedAdapter(getActivity());
        binding.recyclerview.setAdapter(adapter);


        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isLoaded) {
            lazyInit();
            isLoaded = true;
        }
    }

    private void lazyInit() {
        binding.progress.spin();
        RetrofitService service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);
        service.getComicRelated(comicId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ComicRelatedBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ComicRelatedBean bean) {
                        binding.progress.stopSpinning();
                        // 可能增加下拉刷新功能，不一定要去除刷新
                        binding.getRoot().removeView(binding.progress);
                        if (bean.getAuthor_comics() == null || bean.getAuthor_comics().size() == 0) {
                            binding.getRoot().removeView(binding.recyclerview);
                            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                            binding.getRoot().addView(LayoutInflater.from(getContext()).inflate(R.layout.fragment_error, null, false), layoutParams);
                            // ConstraintSet的获取必须在addVIew后面，不然无法约束，视图消失
//                            constraintSet.clone(binding.getRoot());
//                            constraintSet.connect(view.getId(), ConstraintSet.TOP, binding.getRoot().getId(), ConstraintSet.BOTTOM);
//                            constraintSet.applyTo(binding.getRoot());
                            return ;
                        }

                        LogUtil.e("comicId" + comicId);
                        List<ComicRelatedBean.Author_Comics> data = new ArrayList<>(bean.getAuthor_comics());
                        data.add(new ComicRelatedBean.Author_Comics("同类题材", null, bean.getTheme_comics()));
                        adapter.setList(data.subList(0, data.size() - 1));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (e instanceof HttpException) {
                            LogUtil.e("HttpError: " + ((HttpException) e).code());
                        } else {
                            LogUtil.e("OtherError: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isLoaded = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}