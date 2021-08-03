package com.sena.dmzjthird.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sena.dmzjthird.R;
import com.sena.dmzjthird.comic.fragment.ComicClassifyFragment;
import com.sena.dmzjthird.comic.fragment.ComicRankFragment;
import com.sena.dmzjthird.comic.fragment.ComicRecommendFragment;
import com.sena.dmzjthird.comic.fragment.ComicTopicFragment;
import com.sena.dmzjthird.comic.fragment.ComicUpdateFragment;
import com.sena.dmzjthird.databinding.FragmentComicBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;


public class ComicFragment extends Fragment {

    private FragmentComicBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentComicBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        List<Fragment> fragments = Arrays.asList(new ComicRecommendFragment(), new ComicUpdateFragment(),
                new ComicClassifyFragment(), new ComicRankFragment(), new ComicTopicFragment());
        List<String> tabTitles = Arrays.asList(getString(R.string.recommend), getString(R.string.update),
                getString(R.string.classify), getString(R.string.rank), getString(R.string.topic));

        for (String s: tabTitles) {
            binding.tableLayout.addTab(binding.tableLayout.newTab().setText(s).setIcon(R.drawable.ic_search));
        }

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



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}