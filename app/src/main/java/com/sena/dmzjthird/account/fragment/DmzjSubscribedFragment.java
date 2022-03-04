package com.sena.dmzjthird.account.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.account.adapter.DmzjUserSubscribedAdapter;
import com.sena.dmzjthird.account.bean.DmzjUserSubscribedBean;
import com.sena.dmzjthird.databinding.FragmentUserSubscribedBinding;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.RetrofitHelper;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * FileName: DmzjSubscribedFragment
 * Author: JiaoCan
 * Date: 2022/3/4 13:56
 */

public class DmzjSubscribedFragment extends Fragment {

    private static final String ARG_UID = "arg_uid";
    private static final String ARG_TYPE = "arg_type";
    private String mUid;
    private int mType;
    private boolean isLoaded = false;
    private int page = 0;

    private FragmentUserSubscribedBinding binding;
    private RetrofitService service;
    private DmzjUserSubscribedAdapter adapter;

    public static DmzjSubscribedFragment newInstance(String uid, int type) {
        Bundle args = new Bundle();
        args.putString(ARG_UID, uid);
        args.putInt(ARG_TYPE, type);
        DmzjSubscribedFragment fragment = new DmzjSubscribedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUid = getArguments().getString(ARG_UID);
            mType = getArguments().getInt(ARG_TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentUserSubscribedBinding.inflate(inflater, container, false);
        service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);

        initView();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isLoaded) return ;
        isLoaded = true;
        lazyLoad();
    }

    private void lazyLoad() {
        binding.refreshLayout.setRefreshing(true);
        getResponse();
    }

    private void initView() {

        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new DmzjUserSubscribedAdapter(getContext());
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((a, view, position) -> {
            DmzjUserSubscribedBean bean = (DmzjUserSubscribedBean) a.getData().get(position);
            String comicId = bean.getId();
            IntentUtil.goToComicInfoActivity(getActivity(), comicId);
        });

        adapter.getLoadMoreModule().setOnLoadMoreListener(this::getResponse);
        adapter.getLoadMoreModule().setAutoLoadMore(true);
        adapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);

        binding.refreshLayout.setOnRefreshListener(() -> {
            page = 0;
            getResponse();
        });
    }

    private void getResponse() {
        service.getDmzjUserSubscribed(mUid, mType, 1, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<DmzjUserSubscribedBean>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<DmzjUserSubscribedBean> beanList) {
                        binding.refreshLayout.setRefreshing(false);
                        if (page == 0 && beanList.size() == 0) {
                            binding.refreshLayout.setVisibility(View.INVISIBLE);
                            binding.noData.setVisibility(View.VISIBLE);
                        }
                        if (page == 0) {
                            adapter.setList(beanList);
                        } else {
                            adapter.addData(beanList);
                        }
                        if (beanList.size() == 0) {
                            adapter.getLoadMoreModule().loadMoreEnd();
                        } else {
                            adapter.getLoadMoreModule().loadMoreComplete();
                        }
                        page++;
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        binding.refreshLayout.setRefreshing(false);
                        binding.refreshLayout.setVisibility(View.INVISIBLE);
                        binding.noData.setVisibility(View.VISIBLE);
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
