package com.sena.dmzjthird.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.sena.dmzjthird.R;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/7/28
 * Time: 19:02
 */
public class NormalToolbar extends Toolbar {

    private View view;
    private ImageView backIV;
    private TextView titleTV;
    private ImageView favoriteIV;
    private ImageView otherIV;


    public NormalToolbar(@NonNull @NotNull Context context) {
        this(context, null);
    }

    public NormalToolbar(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NormalToolbar(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);

        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ToolbarNormal);

            String title = array.getString(R.styleable.ToolbarNormal_toolbarTitle);
            if (title != null) {
                titleTV.setText(title);
            }
            boolean isShowFavorite = array.getBoolean(R.styleable.ToolbarNormal_isShowFavorite, false);
            favoriteIV.setVisibility(isShowFavorite ? View.VISIBLE : View.GONE);
            boolean isShowOther = array.getBoolean(R.styleable.ToolbarNormal_isShowOther, false);
            otherIV.setVisibility(isShowOther ? View.VISIBLE : View.GONE);

            Drawable drawable = array.getDrawable(R.styleable.ToolbarNormal_otherSrc);
            if (isShowOther && drawable != null) {
                otherIV.setImageDrawable(drawable);
            }
            array.recycle();

        }
    }


    private void init(Context context) {

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.toolbar_normal, null);

            backIV = view.findViewById(R.id.back);
            titleTV = view.findViewById(R.id.title);
            favoriteIV = view.findViewById(R.id.favorite);
            otherIV = view.findViewById(R.id.other);

            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER_HORIZONTAL);
            addView(view, params);
        }

    }

    public void setBackListener(OnClickListener listener) {
        backIV.setOnClickListener(listener);
    }

    public void setFavoriteListener(OnClickListener listener) {
        favoriteIV.setOnClickListener(listener);
    }

    public void setOtherListener(OnClickListener listener) {
        otherIV.setOnClickListener(listener);
    }

    public void setTitle(String title) {
        titleTV.setText(title);
    }

    public void setFavoriteVisibility(int status) {
        favoriteIV.setVisibility(status);
    }

    public void setOtherVisibility(int status) {
        otherIV.setVisibility(status);
    }

    public void setFavoriteResource(int resourcesId) {
        favoriteIV.setImageResource(resourcesId);
    }

    public void setOtherResource(int resourceId) {

    }

    public ImageView getFavoriteIV() {
        return favoriteIV;
    }
}
