package com.sena.dmzjthird.comic.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.comic.adapter.ComicFilterAdapter;
import com.sena.dmzjthird.comic.adapter.ComicFilterTagAdapter;
import com.sena.dmzjthird.comic.bean.ComicClassifyBean;
import com.sena.dmzjthird.comic.bean.ComicClassifyFilterBean;
import com.sena.dmzjthird.databinding.ActivityComicFilterBinding;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.RetrofitHelper;
import com.sena.dmzjthird.utils.ViewHelper;

import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ComicFilterActivity extends AppCompatActivity implements ComicFilterTagAdapter.Callbacks {

    private ActivityComicFilterBinding binding;
    private ComicFilterAdapter adapter;
    private RetrofitService service;

    private String filter;
    private String sort = "0";
    private int page = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComicFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        filter = IntentUtil.getClassifyTagId(this);
        service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);

        initView();
        getResponse();

    }

    private void initView() {

        binding.progress.spin();
        binding.toolbar.setBackListener(v -> finish());

        ViewHelper.immersiveStatus(this, binding.drawerLayout);

        initAdapter();
        initDrawerLayout();
        binding.toolbar.setOtherListener(v -> binding.drawerLayout.openDrawer(binding.drawerContent, true));

    }

    private void initAdapter() {
        binding.recyclerview.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new ComicFilterAdapter(this);
        binding.recyclerview.setAdapter(adapter);

        // ??????ComicInfo??????
        adapter.setOnItemClickListener((adapter, view, position) -> IntentUtil.goToComicInfoActivity(this, ((ComicClassifyBean) adapter.getData().get(position)).getId()));

        // ??????????????????
        adapter.getLoadMoreModule().setOnLoadMoreListener(this::getResponse);
        adapter.getLoadMoreModule().setAutoLoadMore(true);
        adapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);

        // filter?????????page??????
        getResponse();

    }


    private void getResponse() {
        LogUtil.e("url: ; " + "https://nnv3api.muwai.com/classifyWithLevel/"+filter+"/"+sort+"/"+page+".json");

        service.getComicClassify(filter, sort, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ComicClassifyBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<ComicClassifyBean> beans) {
                        binding.progress.stopSpinning();
                        binding.progress.setVisibility(View.GONE);
                        if (page == 0 && beans.size() == 0) {
                            onRequestError(true);
                            return;
                        }

                        // ???????????????????????????noData??????
                        onRequestError(false);

                        if (page == 0) {
                            adapter.setList(beans);
                        } else {
                            adapter.addData(beans);
                        }
                        if (beans.isEmpty()) {
                            adapter.getLoadMoreModule().loadMoreEnd();
                        } else {
                            adapter.getLoadMoreModule().loadMoreComplete();
                        }
                        page++;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        onRequestError(true);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initDrawerLayout() {
        binding.recyclerviewDrawer.setLayoutManager(new LinearLayoutManager(this));
        ComicFilterTagAdapter tagAdapter = new ComicFilterTagAdapter(this);
        binding.recyclerviewDrawer.setAdapter(tagAdapter);

        service.getComicClassifyFilter()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ComicClassifyFilterBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<ComicClassifyFilterBean> beans) {
                        String theme = "0";
                        String readership = "0";
                        String status = "0";
                        String area = "0";
                        // ??????filter??????????????????
                        for (ComicClassifyFilterBean.Items i : beans.get(0).getItems()) {
                            if (filter.equals(i.getTag_id())) {
                                theme = filter;
                                break;
                            }
                        }
                        for (ComicClassifyFilterBean.Items i : beans.get(1).getItems()) {
                            if (filter.equals(i.getTag_id())) {
                                readership = filter;
                                break;
                            }
                        }
                        for (ComicClassifyFilterBean.Items i : beans.get(2).getItems()) {
                            if (filter.equals(i.getTag_id())) {
                                status = filter;
                                break;
                            }
                        }
                        for (ComicClassifyFilterBean.Items i : beans.get(3).getItems()) {
                            if (filter.equals(i.getTag_id())) {
                                area = filter;
                                break;
                            }
                        }

                        tagAdapter.setFilterData(sort + "-" + theme + "-" + readership + "-" + status + "-" + area);

                        List<ComicClassifyFilterBean.Items> items = Arrays.asList(
                                new ComicClassifyFilterBean.Items("0", "????????????"),
                                new ComicClassifyFilterBean.Items("1", "????????????")
                        );
                        beans.add(0, new ComicClassifyFilterBean("??????", items));
                        tagAdapter.setList(beans);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void onRequestError(boolean isError) {
        binding.recyclerview.setVisibility(isError ? View.INVISIBLE : View.VISIBLE);
        binding.error.noData.setVisibility(isError ? View.VISIBLE : View.INVISIBLE);
    }

    // ???????????????tag???????????????????????????drawerLayout??????
    @Override
    public void clickTag(String filter, String sort) {
        this.filter = filter;
        this.sort = sort;

        page = 0;
        getResponse();
        new Handler(getMainLooper()).postDelayed(() -> binding.drawerLayout.closeDrawer(binding.drawerContent), 300);
    }
}