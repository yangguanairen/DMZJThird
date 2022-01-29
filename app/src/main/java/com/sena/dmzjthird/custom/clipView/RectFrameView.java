package com.sena.dmzjthird.custom.clipView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;


/**
 * FileName: RectFrameView
 * Author: JiaoCan
 * Date: 2022/1/25 13:24
 */

public class RectFrameView extends View implements ClipFrameView {

    protected float frameWidth;
    protected float frameHeight;
    protected float frameScale;
    protected float frameStrokeWidth;
    protected int frameStrokeColor;
    protected float mWidth;
    protected float mHeight;

    protected Paint paint;
    protected Path globalPath;
    protected Path framePath;
    protected PorterDuffXfermode xfermode;


    public RectFrameView(Context context) {
        this(context, null);
    }

    public RectFrameView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectFrameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        globalPath = new Path();
        framePath = new Path();
        frameStrokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getContext().getResources().getDisplayMetrics());
        frameScale = 2f / 3;
        frameStrokeColor = Color.parseColor("#ffffff");

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        int length = Math.min(w, h);
        frameWidth = (float) length / 5 * 4;
//        frameHeight = frameWidth / frameScale;
        frameHeight = frameWidth;
        if (globalPath != null) globalPath.addRect((float) -w/2, (float) -h/2, (float) w/2, (float) h/2, Path.Direction.CW);
        if (framePath != null) framePath.addRect(-frameWidth/2, -frameHeight/2, frameWidth/2, frameHeight/2, Path.Direction.CW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制画布阴影
        canvas.translate(mWidth/2, mHeight/2);
        paint.setColor(Color.parseColor("#333333"));
        paint.setAlpha(255/3*2);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(globalPath, paint);
        // 擦除框内阴影
        paint.setXfermode(xfermode);
        canvas.drawPath(framePath, paint);
        paint.setXfermode(null);
        // 绘制边框
        paint.setColor(frameStrokeColor);
        paint.setAlpha(255);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(frameStrokeWidth);
        canvas.drawPath(framePath, paint);
    }

    @Override
    public float getFrameScale() {
        return frameScale;
    }

    @Override
    public float getFrameHeight() {
        return frameHeight;
    }

    @Override
    public float getFrameWidth() {
        return frameWidth;
    }

    @Override
    public float getFrameStrokeWidth() {
        return frameStrokeWidth;
    }

    @Override
    public PointF getFramePosition() {
        float top = (mHeight - frameHeight) / 2;
        float left = (mWidth - frameWidth) /2;
        return new PointF(left, top);
    }
}
