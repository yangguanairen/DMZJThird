package com.sena.dmzjthird.comic.view;

import androidx.appcompat.app.AppCompatActivity;
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
import android.view.DragEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Toast;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.account.MyRetrofitService;
import com.sena.dmzjthird.comic.adapter.ComicViewAdapter;
import com.sena.dmzjthird.comic.adapter.ComicViewCatalogAdapter;
import com.sena.dmzjthird.comic.adapter.MyPageAdapter;
import com.sena.dmzjthird.custom.popup.CustomBottomPopup;
import com.sena.dmzjthird.databinding.ActivityComicViewBinding;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.MyDataStore;
import com.sena.dmzjthird.utils.RetrofitHelper;
import com.sena.dmzjthird.utils.XPopUpUtil;
import com.sena.dmzjthird.utils.api.ComicApi;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ComicViewActivity extends AppCompatActivity implements CustomBottomPopup.Callbacks, MyPageAdapter.Callbacks {

    private ActivityComicViewBinding binding;
    private RetrofitService service;
    private String comicId;
    private String chapterId;


    private final List<String> images = new ArrayList<>();  // 漫画图片url集合

    private boolean mIsVerticalMode = false;
    private int systemMaxBrightness;
    private BroadcastReceiver receiver;

    private BasePopupView popup;

    private List<String> chapterIds;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComicViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setStatusBarColor(Color.BLACK);

        service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);
        comicId = IntentUtil.getObjectId(this);
        chapterId = IntentUtil.getChapterId(this);

        initView();

        initClick();

        initBroadcast();

        getResponse();

    }

    private void initView() {

        systemMaxBrightness = getResources().getInteger(
                getResources().getIdentifier("config_screenBrightnessSettingMaximum", "integer", "android")
        );

        popup = new XPopup.Builder(this).asCustom(new CustomBottomPopup(this, systemMaxBrightness));

        setSubscribeStatus();

        // 侧边栏
        initCatalog();

    }

    /**
     * 初始化侧边栏
     * 展示章节列表
     * 目前先做一个分类的
     */
    private void initCatalog() {

        ComicApi.getComicChapter(comicId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    if (data == null) return ;
                    List<String> chapterNames = new ArrayList<>();
                    chapterIds = new ArrayList<>();
                    for (int i = 0; i < data.getDataCount(); i++) {
                        chapterIds.add(data.getData(i).getChapterId() + "");
                        chapterNames.add(data.getData(i).getChapterTitle());
                    }
                    binding.catalogCount.setText(getString(R.string.catalog, data.getDataCount()));
                    binding.catalogRecyclerview.setLayoutManager(new LinearLayoutManager(this));
                    ComicViewCatalogAdapter adapter = new ComicViewCatalogAdapter(this);
                    binding.catalogRecyclerview.setAdapter(adapter);
                    adapter.setCurrentChapterName(chapterId);
                    adapter.setList(chapterNames);
                    adapter.setOnItemClickListener((adapter1, view, position) -> {
                        String selectedId = chapterIds.get(position);
                        if (chapterId.equals(selectedId)) {
                            return;
                        }
                        chapterId = selectedId;
                        getResponse();
                        binding.drawerLayout.closeDrawer(binding.catalog);
                        adapter.setCurrentChapterName(chapterId);
                        adapter.setList(adapter.getData());
                    });
                });


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
                    binding.preChapter.callOnClick();
                } else if (position == images.size() - 1) {
                    binding.nextChapter.callOnClick();
                } else {
                    binding.seekBar.setProgress(position);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        binding.chapterList.setOnClickListener(v -> binding.drawerLayout.openDrawer(binding.catalog));
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (mIsVerticalMode) {
//                    if (seekBar.isPressed()) {
//                        binding.recyclerview.scrollToPosition(progress);
//                        binding.pageNum.setText((progress + 1) + "/" + images.size());
//                    }
                } else {
//                    binding.viewPager.setCurrentItem(progress + 1);
                    binding.pageNum.setText((progress) + "/" + (images.size() - 2));
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        binding.setting.setOnClickListener(v -> popup.show());

        binding.subscribe.setOnClickListener(v -> controlSubscribe());

        // 设置上一话/下一话监听器
        binding.preChapter.setOnClickListener(v -> {

            int currentIndex = chapterIds.size();
            for (int i = 0; i < chapterIds.size(); i++) {
                if (chapterId.equals(chapterIds.get(i))) {
                    currentIndex = i;
                    break;
                }
            }
            if (currentIndex >= chapterIds.size() - 1) {
                Toast.makeText(this, "已经没有哦", Toast.LENGTH_SHORT).show();
                return ;
            }
            chapterId = chapterIds.get(currentIndex + 1);
            getResponse();
        });
        binding.nextChapter.setOnClickListener(v -> {
            int currentIndex = -1;
            for (int i = 0; i < chapterIds.size(); i++) {
                if (chapterId.equals(chapterIds.get(i))) {
                    currentIndex = i;
                    break;
                }
            }
            if (currentIndex <= 0) {
                Toast.makeText(this, "已经没有哦", Toast.LENGTH_SHORT).show();
                return ;
            }
            chapterId = chapterIds.get(currentIndex - 1);
            getResponse();
        });

//         下拉前往上一章，只有竖屏阅读模式生效
        binding.refresh.setOnRefreshListener(() -> {
            binding.preChapter.callOnClick();
            binding.refresh.setRefreshing(false);
        });
        binding.complaint.setOnClickListener(v -> {

        });
    }


    private void getResponse() {
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
                        setViewPagerData(bean.getPicnum());
                    }
                });
    }

    /**
     * 发送请求询问服务器
     * 该漫画是否订阅
     */
    private void setSubscribeStatus() {
        long uid = MyDataStore.getInstance(this).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, 0L);
//        if ("".equals(uid)) {
//            return;
//        }
        MyRetrofitService myService = RetrofitHelper.getMyServer(MyRetrofitService.MY_BASE_URL);
        myService.querySubscribe(uid, comicId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultBean -> {
                    if (resultBean == null || resultBean.getCode() == 100) {
                        return ;
                    }
                    binding.subscribe.setCompoundDrawablesWithIntrinsicBounds(
                            0, "true".equals(resultBean.getContent()) ? R.drawable.ic_subscribed : R.drawable.ic_subscribe, 0, 0
                    );
                });
    }

    // 订阅按钮的点击事件
    private void controlSubscribe() {
        long uid = MyDataStore.getInstance(this).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, 0L);
        MyRetrofitService myService = RetrofitHelper.getMyServer(MyRetrofitService.MY_BASE_URL);
        myService.controlSubscribe(uid, comicId, "cover", "title", "author")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultBean -> {
                    if (resultBean == null) {
                        XPopUpUtil.showCustomErrorToast(this, "请求失败，请稍后重试");
                        return ;
                    }
                    if (resultBean.getCode() == 100) {
                        XPopUpUtil.showCustomErrorToast(this, getString(R.string.not_login));
                        return ;
                    }
                    binding.subscribe.setCompoundDrawablesWithIntrinsicBounds(
                            0, "true".equals(resultBean.getContent()) ? R.drawable.ic_subscribed : R.drawable.ic_subscribe, 0, 0
                    );
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