package com.sena.dmzjthird.novel.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.databinding.ActivityNovelFilterBinding;
import com.sena.dmzjthird.novel.adapter.NovelFilterAdapter;
import com.sena.dmzjthird.novel.adapter.NovelFilterTagAdapter;
import com.sena.dmzjthird.novel.bean.NovelFilterBean;
import com.sena.dmzjthird.novel.bean.NovelFilterTagBean;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.RetrofitHelper;
import com.sena.dmzjthird.utils.ViewHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class NovelFilterActivity extends AppCompatActivity implements NovelFilterTagAdapter.Callbacks {

    private ActivityNovelFilterBinding binding;
    private RetrofitService service;
    private NovelFilterAdapter filterAdapter;
    private NovelFilterTagAdapter tagAdapter;

    private String tagName = null;
    private int sort = 0;
    private int theme = 0;
    private int status = 0;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNovelFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);

        tagName = IntentUtil.getObjectName(this);

        initView();

    }

    private void initView() {

        ViewHelper.immersiveStatus(this, binding.drawerLayout);


        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        filterAdapter = new NovelFilterAdapter(this);
        binding.recyclerView.setAdapter(filterAdapter);

        filterAdapter.setOnItemClickListener((a, view, position) -> {
            NovelFilterBean bean = (NovelFilterBean) a.getData().get(position);
            String novelId = bean.getId();
            // 跳转轻小说详情页
            IntentUtil.goToNovelInfoActivity(this, novelId);
        });

        filterAdapter.getLoadMoreModule().setOnLoadMoreListener(this::getResponse);
        filterAdapter.getLoadMoreModule().setAutoLoadMore(true);
        filterAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);

        binding.toolbar.setBackListener(v -> finish());
        binding.toolbar.setOtherListener(v -> binding.drawerLayout.openDrawer(binding.drawerContent, true));
        binding.refreshLayout.setOnRefreshListener(() -> {
            page = 0;
            getResponse();
        });

        initDrawerLayout();

    }

    private void initDrawerLayout() {

        service.getNovelFilterTag()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<NovelFilterTagBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<NovelFilterTagBean> beanList) {

                        for (NovelFilterTagBean.NovelFilterItem item : beanList.get(0).getItems()) {
                            if (tagName.equals(item.getTag_name())) {
                                theme = item.getTag_id();
                                break;
                            }
                        }
                        for (NovelFilterTagBean.NovelFilterItem item : beanList.get(1).getItems()) {
                            if (tagName.equals(item.getTag_name())) {
                                status = item.getTag_id();
                                break;
                            }
                        }

                        NovelFilterTagBean tagBean = new NovelFilterTagBean();
                        tagBean.setTitle("排序");
                        List<NovelFilterTagBean.NovelFilterItem> itemList = new ArrayList<>();
                        itemList.add(new NovelFilterTagBean.NovelFilterItem(0, "人气排序"));
                        itemList.add(new NovelFilterTagBean.NovelFilterItem(1, "更新排序"));
                        tagBean.setItems(itemList);
                        beanList.add(0, tagBean);

                        binding.recyclerViewDrawer.setLayoutManager(new LinearLayoutManager(NovelFilterActivity.this));
                        tagAdapter = new NovelFilterTagAdapter(NovelFilterActivity.this, sort, theme, status);
                        binding.recyclerViewDrawer.setAdapter(tagAdapter);
                        tagAdapter.setList(beanList);

                        getResponse();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(NovelFilterActivity.this, "获取小说筛选Tag失败!!", Toast.LENGTH_SHORT).show();
                        getResponseIsError(true);
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private void getResponse() {

        service.getNovelFilter(theme, status, sort, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<NovelFilterBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<NovelFilterBean> beanList) {
                        binding.refreshLayout.setRefreshing(false);

                        if (page == 0 && beanList.size() == 0) {
                            getResponseIsError(true);
                            return;
                        }

                        getResponseIsError(false);

                        if (page == 0) {
                            filterAdapter.setList(beanList);
                            binding.recyclerView.scrollToPosition(0);
                        } else {
                            filterAdapter.addData(beanList);
                        }
                        if (beanList.size() == 0) {
                            filterAdapter.getLoadMoreModule().loadMoreEnd();
                        } else {
                            filterAdapter.getLoadMoreModule().loadMoreComplete();
                        }
                        page++;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getResponseIsError(true);
                        if (e instanceof HttpException) {
                            LogUtil.e("HttpError: " + ((HttpException) e).code());
                        } else {
                            LogUtil.e("OtherError: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private void getResponseIsError(boolean isError) {
        binding.progress.stopSpinning();
        binding.progress.setVisibility(View.GONE);
        if (isError) {
            binding.recyclerView.setVisibility(View.INVISIBLE);
            binding.error.noData.setVisibility(View.VISIBLE);
        } else {
            binding.error.noData.setVisibility(View.INVISIBLE);
            binding.recyclerView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onFilterItemChange(int theme, int status, int sort) {
        this.theme = theme;
        this.status = status;
        this.sort = sort;
        page = 0;
        getResponse();
        binding.drawerLayout.closeDrawer(binding.drawerContent, true);
    }
}