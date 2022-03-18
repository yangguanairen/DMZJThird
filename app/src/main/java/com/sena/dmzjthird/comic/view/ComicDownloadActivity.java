package com.sena.dmzjthird.comic.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.application.ComicDetailRes;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.comic.adapter.ComicDownloadAdapter;
import com.sena.dmzjthird.comic.bean.ComicDownloadBean;
import com.sena.dmzjthird.databinding.ActivityComicDownloadBinding;
import com.sena.dmzjthird.download.DownloadInfo;
import com.sena.dmzjthird.download.DownloadManager;
import com.sena.dmzjthird.download.DownloadObserver;
import com.sena.dmzjthird.room.chapter.Chapter;
import com.sena.dmzjthird.room.comic.Comic;
import com.sena.dmzjthird.room.MyRoomDatabase;
import com.sena.dmzjthird.room.RoomHelper;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.RetrofitHelper;
import com.sena.dmzjthird.utils.api.ComicApi;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ComicDownloadActivity extends AppCompatActivity {

    private ActivityComicDownloadBinding binding;
    private ComicDownloadAdapter adapter;

    private MyRoomDatabase database;


    private String comicId;
    private String comicName;
    private String comicCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComicDownloadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = RoomHelper.getInstance(this);

        comicId = IntentUtil.getObjectId(this);
        comicName = IntentUtil.getObjectName(this);
        comicCover = IntentUtil.getObjectCover(this);
        initView();



        Comic comic = RoomHelper.getInstance(this)
                .comicDao()
                .query(comicId);
        if (comic == null) {
            comic = new Comic();
            comic.comicId = comicId;
            comic.comicName = comicName;
            comic.comicCover = comicCover;
            RoomHelper.getInstance(this)
                    .comicDao().insert(comic);
        }






        getResponse();
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

    private void downloadSelect() {
        RetrofitService service = RetrofitHelper.getServer("https://m.dmzj.com/");


        LogUtil.e("带下载数量: " + adapter.getSelectIdSet().size());
        for (Integer chapterId: adapter.getSelectIdSet()) {
            service.getChapterInfoForDownload(comicId, chapterId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(new Observer<ComicDownloadBean>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull ComicDownloadBean bean) {

                            DownloadManager.getInstance(ComicDownloadActivity.this)
                                    .download(bean.getPage_url(), bean.getFolder(), new DownloadObserver() {

                                        @Override
                                        public void onSubscribe(Disposable d) {
                                            super.onSubscribe(d);
                                            createComicItemInRoom(bean.getFilesize());
                                            createItemInRoom(bean);
                                        }

                                        @Override
                                        public void onNext(DownloadInfo downloadInfo) {
                                            super.onNext(downloadInfo);
                                                database.chapterDao()
                                                        .updateFinishPage(bean.getComic_id(), bean.getId(), downloadInfo.getFinishPage());

                                        }

                                        @Override
                                        public void onComplete() {
                                            super.onComplete();
                                            new Thread(() -> {
                                                database.chapterDao()
                                                        .updateChapterStatus(bean.getComic_id(), bean.getId(), "已完成");
                                            }).start();

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            super.onError(e);
                                        }
                                    });

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

    /**
     * 创建ComicItem，会直接覆盖原先数据
//     * @param fileSize 单个章节的全部图片的总大小
     */
    private void createComicItemInRoom(long fileSize) {
        Comic comic = new Comic();
        comic.comicId = comicId;
        comic.comicName = comicName;
        comic.comicCover = comicCover;
        comic.totalChapter = comic.totalChapter + 1;
        comic.totalSize = comic.totalSize + fileSize;
        database.comicDao().insert(comic);
    }


    /**
     * 创建ChapterItem，不会覆盖原先数据，下载页会去做冗余处理
     * @param bean
     */
    private void createItemInRoom(ComicDownloadBean bean) {
        Chapter chapter = new Chapter();
        chapter.comicId = comicId;
        chapter.chapterId = bean.getId();
        chapter.chapterName = bean.getChapter_name();
        chapter.folder_name = bean.getFolder();
        chapter.urlList = bean.getPage_url();
        chapter.totalPage = bean.getSum_pages();
        chapter.finishPage = 0;
        chapter.status = "未开始";
        database.chapterDao().insert(chapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}