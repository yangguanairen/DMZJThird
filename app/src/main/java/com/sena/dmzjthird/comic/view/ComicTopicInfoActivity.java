package com.sena.dmzjthird.comic.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.sena.dmzjthird.R;
import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.comic.fragment.ComicCommentFragment;
import com.sena.dmzjthird.comic.fragment.ComicTopicInfoFragment;
import com.sena.dmzjthird.comic.fragment.ComicTopicInfoRelatedFragment;
import com.sena.dmzjthird.databinding.ActivityComicTopicInfoBinding;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.RetrofitHelper;

import java.util.Arrays;
import java.util.List;

public class ComicTopicInfoActivity extends AppCompatActivity implements ComicTopicInfoRelatedFragment.Callbacks {

    private ActivityComicTopicInfoBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComicTopicInfoBinding.inflate(getLayoutInflater());
        String topicId = IntentUtil.getTopicId(this);

        binding.progress.spin();
        binding.toolbar.setBackListener(v -> finish());

        setContentView(binding.getRoot());
        binding.viewPager.setOffscreenPageLimit(2);

        List<Fragment> fragments = Arrays.asList(new ComicTopicInfoFragment(),
                ComicTopicInfoRelatedFragment.newInstance(topicId), ComicCommentFragment.newInstance(2, topicId));
        List<String> tabTitles = Arrays.asList("介绍", getString(R.string.icon_comic_title), "评论");


        binding.viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @androidx.annotation.NonNull
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
    public void loadDataFinish(String title) {
        new Handler().postDelayed(() -> {
            if (isFinishing()) {
                return ;
            }
            binding.progress.stopSpinning();
            binding.progress.setVisibility(View.GONE);
            if (title == null) {
                binding.noData.setVisibility(View.VISIBLE);
            } else {
                binding.toolbar.setTitle(title);
                binding.tableLayout.setVisibility(View.VISIBLE);
                binding.viewPager.setVisibility(View.VISIBLE);
            }
        }, 2000);
    }
}