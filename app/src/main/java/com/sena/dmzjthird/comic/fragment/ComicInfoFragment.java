package com.sena.dmzjthird.comic.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.application.ComicDetailRes;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.comic.adapter.ComicInfoAdapter;
import com.sena.dmzjthird.databinding.FragmentComicInfoBinding;
import com.sena.dmzjthird.utils.GlideUtil;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.TimeUtil;
import com.sena.dmzjthird.utils.api.ComicApi;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ComicInfoFragment extends Fragment {

    private static final String COMIC_ID = "comic_id";

    private Callbacks mCallbacks;

    private FragmentComicInfoBinding binding;
    private String comicId;
    private ComicInfoAdapter adapter;

    private String coverUrl = "";

    public interface Callbacks {
        void loadingDataFinish(String title, String cover, String author);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public static ComicInfoFragment newInstance(String id) {

        Bundle args = new Bundle();
        args.putString(COMIC_ID, id);

        ComicInfoFragment fragment = new ComicInfoFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentComicInfoBinding.inflate(inflater, container, false);



        binding.cover.setOnClickListener(v -> IntentUtil.goToLargeImageActivity(getActivity(), coverUrl));


        getResponse();

        return binding.getRoot();
    }

    private void getResponse() {

        Observer<ComicDetailRes.ComicDetailInfoResponse> observer = new Observer<ComicDetailRes.ComicDetailInfoResponse>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

            }

            @Override
            public void onNext(ComicDetailRes.@io.reactivex.rxjava3.annotations.NonNull ComicDetailInfoResponse data) {

                binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter = new ComicInfoAdapter(getContext(), data.getId() + "", data.getCover(), data.getTitle());
                binding.recyclerview.setAdapter(adapter);

                coverUrl = data.getCover() == null ? "" : data.getCover();
                GlideUtil.loadImageWithCookie(getActivity(), coverUrl, binding.cover);

                binding.title.setText(data.getTitle());

                StringBuilder sb = new StringBuilder();
                String authors;
                for (ComicDetailRes.ComicDetailTypeItemResponse t: data.getAuthorsList()) {
                    if (data.getAuthorsList().indexOf(t) == data.getAuthorsList().size() - 1) {
                        sb.append(t.getTagName());
                    } else {
                        sb.append(t.getTagName()).append("/");
                    }
                }
                authors = sb.toString();
                binding.author.setText(authors);

                binding.updateTime.setText(TimeUtil.millConvertToDate(data.getLastUpdatetime() * 1000));

                binding.description.setText(data.getDescription());

                adapter.setList(data.getChaptersList());

                // 设置tag
                for (ComicDetailRes.ComicDetailTypeItemResponse t: data.getStatusList()) {
                    addTagView(t.getTagName(), t.getTagId() + "", binding.tag1);
                }
                for (ComicDetailRes.ComicDetailTypeItemResponse t: data.getTypesList()) {
                    addTagView(t.getTagName(), t.getTagId() + "", binding.tag2);
                }

                mCallbacks.loadingDataFinish(data.getTitle(), data.getCover(), authors);
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                mCallbacks.loadingDataFinish(null, null, null);
            }

            @Override
            public void onComplete() {

            }
        };

        ComicApi.getComicInfo(comicId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private void addTagView(String tagName, String tagId, LinearLayout targetView) {
        TextView textView = new TextView(getActivity());
        textView.setText(tagName);
        textView.setBackgroundResource(R.drawable.shape_filter_tag);
        textView.setPadding(20, 10, 20, 10);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.rightMargin = 20;
        textView.setLayoutParams(params);

        textView.setOnClickListener(v ->
                IntentUtil.goToComicClassifyActivity(getActivity(), String.valueOf(tagId)));

        targetView.addView(textView);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}