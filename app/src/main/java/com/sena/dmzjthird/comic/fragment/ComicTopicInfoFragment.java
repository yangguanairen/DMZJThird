package com.sena.dmzjthird.comic.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.sena.dmzjthird.databinding.FragmentComicTopicInfoBinding;
import com.sena.dmzjthird.utils.GlideUtil;
import com.sena.dmzjthird.utils.IntentUtil;


public class ComicTopicInfoFragment extends Fragment {

    @SuppressLint("StaticFieldLeak")
    private static FragmentComicTopicInfoBinding binding;
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private static String coverUrl;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentComicTopicInfoBinding.inflate(inflater, container, false);
        context = getActivity();

        initView();

        return binding.getRoot();
    }

    private void initView() {
        binding.cover.setOnClickListener(v -> {
            if (coverUrl == null) return ;
            IntentUtil.goToLargeImageActivity(getContext(), coverUrl);
        });
    }

    public static void updateInfo(String cover, String title, String description) {
        coverUrl = cover;
        Glide.with(context)
                .load(GlideUtil.addCookie(cover))
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(5)))
                .into(binding.cover);

        binding.title.setText(title);
        binding.description.setText(description);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}