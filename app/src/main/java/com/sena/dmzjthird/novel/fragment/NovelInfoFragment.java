package com.sena.dmzjthird.novel.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.application.NovelDetailRes;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.databinding.FragmentNovelInfoBinding;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.TimeUtil;
import com.sena.dmzjthird.utils.api.NovelApi;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NovelInfoFragment extends Fragment {

    private FragmentNovelInfoBinding binding;
    private Callbacks callbacks;

    private static final String ARG_NOVEL_ID = "novel_id";
    private String mNovelId;
    private String mCoverUrl;

    public static NovelInfoFragment newInstance(String novelId) {
        NovelInfoFragment fragment = new NovelInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NOVEL_ID, novelId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callbacks = (Callbacks) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNovelId = getArguments().getString(ARG_NOVEL_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentNovelInfoBinding.inflate(inflater, container, false);

        initView();

        getResponse();

        return binding.getRoot();
    }

    private void initView() {
        binding.cover.setOnClickListener(v -> IntentUtil.goToLargeImageActivity(getContext(), mCoverUrl));
    }

    private void getResponse() {
        NovelApi.getNovelInfo(mNovelId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NovelDetailRes.NovelDetailInfoResponse>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(NovelDetailRes.@io.reactivex.rxjava3.annotations.NonNull NovelDetailInfoResponse data) {

                        mCoverUrl = data.getCover();

                        Glide.with(getContext()).load(data.getCover())
                                .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                                .into(binding.cover);

                        binding.title.setText(data.getName());
                        binding.author.setText(getString(R.string.object_info_author, data.getAuthors()));
                        binding.hitsCount.setText(getString(R.string.object_info_hit_count, data.getHotHits()));
                        binding.subscribeCount.setText(getString(R.string.object_info_subscribe_count, data.getSubscribeNum()));
                        binding.status.setText(getString(R.string.object_info_status, data.getStatus()));
                        binding.updateTime.setText(getString(R.string.object_info_last_update_time, TimeUtil.millConvertToDate(data.getLastUpdateTime() * 1000)));
                        binding.description.setText(data.getIntroduction());

                        callbacks.onLoadInfoEnd(data.getName());
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        // 出错处理
                        callbacks.onLoadInfoEnd(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public interface Callbacks {
        void onLoadInfoEnd(String title);
    }
}