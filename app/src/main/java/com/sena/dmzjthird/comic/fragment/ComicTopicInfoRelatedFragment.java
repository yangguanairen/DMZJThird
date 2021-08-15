package com.sena.dmzjthird.comic.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sena.dmzjthird.R;
import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.comic.adapter.ComicTopicInfoAdapter;
import com.sena.dmzjthird.comic.bean.ComicTopicInfoBean;
import com.sena.dmzjthird.comic.view.ComicInfoActivity;
import com.sena.dmzjthird.databinding.FragmentComicTopicInfoRelatedBinding;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.RetrofitHelper;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class ComicTopicInfoRelatedFragment extends Fragment {


    private FragmentComicTopicInfoRelatedBinding binding;
    private ComicTopicInfoAdapter adapter;
    private Callbacks callbacks;

    private String topicId;
    private static final String ARG_ID = "arg_topic_id";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            topicId = getArguments().getString(ARG_ID);
        }
    }

    public static ComicTopicInfoRelatedFragment newInstance(String id) {

        Bundle args = new Bundle();

        ComicTopicInfoRelatedFragment fragment = new ComicTopicInfoRelatedFragment();
        args.putString(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = FragmentComicTopicInfoRelatedBinding.inflate(inflater, container, false);

        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ComicTopicInfoAdapter(getActivity());
        binding.recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(getActivity(), ComicInfoActivity.class);
            intent.putExtra(getString(R.string.intent_comic_id), ((ComicTopicInfoBean.Comics) adapter.getData().get(position)).getId());
            startActivity(intent);
        });

        getResponse();

        return binding.getRoot();
    }



    private void getResponse() {
        RetrofitService service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);
        service.getTopicInfo(topicId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ComicTopicInfoBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ComicTopicInfoBean bean) {
                        ComicTopicInfoBean.Data data = bean.getData();
                        ComicTopicInfoFragment.updateInfo(data.getMobile_header_pic(), data.getTitle(), data.getDescription());
                        callbacks.loadDataFinish(data.getTitle());
//                        callbacks.loadDataFinish(data.getMobile_header_pic(), data.getTitle(), data.getDescription());
                        // 设置recyclerview
                        adapter.setList(data.getComics());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogUtil.internetError(e);
                        callbacks.loadDataFinish(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onAttach(@androidx.annotation.NonNull Context context) {
        super.onAttach(context);
        callbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    public interface Callbacks {

        void loadDataFinish(String title);
    }
}