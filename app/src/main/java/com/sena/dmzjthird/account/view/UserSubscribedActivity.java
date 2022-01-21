package com.sena.dmzjthird.account.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.os.Bundle;

import com.sena.dmzjthird.R;
import com.sena.dmzjthird.account.fragment.UserSubscribedFragment;
import com.sena.dmzjthird.databinding.ActivityUserSubscribedBinding;
import com.sena.dmzjthird.utils.MyDataStore;
import com.sena.dmzjthird.utils.PreferenceHelper;

import java.util.Arrays;
import java.util.List;

public class UserSubscribedActivity extends AppCompatActivity {

    private ActivityUserSubscribedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserSubscribedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setBackListener(v -> finish());

        String uid = MyDataStore.getInstance(this).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, "");

        List<Fragment> fragments = Arrays.asList(UserSubscribedFragment.newInstance(0, uid),
                UserSubscribedFragment.newInstance(1, uid));
        List<String> tabTitles = Arrays.asList(getString(R.string.icon_comic_title), getString(R.string.icon_novel_title));

        binding.viewPager.setOffscreenPageLimit(1);
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
}