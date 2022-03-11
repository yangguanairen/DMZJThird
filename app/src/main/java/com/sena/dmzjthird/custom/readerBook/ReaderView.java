package com.sena.dmzjthird.custom.readerBook;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.sena.dmzjthird.R;

/**
 * FileName: ReaderView
 * Author: JiaoCan
 * Date: 2022/3/8 17:24
 */

public class ReaderView extends View {

    private TextPaint paint;
    private float mTextSize;
    private float mLineSpace;
    private String mContent;
    private float mXOffset;
    private float mYOffset;


    public ReaderView(Context context) {
        this(context, null);
    }

    public ReaderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setReaderData(float textSize, float lineSpace, String content, float xOffset, float yOffset, boolean isBlackBg) {
        paint = new TextPaint();
        mTextSize = textSize;
        mLineSpace = lineSpace;
        paint.setTextSize(textSize);
        if (isBlackBg) paint.setColor(Color.WHITE);
        mContent = content;
        mXOffset = xOffset;
        mYOffset = yOffset;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (paint == null) return ;

        if (canvas == null) canvas = new Canvas();

        String[] lineStrs = mContent.split("\n");
        for (int i = 0; i < lineStrs.length; i++) {
            canvas.drawText(lineStrs[i], mXOffset, (i) * mLineSpace + mYOffset, paint);
        }
    }
}
