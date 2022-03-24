package com.sena.dmzjthird.comic.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.comic.adapter.AuthorInfoAdapter;
import com.sena.dmzjthird.comic.bean.AuthorInfoBean;
import com.sena.dmzjthird.databinding.ActivityAuthorInfoBinding;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.RetrofitHelper;
import com.sena.dmzjthird.utils.ViewHelper;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AuthorInfoActivity extends AppCompatActivity {

    private ActivityAuthorInfoBinding binding;
    private AuthorInfoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthorInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();

        getResponse();


    }

    private void initView() {

        ViewHelper.immersiveStatus(this, binding.toolbar);


        binding.toolbar.setBackListener(v -> finish());

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AuthorInfoAdapter(this);
        binding.recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) ->
                IntentUtil.goToComicInfoActivity(this, ((AuthorInfoBean.Data) adapter.getData().get(position)).getId()));


        binding.refreshLayout.setOnRefreshListener(this::getResponse);

    }

    private void getResponse() {
        RetrofitService service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);
        service.getAuthorInfo(IntentUtil.getAuthorId(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AuthorInfoBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull AuthorInfoBean bean) {

                        if (bean.getData().isEmpty()) {
                            onRequestError(true);
                            return ;
                        }
                        onRequestError(false);

                        binding.toolbar.setTitle(bean.getNickname());
                        adapter.setList(bean.getData());

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        binding.refreshLayout.setRefreshing(false);
                        onRequestError(true);
                    }

                    @Override
                    public void onComplete() {
                        binding.refreshLayout.setRefreshing(false);
                    }
                });
    }


    private void onRequestError(boolean isError) {
        binding.toolbar.setTitle("");
        binding.error.noData.setVisibility(isError ? View.VISIBLE : View.INVISIBLE);
        binding.recyclerView.setVisibility(isError ? View.INVISIBLE : View.VISIBLE);
    }
}