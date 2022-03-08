package com.sena.dmzjthird.custom.readerBook;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.sena.dmzjthird.utils.LogUtil;

/**
 * FileName: ReaderPageAdapter
 * Author: JiaoCan
 * Date: 2022/3/8 17:28
 */

public class ReaderPageAdapter extends PagerAdapter {

    private final Context mContext;
    private final ReaderData readerData;

    public ReaderPageAdapter(Context context, String content, float viewWidth, float viewHeight, float textSize, float lineSpace) {
        mContext = context;
        readerData = new ReaderData(content, viewWidth, viewHeight, textSize, lineSpace);
    }

    @Override
    public int getCount() {
        LogUtil.e("总页数: " + readerData.totalPageNum);
        return readerData.totalPageNum;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ReaderView readerView = new ReaderView(mContext);
        readerView.setReaderData(readerData.mTextSize, readerData.mLineSpace,  readerData.pageContents.get(position), readerData.xOffset, readerData.yOffset);
        container.addView(readerView);
        return readerView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
