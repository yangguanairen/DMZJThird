package com.sena.dmzjthird.custom;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.CenterPopupView;
import com.sena.dmzjthird.R;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2022/1/22
 * Time: 12:39
 */
public class CustomToast extends CenterPopupView {

    private final int mIcon;
    private final String mTitle;
    private final String mContent;

    private ImageView ivIcon;
    private TextView tvTitle;
    private TextView tvContent;

    public CustomToast(@NonNull Context context, int icon, String title, String content) {
        super(context);
        mIcon = icon;
        mTitle = title;
        mContent = content;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_toast;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        ivIcon = findViewById(R.id.icon);
        tvTitle = findViewById(R.id.title);
        tvContent = findViewById(R.id.content);

        if (mIcon != 0) {
            ivIcon.setVisibility(View.VISIBLE);
            ivIcon.setImageResource(mIcon);
        }

        if (!"".equals(mTitle)) {
            tvTitle.setText(mTitle);
        }
        if (!"".equals(mContent)) {
            tvContent.setText(mContent);
        }

    }
}
