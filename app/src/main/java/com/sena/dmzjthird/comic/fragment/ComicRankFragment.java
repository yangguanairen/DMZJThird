package com.sena.dmzjthird.comic.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.RetrofitService;
import com.sena.dmzjthird.databinding.FragmentComicRankBinding;


public class ComicRankFragment extends Fragment {

    private FragmentComicRankBinding binding;

    private RetrofitService service;
    private BaseQuickAdapter adapter;
    private String time = "0";
    private int page = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentComicRankBinding.inflate(inflater, container, false);



        return binding.getRoot();
    }

    private void initAdapter() {
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));


        adapter.getLoadMoreModule().setOnLoadMoreListener(this::getResponse);
        adapter.getLoadMoreModule().setAutoLoadMore(false);
        adapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);

        page = 0;
        getResponse();
    }

    private void getResponse() {

    }
}