package com.sena.dmzjthird.custom.readerComic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.sena.dmzjthird.R;
import com.sena.dmzjthird.comic.view.ComicViewActivity;
import com.sena.dmzjthird.utils.LogUtil;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2022/3/13
 * Time: 18:01
 */
public class ComicTopView extends ConstraintLayout {

    private final Context mContext;
    private final ComicViewVM vm;
    private View view;
    private ImageView ivBack;
    private TextView tvTitle;

    public ComicTopView(@NonNull Context context) {
        this(context, null);
    }

    public ComicTopView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ComicTopView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        vm = new ViewModelProvider((AppCompatActivity) context).get(ComicViewVM.class);
        init();
    }

    @SuppressLint("InflateParams")
    private void init() {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.toolbar_normal, null);
        }
        ivBack = view.findViewById(R.id.back);
        tvTitle = view.findViewById(R.id.title);
        addClick();

        @SuppressLint("Recycle")
        TypedArray actionbarSizeTypedArray = mContext.obtainStyledAttributes(new int[] {
                android.R.attr.actionBarSize
        });

        float actionBarHeight = actionbarSizeTypedArray.getDimension(0, 0);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) actionBarHeight);
        addView(view, layoutParams);
    }

    private void addClick() {
        ivBack.setOnClickListener(v -> ((ComicViewActivity) mContext).finish());

        vm.isShowToolView.observe((AppCompatActivity) mContext, isShow ->
                setVisibility(isShow ? VISIBLE : INVISIBLE));
        vm.currentChapterName.observe((AppCompatActivity) mContext, chapterName -> {
            LogUtil.e("漫画名称: " + chapterName);
            tvTitle.setText(chapterName);
        });
    }

}
