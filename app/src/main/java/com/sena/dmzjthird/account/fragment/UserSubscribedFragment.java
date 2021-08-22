package com.sena.dmzjthird.account.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sena.dmzjthird.R;
import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.account.adapter.UserSubscribedAdapter;
import com.sena.dmzjthird.account.bean.UserSubscribedBean;
import com.sena.dmzjthird.comic.view.ComicInfoActivity;
import com.sena.dmzjthird.databinding.FragmentUserSubscribedBinding;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.RetrofitHelper;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;


public class UserSubscribedFragment extends Fragment {

    private static final String ARG_TYPE = "arg_type";
    private static final String ARG_UID = "arg_uid";

    private int type;
    private String uid;
    private int page = 0;

    private FragmentUserSubscribedBinding binding;
    private RetrofitService service;
    private UserSubscribedAdapter adapter;


    public static UserSubscribedFragment newInstance(int type, String uid) {
        UserSubscribedFragment fragment = new UserSubscribedFragment();
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
        binding = FragmentUserSubscribedBinding.inflate(inflater, container, false);
        service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);

        binding.progress.spin();

        binding.recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        adapter = new UserSubscribedAdapter(getActivity());
        binding.recyclerview.setAdapter(adapter);

        adapter.setOnItemClickListener((adapter, view, position) -> {
            if (type == 0) {
                IntentUtil.goToComicInfoActivity(getActivity(), ((UserSubscribedBean) adapter.getData().get(position)).getObj_id());
            } else {
                Toast.makeText(getActivity(), "跳转: 小说Info页面, 没写完", Toast.LENGTH_SHORT).show();
            }

        });
        adapter.getLoadMoreModule().setOnLoadMoreListener(this::getResponse);
        adapter.getLoadMoreModule().setAutoLoadMore(true);
        adapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);

        getResponse();

        return binding.getRoot();
    }

    private void getResponse() {
        service.getUserSubscribed(type, uid, page, uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<UserSubscribedBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<UserSubscribedBean> beans) {
                        binding.progress.stopSpinning();
                        binding.progress.setVisibility(View.GONE);

                        if (beans.size() == 0) {
                            // 空数据处理
                            binding.noData.setVisibility(View.VISIBLE);
                            return;
                        }
                        binding.recyclerview.setVisibility(View.VISIBLE);
                        if (beans.size() < 30) {
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
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}