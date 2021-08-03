package com.sena.dmzjthird.comic.fragment;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sena.dmzjthird.R;
import com.sena.dmzjthird.custom.ProgressWheel;
import com.sena.dmzjthird.databinding.FragmentComicRecommendBinding;


public class ComicRecommendFragment extends Fragment {

    private FragmentComicRecommendBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentComicRecommendBinding.inflate(inflater, container, false);


        binding.progress.spin();


        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}