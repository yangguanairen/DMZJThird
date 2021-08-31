package com.sena.dmzjthird.comic.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.comic.adapter.ComicViewCatalogAdapter;
import com.sena.dmzjthird.comic.bean.ComicInfoBean;
import com.sena.dmzjthird.comic.bean.ComicViewBean;
import com.sena.dmzjthird.databinding.ActivityComicViewBinding;
import com.sena.dmzjthird.utils.GlideUtil;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.LogUtil;
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
    private List<String> images = new ArrayList<>();
    private ComicViewBean mBean; // 自己请求的数据
    private boolean preChapterClick = false;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComicViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        service = RetrofitHelper.getServer(RetrofitService.BASE_ORIGIN_URL);
        comicId = IntentUtil.getObjectId(this);
        chapterId = IntentUtil.getChapterId(this);
        bean = (ComicInfoBean) IntentUtil.getSerialize(this);

        getWindow().setStatusBarColor(Color.BLACK);

        initControl();

        getResponse();

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

                        // 添加头尾两页，与AutoBanner实现类似
                        images.add("1");
                        images.addAll(bean.getPage_url());
                        images.add("1");

                        binding.viewPager.setAdapter(new MyAdapter());
                        binding.viewPager.setOffscreenPageLimit(bean.getSum_pages()+1);
                        binding.seekBar.setMax(bean.getSum_pages()-1);

                        // 点击上一话，则跳到该章节最后一页
                        if (preChapterClick) {
                            preChapterClick = false;
                            binding.seekBar.setProgress(bean.getSum_pages()-2);
                        } else {
                            binding.seekBar.setProgress(0);
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


    private void initControl() {

        binding.viewPager.setRotation(-90);

        binding.toolbar.setBackListener(v -> finish());
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                binding.seekBar.setProgress(position-1);
                if (position == 0) {
                    // 前往上一章
                    binding.preChapter.callOnClick();
                }
                if (position == images.size()-1) {
                    // 前往下一章
                    binding.nextChapter.callOnClick();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        binding.chapterList.setOnClickListener(v -> binding.drawerLayout.openDrawer(binding.catalog));
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.viewPager.setCurrentItem(progress+1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // subscribe设置初始值
        binding.subscribe.setOnClickListener(v -> {
            if (PreferenceHelper.findStringByKey(this, PreferenceHelper.USER_UID) == null) {
                Toast.makeText(this, getString(R.string.not_login), Toast.LENGTH_SHORT).show();
                return ;
            }
            RetrofitService service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);
            if (binding.subscribe.getContentDescription().equals("0")) {
                // 订阅
                service.subscribeComic(comicId, PreferenceHelper.findStringByKey(this, PreferenceHelper.USER_UID), "mh")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(bean1 -> {
                            if (bean1.getCode() != 0) {
                                Toast.makeText(this, getString(R.string.subscribe_fail), Toast.LENGTH_SHORT).show();
                            } else {
                                binding.subscribe.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_subscribed, 0, 0);
                                binding.subscribe.setContentDescription("1");
                                Toast.makeText(this, getString(R.string.subscribe_success), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                // 取消订阅
                service.cancelSubscribeComic(comicId, PreferenceHelper.findStringByKey(this, PreferenceHelper.USER_UID), "mh")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(bean1 -> {
                            if (bean1.getCode() != 0) {
                                Toast.makeText(this, getString(R.string.cancel_subscribe_fail), Toast.LENGTH_SHORT).show();
                            } else {
                                binding.subscribe.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_subscribe, 0, 0);
                                binding.subscribe.setContentDescription("0");
                                Toast.makeText(this, getString(R.string.cancel_subscribe_success), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
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

    }

    private void setSubscribeStatus() {
        String uid = PreferenceHelper.findStringByKey(this, PreferenceHelper.USER_UID);
        if (uid == null) {
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
    }

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
            view.setRotation(90);

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
            photoView.setOnClickListener(v -> {
                if ("0".contentEquals(binding.viewPager.getContentDescription())) {
                    binding.toolbar.setVisibility(View.VISIBLE);
                    binding.bottomView.setVisibility(View.VISIBLE);
                    binding.viewPager.setContentDescription("1");
                } else {
                    binding.toolbar.setVisibility(View.INVISIBLE);
                    binding.bottomView.setVisibility(View.INVISIBLE);
                    binding.viewPager.setContentDescription("0");
                }
            });

//            WindowManag/er.LayoutParams params = new WindowManager.LayoutParams();
//            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
//            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//            photoView.setLayoutParams(params);

            //            photoView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(ComicViewActivity.this)
                    .load(GlideUtil.addCookie(images.get(position)))
                    .into(photoView);
            container.addView(photoView);

            return photoView;
        }
    }


}