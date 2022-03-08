package com.sena.dmzjthird.novel.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.os.Bundle;

import com.sena.dmzjthird.databinding.ActivityNovelInfoBinding;
import com.sena.dmzjthird.novel.fragment.NovelChapterFragment;
import com.sena.dmzjthird.novel.fragment.NovelInfoFragment;
import com.sena.dmzjthird.utils.IntentUtil;

import java.util.Arrays;
import java.util.List;

public class NovelInfoActivity extends AppCompatActivity implements NovelInfoFragment.Callbacks {

    private ActivityNovelInfoBinding binding;

    private List<Fragment> fragmentList;
    private List<String> titleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNovelInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String novelId = IntentUtil.getObjectId(this);

        fragmentList = Arrays.asList(NovelInfoFragment.newInstance(novelId), NovelChapterFragment.newInstance(novelId));
        titleList = Arrays.asList("详情", "章节");

        initView();

    }

    private void initView() {

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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onLoadInfoEnd(String title) {
        binding.toolbar.setBackListener(v -> finish());

        if (title == null) {
            // 出错处理
            return ;
        }
        binding.toolbar.setTitle(title);
        // 订阅
        binding.toolbar.setFavoriteListener(v -> {

        });
        // 下载
        binding.toolbar.setOtherListener(v -> {

        });
    }
}