package com.sena.dmzjthird.home;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    private FragmentManager fragmentManager;
    private ComicFragment comicFragment;
    private TopicFragment topicFragment;
    private NovelHomeFragment novelHomeFragment;
    private AccountFragment accountFragment;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());


        fragmentManager = getSupportFragmentManager();

        initView();

        initClick();

    }

    private void initView() {

        // 隐藏底部手势导航栏
        ImmersionBar.with(this)
                .statusBarColor(R.color.theme_blue)
                .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                .titleBarMarginTop(binding.container)
                .init();

        comicFragment = ComicFragment.newInstance();
        topicFragment = TopicFragment.newInstance();
        novelHomeFragment = NovelHomeFragment.newInstance();
        accountFragment = AccountFragment.newInstance();

        navigateFragment(comicFragment, "comicFragment");
    }

    private void initClick() {
        binding.comicLayout.setOnClickListener(v -> navigateFragment(comicFragment, "comicFragment"));

        binding.topicLayout.setOnClickListener(v -> navigateFragment(topicFragment, "topicFragment"));

        binding.novelLayout.setOnClickListener(v -> navigateFragment(novelHomeFragment, "novelHomeFragment"));

        binding.accountLayout.setOnClickListener(v -> navigateFragment(accountFragment, "accountFragment"));
    }

    private void navigateFragment(Fragment fragment, String tag) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (currentFragment == fragment) return ;
        for (Fragment f: fragmentManager.getFragments()) {
            transaction.hide(f);
        }
        if (!fragment.isAdded()) transaction.add(R.id.contentView, fragment, tag);
        transaction.show(fragment);
        transaction.setReorderingAllowed(true);
        transaction.commit();

        currentFragment = fragment;
        changeColor();
    }

    private void changeColor() {

        binding.comicTitle.setTextColor(currentFragment == comicFragment ? getColor(R.color.theme_blue) : Color.GRAY);
        binding.topicTitle.setTextColor(currentFragment == topicFragment ? getColor(R.color.theme_blue) : Color.GRAY);
        binding.novelTitle.setTextColor(currentFragment == novelHomeFragment ? getColor(R.color.theme_blue) : Color.GRAY);
        binding.accountTitle.setTextColor(currentFragment == accountFragment ? getColor(R.color.theme_blue) : Color.GRAY);

        binding.comicIcon.setImageResource(currentFragment == comicFragment ? R.drawable.ic_comic_blue : R.drawable.ic_comic);
        binding.topicIcon.setImageResource(currentFragment == topicFragment ? R.drawable.ic_topic_blue : R.drawable.ic_topic);
        binding.novelIcon.setImageResource(currentFragment == novelHomeFragment ? R.drawable.ic_novel_blue : R.drawable.ic_novel);
        binding.accountIcon.setImageResource(currentFragment == accountFragment ? R.drawable.ic_account_blue : R.drawable.ic_account);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}