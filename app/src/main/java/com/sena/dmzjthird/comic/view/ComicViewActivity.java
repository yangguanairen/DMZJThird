package com.sena.dmzjthird.comic.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.application.ComicDetailRes;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.account.MyRetrofitService;
import com.sena.dmzjthird.account.bean.ResultBean;
import com.sena.dmzjthird.comic.adapter.ComicViewAdapter;
import com.sena.dmzjthird.comic.adapter.ComicViewCatalogAdapter;
import com.sena.dmzjthird.comic.adapter.MyPageAdapter;
import com.sena.dmzjthird.comic.vm.ComicViewViewModel;
import com.sena.dmzjthird.custom.popup.CustomBottomPopup;
import com.sena.dmzjthird.databinding.ActivityComicViewBinding;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.MyDataStore;
import com.sena.dmzjthird.utils.RetrofitHelper;
import com.sena.dmzjthird.utils.ViewHelper;
import com.sena.dmzjthird.utils.XPopUpUtil;
import com.sena.dmzjthird.utils.api.ComicApi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ComicViewActivity extends AppCompatActivity implements CustomBottomPopup.Callbacks, MyPageAdapter.Callbacks {

    private ActivityComicViewBinding binding;
    private RetrofitService service;
    private String comicId;
    private String comicName;
    private String comicCover;

    private ComicViewViewModel vm;

    private final List<String> images = new ArrayList<>();  // 漫画图片url集合

    private boolean mIsVerticalMode = false;
    private int systemMaxBrightness;
    private BroadcastReceiver receiver;

    private BasePopupView popup;

    private final List<String> chapterIdList = new ArrayList<>();
    private final List<String> chapterNameList = new ArrayList<>();

    private boolean isUserOperate = false;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComicViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        vm = new ViewModelProvider(this).get(ComicViewViewModel.class);

        getWindow().setStatusBarColor(Color.BLACK);

        service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);
        comicId = IntentUtil.getObjectId(this);
        comicCover = IntentUtil.getObjectCover(this);
        comicName = IntentUtil.getObjectName(this);
        String chapterId = IntentUtil.getChapterId(this);
        String chapterName = IntentUtil.getChapterName(this);

        initView(chapterName);

        initClick();

        initViewModel();
        initBroadcast();

        vm.currentChapterId.postValue(chapterId);

    }

    private void initView(String chapterName) {

        systemMaxBrightness = getResources().getInteger(
                getResources().getIdentifier("config_screenBrightnessSettingMaximum", "integer", "android")
        );

        popup = new XPopup.Builder(this).asCustom(new CustomBottomPopup(this, systemMaxBrightness));

        ViewHelper.setSubscribeStatus(this, binding.subscribe, comicId);

        // 侧边栏
        initCatalog(chapterName);

    }

    /**
     * 初始化侧边栏
     * 展示章节列表
     * 目前先做一个分类的
     */
    private void initCatalog(String chapterName) {

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
                            chapterIdList.add(d.getChapterId() + "");
                            chapterNameList.add(d.getChapterTitle());
                        }

                        binding.catalogCount.setText(getString(R.string.catalog, data.getDataCount()));
                        binding.catalogRecyclerview.setLayoutManager(new LinearLayoutManager(ComicViewActivity.this));
                        ComicViewCatalogAdapter adapter = new ComicViewCatalogAdapter(ComicViewActivity.this);
                        binding.catalogRecyclerview.setAdapter(adapter);

                        adapter.setOnItemClickListener((a, view, position) -> {
                            String selectChapterId = chapterIdList.get(position);
                            if (!selectChapterId.equals(vm.currentChapterId.getValue())) {
                                vm.currentChapterId.postValue(selectChapterId);
                                vm.currentChapterName.postValue(chapterIdList.get(position));
                            }
                            adapter.setCurrentChapterName(chapterNameList.get(position));
                            adapter.setList(chapterNameList);
                            binding.drawerLayout.closeDrawer(binding.catalog);
                        });

                        adapter.setCurrentChapterName(chapterName);
                        adapter.setList(chapterNameList);
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
            if (isUserOperate) {
                binding.viewPager.setCurrentItem(integer); // 是否包括首位空白页
            }

            if (mIsVerticalMode) {

            } else {
                binding.pageNum.setText(integer + "/" + binding.seekBar.getMax());
            }
        });

        vm.totalPage.observe(this, integer -> {
            binding.seekBar.setMax(integer);
        });

        vm.currentChapterId.observe(this, s -> {
            ViewHelper.addHistory(ComicViewActivity.this, comicId, comicCover, comicName, s, vm.currentChapterName.getValue());
            getResponse(s);
        });

    }

    private void updateChapterId(boolean isPreClick) {

        int index = -1;
        String currentChapterId = vm.currentChapterId.getValue();
        for (int i = 0; i < chapterIdList.size(); i++) {
            if (currentChapterId.equals(chapterIdList.get(i))) {
                index = i;
                break;
            }
        }

        String cId; // 即将浏览的章节
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

    private void initClick() {

        binding.toolbar.setBackListener(v -> finish());
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
                    binding.seekBar.setProgress(position);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                vm.currentPage.postValue(progress);
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
        binding.chapterList.setOnClickListener(v -> binding.drawerLayout.openDrawer(binding.catalog));

        binding.setting.setOnClickListener(v -> popup.show());

        binding.subscribe.setOnClickListener(v -> ViewHelper.controlSubscribe(this, binding.subscribe, comicId, "cover", "title", "author"));

        // 设置上一话/下一话监听器
        binding.preChapter.setOnClickListener(v -> updateChapterId(true));
        binding.nextChapter.setOnClickListener(v -> updateChapterId(false));

//         下拉前往上一章，只有竖屏阅读模式生效
        binding.refresh.setOnRefreshListener(() -> {
            binding.preChapter.callOnClick();
            binding.refresh.setRefreshing(false);
        });
        binding.complaint.setOnClickListener(v -> {

        });
    }

    private void getResponse(String chapterId) {
        service.getChapterInfo(comicId, chapterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    if (bean == null) {
                        // 空数据处理
                        return ;
                    }
                    images.clear();
                    binding.complaint.setText(getString(R.string.complaint, bean.getComment_count()));
                    binding.toolbar.setTitle(bean.getTitle());
                    binding.chapterName.setText(bean.getTitle());

                    images.add("上一章");
                    images.addAll(bean.getPage_url());
                    images.add("下一章");

                    if (mIsVerticalMode) {
                        setRecyclerViewData();
                    } else {
                        setViewPagerData(images.size());
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
        popup.destroy();
        unregisterReceiver(receiver);
    }


    /**
     * 用于监听电池电量和网络状态变化
     */
    private void initBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        receiver = new BroadcastReceiver() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case Intent.ACTION_BATTERY_CHANGED:
                        int currentBattery = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                        int maxBattery = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
                        binding.batteryNum.setText((currentBattery * 100) / maxBattery + "% 电量");
                        break;
                    case "android.net.conn.CONNECTIVITY_CHANGE":
                        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkCapabilities info = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                        if (null != info) {
                            if (info.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                                binding.internetType.setText("WIFI");
                            } else if (info.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                                binding.internetType.setText("数据网络");
                            }
                        } else {
                            binding.internetType.setText("无连接");
                        }
                        break;

                    default:
                        break;
                }

            }
        };
        registerReceiver(receiver, filter);

    }

    private void setViewPagerData(int totalPages) {

        binding.refresh.setEnabled(false);
        binding.viewPager.setVisibility(View.VISIBLE);
        binding.recyclerview.setVisibility(View.GONE);

        binding.seekBar.setMax(totalPages - 2);

        binding.viewPager.setAdapter(new MyPageAdapter(this, images));
        binding.viewPager.setOffscreenPageLimit(totalPages + 1);
        binding.viewPager.setCurrentItem(1);
//
//        binding.seekBar.setProgress(0);

    }

    private void setRecyclerViewData() {

        binding.refresh.setEnabled(true);
        binding.seekBar.setMax(images.size());
        binding.viewPager.setVisibility(View.GONE);
        binding.recyclerview.setVisibility(View.VISIBLE);

        binding.recyclerview.setLayoutManager(new LinearLayoutManager(ComicViewActivity.this));
        ComicViewAdapter adapter = new ComicViewAdapter(ComicViewActivity.this);
        binding.recyclerview.setAdapter(adapter);

        // 点击弹出弹窗
        adapter.setOnItemClickListener((a, view, position) -> isShowTopBottom(view));
        adapter.getLoadMoreModule().setOnLoadMoreListener(() -> binding.nextChapter.callOnClick());
        adapter.getLoadMoreModule().setAutoLoadMore(false);
        adapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);

        images.remove(0);
        images.remove(images.size()-1);
        adapter.setList(images);
    }

    private void isShowTopBottom(View view) {
        if (view.getTag() == null || "0".equals(view.getTag().toString())) {
            binding.toolbar.setVisibility(View.VISIBLE);
            binding.bottomView.setVisibility(View.VISIBLE);
            view.setTag("1");
        } else {
            binding.toolbar.setVisibility(View.INVISIBLE);
            binding.bottomView.setVisibility(View.INVISIBLE);
            view.setTag("0");
        }
    }

    @Override
    public void onUseSystemBrightness() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        getWindow().setAttributes(params);

    }

    @Override
    public void onBrightnessChange(int brightness) {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.screenBrightness = brightness / (float) systemMaxBrightness;
        getWindow().setAttributes(params);
    }

    @Override
    public void onVerticalModeChange(boolean isVerticalMode) {
        mIsVerticalMode = isVerticalMode;
        if (isVerticalMode) {
            setRecyclerViewData();
        } else {
            setViewPagerData(images.size());
        }
    }

    @Override
    public void onComicModeChange(boolean flag) {

    }

    @Override
    public void onFullScreenChange(boolean isFullScreen) {
        if (isFullScreen) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    public void onKeepLightChange(boolean isKeepLight) {
        if (isKeepLight) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @Override
    public void onShowStateChange(boolean isShowState) {
        binding.state.setVisibility(isShowState ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onPhotoViewClick(View view) {
        isShowTopBottom(view);
    }

}