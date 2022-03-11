package com.sena.dmzjthird.novel.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.application.NovelChapterRes;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.databinding.FragmentNovelChapterBinding;
import com.sena.dmzjthird.novel.adapter.NovelChapterAdapter;
import com.sena.dmzjthird.utils.api.NovelApi;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class NovelChapterFragment extends Fragment {

    private FragmentNovelChapterBinding binding;

    private static final String ARG_NOVEL_ID = "novel_id";
    private String mNovelId;
    private String mNovelName;
    private String mNovelCover;
    private boolean isLoaded = false;

    public static NovelChapterFragment newInstance(String novelId) {
        NovelChapterFragment fragment = new NovelChapterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NOVEL_ID, novelId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNovelId = getArguments().getString(ARG_NOVEL_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentNovelChapterBinding.inflate(inflater, container, false);

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

    }


    private void lazyLoad() {
        getResponse();
    }

    private void getResponse() {
        NovelApi.getNovelChapter(mNovelId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<NovelChapterRes.NovelChapterVolumeResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<NovelChapterRes.NovelChapterVolumeResponse> dataList) {
                        if (dataList.size() == 0) {
                            // 出错处理
                            return ;
                        }
                        binding.noData.setVisibility(View.INVISIBLE);
                        binding.recyclerView.setVisibility(View.VISIBLE);
                        NovelChapterAdapter adapter = new NovelChapterAdapter(getContext(), dataList, mNovelId, NovelInfoFragment.getNovelName(), NovelInfoFragment.getNovelCover());
                        binding.recyclerView.setAdapter(adapter);
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