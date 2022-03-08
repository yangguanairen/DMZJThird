package com.sena.dmzjthird.novel.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.custom.readerBook.ReaderPageAdapter;
import com.sena.dmzjthird.databinding.ActivityNovelViewBinding;
import com.sena.dmzjthird.utils.api.NovelApi;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NovelViewActivity extends AppCompatActivity {

    private ActivityNovelViewBinding binding;

    private int volumeId;
    private int chapterId;
    private String responseStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNovelViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        volumeId = getIntent().getIntExtra("volumeId", 0);
        chapterId = getIntent().getIntExtra("chapterId", 0);

        ImmersionBar.with(this)
                .statusBarColor(R.color.theme_blue)
                .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                .titleBarMarginTop(binding.viewPager)
                .init();

        getResponse();
    }

    private void getResponse() {

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

                        binding.viewPager.setAdapter(new ReaderPageAdapter(NovelViewActivity.this , responseStr,
                                binding.viewPager.getWidth(), binding.viewPager.getHeight(),
                                50f, 70f));
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


}