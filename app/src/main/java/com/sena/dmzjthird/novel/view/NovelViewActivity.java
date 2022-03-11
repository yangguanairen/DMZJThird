package com.sena.dmzjthird.novel.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
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
import com.sena.dmzjthird.account.MyRetrofitService;
import com.sena.dmzjthird.custom.popup.NovelBottomPopup;
import com.sena.dmzjthird.custom.readerBook.ReaderPageAdapter;
import com.sena.dmzjthird.databinding.ActivityNovelViewBinding;
import com.sena.dmzjthird.novel.adapter.NovelViewCatalogAdapter;
import com.sena.dmzjthird.novel.vm.NovelViewVM;
import com.sena.dmzjthird.utils.BroadcastHelper;
import com.sena.dmzjthird.utils.HtmlUtil;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.MyDataStore;
import com.sena.dmzjthird.utils.ViewHelper;
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
    private String novelName;
    private String novelCover;
    private int volumeId;
    private String volumeName;
    private String responseStr;

    private NovelViewVM vm;
    private boolean isUserOperate = false;
    private final List<Integer> chapterIdList = new ArrayList<>();
    private final List<String> chapterNameList = new ArrayList<>();
    private NovelViewCatalogAdapter catalogAdapter;
    private BroadcastReceiver receiver;

    private float currentTextSize;
    private float currentSpaceLine;
    private int currentBgColorId;
    private BasePopupView popupView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNovelViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        novelId = IntentUtil.getObjectId(this);
        novelName = IntentUtil.getObjectName(this);
        novelCover = IntentUtil.getObjectCover(this);
        volumeId = IntentUtil.getVolumeId(this);
        volumeName = IntentUtil.getVolumeName(this);
        int chapterId = IntentUtil.getChapterId(this);
        String chapterName = IntentUtil.getChapterName(this);

        vm = new ViewModelProvider(this).get(NovelViewVM.class);

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
        currentSpaceLine = dataStore.getValue(MyDataStore.DATA_STORE_NOVEL_READ_SETTING, MyDataStore.SETTING_NOVEL_SPACE_LIN, 25f);
        currentBgColorId = dataStore.getValue(MyDataStore.DATA_STORE_NOVEL_READ_SETTING, MyDataStore.SETTING_NOVEL_BG, R.color.white);
    }

    private void initView() {

        ImmersionBar.with(this)
                .statusBarColor(currentBgColorId)
                .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                .titleBarMarginTop(binding.consLayout)
                .init();

        // Battery And Network
        receiver = BroadcastHelper.getBatteryAndNetworkBroadcast(this, binding.batteryNum, binding.internetType);

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
        ViewHelper.setSubscribeStatus(this, novelId, MyRetrofitService.TYPE_NOVEL, binding.bottomView.subscribeIcon, binding.bottomView.subscribeText);
        binding.bottomView.subscribeLayout.setOnClickListener(v -> {
            ViewHelper.controlSubscribe(this, novelId, novelCover, novelName, "author", MyRetrofitService.TYPE_NOVEL,
                    binding.bottomView.subscribeIcon, binding.bottomView.subscribeText);
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
            binding.drawerLayout.closeDrawer(binding.catalog);
        });

    }

    private void initVM() {

        vm.currentChapterId.observe(this, chapterId -> {
            getResponse(chapterId);
            catalogAdapter.setSelectId(chapterId);

        });
        vm.currentChapterName.observe(this, name -> {
            binding.toolbar.setTitle(name);
            ViewHelper.addHistory(this, novelId, novelCover, novelName, MyRetrofitService.TYPE_NOVEL,
                    volumeId, "volumeName", vm.currentChapterId.getValue(), name);
        });

        vm.isShowToolView.observe(this, isShowToolView -> {
            binding.toolbar.setVisibility(isShowToolView ? View.VISIBLE : View.INVISIBLE);
            binding.bottomView.consLayout.setVisibility(isShowToolView ? View.VISIBLE : View.INVISIBLE);
        });

        vm.totalPageNum.observe(this, totalPage -> {
            // 这样写不好，后期改正
            binding.bottomView.seekBar.setMax(totalPage - 1);
            // viewPager的下标从0开始
            binding.pageNum.setText((binding.bottomView.seekBar.getProgress() + 1) + "/" + totalPage);
        });
        vm.currentPageIndex.observe(this, currentPage -> {
            if (isUserOperate) {
                binding.viewPager.setCurrentItem(currentPage);
            }
            // 设置底部监听
            // 这样写不好，后期改正
            binding.pageNum.setText((currentPage + 1) + "/" + (binding.bottomView.seekBar.getMax() + 1));
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

                        String[] tmpArr = s.split("\n");
                        StringBuilder sb = new StringBuilder();
                        for (String tmp: tmpArr) {
                            String a = tmp.replaceAll("<br />\\s++", "")
                                    .replace("<br />", "")
                                    .replace("<br/>", "\n  ");
                            if (a.isEmpty() || "\n".equals(a)) {
                                continue;
                            }
                            sb.append("  ").append(a).append("\n");
                        }

                        LogUtil.e(sb.toString());

                        responseStr = HtmlUtil.convertSpecialCharacters(sb.toString());


                        updateViewPager(0, currentBgColorId == R.color.black);
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
                cId = chapterIdList.get(index - 1);
                cName = chapterNameList.get(index - 1);

            }
        } else {
            if (index == -1 || index == chapterIdList.size() - 1) {
                Toast.makeText(this, "已经没有下一章了", Toast.LENGTH_SHORT).show();
                return;
            } else {
                cId = chapterIdList.get(index + 1);
                cName = chapterNameList.get(index + 1);
            }
        }
        vm.currentChapterId.postValue(cId);
        vm.currentChapterName.postValue(cName);
    }

    /**
     * 字体大小、行间距、背景改变会重新计算页面
     *
     * @param currentPage 保留当前阅读状态
     * @param isBlackBg   黑色背景下，文字变为白色
     */
    private void updateViewPager(int currentPage, boolean isBlackBg) {
        ReaderPageAdapter pageAdapter = new ReaderPageAdapter(NovelViewActivity.this, responseStr,
                binding.viewPager.getWidth(), binding.viewPager.getHeight(),
                currentTextSize, currentSpaceLine, isBlackBg);
        binding.viewPager.setAdapter(pageAdapter);
        LogUtil.e("当前页面: " + currentPage);
        binding.viewPager.setCurrentItem(currentPage);
        vm.totalPageNum.postValue(pageAdapter.getCount());
        binding.bottomView.seekBar.setProgress(currentPage);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
        popupView.onDestroy();
        BroadcastHelper.unregisterBroadcast(this, receiver);
    }

    @Override
    public void onTextSizeChange(float textSize) {
        currentTextSize = textSize;
        updateViewPager(binding.viewPager.getCurrentItem(), currentBgColorId == R.color.black);
    }

    @Override
    public void onSpaceLineChange(float spaceLine) {
        currentSpaceLine = spaceLine;
        updateViewPager(binding.viewPager.getCurrentItem(), currentBgColorId == R.color.black);
    }

    @Override
    public void onBgChange(int colorResourcesId) {
        currentBgColorId = colorResourcesId;
        binding.viewPager.setBackgroundResource(colorResourcesId);
        updateViewPager(binding.viewPager.getCurrentItem(), currentBgColorId == R.color.black);
        ImmersionBar.with(this)
                .statusBarColor(colorResourcesId)
                .init();
    }
}