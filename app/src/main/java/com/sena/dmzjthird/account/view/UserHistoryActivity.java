package com.sena.dmzjthird.account.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.os.Bundle;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.account.MyRetrofitService;
import com.sena.dmzjthird.account.fragment.UserHistoryFragment;
import com.sena.dmzjthird.databinding.ActivityUserHistoryBinding;

import java.util.Arrays;
import java.util.List;

public class UserHistoryActivity extends AppCompatActivity {

    private ActivityUserHistoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
    }

    private void initView() {

        ImmersionBar.with(this)
                .statusBarColor(R.color.theme_blue)
                .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                .titleBarMarginTop(binding.toolbar)
                .init();

        binding.toolbar.setBackListener(v -> finish());

        List<Fragment> fragmentList = Arrays.asList(UserHistoryFragment.newInstance(MyRetrofitService.TYPE_COMIC),
                UserHistoryFragment.newInstance(MyRetrofitService.TYPE_NOVEL));
        List<String> titleList = Arrays.asList(getString(R.string.icon_comic_title), getString(R.string.icon_novel_title));

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

        binding.tableLayout.setupWithViewPager(binding.viewPager);

    }
}