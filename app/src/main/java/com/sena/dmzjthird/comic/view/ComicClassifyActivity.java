package com.sena.dmzjthird.comic.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.sena.dmzjthird.R;
import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.comic.adapter.ComicClassifyAdapter;
import com.sena.dmzjthird.comic.adapter.ComicFilterAdapter;
import com.sena.dmzjthird.comic.bean.ComicClassifyBean;
import com.sena.dmzjthird.comic.bean.ComicClassifyFilterBean;
import com.sena.dmzjthird.databinding.ActivityComicClassifyBinding;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.RetrofitHelper;

import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class ComicClassifyActivity extends AppCompatActivity implements ComicFilterAdapter.Callbacks {

    private ActivityComicClassifyBinding binding;
    private ComicClassifyAdapter adapter;
    private RetrofitService service;

    private String filter;
    private String sort = "0";
    private int page = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComicClassifyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        filter = getIntent().getStringExtra(getString(R.string.intent_classify_id));
        service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);

        binding.progress.spin();
        binding.toolbar.setBackListener(v -> finish());

        // 初始化DrawerLayout
        initDrawerLayout();
        binding.toolbar.setOtherListener(v -> binding.drawerLayout.openDrawer(binding.drawerContent, true));

        initAdapter();

    }

    private void initAdapter() {
        binding.recyclerview.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new ComicClassifyAdapter(this);
        binding.recyclerview.setAdapter(adapter);

        // 跳转ComicInfo页面
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(ComicClassifyActivity.this, ComicInfoActivity.class);
            intent.putExtra(getString(R.string.intent_comic_id), ((ComicClassifyBean) adapter.getData().get(position)).getId());
            startActivity(intent);
        });

        // 设置自动加载
        adapter.getLoadMoreModule().setOnLoadMoreListener(this::getResponse);
        adapter.getLoadMoreModule().setAutoLoadMore(true);
        adapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);

        // filter改变，page置零
        page = 0;
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
                        if (beans.size() == 0) {
                            binding.noData.setVisibility(View.VISIBLE);
                            return;
                        }
                        // 防止上一次请求导致noData显示
                        binding.noData.setVisibility(View.INVISIBLE);
                        binding.recyclerview.setVisibility(View.VISIBLE);
                        if (beans.size() < 10) {
                            adapter.getLoadMoreModule().loadMoreEnd();
                        } else {
                            adapter.getLoadMoreModule().loadMoreComplete();
                            page++;
                        }
                        if (page == 0) {
                            adapter.setList(beans);
                        } else {
                            adapter.addData(beans);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
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

    private void initDrawerLayout() {
        binding.recyclerviewDrawer.setLayoutManager(new LinearLayoutManager(this));
        ComicFilterAdapter filterAdapter = new ComicFilterAdapter(this);
        binding.recyclerviewDrawer.setAdapter(filterAdapter);

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
                        // 寻找filter归属于哪个类
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

                        filterAdapter.setFilterData(sort + "-" + theme + "-" + readership + "-" + status + "-" + area);

                        List<ComicClassifyFilterBean.Items> items = Arrays.asList(
                                new ComicClassifyFilterBean.Items("0", "人气排序"),
                                new ComicClassifyFilterBean.Items("1", "更新排序")
                        );
                        beans.add(0, new ComicClassifyFilterBean("排序", items));
                        filterAdapter.setList(beans);
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

    // 实现接口，tag被点击，刷新数据，drawerLayout关闭
    @Override
    public void clickTag(String filter, String sort) {
        this.filter = filter;
        this.sort = sort;

        initAdapter();
        new Handler().postDelayed(() -> binding.drawerLayout.closeDrawer(binding.drawerContent), 300);
    }
}