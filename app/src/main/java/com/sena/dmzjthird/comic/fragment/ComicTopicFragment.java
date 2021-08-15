package com.sena.dmzjthird.comic.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sena.dmzjthird.R;
import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.comic.adapter.ComicTopicAdapter;
import com.sena.dmzjthird.comic.bean.ComicTopicBean;
import com.sena.dmzjthird.comic.view.ComicTopicInfoActivity;
import com.sena.dmzjthird.databinding.FragmentComicTopicBinding;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.RetrofitHelper;

import org.jetbrains.annotations.NotNull;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;


public class ComicTopicFragment extends Fragment {

    private FragmentComicTopicBinding binding;
    private ComicTopicAdapter adapter;
    private int page = 0;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentComicTopicBinding.inflate(inflater, container, false);

        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ComicTopicAdapter(getActivity());
        binding.recyclerview.setAdapter(adapter);

        adapter.setOnItemClickListener((adapter, view, position) -> {
            // 跳转
            Intent intent = new Intent(getActivity(), ComicTopicInfoActivity.class);
            intent.putExtra(getString(R.string.intent_topic_id), ((ComicTopicBean.Data) adapter.getData().get(position)).getId());
            startActivity(intent);
        });

        adapter.getLoadMoreModule().setOnLoadMoreListener(this::getResponse);
        adapter.getLoadMoreModule().setAutoLoadMore(true);
        adapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(true);

        getResponse();


        return binding.getRoot();
    }

    private void getResponse() {
        RetrofitService service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);
        service.getComicTopic(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ComicTopicBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ComicTopicBean bean) {
//                        if ((beans == null && page == 0) || beans != null && page == 0 && beans.size() == 0) {
//                            binding.tvNoData.setVisibility(View.VISIBLE);
//                            binding.recyclerViewRank.setVisibility(View.GONE);
//                            Toast.makeText(getApplicationContext(), "没有数据了", Toast.LENGTH_LONG).show();
//                        }
                        if (bean.getData().size() < 10) {
                            adapter.getLoadMoreModule().loadMoreEnd();
                        } else {
                            adapter.getLoadMoreModule().loadMoreComplete();
                            page++;
                        }
                        if (page == 0) {
                            adapter.setList(bean.getData());
                        } else {
                            adapter.addData(bean.getData());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (e instanceof HttpException) {
                            LogUtil.d("HttpError: " + ((HttpException) e).code() );
                        } else {
                            LogUtil.d("OtherError: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}