package com.sena.dmzjthird.account.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.sena.dmzjthird.databinding.ActivityUserDownloadBinding;
import com.sena.dmzjthird.download.DownloadChapterBean;
import com.sena.dmzjthird.download.DownloadComicBean;
import com.sena.dmzjthird.download.DownloadHelper;
import com.sena.dmzjthird.download.DownloadUrlBean;
import com.sena.dmzjthird.room.Chapter;
import com.sena.dmzjthird.room.Comic;
import com.sena.dmzjthird.room.MyRoomDatabase;
import com.sena.dmzjthird.room.RoomHelper;
import com.sena.dmzjthird.room.Url;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserDownloadActivity extends AppCompatActivity {

    private ActivityUserDownloadBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDownloadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }

    private void initView() {
        binding.toolbar.setBackListener(v -> finish());

    }

    /**
     * 从本地数据库获取数据
     * 生成xx对象，传递给Adapter
     * 异步操作
     */
    private void initData() {
        Observable.create((ObservableOnSubscribe<List<DownloadComicBean>>) emitter -> {

            emitter.onNext(DownloadHelper.getAllData(this));

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<DownloadComicBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull List<DownloadComicBean> dataList) {
                        if (dataList.isEmpty()) {
                            binding.error.noData.setVisibility(View.INVISIBLE);
                            return ;
                        }
                        // 设置数据
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}