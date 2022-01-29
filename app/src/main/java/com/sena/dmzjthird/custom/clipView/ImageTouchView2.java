package com.sena.dmzjthird.custom.clipView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * FileName: ImageTouchView
 * Author: JiaoCan
 * Date: 2022/1/25 13:42
 * <p>
 * https://blog.csdn.net/u012551350/article/details/87928720
 * https://www.jianshu.com/p/0820c42da114
 */

public class ImageTouchView2 extends androidx.appcompat.widget.AppCompatImageView {

    private final GestureDetector mGestureDetector;
    private final ScaleGestureDetector mScaleGestureDetector;
    private Matrix mMatrix;
    private ClipFrameView mFrameView;

    private float mWidth;
    private float mHeight;

    private float mPreScaleFactor = 1.0f;
    private final float mMaxScale = 3.0f;
    private float mBaseScale;

    private float mLastFocusX;
    private float mLastFocusY;

    private static final int SCALE_ANIM_COUNT = 10;
    private static final int ZOOM_OUT_ANIM_WHIT = 0;
    private static final int ZOOM_ANIM_WHIT = 1;

    private static final int MAX_SCROLL_FACTOR = 3;
    private static final float DAMP_FACTOR = 9.0f;

    private int mCurrentScaleAnimCount;

    public ImageTouchView2(Context context) {
        this(context, null);
    }

    public ImageTouchView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageTouchView2(Context context, @Nullable AttributeSet attributeSet, int defStyleAttr){
        super(context, attributeSet, defStyleAttr);
        setScaleType(ScaleType.MATRIX);
        mMatrix = new Matrix();
        mGestureDetector = new GestureDetector(context, mSimpleOnGestureListener);
        mScaleGestureDetector = new ScaleGestureDetector(context, mOnScaleGestureListener);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    private final GestureDetector.SimpleOnGestureListener mSimpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (e1.getPointerCount() == e2.getPointerCount() && e1.getPointerCount() == 1) {

                RectF rectF = getMatrixRectF();

                float leftEdgeDistanceLeft = rectF.left - mFrameView.getFramePosition().x + mFrameView.getFrameStrokeWidth();
                float topEdgeDistanceTop = rectF.top - mFrameView.getFramePosition().y + mFrameView.getFrameStrokeWidth();
                float rightEdgeDistanceRight = rectF.right - (mFrameView.getFramePosition().x + mFrameView.getFrameWidth());
                float bottomEdgeDistanceBottom = rectF.bottom - (mFrameView.getFramePosition().y + mFrameView.getFrameHeight());

                int maxOffsetWidth = getWidth() / MAX_SCROLL_FACTOR;
                int maxOffsetHeight = getHeight() / MAX_SCROLL_FACTOR;

                if (leftEdgeDistanceLeft > 0 && rightEdgeDistanceRight > 0) {
                    if (distanceX < 0) {
                        if (leftEdgeDistanceLeft < maxOffsetWidth) {
                            int ratio = (int) (DAMP_FACTOR / maxOffsetWidth * leftEdgeDistanceLeft) + 1;
                            distanceX /= ratio;
                        } else {
                            distanceX = 0;
                        }
                    }
                } else if (leftEdgeDistanceLeft < 0 && rightEdgeDistanceRight < 0) {
                    if (distanceX > 0) {
                        if (rightEdgeDistanceRight > -maxOffsetWidth) {
                            int ratio = (int) (DAMP_FACTOR / maxOffsetWidth * -rightEdgeDistanceRight) + 1;
                            distanceX /= ratio;
                        } else {
                            distanceX = 0;
                        }
                    }
                }
                if (topEdgeDistanceTop > 0 && bottomEdgeDistanceBottom > 0) {
                    if (distanceY < 0) {
                        if (topEdgeDistanceTop < maxOffsetHeight) {
                            int ratio = (int) (DAMP_FACTOR / maxOffsetHeight * topEdgeDistanceTop) + 1;
                            distanceY /= ratio;
                        } else {
                            distanceY = 0;
                        }
                    }
                } else if (topEdgeDistanceTop < 0 && bottomEdgeDistanceBottom < 0) {
                    if (distanceY > 0) {
                        if (bottomEdgeDistanceBottom > -maxOffsetHeight) {
                            int ratio = (int) (DAMP_FACTOR / maxOffsetHeight * -bottomEdgeDistanceBottom) + 1;
                            distanceY /= ratio;
                        } else {
                            distanceY = 0;
                        }
                    }
                }

                mMatrix.postTranslate(-distanceX, -distanceY);
                setImageMatrix(mMatrix);
                return true;
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

    };

    private final OnScaleGestureListener mOnScaleGestureListener = new OnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if (getDrawable() == null || mMatrix == null) return true;

            float scaleFactor = detector.getScaleFactor();

            float deltaFactor = scaleFactor - mPreScaleFactor;

            if (scaleFactor != 1.0f && deltaFactor != 0f) {
                mMatrix.postScale(deltaFactor + 1f, deltaFactor + 1f, mLastFocusX = detector.getFocusX(), mLastFocusY = detector.getFocusY());
                setImageMatrix(mMatrix);
            }
            mPreScaleFactor = scaleFactor;
            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mCurrentScaleAnimCount = 0;
                invalidate();
                float scale = getScale();
                if (scale > mMaxScale) {
                    sendScaleMessage(getRelativeValue(mMaxScale / scale, SCALE_ANIM_COUNT), ZOOM_OUT_ANIM_WHIT, 0);
                } else if (scale < mBaseScale) {
                    sendScaleMessage(getRelativeValue(mMaxScale / scale, SCALE_ANIM_COUNT), ZOOM_ANIM_WHIT, 0);
                } else {
                    checkBound();
                }
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
        }

        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }
        mScaleGestureDetector.onTouchEvent(event);
        return true;
    }

    private void checkBound() {

        RectF rectF = getMatrixRectF();
        float leftEdgeDistanceLeft = rectF.left - mFrameView.getFramePosition().x + mFrameView.getFrameStrokeWidth();
        float topEdgeDistanceTop = rectF.top - mFrameView.getFramePosition().y + mFrameView.getFrameStrokeWidth();
        float rightEdgeDistanceRight = rectF.right - (mFrameView.getFramePosition().x + mFrameView.getFrameWidth());
        float bottomEdgeDistanceBottom = rectF.bottom - (mFrameView.getFramePosition().y + mFrameView.getFrameHeight());

        float[] values = new float[9];
        mMatrix.getValues(values);
        if (leftEdgeDistanceLeft > 0) {
            values[Matrix.MTRANS_X] -= leftEdgeDistanceLeft;
        } else if (rightEdgeDistanceRight < 0) {
            values[Matrix.MTRANS_X] -= rightEdgeDistanceRight;
        }
        if (topEdgeDistanceTop > 0) {
            values[Matrix.MTRANS_Y] -= topEdgeDistanceTop;
        } else if (bottomEdgeDistanceBottom < 0) {
            values[Matrix.MTRANS_Y] -= bottomEdgeDistanceBottom;
        }
        mMatrix.setValues(values);
        setImageMatrix(mMatrix);
    }

    private RectF getMatrixRectF() {
        RectF rectF = new RectF();
        Drawable drawable = getDrawable();
        if (drawable != null) {
            rectF.set(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            mMatrix.mapRect(rectF);
        }
        return rectF;
    }

    private float getScale() {
        float[] values = new float[9];
        mMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }


    private static float getRelativeValue(double d, double count) {
        if (count == 0) {
            return 1f;
        }
        count = 1 / count;
        return (float) Math.pow(d, count);
    }

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == ZOOM_ANIM_WHIT) {
                autoFillClipFrame();
                return;
            }
            if (mCurrentScaleAnimCount < SCALE_ANIM_COUNT) {
                float obj = (float) msg.obj;
                mMatrix.postScale(obj, obj, mLastFocusX, mLastFocusY);
                setImageMatrix(mMatrix);
                mCurrentScaleAnimCount++;
                sendScaleMessage(obj, msg.what, SCALE_ANIM_COUNT);
            } else {
                if (msg.what == ZOOM_OUT_ANIM_WHIT) {
                    float[] values = new float[9];
                    mMatrix.getValues(values);
                    values[Matrix.MSCALE_X] = mMaxScale;
                    values[Matrix.MSCALE_Y] = mMaxScale;
                    mMatrix.setValues(values);
                    setImageMatrix(mMatrix);
                }

            }
        }
    };

    private void sendScaleMessage(float relativeScale, int what, long delayMills) {
        Message message = new Message();
        message.obj = relativeScale;
        message.what = what;
        mHandler.sendMessageDelayed(message, delayMills);
    }

    public Bitmap getBitmap(ClipFrameView frameView) {
        if (frameView == null) return null;

        int left = frameView.getFramePosition().x > 0 ? (int) frameView.getFramePosition().x : 0;
        int top = frameView.getFramePosition().y > 0 ? (int) frameView.getFramePosition().y : 0;
        int width = frameView.getFrameWidth() < mWidth ? (int) frameView.getFrameWidth() : (int) mWidth;
        int height = frameView.getFrameHeight() < mHeight ? (int) frameView.getFrameHeight() : (int) mHeight;

        Bitmap bitmap = Bitmap.createBitmap((int) mWidth, (int) mHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        this.draw(canvas);

        return Bitmap.createBitmap(bitmap, left, top, width, height);
    }

    public void autoFillClipFrame() {
        if (getDrawable() == null || mFrameView == null) return;

        float left = mFrameView.getFramePosition().x;
        float top = mFrameView.getFramePosition().y;
        float width = mFrameView.getFrameWidth();
        float height = mFrameView.getFrameHeight();
        int drawableWidth = getDrawable().getIntrinsicWidth();
        int drawableHeight = getDrawable().getIntrinsicHeight();

        mBaseScale = Math.max(width / drawableWidth, height / drawableHeight);

        Matrix newMatrix = new Matrix();

        newMatrix.setScale(mBaseScale, mBaseScale); // 图片填充满控件
        newMatrix.postTranslate(left, (top * 2 + height - (drawableHeight * mBaseScale)) / 2);  // 居中显示，高度：top*2+height为ImageTouchView的总高度
        setImageMatrix(newMatrix);
        mMatrix = newMatrix;
        invalidate();

    }


    public void setImageFile(final String filePath, final int multiple, ClipFrameView frameView) {
        post(() -> {
            Bitmap bitmap = getSmallBitmap(filePath, (int) mWidth, (int) mHeight, multiple);
            if (bitmap != null) setImageBitmap(bitmap);
            mFrameView = frameView;
            autoFillClipFrame();
        });
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight, int multiple) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight * multiple || width > reqWidth * multiple) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = Math.min(heightRatio, widthRatio);
        }
        return inSampleSize;

    }

    private static Bitmap getSmallBitmap(String filePath, int w, int h, int multiple) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, w, h, multiple);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDetachedFromWindow();
    }
}
