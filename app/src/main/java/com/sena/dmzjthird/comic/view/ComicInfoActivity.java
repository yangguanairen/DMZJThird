package com.sena.dmzjthird.comic.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.os.Bundle;

import com.sena.dmzjthird.R;
import com.sena.dmzjthird.comic.fragment.ComicCommentFragment;
import com.sena.dmzjthird.comic.fragment.ComicInfoFragment;
import com.sena.dmzjthird.comic.fragment.ComicRelatedFragment;
import com.sena.dmzjthird.databinding.ActivityComicInfoBinding;

import java.util.Arrays;
import java.util.List;

public class ComicInfoActivity extends AppCompatActivity {

    private ActivityComicInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComicInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // infoFragment请求数据，返回title和订阅状态
        // 此时取消加载动画
        binding.progress.spin();

        String comicId = getIntent().getStringExtra(getString(R.string.intent_comic_id));

        List<Fragment> fragments = Arrays.asList(ComicInfoFragment.newInstance(comicId),
                ComicCommentFragment.newInstance(comicId), ComicRelatedFragment.newInstance(comicId));
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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}