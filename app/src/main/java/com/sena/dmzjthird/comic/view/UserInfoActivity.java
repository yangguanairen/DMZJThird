package com.sena.dmzjthird.comic.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.os.Bundle;

import com.sena.dmzjthird.R;
import com.sena.dmzjthird.account.MyRetrofitService;
import com.sena.dmzjthird.account.fragment.DmzjSubscribedFragment;
import com.sena.dmzjthird.account.fragment.UserCommentFragment;
import com.sena.dmzjthird.account.fragment.UserSubscribedFragment;
import com.sena.dmzjthird.databinding.ActivityUserInfoBinding;
import com.sena.dmzjthird.utils.IntentUtil;

import java.util.Arrays;
import java.util.List;

public class UserInfoActivity extends AppCompatActivity {

    private ActivityUserInfoBinding binding;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserInfoBinding.inflate(getLayoutInflater());
        uid = IntentUtil.getUserId(this);
        setContentView(binding.getRoot());

        binding.toolbar.setBackListener(v -> finish());

        uid = MyRetrofitService.DMZJ_UID;

        List<String> tabTitles = Arrays.asList(getString(R.string.comic_subscribed), getString(R.string.comic_comment),
                getString(R.string.novel_subscribed), getString(R.string.novel_comment));
        List<Fragment> fragments = Arrays.asList(DmzjSubscribedFragment.newInstance(uid, 0), UserCommentFragment.newInstance(0, uid),
                DmzjSubscribedFragment.newInstance(uid, 1), UserCommentFragment.newInstance(1, uid));

        binding.viewPager.setOffscreenPageLimit(3);
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