package com.sena.dmzjthird.comic.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.comic.adapter.AuthorInfoAdapter;
import com.sena.dmzjthird.comic.bean.AuthorInfoBean;
import com.sena.dmzjthird.databinding.ActivityAuthorInfoBinding;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.RetrofitHelper;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class AuthorInfoActivity extends AppCompatActivity {

    private ActivityAuthorInfoBinding binding;
    private AuthorInfoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthorInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.progress.spin();
        binding.toolbar.setBackListener(v -> finish());

        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AuthorInfoAdapter(this);
        binding.recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) ->
                IntentUtil.goToComicInfoActivity(this, ((AuthorInfoBean.Data) adapter.getData().get(position)).getId()));

        getResponse();


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
                        binding.progress.stopSpinning();
                        binding.progress.setVisibility(View.GONE);
                        if (bean.getData().size() == 0) {
                            binding.noData.setVisibility(View.VISIBLE);
                        } else {
                            binding.recyclerview.setVisibility(View.VISIBLE);
                            binding.toolbar.setTitle(bean.getNickname());
                            adapter.setList(bean.getData());
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
}