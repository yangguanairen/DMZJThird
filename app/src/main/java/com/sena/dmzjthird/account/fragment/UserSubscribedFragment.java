package com.sena.dmzjthird.account.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sena.dmzjthird.account.MyRetrofitService;
import com.sena.dmzjthird.account.adapter.UserSubscribedAdapter;
import com.sena.dmzjthird.account.bean.UserSubscribedBean;
import com.sena.dmzjthird.databinding.FragmentUserSubscribedBinding;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.MyDataStore;
import com.sena.dmzjthird.utils.RetrofitHelper;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class UserSubscribedFragment extends Fragment {

    private static final String ARG_TYPE = "arg_type";

    private boolean isLoaded = false;
    private int type;

    private FragmentUserSubscribedBinding binding;
    private MyRetrofitService service;
    private UserSubscribedAdapter adapter;


    public static UserSubscribedFragment newInstance(int type) {
        UserSubscribedFragment fragment = new UserSubscribedFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserSubscribedBinding.inflate(inflater, container, false);
        service = RetrofitHelper.getMyServer(MyRetrofitService.MY_BASE_URL);

        initView();

        return binding.getRoot();
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
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        adapter = new UserSubscribedAdapter(getActivity());
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((adapter, view, position) -> {
            if (type == 0) {
                IntentUtil.goToComicInfoActivity(getActivity(), ((UserSubscribedBean) adapter.getData().get(position)).getComicId());
            } else {
                Toast.makeText(getActivity(), "跳转: 小说Info页面, 没写完", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getResponse() {
        long uid = MyDataStore.getInstance(getContext()).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, 0L);
        service.getAllSubscribe(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<UserSubscribedBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<UserSubscribedBean> beanList) {
                        binding.refreshLayout.setRefreshing(false);
                        if (beanList.size() == 0) {
                            binding.refreshLayout.setVisibility(View.INVISIBLE);
                            binding.noData.setVisibility(View.VISIBLE);
                        }
                        adapter.setList(beanList);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
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