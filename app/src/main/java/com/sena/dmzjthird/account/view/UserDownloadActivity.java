package com.sena.dmzjthird.account.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.account.adapter.UserDownloadAdapter;
import com.sena.dmzjthird.databinding.ActivityUserDownloadBinding;
import com.sena.dmzjthird.download.DownloadBean;
import com.sena.dmzjthird.download.DownloadHelper;
import com.sena.dmzjthird.utils.ViewHelper;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserDownloadActivity extends AppCompatActivity {

    private ActivityUserDownloadBinding binding;
    private UserDownloadAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDownloadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
        initData();

    }

    private void initView() {

        ViewHelper.immersiveStatus(this, binding.toolbar);

        binding.toolbar.setBackListener(v -> finish());
    }

    /**
     * 从本地数据库获取数据
     * 生成xx对象，传递给Adapter
     * 异步操作
     */
    private void initData() {
        Observable.create((ObservableOnSubscribe<List<DownloadBean>>) emitter -> {

            emitter.onNext(DownloadHelper.getAllData(this));

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<DownloadBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull List<DownloadBean> dataList) {
                        if (dataList.isEmpty()) {
                            onErrorAppear(true);
                            return ;
                        }
                        onErrorAppear(false);
                        // 设置数据
                        adapter = new UserDownloadAdapter(UserDownloadActivity.this, dataList);
                        binding.recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        onErrorAppear(true);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void onErrorAppear(boolean isError) {
        binding.error.noData.setVisibility(isError ? View.VISIBLE : View.INVISIBLE);
        binding.recyclerView.setVisibility(isError ? View.INVISIBLE : View.VISIBLE);
    }


}