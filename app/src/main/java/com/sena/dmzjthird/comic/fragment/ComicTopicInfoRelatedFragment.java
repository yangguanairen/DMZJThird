package com.sena.dmzjthird.comic.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.comic.adapter.ComicTopicRelatedAdapter;
import com.sena.dmzjthird.comic.bean.ComicTopicInfoBean;
import com.sena.dmzjthird.databinding.FragmentComicTopicInfoRelatedBinding;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.RetrofitHelper;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class ComicTopicInfoRelatedFragment extends Fragment {


    private FragmentComicTopicInfoRelatedBinding binding;
    private ComicTopicRelatedAdapter adapter;
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

        initView();

        getResponse();

        return binding.getRoot();
    }


    private void initView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ComicTopicRelatedAdapter(getActivity());
        binding.recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) ->
                IntentUtil.goToComicInfoActivity(getActivity(), ((ComicTopicInfoBean.Comics) adapter.getData().get(position)).getId()));
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
                        if (data.getComics().isEmpty()) {
                            // ????????????
                            return ;
                        }

                        ComicTopicInfoFragment.updateInfo(data.getMobile_header_pic(), data.getTitle(), data.getDescription());
                        callbacks.loadDataFinish(data.getTitle());
                        // ??????recyclerview
                        adapter.setList(data.getComics());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
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