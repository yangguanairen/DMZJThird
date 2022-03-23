package com.sena.dmzjthird.comic.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.sena.dmzjthird.ErrorFragment;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.account.MyRetrofitService;
import com.sena.dmzjthird.comic.fragment.CommentFragment;
import com.sena.dmzjthird.comic.fragment.ComicInfoFragment;
import com.sena.dmzjthird.comic.fragment.ComicRelatedFragment;
import com.sena.dmzjthird.databinding.ActivityComicInfoBinding;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.ViewHelper;

import java.util.Arrays;
import java.util.List;

public class ComicInfoActivity extends AppCompatActivity implements ComicInfoFragment.Callbacks {

    private ActivityComicInfoBinding binding;

    private String comicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComicInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.progress.spin();

        comicId = IntentUtil.getObjectId(this);

        ImmersionBar.with(this)
                .statusBarColor(R.color.theme_blue)
                .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                .titleBarMarginTop(binding.toolbar)
                .init();

        binding.toolbar.setBackListener(v -> finish());


        List<Fragment> fragments = Arrays.asList(ComicInfoFragment.newInstance(comicId),
                CommentFragment.newInstance(4, comicId), ComicRelatedFragment.newInstance(comicId));
        List<String> tabTitles = Arrays.asList("详情", "评论", "相关");

//        binding.viewPager.setOffscreenPageLimit(2);
        binding.viewPager.setCurrentItem(1);
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

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//                防止被销毁
//                super.destroyItem(container, position, object);

            }
        });

        binding.tableLayout.setupWithViewPager(binding.viewPager);

        ViewHelper.setSubscribeStatus(this, comicId, MyRetrofitService.TYPE_COMIC, binding.toolbar.getFavoriteIV(), null);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ViewHelper.setSubscribeStatus(this, comicId, MyRetrofitService.TYPE_COMIC, binding.toolbar.getFavoriteIV(), null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e("onDestroy");
        binding = null;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void loadingDataFinish(String title, String cover, String author) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (isFinishing() || binding == null) {
                LogUtil.e("activity is finish");
                return;
            }
            binding.progress.stopSpinning();
            binding.progress.setVisibility(View.GONE);
            binding.getRoot().removeView(binding.progress);

            if (title == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, ErrorFragment.newInstance(comicId)).commit();
//                binding.noData.setVisibility(View.VISIBLE);
//                binding.noData.setText("漫画ID:"+comicId+"\n"+getString(R.string.copyright_error));
            } else {
                binding.layoutContent.setVisibility(View.VISIBLE);
//                binding.viewPager.setVisibility(View.VISIBLE);
//                binding.tableLayout.setVisibility(View.VISIBLE);
                binding.toolbar.setTitle(title);
                binding.toolbar.setFavoriteVisibility(View.VISIBLE);
                binding.toolbar.setOtherVisibility(View.VISIBLE);

                binding.toolbar.setFavoriteListener(v -> ViewHelper.controlSubscribe(this, comicId, cover, title, author, MyRetrofitService.TYPE_COMIC, binding.toolbar.getFavoriteIV(), null));



                binding.toolbar.setOtherListener(v -> IntentUtil.goToComicDownloadActivity(this, comicId, title, cover));
            }
        }, 3000);


    }
}