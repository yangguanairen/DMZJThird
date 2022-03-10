package com.sena.dmzjthird.novel.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.application.NovelChapterRes;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.custom.popup.NovelBottomPopup;
import com.sena.dmzjthird.custom.readerBook.ReaderPageAdapter;
import com.sena.dmzjthird.databinding.ActivityNovelViewBinding;
import com.sena.dmzjthird.novel.adapter.NovelViewCatalogAdapter;
import com.sena.dmzjthird.novel.vm.NovelViewVM;
import com.sena.dmzjthird.utils.MyDataStore;
import com.sena.dmzjthird.utils.api.NovelApi;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NovelViewActivity extends AppCompatActivity implements NovelBottomPopup.Callbacks {

    private ActivityNovelViewBinding binding;

    private String novelId;
    private int volumeId;
    private String responseStr;

    private NovelViewVM vm;
    private boolean isUserOperate = false;
    private List<Integer> chapterIdList = new ArrayList<>();
    private List<String> chapterNameList = new ArrayList<>();
    private NovelViewCatalogAdapter catalogAdapter;

    private float currentTextSize;
    private float currentSpaceLine;
    private int currentBgColorId;
    private BasePopupView popupView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNovelViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        novelId = getIntent().getStringExtra("novelId");
        volumeId = getIntent().getIntExtra("volumeId", 0);
        int chapterId = getIntent().getIntExtra("chapterId", 0);
        String chapterName = getIntent().getStringExtra("chapterName");

        vm = new ViewModelProvider(this).get(NovelViewVM.class);

        ImmersionBar.with(this)
                .statusBarColor(R.color.theme_blue)
                .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                .titleBarMarginTop(binding.consLayout)
                .init();

        initData();
        initView();
        initVM();

        initCategory();

        vm.isShowToolView.postValue(false);
        vm.currentChapterId.postValue(chapterId);
        vm.currentChapterName.postValue(chapterName);

    }

    private void initData() {
        MyDataStore dataStore = MyDataStore.getInstance(this);
        currentTextSize = dataStore.getValue(MyDataStore.DATA_STORE_NOVEL_READ_SETTING, MyDataStore.SETTING_NOVEL_TEXT_SIZE, 50f);
        currentSpaceLine = dataStore.getValue(MyDataStore.DATA_STORE_NOVEL_READ_SETTING, MyDataStore.SETTING_NOVEL_SPACE_LIN, 70f);
        currentBgColorId = dataStore.getValue(MyDataStore.DATA_STORE_NOVEL_READ_SETTING, MyDataStore.SETTING_NOVEL_BG, R.color.white);
    }

    private void initView() {

        // ToolBar
        binding.toolbar.setBackListener(v -> finish());

        // ViewPager
        binding.viewPager.setBackgroundResource(currentBgColorId);
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                binding.bottomView.seekBar.setProgress(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // BottomView
        popupView = new XPopup.Builder(this).asCustom(new NovelBottomPopup(this));
        binding.bottomView.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                vm.currentPageIndex.postValue(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isUserOperate = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isUserOperate = false;
            }
        });
        binding.bottomView.preChapter.setOnClickListener(v -> updateChapterId(true));
        binding.bottomView.nextChapter.setOnClickListener(v -> updateChapterId(false));
        binding.bottomView.subscribeLayout.setOnClickListener(v -> {
        });
        binding.bottomView.settingLayout.setOnClickListener(v -> popupView.show());
        binding.bottomView.chapterListLayout.setOnClickListener(v -> binding.drawerLayout.openDrawer(binding.catalog));

        // NavigationView
        binding.catalogRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        catalogAdapter = new NovelViewCatalogAdapter(this);
        binding.catalogRecyclerview.setAdapter(catalogAdapter);
        catalogAdapter.setOnItemClickListener((a, view, position) -> {
            NovelChapterRes.NovelChapterItemResponse data = (NovelChapterRes.NovelChapterItemResponse) a.getData().get(position);
            int chapterId = data.getChapterId();
            String chapterName = data.getChapterName();
            vm.currentChapterId.postValue(chapterId);
            vm.currentChapterName.postValue(chapterName);
        });

    }

    private void initVM() {

        vm.currentChapterId.observe(this, chapterId -> {
            getResponse(chapterId);
            catalogAdapter.setSelectId(chapterId);
        });
        vm.currentChapterName.observe(this, name -> binding.toolbar.setTitle(name));

        vm.isShowToolView.observe(this, isShowToolView -> {
            binding.toolbar.setVisibility(isShowToolView ? View.VISIBLE : View.INVISIBLE);
            binding.bottomView.consLayout.setVisibility(isShowToolView ? View.VISIBLE : View.INVISIBLE);
        });

        vm.totalPageNum.observe(this, totalPage -> {
            binding.bottomView.seekBar.setMax(totalPage);
            binding.pageNum.setText(binding.bottomView.seekBar.getProgress() + "/" + totalPage);
        });
        vm.currentPageIndex.observe(this, currentPage -> {
            if (isUserOperate) {
                binding.viewPager.setCurrentItem(currentPage);
            }
            // 设置底部监听
            binding.pageNum.setText(currentPage + "/" + binding.bottomView.seekBar.getMax());
        });

    }

    private void initCategory() {
        NovelApi.getNovelVolumeChapter(novelId, volumeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<NovelChapterRes.NovelChapterItemResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<NovelChapterRes.NovelChapterItemResponse> dataList) {
                        if (dataList.isEmpty()) {
                            // 出错处理
                            return;
                        }
                        for (NovelChapterRes.NovelChapterItemResponse item : dataList) {
                            chapterIdList.add(item.getChapterId());
                            chapterNameList.add(item.getChapterName());
                        }
                        binding.catalogCount.setText(getString(R.string.catalog, dataList.size()));
                        // 目录注入数据
                        catalogAdapter.setList(dataList);
                        catalogAdapter.setSelectId(vm.currentChapterId.getValue());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        // 出错处理

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getResponse(int chapterId) {

        NovelApi.getNovelContent(volumeId, chapterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String s) {

                        responseStr = s.replaceAll("\\s+&nbsp;", "  ")
                                .replace("&nbsp;", " ")
                                .replace("&hellip;", "...")
                                .replace("&bull;", "·")
                                .replace("&ldquo;", "「")
                                .replace("&rdquo;", "」")
                                .replace("&mdash;", "-")
                                .replace("&lsquo;", "‘")
                                .replace("&rsquo;", "’")
                                .replace("<br/>", "\n ")
                                .replace("<br />", "");

                        updateViewPager(0);
//                        binding.viewPager.setAdapter(new ReaderPageAdapter(NovelViewActivity.this, responseStr,
//                                binding.viewPager.getWidth(), binding.viewPager.getHeight(),
//                                50f, 70f));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        // 出错处理
                    }

                    @Override
                    public void onComplete() {

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
            if (index == -1 || index == 0) {
                Toast.makeText(this, "已经没有上一章了", Toast.LENGTH_SHORT).show();
                return;
            } else {
                cId = chapterIdList.get(index + 1);
                cName = chapterNameList.get(index + 1);
            }
        } else {
            if (index == -1 || index == chapterIdList.size() - 1) {
                Toast.makeText(this, "已经没有下一章了", Toast.LENGTH_SHORT).show();
                return;
            } else {
                cId = chapterIdList.get(index - 1);
                cName = chapterNameList.get(index - 1);
            }
        }
        vm.currentChapterId.postValue(cId);
        vm.currentChapterName.postValue(cName);
    }

    private void updateViewPager(int currentPage) {
        ReaderPageAdapter pageAdapter = new ReaderPageAdapter(NovelViewActivity.this, responseStr,
                binding.viewPager.getWidth(), binding.viewPager.getHeight(),
                currentTextSize, currentSpaceLine);
        binding.viewPager.setAdapter(pageAdapter);
        binding.viewPager.setCurrentItem(currentPage);
        vm.totalPageNum.postValue(pageAdapter.getCount());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
        popupView.onDestroy();
    }

    @Override
    public void onTextSizeChange(float textSize) {
        currentTextSize = textSize;
        updateViewPager(binding.viewPager.getCurrentItem());
    }

    @Override
    public void onSpaceLineChange(float spaceLine) {
        currentSpaceLine = spaceLine;
        updateViewPager(binding.viewPager.getCurrentItem());
    }

    @Override
    public void onBgChange(int colorResourcesId) {
        binding.viewPager.setBackgroundResource(colorResourcesId);
    }
}