package com.sena.dmzjthird.comic.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.account.MyRetrofitService;
import com.sena.dmzjthird.comic.adapter.ComicRecommendAdapter;
import com.sena.dmzjthird.comic.bean.ComicRecommendBean;
import com.sena.dmzjthird.comic.bean.ComicRecommendChildBean1;
import com.sena.dmzjthird.comic.bean.ComicRecommendChildBean2;
import com.sena.dmzjthird.comic.bean.ComicRecommendChildBean3;
import com.sena.dmzjthird.comic.bean.ComicTopicBean;
import com.sena.dmzjthird.databinding.FragmentComicRecommendBinding;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.MyDataStore;
import com.sena.dmzjthird.utils.RetrofitHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;


public class ComicRecommendFragment extends Fragment {

    /**
     * 46: 大图推荐-child1
     * 47: 近期必看-Child1
     * 93: 游戏专区-child1
     * 48: 火热专题-child1
     * 50: 猜你喜欢-child2
     * 51: 大师作品-child1
     * 52: 国漫精彩-child1
     * 53: 美漫事件-child1
     * 54: 热门连载-child1
     * 55: 条漫专区-child1
     * 56: 最新上架-child2
     */

    private FragmentComicRecommendBinding binding;

    private ComicRecommendAdapter adapter;
    private final List<ComicRecommendBean> list = new ArrayList<>();

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentComicRecommendBinding.inflate(inflater, container, false);

        binding.progress.spin();

//        binding.refresh.setOnRefreshListener(() -> new Handler().postDelayed(() -> binding.refresh.setRefreshing(false), 5000));

        initBanner();

        initAdapter();

        return binding.getRoot();
    }

    // 集成所有请求结果，集中发送给adapter
    private void setRecommendList(ComicRecommendBean bean) {
        list.add(bean);
        boolean isLogin = 0L == MyDataStore.getInstance(getContext()).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, 0L);
        if (list.size() == (isLogin ? 8 : 9)) {
//            binding.progress.stopSpinning();
//            binding.progress.setVisibility(View.GONE);
            // 设置adapter
            list.sort((o1, o2) -> {
                Integer i1 = o1.getSort();
                Integer i2 = o2.getSort();
                return i1.compareTo(i2);
            });
            adapter.setList(list);
//            new Handler().postDelayed(() -> {
                binding.banner.setVisibility(View.VISIBLE);
                binding.progress.stopSpinning();
                binding.progress.setVisibility(View.GONE);
//            }, 2000);
        }
    }


    private void initBanner() {

        RetrofitService service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);

        service.getComicRecommend1(46)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> binding.banner.setDataList(bean.getData().getData()));
    }


    private void initAdapter() {


        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ComicRecommendAdapter(getActivity());
        binding.recyclerview.setAdapter(adapter);

        RetrofitService service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);

        List<Integer> categoryList = Arrays.asList(47, 51, 52, 53, 54, 55);
        for (int id : categoryList) {
            service.getComicRecommend1(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(generateObserver(ComicRecommendChildBean1.class));
        }

        // 获取猜你喜欢
        service.getComicRecommend2(50)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(generateObserver(ComicRecommendChildBean2.class));

        // 获取我的订阅
        long uid = MyDataStore.getInstance(getContext()).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, 0L);
        if (uid != 0L) {
            service.getComicRecommend3(MyRetrofitService.DMZJ_UID, 49)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(generateObserver(ComicRecommendChildBean3.class));
        }

        // 获取专题
        service.getComicTopic(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    List<ComicRecommendBean.Data> tmp = new ArrayList<>();
                    for (int i = 0; i < 4; i++) {
                        ComicTopicBean.Data data = bean.getData().get(i);
                        tmp.add(new ComicRecommendBean.Data(data.getSmall_cover(), data.getTitle(), null,
                                0, null, data.getId(), null));
                    }
                    setRecommendList(new ComicRecommendBean(48,
                            "火热专题", 5, tmp));
                });
    }

    private <T> Observer<T> generateObserver(T t) {
        return new Observer<T>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull T t1) {
                if (t1.getClass().equals(ComicRecommendChildBean2.class)) {

                    ComicRecommendChildBean2.Data1 bean = ((ComicRecommendChildBean2) t1).getData();
                    List<ComicRecommendBean.Data> tmp = new ArrayList<>();
                    for (ComicRecommendChildBean2.Data1.Data data : bean.getData()) {
                        tmp.add(new ComicRecommendBean.Data(data.getCover(), data.getTitle(), data.getAuthors(),
                                0, null, data.getId(), data.getStatus()));
                    }

                    setRecommendList(new ComicRecommendBean(bean.getCategory_id(),
                            bean.getTitle(), bean.getSort(), tmp));

                } else if(t1.getClass().equals(ComicRecommendChildBean3.class)) {

                    ComicRecommendChildBean3.Data1 bean = ((ComicRecommendChildBean3) t1).getData();
                    List<ComicRecommendBean.Data> tmp = new ArrayList<>();
                    for (ComicRecommendChildBean3.Data1.Data data: bean.getData()) {
                        tmp.add( new ComicRecommendBean.Data(data.getCover(), data.getTitle(), data.getAuthors(),
                                0, null, data.getId(), data.getStatus()));
                    }

                    setRecommendList(new ComicRecommendBean(bean.getCategory_id(),bean.getTitle(),
                            bean.getSort(), tmp));

                } else {
                    setRecommendList(((ComicRecommendChildBean1) t1).getData());
                }

            }

            @Override
            public void onError(@NonNull Throwable e) {
                if (e instanceof HttpException) {
                    LogUtil.d("HttpError: " + ((HttpException) e).code());
                } else {
                    LogUtil.d("OtherError: " + e.getMessage());
                }
            }

            @Override
            public void onComplete() {

            }
        };
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }




}