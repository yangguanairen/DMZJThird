package com.sena.dmzjthird.novel.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.sena.dmzjthird.account.MyRetrofitService;
import com.sena.dmzjthird.databinding.ActivityNovelInfoBinding;
import com.sena.dmzjthird.novel.fragment.NovelChapterFragment;
import com.sena.dmzjthird.novel.fragment.NovelInfoFragment;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.ViewHelper;

import java.util.Arrays;
import java.util.List;

public class NovelInfoActivity extends AppCompatActivity implements NovelInfoFragment.Callbacks {

    private ActivityNovelInfoBinding binding;

    private List<Fragment> fragmentList;
    private List<String> titleList;
    private String novelId;
    private String novelCover;
    private String novelName;
    private String novelAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNovelInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.progress.spin();

        novelId = IntentUtil.getObjectId(this);

        fragmentList = Arrays.asList(NovelInfoFragment.newInstance(novelId), NovelChapterFragment.newInstance(novelId));
        titleList = Arrays.asList("详情", "章节");

        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        ViewHelper.setSubscribeStatus(this, novelId, MyRetrofitService.TYPE_NOVEL, binding.toolbar.getFavoriteIV(), null);
    }

    private void initView() {

        ViewHelper.immersiveStatus(this, binding.toolbar);

        binding.toolbar.setBackListener(v -> finish());

        binding.viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titleList.get(position);
            }
        });
        binding.viewPager.setOffscreenPageLimit(2);

        binding.tableLayout.setupWithViewPager(binding.viewPager);

        // 订阅
        binding.toolbar.setFavoriteListener(v ->
                ViewHelper.controlSubscribe(this, novelId, novelCover, novelName, novelAuthor, MyRetrofitService.TYPE_NOVEL,
                        binding.toolbar.getFavoriteIV(), null));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onLoadInfoEnd(String title, String cover, String author) {

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (binding == null) return ;
            binding.progress.stopSpinning();
            binding.constLayout.removeView(binding.progress);


            if (title == null) {
                // 出错处理
                binding.error.noData.setVisibility(View.VISIBLE);
                return;
            }
            binding.contentLayout.setVisibility(View.VISIBLE);

            binding.toolbar.setTitle(title);
            binding.toolbar.setFavoriteVisibility(View.VISIBLE);
            novelCover = cover;
            novelName = title;
            novelAuthor = author;
        }, 500);


    }
}