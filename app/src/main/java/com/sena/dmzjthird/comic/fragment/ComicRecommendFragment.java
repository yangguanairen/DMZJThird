package com.sena.dmzjthird.comic.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sena.dmzjthird.R;
import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.comic.adapter.ComicRecommendAdapter;
import com.sena.dmzjthird.comic.bean.ComicRecommendBean;
import com.sena.dmzjthird.comic.bean.ComicRecommendChildBean1;
import com.sena.dmzjthird.comic.bean.ComicRecommendChildBean2;
import com.sena.dmzjthird.custom.ProgressWheel;
import com.sena.dmzjthird.databinding.FragmentComicRecommendBinding;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.RetrofitHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
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
    private List<ComicRecommendBean> list = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentComicRecommendBinding.inflate(inflater, container, false);


//        binding.progress.spin();

        initAdapter();


        return binding.getRoot();
    }





    private void setRecommendList(ComicRecommendBean bean) {
        list.add(bean);
        if (list.size() == 8) {
            binding.progress.stopSpinning();
            binding.progress.setVisibility(View.GONE);
            // 设置adapter
            list.sort((o1, o2) -> {
                Integer i1 = o1.getCategory_id();
                Integer i2 = o2.getCategory_id();
                return i1.compareTo(i2);
            });
            adapter.setList(list);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void initAdapter() {


        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ComicRecommendAdapter(getActivity());
        binding.recyclerview.setAdapter(adapter);

        RetrofitService service = RetrofitHelper.getServer(RetrofitService.BASE_URL);

        List<Integer> categoryList = Arrays.asList(47, 48, 51, 52, 53, 54, 55);
        for (int id: categoryList) {
            service.getComicRecommend1(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ComicRecommendChildBean1>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull ComicRecommendChildBean1 bean) {
                            setRecommendList(bean.getData());
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

        service.getComicRecommend2(50)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ComicRecommendChildBean2>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ComicRecommendChildBean2 bean) {
                        List<ComicRecommendBean.Data> tmp = new ArrayList<>();
                        for (ComicRecommendChildBean2.Data1.Data data: bean.getData().getData()) {
                            tmp.add(new ComicRecommendBean.Data(data.getCover(), data.getTitle(),data.getAuthors(),
                                    0, null, data.getId(), data.getStatus()));
                        }
                        setRecommendList(new ComicRecommendBean(bean.getData().getCategory_id(),
                                bean.getData().getTitle(), bean.getData().getSort(), tmp));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}