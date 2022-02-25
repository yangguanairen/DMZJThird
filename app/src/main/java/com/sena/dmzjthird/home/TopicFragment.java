package com.sena.dmzjthird.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.databinding.FragmentTopicBinding;
import com.sena.dmzjthird.news.NewsLatestFragment;
import com.sena.dmzjthird.news.NewsListFragment;
import com.sena.dmzjthird.news.bean.NewsCategoryBean;
import com.sena.dmzjthird.utils.RetrofitHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TopicFragment extends Fragment {

    private FragmentTopicBinding binding;

    private List<Fragment> fragmentList;
    private List<String> tableTitleList;


    public static TopicFragment newInstance() {

        return new TopicFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTopicBinding.inflate(inflater, container, false);

        getResponse();

        return binding.getRoot();
    }

    private void getResponse() {

        RetrofitService service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);
        service.getNewsCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(beans -> {
                    if (beans == null) {
                        // 出错处理
                        return ;
                    }
                    fragmentList = new ArrayList<>();
                    tableTitleList = new ArrayList<>();
                    fragmentList.add(NewsLatestFragment.newInstance());
                    tableTitleList.add("最新");
                    for (NewsCategoryBean b: beans) {
                        fragmentList.add(NewsListFragment.newInstance(b.getTag_id()));
                        tableTitleList.add(b.getTag_name());
                    }

                    initView();

                });

    }

    private void initView() {

        binding.viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
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
                return tableTitleList.get(position);
            }
        });

        binding.viewPager.setOffscreenPageLimit(10);

        binding.tableLayout.setupWithViewPager(binding.viewPager);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}