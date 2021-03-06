package com.sena.dmzjthird.comic.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.comic.adapter.ComicRelatedAdapter;
import com.sena.dmzjthird.comic.bean.ComicRelatedBean;
import com.sena.dmzjthird.databinding.FragmentComicRelatedBinding;
import com.sena.dmzjthird.utils.RetrofitHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


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

        initView();


        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isLoaded) return ;
        isLoaded = true;
        lazyInit();
    }

    private void initView() {

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ComicRelatedAdapter(getActivity());
        binding.recyclerView.setAdapter(adapter);

        binding.refreshLayout.setOnRefreshListener(this::getResponse);

    }

    private void getResponse() {
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
                        if (bean.getAuthor_comics().isEmpty()) {
                            onRequestError(false);
                            return ;
                        }
                        onRequestError(false);

                        List<ComicRelatedBean.Author_Comics> data = new ArrayList<>(bean.getAuthor_comics());
                        data.add(new ComicRelatedBean.Author_Comics("????????????", null, bean.getTheme_comics()));
                        adapter.setList(data.subList(0, data.size() - 1));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        binding.refreshLayout.setRefreshing(false);
                        onRequestError(true);
                    }

                    @Override
                    public void onComplete() {
                        binding.refreshLayout.setRefreshing(false);
                    }
                });
    }

    private void onRequestError(boolean isError) {
        binding.error.noData.setVisibility(isError ? View.VISIBLE : View.INVISIBLE);
        binding.recyclerView.setVisibility(isError ? View.INVISIBLE : View.VISIBLE);
    }

    private void lazyInit() {
        binding.refreshLayout.setRefreshing(true);
        getResponse();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        isLoaded = false;
    }

}