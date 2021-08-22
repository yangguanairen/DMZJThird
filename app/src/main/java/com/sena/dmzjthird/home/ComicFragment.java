package com.sena.dmzjthird.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sena.dmzjthird.R;
import com.sena.dmzjthird.account.UserSubscribedActivity;
import com.sena.dmzjthird.comic.adapter.ComicRecommendAdapter;
import com.sena.dmzjthird.comic.fragment.ComicClassifyFragment;
import com.sena.dmzjthird.comic.fragment.ComicRankFragment;
import com.sena.dmzjthird.comic.fragment.ComicRecommendFragment;
import com.sena.dmzjthird.comic.fragment.ComicTopicFragment;
import com.sena.dmzjthird.comic.fragment.ComicLatestFragment;
import com.sena.dmzjthird.comic.view.ComicSearchActivity;
import com.sena.dmzjthird.databinding.FragmentComicBinding;
import com.sena.dmzjthird.utils.IntentUtil;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;


public class ComicFragment extends Fragment {

    private FragmentComicBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentComicBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.search.setOnClickListener(v -> startActivity(new Intent(getActivity(), ComicSearchActivity.class)));

        List<Fragment> fragments = Arrays.asList(new ComicRecommendFragment(), new ComicLatestFragment(),
                new ComicClassifyFragment(), new ComicRankFragment(), new ComicTopicFragment());
        List<String> tabTitles = Arrays.asList(getString(R.string.recommend), getString(R.string.update),
                getString(R.string.classify), getString(R.string.rank), getString(R.string.topic));

        binding.viewPager.setOffscreenPageLimit(4);

        binding.viewPager.setAdapter(new FragmentPagerAdapter(getActivity().getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @NotNull
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Nullable
            @org.jetbrains.annotations.Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return tabTitles.get(position);
            }


        });


        binding.tableLayout.setupWithViewPager(binding.viewPager);


        initBroadcast();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initBroadcast() {

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                binding.viewPager.setCurrentItem(4);
            }
        };

        IntentFilter filter = new IntentFilter(ComicRecommendAdapter.BROADCAST_INTENT);
        getActivity().registerReceiver(receiver, filter);


    }
}