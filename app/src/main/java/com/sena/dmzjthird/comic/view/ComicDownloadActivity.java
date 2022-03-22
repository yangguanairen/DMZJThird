package com.sena.dmzjthird.comic.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.application.ComicDetailRes;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.account.view.UserDownloadActivity;
import com.sena.dmzjthird.comic.adapter.ComicDownloadAdapter;
import com.sena.dmzjthird.comic.bean.ComicChapterInfoBean;
import com.sena.dmzjthird.databinding.ActivityComicDownloadBinding;
import com.sena.dmzjthird.room.RoomHelper;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.RetrofitHelper;
import com.sena.dmzjthird.utils.api.ComicApi;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ComicDownloadActivity extends AppCompatActivity {

    private ActivityComicDownloadBinding binding;
    private ComicDownloadAdapter adapter;

    private RoomHelper roomHelper;


    private String comicId;
    private String comicName;
    private String comicCover;

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComicDownloadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        roomHelper = RoomHelper.getInstance(this);

        comicId = IntentUtil.getObjectId(this);
        comicName = IntentUtil.getObjectName(this);
        comicCover = IntentUtil.getObjectCover(this);
        initView();


        getResponse();
    }

    private void initView() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.theme_blue)
                .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                .titleBarMarginTop(binding.toolbar)
                .init();

        binding.toolbar.setBackListener(v -> finish());

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ComicDownloadAdapter(this);
        binding.recyclerView.setAdapter(adapter);

        binding.startDownload.setOnClickListener(v -> downloadSelect());
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
                            return;
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

    private void downloadSelect() {
        RetrofitService service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);


        LogUtil.e("带下载数量: " + adapter.getSelectIdSet().size());

        count = 0;
        for (Integer chapterId : adapter.getSelectIdSet()) {
            service.getChapterInfo(comicId, chapterId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(new Observer<ComicChapterInfoBean>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull ComicChapterInfoBean bean) {


                            roomHelper.initComic(comicId, comicName, comicCover);
                            roomHelper.updateComicTotalChapter(comicId);
                            roomHelper.insertChapter(comicName, bean);
                            checkJump(adapter.getSelectIdSet().size());
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            LogUtil.e("下载Error");
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }




    }

    private synchronized void checkJump(int targetSize) {
        count++;
        if (count == targetSize) {
            startActivity(new Intent(ComicDownloadActivity.this, UserDownloadActivity.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}