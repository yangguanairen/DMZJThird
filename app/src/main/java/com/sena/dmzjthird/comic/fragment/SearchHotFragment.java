package com.sena.dmzjthird.comic.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.account.MyRetrofitService;
import com.sena.dmzjthird.comic.adapter.SearchHotAdapter;
import com.sena.dmzjthird.comic.bean.SearchHotBean;
import com.sena.dmzjthird.databinding.FragmentSearchHotBinding;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.RetrofitHelper;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class SearchHotFragment extends Fragment {

    private static final String ARG_TYPE = "arg_type";
    private int mType;

    private FragmentSearchHotBinding binding;
    private SearchHotAdapter adapter;

    public static SearchHotFragment newInstance(int type) {
        SearchHotFragment fragment = new SearchHotFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchHotBinding.inflate(inflater, container, false);

        FlexboxLayoutManager manager = new FlexboxLayoutManager(getActivity());
        manager.setFlexWrap(FlexWrap.WRAP);
        manager.setFlexDirection(FlexDirection.ROW);
        manager.setAlignItems(AlignItems.STRETCH);
        manager.setJustifyContent(JustifyContent.FLEX_START);
        binding.recyclerview.setLayoutManager(manager);
        adapter = new SearchHotAdapter();
        binding.recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener((a, view, position) -> {
            String objectId = ((SearchHotBean) a.getData().get(position)).getId();
            if (mType == MyRetrofitService.TYPE_COMIC) {
                IntentUtil.goToComicInfoActivity(getActivity(), objectId);
            } else if (mType == MyRetrofitService.TYPE_NOVEL) {
                IntentUtil.goToNovelInfoActivity(getActivity(), objectId);
            }
        });

        getResponse();

        return binding.getRoot();
    }

    private void getResponse() {
        RetrofitService service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);
        service.getSearchHot(mType == MyRetrofitService.TYPE_COMIC ? 0 : 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<SearchHotBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<SearchHotBean> beans) {
                        adapter.setList(beans);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogUtil.internetError(e);
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