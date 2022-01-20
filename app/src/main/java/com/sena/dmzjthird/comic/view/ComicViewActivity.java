package com.sena.dmzjthird.comic.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.comic.adapter.ComicViewAdapter;
import com.sena.dmzjthird.comic.adapter.ComicViewCatalogAdapter;
import com.sena.dmzjthird.comic.bean.ComicInfoBean;
import com.sena.dmzjthird.comic.bean.ComicViewBean;
import com.sena.dmzjthird.databinding.ActivityComicViewBinding;
import com.sena.dmzjthird.utils.GlideUtil;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.MyDataStore;
import com.sena.dmzjthird.utils.OnClickListenerHelper;
import com.sena.dmzjthird.utils.PreferenceHelper;
import com.sena.dmzjthird.utils.RetrofitHelper;

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
    private String chapterId;
    private ComicInfoBean bean;  // 源Activity传来的数据，用于添加catalog
    private ComicViewBean mBean; // 自己请求的数据
    private boolean preChapterClick = false;
    private ComicViewAdapter adapter;

    private List<String> images = new ArrayList<>();  // 漫画图片url集合

    private boolean isUesSystemBrightness;
    private int seekbarBrightness;
    private boolean isVerticalMode;   // 控制该展现的视图，true: RecyclerView; false: ViewPager
    private boolean isJapaneseComicMode;
    private boolean isKeepLight;
    private boolean isFullScreen;
    private boolean isShowState;
    private int systemMaxBrightness;
    private BroadcastReceiver receiver;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComicViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        systemMaxBrightness = getResources().getInteger(
                getResources().getIdentifier("config_screenBrightnessSettingMaximum", "integer", "android")
        );
        isUesSystemBrightness = PreferenceHelper.findBooleanByKey(this, PreferenceHelper.IS_USE_SYSTEM_BRIGHTNESS);
        seekbarBrightness = PreferenceHelper.findIntByKey(this, PreferenceHelper.SEEKBAR_BRIGHTNESS);
        isVerticalMode = PreferenceHelper.findBooleanByKey(this, PreferenceHelper.IS_VERTICAL_MODE);
        isJapaneseComicMode = PreferenceHelper.findBooleanByKey(this, PreferenceHelper.IS_JAPANESE_COMIC_MODE);
        isKeepLight = PreferenceHelper.findBooleanByKey(this, PreferenceHelper.IS_KEEP_LIGHT_ALWAYS);
        isFullScreen = PreferenceHelper.findBooleanByKey(this, PreferenceHelper.IS_FULL_SCREEN);
        isShowState = PreferenceHelper.findBooleanByKey(this, PreferenceHelper.IS_SHOW_STATE);

        getWindow().setStatusBarColor(Color.BLACK);
        isUseSystemBrightness();
        isKeepLightAlways();
        isFullScreen();
        isShowState();

        service = RetrofitHelper.getServer(RetrofitService.BASE_ORIGIN_URL);
        comicId = IntentUtil.getObjectId(this);
        chapterId = IntentUtil.getChapterId(this);
        bean = (ComicInfoBean) IntentUtil.getSerialize(this);


        initControl();

        initBroadcast();

        getResponse();

    }


    private void isShowState() {
        binding.state.setVisibility(isShowState?View.VISIBLE:View.INVISIBLE);
    }

    private void isFullScreen() {
        if (isFullScreen) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    private void isKeepLightAlways() {
        if (isKeepLight) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    private void isUseSystemBrightness() {
        if (isUesSystemBrightness) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
            getWindow().setAttributes(params);
        } else {
            try {
                seekbarBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            } catch (Exception e) {
                e.printStackTrace();
                seekbarBrightness = 0;
            }
        }
    }

    private void changeSystemBrightness() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.screenBrightness = seekbarBrightness / (float) systemMaxBrightness;
        getWindow().setAttributes(params);
    }


    private void getResponse() {
        service.getChapterInfo(comicId, chapterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ComicViewBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ComicViewBean bean) {
                        // 数据清空，章节改变
                        images.clear();
                        mBean = bean;
                        binding.complaint.setText(getString(R.string.complaint, bean.getComment_count()));
                        binding.toolbar.setTitle(bean.getChapter_name());
                        binding.chapterName.setText(bean.getChapter_name());

                        images.add("1");
                        images.addAll(bean.getPage_url());
                        images.add("1");

                        if (isVerticalMode) {
                            setRecyclerViewData();
                        } else {
                            setViewPagerData(bean.getSum_pages());
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogUtil.internetError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @SuppressLint({"ResourceAsColor", "UseSwitchCompatOrMaterialCode"})
    private void initControl() {

        binding.toolbar.setBackListener(v -> finish());
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            @Override
            public void onPageSelected(int position) {
                binding.seekBar.setProgress(position - 1);
                if (position == 0) {
                    // 前往上一章
                    binding.preChapter.callOnClick();
                }
                if (position == images.size() - 1) {
                    // 前往下一章
                    binding.nextChapter.callOnClick();
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) { }
        });
        binding.chapterList.setOnClickListener(v -> binding.drawerLayout.openDrawer(binding.catalog));
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (isVerticalMode) {
//                    if (seekBar.isPressed()) {
//                        binding.recyclerview.scrollToPosition(progress);
//                        binding.pageNum.setText((progress + 1) + "/" + images.size());
//                    }
                } else {
                    binding.viewPager.setCurrentItem(progress + 1);
                    binding.pageNum.setText((progress + 1) + "/" + images.size());
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // subscribe设置初始值
        binding.subscribe.setOnClickListener(v -> OnClickListenerHelper.subscribeComic(this, comicId, binding.subscribe));
        setSubscribeStatus();

        // 设置上一话/下一话监听器
        binding.preChapter.setOnClickListener(v -> {
            if (TextUtils.isEmpty(mBean.getPrev_chap_id())) {
                Toast.makeText(this, "已经没有哦", Toast.LENGTH_SHORT).show();
            }
            chapterId = mBean.getPrev_chap_id();
            preChapterClick = true;
            getResponse();
        });
        binding.nextChapter.setOnClickListener(v -> {
            if (TextUtils.isEmpty(mBean.getNext_chap_id())) {
                Toast.makeText(this, "已经没有哦", Toast.LENGTH_SHORT).show();
            }
            chapterId = mBean.getNext_chap_id();
            getResponse();
        });

        // 侧边栏
        binding.catalogCount.setText(getString(R.string.catalog, bean.getData().size()));
        binding.catalogRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        ComicViewCatalogAdapter adapter = new ComicViewCatalogAdapter(this);
        binding.catalogRecyclerview.setAdapter(adapter);
        adapter.setCurrentChapterId(chapterId);
        adapter.setList(bean.getData());
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            String clickChapterId = ((ComicInfoBean.Data) adapter1.getData().get(position)).getId();
            if (chapterId.equals(clickChapterId)) {
                return;
            }
            chapterId = clickChapterId;
            getResponse();
            binding.drawerLayout.closeDrawer(binding.catalog);
            adapter.setCurrentChapterId(chapterId);
            adapter.setList(adapter.getData());
        });

        // 点击设置弹出底部弹窗
        binding.setting.setOnClickListener(v -> {

            View view = LayoutInflater.from(this).inflate(R.layout.dialog_view_setting, null, false);
            Switch lightSwitch = view.findViewById(R.id.lightSwitch);
            SeekBar lightSeekBar = view.findViewById(R.id.lightSeekBar);
            ConstraintLayout lightCons = view.findViewById(R.id.lightCons);
            Switch verticalSwitch = view.findViewById(R.id.verticalSwitch);
            ConstraintLayout modeCons = view.findViewById(R.id.modeCons);
            Switch modeSwitch = view.findViewById(R.id.modeSwitch);
            Switch keepSwitch = view.findViewById(R.id.keepSwitch);
            Switch fullSwitch = view.findViewById(R.id.fullSwitch);
            Switch stateSwitch = view.findViewById(R.id.stateSwitch);

            AlertDialog alertDialog = new AlertDialog.Builder(this).create();

            lightSwitch.setChecked(isUesSystemBrightness);
            lightCons.setVisibility(isUesSystemBrightness?View.VISIBLE:View.GONE);
            lightSeekBar.setMax(systemMaxBrightness);
            lightSeekBar.setProgress(seekbarBrightness);
            verticalSwitch.setChecked(isVerticalMode);
            modeCons.setVisibility(isVerticalMode?View.VISIBLE:View.GONE);
            modeSwitch.setChecked(isJapaneseComicMode);
            keepSwitch.setChecked(isKeepLight);
            fullSwitch.setChecked(isFullScreen);
            stateSwitch.setChecked(isShowState);


            // 添加监听器
            lightSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                isUesSystemBrightness = isChecked;
                lightCons.setVisibility(isChecked?View.VISIBLE:View.GONE);
            });
            lightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    seekbarBrightness = progress;
                    changeSystemBrightness(); }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) { }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) { }
            });
            verticalSwitch.setOnCheckedChangeListener(((buttonView, isChecked) -> {
                isVerticalMode = isChecked;
                if (isChecked) {
                    modeCons.setVisibility(View.GONE);
                    modeSwitch.setChecked(false);
                    setRecyclerViewData();
                } else {
                    modeCons.setVisibility(View.VISIBLE);
                    setViewPagerData(images.size());
                }

            }));
            fullSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                isFullScreen = isChecked;
                isFullScreen();
            });
            keepSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                isKeepLight = isChecked;
                isKeepLightAlways();
            });
            stateSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                isShowState = isChecked;
                isShowState();
            });

            // 修改AlertDialog横向铺满整个屏幕
            WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
            alertDialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
            alertDialog.getWindow().getDecorView().setBackgroundColor(R.color.view_top_bottom_color);
            alertDialog.getWindow().setGravity(Gravity.BOTTOM);
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            alertDialog.getWindow().setAttributes(params);

            alertDialog.setView(view);
            alertDialog.show();
        });

//         下拉前往上一章，只有竖屏阅读模式生效
        binding.refresh.setOnRefreshListener(() -> {
            binding.preChapter.callOnClick();
            binding.refresh.setRefreshing(false);
        });
        binding.complaint.setOnClickListener(v -> {

        });

    }

    /**
     * 发送请求询问服务器
     * 该漫画是否订阅
     */
    private void setSubscribeStatus() {
        String uid = MyDataStore.getInstance(this).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, "");
        if ("".equals(uid)) {
            return;
        }
        RetrofitService originService = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);
        originService.isSubscribe(uid, comicId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    if (bean.getCode() == 0) {
                        binding.subscribe.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_subscribed, 0, 0);
                        binding.subscribe.setContentDescription("1");
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
        unregisterReceiver(receiver);
    }

    /**
     * 保存AlertDialog内控件的状态
     */
    @Override
    protected void onStop() {
        super.onStop();
        PreferenceHelper.setBooleanByKey(this, PreferenceHelper.IS_USE_SYSTEM_BRIGHTNESS, isUesSystemBrightness);
        PreferenceHelper.setIntByKey(this, PreferenceHelper.SEEKBAR_BRIGHTNESS, seekbarBrightness);
        PreferenceHelper.setBooleanByKey(this, PreferenceHelper.IS_VERTICAL_MODE, isVerticalMode);
        PreferenceHelper.setBooleanByKey(this, PreferenceHelper.IS_JAPANESE_COMIC_MODE, isJapaneseComicMode);
        PreferenceHelper.setBooleanByKey(this, PreferenceHelper.IS_KEEP_LIGHT_ALWAYS, isKeepLight);
        PreferenceHelper.setBooleanByKey(this, PreferenceHelper.IS_FULL_SCREEN, isFullScreen);
        PreferenceHelper.setBooleanByKey(this, PreferenceHelper.IS_SHOW_STATE, isShowState);
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

        binding.viewPager.setAdapter(new MyAdapter());
        binding.viewPager.setOffscreenPageLimit(totalPages + 1);
        binding.seekBar.setMax(totalPages - 1);



        // 点击上一话，则跳到该章节最后一页
        if (preChapterClick) {
            preChapterClick = false;
            binding.seekBar.setProgress(totalPages - 2);
        } else {
            binding.seekBar.setProgress(0);
        }
    }

    private void setRecyclerViewData() {

        binding.refresh.setEnabled(true);
        binding.seekBar.setMax(images.size());
        binding.viewPager.setVisibility(View.GONE);
        binding.recyclerview.setVisibility(View.VISIBLE);

        binding.recyclerview.setLayoutManager(new LinearLayoutManager(ComicViewActivity.this));
        adapter = new ComicViewAdapter(ComicViewActivity.this);
        binding.recyclerview.setAdapter(adapter);

        // 点击弹出弹窗
        adapter.setOnItemClickListener((adapter, view, position) -> isShowTopBottom(view));
        adapter.getLoadMoreModule().setOnLoadMoreListener(() -> {
            binding.nextChapter.callOnClick();
        });
        adapter.getLoadMoreModule().setAutoLoadMore(false);
        adapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);

        images.remove(0);
        images.remove(images.size()-1);
        adapter.setList(images);
    }

    private void isShowTopBottom(View view) {
        if ("0".equals(view.getContentDescription())) {
            binding.toolbar.setVisibility(View.VISIBLE);
            binding.bottomView.setVisibility(View.VISIBLE);
            view.setContentDescription("1");
        } else {
            binding.toolbar.setVisibility(View.INVISIBLE);
            binding.bottomView.setVisibility(View.INVISIBLE);
            view.setContentDescription("0");
        }
    }


    // 自定义PagerAdapter，使用PhotoView显示图片
    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if (images != null) {
                return images.size();
            }
            return 0;
        }


        @Override
        public boolean isViewFromObject(@androidx.annotation.NonNull View view, @androidx.annotation.NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@androidx.annotation.NonNull ViewGroup container, int position, @androidx.annotation.NonNull Object object) {
            container.removeView((View) object);
        }

        @androidx.annotation.NonNull
        @Override
        public Object instantiateItem(@androidx.annotation.NonNull ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(ComicViewActivity.this);
            photoView.setOnClickListener(v -> isShowTopBottom(v));

            Glide.with(ComicViewActivity.this)
                    .load(GlideUtil.addCookie(images.get(position)))
                    .into(photoView);
            container.addView(photoView);

            return photoView;
        }
    }


}