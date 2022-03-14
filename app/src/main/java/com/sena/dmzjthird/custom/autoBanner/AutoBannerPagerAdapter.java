package com.sena.dmzjthird.custom.autoBanner;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.sena.dmzjthird.utils.GlideUtil;
import com.sena.dmzjthird.utils.IntentUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * FileName: AutoBannerPagerAdapter
 * Author: JiaoCan
 * Date: 2022/3/14 10:29
 */

public class AutoBannerPagerAdapter extends PagerAdapter {

    private final Context mContext;
    private final List<AutoBannerData> dataList;

    public AutoBannerPagerAdapter(Context context, List<AutoBannerData> dataList) {
        mContext = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @NotNull
    @Override
    public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        AutoBannerData data = dataList.get(position);
        GlideUtil.loadImageWithCookie(mContext, data.getCoverUrl(), imageView);

        imageView.setOnClickListener(v -> {

            switch (data.getType()) {
                case 0:
                    // 跳转漫画详情页
                    break;
                case 1:
                    // 跳转小说详情页
                    IntentUtil.goToNovelInfoActivity(mContext, data.getObjectId());
                    break;
                case 2:
                    // 跳转webView加载网页链接
                    IntentUtil.goToWebViewActivity(mContext, data.getObjectId(), data.getTitle(), data.getCoverUrl(), data.getPageUrl());
                    break;
            }
//                Toast.makeText(mContext, "暂时这样" + dataList.get(position).get(), Toast.LENGTH_SHORT).show();
        });
        container.addView(imageView);

        return imageView;

    }
}
