package com.sena.dmzjthird.comic.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.application.ComicDetailRes;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.comic.adapter.ComicDownloadAdapter;
import com.sena.dmzjthird.databinding.ActivityComicDownloadBinding;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.api.ComicApi;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ComicDownloadActivity extends AppCompatActivity {

    private ActivityComicDownloadBinding binding;
    private ComicDownloadAdapter adapter;

    private String comicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComicDownloadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        comicId = IntentUtil.getObjectId(this);
    }

    private void initView() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.theme_blue)
                .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                .titleBarMarginTop(binding.toolbar)
                .init();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ComicDownloadAdapter(this);
        binding.recyclerView.setAdapter(adapter);


    }

    private void getResponse() {
        ComicApi.getComicInfo(comicId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ComicDetailRes.ComicDetailInfoResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(ComicDetailRes.@NonNull ComicDetailInfoResponse data) {
                        if (data.getChaptersList().isEmpty()) {
                            // 出错处理
                            return ;
                        }
                        adapter.setList(data.getChaptersList());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}