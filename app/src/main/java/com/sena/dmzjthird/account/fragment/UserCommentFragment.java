package com.sena.dmzjthird.account.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.account.adapter.UserComicCommentAdapter;
import com.sena.dmzjthird.databinding.FragmentUserCommentBinding;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.RetrofitHelper;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class UserCommentFragment extends Fragment {

    private static final String ARG_TYPE = "arg_type";
    private static final String ARG_UID = "arg_uid";

    private int type;
    private String uid;

    private FragmentUserCommentBinding binding;
    private RetrofitService service;
    private UserComicCommentAdapter adapter;
    private int page = 0;

    public static UserCommentFragment newInstance(int type, String uid) {
        UserCommentFragment fragment = new UserCommentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        args.putString(ARG_UID, uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(ARG_TYPE);
            uid = getArguments().getString(ARG_UID);
        }
    }

    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserCommentBinding.inflate(inflater, container, false);
        service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);

        binding.progress.spin();

        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new UserComicCommentAdapter(getActivity(), type);
        binding.recyclerview.setAdapter(adapter);

        adapter.getLoadMoreModule().setAutoLoadMore(true);
        adapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);

//        uid = "101312820";
//        uid = "100488392";

        if (type == 0) {
            adapter.getLoadMoreModule().setOnLoadMoreListener(() -> getResponse(service.getUserComicComments(uid, page)));
            getResponse(service.getUserComicComments(uid, page));
        } else {
            adapter.getLoadMoreModule().setOnLoadMoreListener(() -> getResponse(service.getUserNovelComments(uid, page)));
            getResponse(service.getUserNovelComments(uid, page));
        }

        return binding.getRoot();
    }

    private <T> void getResponse(Observable<List<T>> observable) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<T>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<T> beans) {
                        binding.progress.stopSpinning();
                        binding.progress.setVisibility(View.GONE);
                        if (page == 0 && beans.size() == 0) {
                            binding.noData.setVisibility(View.VISIBLE);
                            return ;
                        }
                        binding.recyclerview.setVisibility(View.VISIBLE);
                        if (beans.size() == 0) {
                            adapter.getLoadMoreModule().loadMoreEnd();
                        } else {
                            adapter.getLoadMoreModule().loadMoreComplete();
                            page++;
                        }
                        if (page == 0) {
                            adapter.setList(beans);
                        } else {
                            adapter.addData(beans);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogUtil.internetError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}