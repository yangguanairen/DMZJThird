package com.sena.dmzjthird.comic.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

/**
 * FileName: MyPageAdapter
 * Author: JiaoCan
 * Date: 2022/2/24 16:03
 */

public class MyPageAdapter extends PagerAdapter {

    private final Context mContext;
    private final List<String> mImageUrlList;

    private final Callbacks callbacks;

    public MyPageAdapter(Context context, List<String> imageUrlList) {
        mContext = context;
        callbacks = (Callbacks) context;
        mImageUrlList = imageUrlList;
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
        PhotoView photoView = new PhotoView(mContext);
        photoView.setOnClickListener(v -> callbacks.onPhotoViewClick(photoView));


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

    public interface Callbacks {
        void onPhotoViewClick(View view);
    }

}
