package com.sena.dmzjthird.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sena.dmzjthird.databinding.FragmentNovelHomeBinding;
import com.sena.dmzjthird.novel.fragment.NovelCategoryFragment;
import com.sena.dmzjthird.novel.fragment.NovelLatestFragment;
import com.sena.dmzjthird.novel.fragment.NovelRankFragment;
import com.sena.dmzjthird.novel.fragment.NovelRecommendFragment;

import java.util.Arrays;
import java.util.List;

public class NovelHomeFragment extends Fragment {

    private FragmentNovelHomeBinding binding;
    private BroadcastReceiver receiver;

    private List<Fragment> fragmentList;
    private List<String> titleList;

    public static NovelHomeFragment newInstance() {
        return new NovelHomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentNovelHomeBinding.inflate(inflater, container, false);

        fragmentList = Arrays.asList(NovelRecommendFragment.newInstance(), NovelLatestFragment.newInstance(),
                NovelCategoryFragment.newInstance(), NovelRankFragment.newInstance());
        titleList = Arrays.asList("推荐", "更新", "分类", "排行");

        initView();
        initBroadcast();

        return binding.getRoot();
    }

    private void initView() {

        binding.viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @Override
            public int getCount() {
                return fragmentList.size();
            }

            @NonNull
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titleList.get(position);
            }
        });
        binding.viewPager.setOffscreenPageLimit(3);
        binding.tableLayout.setupWithViewPager(binding.viewPager);

        binding.search.setOnClickListener(v -> {
            // 跳转小说搜索

        });
    }

    private void initBroadcast() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                binding.viewPager.setCurrentItem(1);
            }
        };
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getContext());
        manager.registerReceiver(receiver, new IntentFilter("goToNovelLatest"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getContext());
        manager.unregisterReceiver(receiver);
        binding = null;
    }
}