package com.sena.dmzjthird.comic.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import com.example.application.ComicDetailRes;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.account.MyRetrofitService;
import com.sena.dmzjthird.comic.adapter.ComicViewCatalogAdapter;
import com.sena.dmzjthird.comic.adapter.ComicViewPagerAdapter;
import com.sena.dmzjthird.comic.bean.ComicChapterInfoBean;
import com.sena.dmzjthird.custom.readerComic.ComicViewVM;
import com.sena.dmzjthird.databinding.ActivityComicViewBinding;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.RetrofitHelper;
import com.sena.dmzjthird.utils.ViewHelper;
import com.sena.dmzjthird.utils.api.ComicApi;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ComicViewActivity extends AppCompatActivity {

    private ActivityComicViewBinding binding;
    private RetrofitService service;
    private String comicId;
    private String comicName;
    private String comicCover;

    private ComicViewVM vm;

    private final List<String> images = new ArrayList<>();  // 漫画图片url集合

    private ComicViewCatalogAdapter catalogAdapter;

    private final List<Integer> chapterIdList = new ArrayList<>();
    private final List<String> chapterNameList = new ArrayList<>();



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComicViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        vm = new ViewModelProvider(this).get(ComicViewVM.class);


        service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);
        comicId = IntentUtil.getObjectId(this);
        comicCover = IntentUtil.getObjectCover(this);
        comicName = IntentUtil.getObjectName(this);
        int chapterId = IntentUtil.getChapterId(this);
        String chapterName = IntentUtil.getChapterName(this);

        initView();
        initViewModel();

        initCatalog(chapterId);

        vm.currentChapterId.postValue(chapterId);
        vm.currentChapterName.postValue(chapterName);
        vm.isShowToolView.postValue(false);

    }

    private void initView() {

        binding.bottomView.setChapterListListener(v -> {
            binding.drawerLayout.openDrawer(binding.catalog);
        });
        ViewHelper.setSubscribeStatus(this, comicId, MyRetrofitService.TYPE_COMIC,
                binding.bottomView.getSubscribeIconView(), binding.bottomView.getSubscribeTextView());
        binding.bottomView.setSubscribeListener(v -> {
            ViewHelper.controlSubscribe(this, comicId, comicCover, comicName, "author",
                    MyRetrofitService.TYPE_COMIC,
                    binding.bottomView.getSubscribeIconView(), binding.bottomView.getSubscribeTextView());
        });


        // 侧边栏
        binding.catalogRecyclerview.setLayoutManager(new LinearLayoutManager(ComicViewActivity.this));
        catalogAdapter = new ComicViewCatalogAdapter(ComicViewActivity.this);
        binding.catalogRecyclerview.setAdapter(catalogAdapter);
        catalogAdapter.setOnItemClickListener((a, view, position) -> {
            ComicDetailRes.ComicDetailChapterInfoResponse data = (ComicDetailRes.ComicDetailChapterInfoResponse) a.getData().get(position);
            int selectChapterId = data.getChapterId();
            if (selectChapterId != vm.currentChapterId.getValue()) {
                vm.currentChapterId.postValue(selectChapterId);
                vm.currentChapterName.postValue(data.getChapterTitle());
            }
            binding.drawerLayout.closeDrawer(binding.catalog);
        });

        // ViewPager
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    updateChapterId(true);
                } else if (position == images.size() - 1) {
                    updateChapterId(false);
                } else {
                    binding.bottomView.setSeekBarProgress(position);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 初始化侧边栏
     * 展示章节列表
     * 目前先做一个分类的
     */
    private void initCatalog(int chapterId) {

        ComicApi.getComicChapter(comicId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ComicDetailRes.ComicDetailChapterResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(ComicDetailRes.@NonNull ComicDetailChapterResponse data) {
                        for (ComicDetailRes.ComicDetailChapterInfoResponse d: data.getDataList()) {
                            chapterIdList.add(d.getChapterId());
                            chapterNameList.add(d.getChapterTitle());
                        }

                        vm.chapterIdList.postValue(chapterIdList);
                        vm.chapterNameList.postValue(chapterNameList);

                        binding.catalogCount.setText(getString(R.string.catalog, data.getDataCount()));
                        catalogAdapter.setList(data.getDataList());
                        catalogAdapter.setCurrentChapterId(chapterId);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }



    private void initViewModel() {

        vm.currentPage.observe(this, integer -> {
            if (vm.isUserOperate.getValue() != null && vm.isUserOperate.getValue()) {
                binding.viewPager.setCurrentItem(integer); // 是否包括首位空白页
            }

        });

        vm.currentChapterId.observe(this, chapterId -> {
            getResponse(chapterId);
            catalogAdapter.setCurrentChapterId(chapterId);
        });

        vm.isFullMode.observe(this, isFullScreen -> {
            if (isFullScreen) {
                ImmersionBar.with(this)
                        .reset()
                        .statusBarColor(R.color.black)
                        .statusBarDarkFont(true)
                        .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                        .titleBarMarginTop(binding.toolbar)
                        .init();
            } else {
                ImmersionBar.with(this)
                        .reset()
                        .statusBarColor(R.color.black)
                        .statusBarDarkFont(false)
                        .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                        .titleBarMarginTop(binding.toolbar)
                        .init();
            }
        });



    }

    private void updateChapterId(boolean isPreClick) {

        int index = -1;
        int currentChapterId = vm.currentChapterId.getValue();
        for (int i = 0; i < chapterIdList.size(); i++) {
            if (currentChapterId == chapterIdList.get(i)) {
                index = i;
                break;
            }
        }

        int cId; // 即将浏览的章节
        String cName;
        if (isPreClick) {
            if (index == -1 || index == chapterIdList.size() - 1) {
                Toast.makeText(this, "已经没有上一章了", Toast.LENGTH_SHORT).show();
                return ;
            } else {
                cId = chapterIdList.get(index + 1);
                cName = chapterNameList.get(index + 1);
            }
        } else {
            if (index == -1 || index == 0) {
                Toast.makeText(this, "已经没有下一章了", Toast.LENGTH_SHORT).show();
                return ;
            } else {
                cId = chapterIdList.get(index - 1);
                cName = chapterNameList.get(index - 1);
            }
        }
        vm.currentChapterId.postValue(cId);
        vm.currentChapterName.postValue(cName);

    }


    private void getResponse(int chapterId) {
        service.getChapterInfo(comicId, chapterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ComicChapterInfoBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ComicChapterInfoBean bean) {
                        if (bean.getPage_url().isEmpty()) {
                            // 出错处理
                            return ;
                        }
                        images.clear();
                        images.add(null);
                        images.addAll(bean.getPage_url());
                        images.add(null);

                        vm.currentChapterName.postValue(bean.getTitle());
                        vm.totalPage.postValue(images.size() - 2);

                        binding.viewPager.setAdapter(new ComicViewPagerAdapter(ComicViewActivity.this, images));
                        binding.viewPager.setOffscreenPageLimit(images.size() + 1);
                        binding.viewPager.setCurrentItem(1);

                        ViewHelper.addHistory(ComicViewActivity.this, comicId, comicCover, comicName, MyRetrofitService.TYPE_COMIC,
                                0, "Null", chapterId, vm.currentChapterName.getValue());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        // 空数据处理
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