package com.sena.dmzjthird.novel.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lxj.xpopup.XPopup;
import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.databinding.FragmentNovelRankBinding;
import com.sena.dmzjthird.novel.adapter.NovelRankAdapter;
import com.sena.dmzjthird.novel.bean.NovelRankBean;
import com.sena.dmzjthird.novel.bean.NovelRankTagBean;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.RetrofitHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class NovelRankFragment extends Fragment {

    private FragmentNovelRankBinding binding;
    private RetrofitService service;
    private NovelRankAdapter adapter;

    private boolean isLoaded = false;

    private List<Integer> tagIdList;
    private String[] tagNameList;
    private int selectedTagIndex = 0;
    private int selectSortIndex = 0;
    private int page = 0;

    public static NovelRankFragment newInstance() {
        return new NovelRankFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentNovelRankBinding.inflate(inflater, container, false);
        service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);

        initView();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isLoaded) return ;
        isLoaded = false;
        lazyLoad();
    }

    private void lazyLoad() {
        binding.refreshLayout.setRefreshing(true);
        getResponse();

        initData();
    }

    private void initData() {

        service.getNovelRankTag()
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<NovelRankTagBean>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<NovelRankTagBean> beanList) {
                        tagIdList = new ArrayList<>();
                        tagNameList = new String[beanList.size()];
                        for (int i = 0; i < beanList.size(); i++) {
                            tagIdList.add(beanList.get(i).getTag_id());
                            tagNameList[i] = beanList.get(i).getTag_name();
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Toast.makeText(getContext(), "获取轻小说排行Tag失败!!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initView() {

        initAdapter();

        binding.refreshLayout.setOnRefreshListener(() -> {
            page = 0;
            getResponse();
        });

        binding.tag.setOnClickListener(v -> {
            if (tagNameList == null || tagNameList.length == 0) {
                Toast.makeText(getContext(), "暂无可选项", Toast.LENGTH_SHORT).show();
                return ;
            }
            new XPopup.Builder(getContext())
                    .borderRadius(10)
                    .maxHeight(1000)
                    .atView(binding.tag)
                    .asCenterList("", tagNameList, new int[]{}, selectedTagIndex, (position, text) -> {
                        if (selectedTagIndex == position) return ;

                        selectedTagIndex = position;
                        binding.tag.setText(text);

                        initAdapter();
                        getResponse();

                    })
                    .show();
        });

        binding.sort.setOnClickListener(v -> {
            if (tagNameList == null || tagNameList.length == 0) {
                Toast.makeText(getContext(), "暂无可选项", Toast.LENGTH_SHORT).show();
                return ;
            }
            new XPopup.Builder(getContext())
                    .borderRadius(10)
                    .atView(binding.sort)
                    .asCenterList("", new String[]{"人气排行", "订阅排行"}, new int[]{}, selectSortIndex, (position, text) -> {
                        if (selectSortIndex == position) return ;

                        selectSortIndex = position;
                        binding.sort.setText(text);

                        initAdapter();
                        getResponse();
                    })
                    .show();
        });

    }

    private void initAdapter() {

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NovelRankAdapter(getContext());
        binding.recyclerView.setAdapter(adapter);

        adapter.setOnItemChildClickListener((a, view, position) -> {
            NovelRankBean bean = (NovelRankBean) a.getData().get(position);
            String novelId = bean.getId();
            // 跳转小说详情页
            IntentUtil.goToNovelInfoActivity(getContext(), novelId);
        });
        adapter.getLoadMoreModule().setOnLoadMoreListener(this::getResponse);
        adapter.getLoadMoreModule().setAutoLoadMore(true);
        adapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
    }

    private void getResponse() {

        int tag = tagIdList == null ? 0 : tagIdList.get(selectedTagIndex);

        service.getNovelRank(tag, selectedTagIndex, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<NovelRankBean>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<NovelRankBean> beanList) {
                        binding.refreshLayout.setRefreshing(false);
                        if (beanList.size() == 0) {
                            // 出错处理
                            return ;
                        }

                        if (page == 0) {
                            adapter.setList(beanList);
                        } else {
                            adapter.addData(beanList);
                        }

                        if (beanList.size() == 0) {
                            adapter.getLoadMoreModule().loadMoreEnd();
                        }
                        adapter.getLoadMoreModule().loadMoreComplete();
                        page++;
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        // 出错处理
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}