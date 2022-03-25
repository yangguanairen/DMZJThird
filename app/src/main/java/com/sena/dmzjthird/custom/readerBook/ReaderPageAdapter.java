package com.sena.dmzjthird.custom.readerBook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.PagerAdapter;

import com.github.chrisbanes.photoview.PhotoView;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.novel.vm.NovelViewVM;
import com.sena.dmzjthird.utils.GlideUtil;
import com.sena.dmzjthird.utils.LogUtil;

/**
 * FileName: ReaderPageAdapter
 * Author: JiaoCan
 * Date: 2022/3/8 17:28
 */

public class ReaderPageAdapter extends PagerAdapter {

    private final Context mContext;
    private final ReaderData readerData;
    private final NovelViewVM vm;

    private final boolean mIsBlackBg;

    public ReaderPageAdapter(Context context, String content, float viewWidth, float viewHeight, float textSize, float lineSpace, boolean isBlackBg) {
        mContext = context;
        readerData = new ReaderData(content, viewWidth, viewHeight, textSize, lineSpace);
        vm = new ViewModelProvider((AppCompatActivity) context).get(NovelViewVM.class);
        mIsBlackBg = isBlackBg;
    }

    @Override
    public int getCount() {
        if (readerData.imageList.isEmpty()) {
            LogUtil.e("文字总页数: " + readerData.totalPageNum);
            return readerData.totalPageNum + 2;
        } else {
            LogUtil.e("图片总页数: " + readerData.imageList.size());
            return readerData.imageList.size() + 2;
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        if (position == 0) {
            @SuppressLint("InflateParams")
            View view = LayoutInflater.from(mContext).inflate(R.layout.default_pre_page, null);
            container.addView(view);
            return view;
        }
        if (position == getPageNum() + 1) {
            @SuppressLint("InflateParams")
            View view = LayoutInflater.from(mContext).inflate(R.layout.default_next_page, null);
            container.addView(view);
            return view;
        }


        if (readerData.imageList.isEmpty()) {
            ReaderView readerView = new ReaderView(mContext);
            readerView.setReaderData(readerData.mTextSize, readerData.mFinalLineSpace,  readerData.pageContents.get(position - 1),
                    readerData.xOffset, readerData.yOffset,
                    mIsBlackBg);

            readerView.setOnClickListener(v -> {
                // headView & bottomView
                // 控制顶部和底部工具栏的显现与隐藏
                boolean isShow = vm.isShowToolView.getValue();
                vm.isShowToolView.postValue(!isShow);
            });

            container.addView(readerView);
            return readerView;
        } else {
            PhotoView photoView = new PhotoView(mContext);
            photoView.setOnClickListener(v -> {
                boolean isShow = vm.isShowToolView.getValue();
                vm.isShowToolView.postValue(!isShow);
            });

            // Adapter的Count比imageList多首尾两张
            GlideUtil.loadImage(mContext, readerData.imageList.get(position - 1), photoView);

            container.addView(photoView);
            return photoView;
        }

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    /**
     * 不包括首尾上下章节页面
     */
    public int getPageNum() {
        if (readerData.imageList.isEmpty()) {
            return readerData.totalPageNum;
        } else {
            return readerData.imageList.size();
        }
    }
}
