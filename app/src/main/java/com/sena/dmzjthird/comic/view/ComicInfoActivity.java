package com.sena.dmzjthird.comic.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.sena.dmzjthird.R;
import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.comic.fragment.ComicCommentFragment;
import com.sena.dmzjthird.comic.fragment.ComicInfoFragment;
import com.sena.dmzjthird.comic.fragment.ComicRelatedFragment;
import com.sena.dmzjthird.databinding.ActivityComicInfoBinding;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.PreferenceHelper;
import com.sena.dmzjthird.utils.RetrofitHelper;

import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ComicInfoActivity extends AppCompatActivity implements ComicInfoFragment.Callbacks {

    private ActivityComicInfoBinding binding;

    private String comicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComicInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // infoFragment请求数据，返回title和订阅状态
        // 此时取消加载动画
        binding.progress.spin();


        comicId = IntentUtil.getObjectId(this);

        binding.toolbar.setBackListener(v -> finish());



        List<Fragment> fragments = Arrays.asList(ComicInfoFragment.newInstance(comicId),
                ComicCommentFragment.newInstance(4, comicId), ComicRelatedFragment.newInstance(comicId));
        List<String> tabTitles = Arrays.asList("详情", "评论", "相关");

        binding.viewPager.setOffscreenPageLimit(2);
        binding.viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return tabTitles.get(position);
            }
        });

        binding.tableLayout.setupWithViewPager(binding.viewPager);

        setSubscribeStatus();

    }

    private void setSubscribeStatus() {
        String uid = PreferenceHelper.findStringByKey(this, PreferenceHelper.USER_UID);
        if (uid == null) {
            return;
        }
        RetrofitService service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);
        service.isSubscribe(uid, comicId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    if (bean.getCode() == 0) {
                        binding.toolbar.setFavoriteBackgrounds(R.drawable.ic_subscribed);
                        binding.toolbar.setFavoriteContentDescription("1");
                    } else {
                        binding.toolbar.setFavoriteContentDescription("0");
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e("onDestory");
        binding = null;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void loadingDataFinish(String title) {
        new Handler().postDelayed(() -> {
            if (isFinishing()) {
                LogUtil.e("activity is finish");
                return;
            }
            binding.progress.stopSpinning();
            binding.progress.setVisibility(View.GONE);

            if (title == null) {
                binding.noData.setVisibility(View.VISIBLE);
                binding.noData.setText("漫画ID:"+comicId+"\n"+getString(R.string.copyright_error));
            } else {
                binding.viewPager.setVisibility(View.VISIBLE);
                binding.tableLayout.setVisibility(View.VISIBLE);
                binding.toolbar.setTitle(title);
                binding.toolbar.setFavoriteIVVisibility(View.VISIBLE);
                binding.toolbar.setOtherTVVisibility(View.VISIBLE);

                binding.toolbar.setFavoriteListener(v -> {

                    RetrofitService service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);

                    if ("0".equals(binding.toolbar.getFavoriteContentDescription())) {
// 订阅
                        service.subscribeComic(comicId, PreferenceHelper.findStringByKey(this, PreferenceHelper.USER_UID), "mh")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(bean1 -> {
                                    if (bean1.getCode() != 0) {
                                        Toast.makeText(this, getString(R.string.subscribe_fail), Toast.LENGTH_SHORT).show();
                                    } else {
                                        binding.toolbar.setFavoriteBackgrounds(R.drawable.ic_subscribed);
                                        binding.toolbar.setFavoriteContentDescription("1");
                                        Toast.makeText(this, getString(R.string.subscribe_success), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    } else {
                        service.subscribeComic(comicId, PreferenceHelper.findStringByKey(this, PreferenceHelper.USER_UID), "mh")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(bean1 -> {
                                    if (bean1.getCode() != 0) {
                                        Toast.makeText(this, getString(R.string.subscribe_fail), Toast.LENGTH_SHORT).show();
                                    } else {
                                        binding.toolbar.setFavoriteBackgrounds(R.drawable.ic_subscribed);
                                        binding.toolbar.setFavoriteContentDescription("0");

                                        Toast.makeText(this, getString(R.string.subscribe_success), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                binding.toolbar.setOtherListener(v -> {});
            }
        }, 3000);


    }
}