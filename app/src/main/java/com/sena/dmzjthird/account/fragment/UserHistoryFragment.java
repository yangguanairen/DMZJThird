package com.sena.dmzjthird.account.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sena.dmzjthird.account.MyRetrofitService;
import com.sena.dmzjthird.account.adapter.UserHistoryAdapter;
import com.sena.dmzjthird.account.bean.UserHistoryBean;
import com.sena.dmzjthird.databinding.FragmentUserHistoryBinding;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.MyDataStore;
import com.sena.dmzjthird.utils.RetrofitHelper;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserHistoryFragment extends Fragment {

    private static final String ARG_TYPE = "arg_type";
    private int mType;

    private FragmentUserHistoryBinding binding;
    private MyRetrofitService service;
    private UserHistoryAdapter adapter;

    public static UserHistoryFragment newInstance(int type) {
        UserHistoryFragment fragment = new UserHistoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentUserHistoryBinding.inflate(inflater, container, false);
        service = RetrofitHelper.getMyServer(MyRetrofitService.MY_BASE_URL);

        initView();
        getResponse();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getResponse();
    }


    private void initView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UserHistoryAdapter(getContext());
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((a, view, position) -> {
            UserHistoryBean bean = (UserHistoryBean) a.getData().get(position);
            String objectId = bean.getObjectId();
            if (mType == MyRetrofitService.TYPE_COMIC) {
                IntentUtil.goToComicInfoActivity(getActivity(), objectId);
            } else {
                IntentUtil.goToNovelInfoActivity(getContext(), objectId);
            }
        });
    }

    private void getResponse() {

        long uid = MyDataStore.getInstance(getContext()).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, 0L);
        service.getAllHistory(uid, mType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<UserHistoryBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<UserHistoryBean> beanList) {
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
}