package com.sena.dmzjthird.custom.readerBook;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * FileName: BottomToolView
 * Author: JiaoCan
 * Date: 2022/3/9 10:33
 */

public class BottomToolView extends ConstraintLayout {


    private View view;
    private Callbacks callbacks;

    public BottomToolView(@NonNull Context context) {
        this(context, null);
    }

    public BottomToolView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomToolView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface Callbacks {

        void onSubscribeClick(); // 发送请求

        void onSettingClick();   // 弹出弹窗

        void onChapterListClick(); // 侧边栏打开

    }

}
