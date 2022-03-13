package com.sena.dmzjthird.comic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.github.chrisbanes.photoview.PhotoView;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.custom.readerComic.ComicViewVM;

import java.util.List;

/**
 * FileName: MyPageAdapter
 * Author: JiaoCan
 * Date: 2022/2/24 16:03
 */

public class ComicViewPagerAdapter extends PagerAdapter {

    private final Context mContext;
    private final List<String> mImageUrlList;
    private final ComicViewVM vm;

    public ComicViewPagerAdapter(Context context, List<String> imageUrlList) {
        mContext = context;
        mImageUrlList = imageUrlList;
        vm = new ViewModelProvider((AppCompatActivity) context).get(ComicViewVM.class);
    }


    @Override
    public int getCount() {
        return mImageUrlList.size();
    }


    @Override
    public boolean isViewFromObject(@androidx.annotation.NonNull View view, @androidx.annotation.NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@androidx.annotation.NonNull ViewGroup container, int position, @androidx.annotation.NonNull Object object) {
        container.removeView((View) object);
    }

    @androidx.annotation.NonNull
    @Override
    public Object instantiateItem(@androidx.annotation.NonNull ViewGroup container, int position) {

        if (position == 0) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.default_pre_page, null);
            view.setOnClickListener(v -> {
                boolean isShow = vm.isShowToolView.getValue();
                vm.isShowToolView.postValue(!isShow);
            });
            container.addView(view);
            return view;
        }
        if (position == mImageUrlList.size() - 1) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.default_next_page, null);
            view.setOnClickListener(v -> {
                boolean isShow = vm.isShowToolView.getValue();
                vm.isShowToolView.postValue(!isShow);
            });
            container.addView(view);
            return view;
        }


        PhotoView photoView = new PhotoView(mContext);
        photoView.setOnClickListener(v -> {
            boolean isShow = vm.isShowToolView.getValue();
            vm.isShowToolView.postValue(!isShow);
        });


        GlideUrl cookieUrl = new GlideUrl(mImageUrlList.get(position), new LazyHeaders.Builder()
                .addHeader("Host", "imgsmall.dmzj.com")
                .addHeader("Referer", "https://nnv3api.muwai.com/")
                .addHeader("Sec-Fetch-Dest", "image")
                .build());

        Glide.with(mContext)
                .load(cookieUrl)
                .into(photoView);
        container.addView(photoView);

        return photoView;
    }

}
