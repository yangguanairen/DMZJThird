package com.sena.dmzjthird.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sena.dmzjthird.R;
import com.sena.dmzjthird.utils.GlideUtil;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.LogUtil;

import java.util.List;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/25
 * Time: 18:52
 */
public class CommentTextContent extends RelativeLayout {


    private View view;
    private RelativeLayout layout;
    private LinearLayout otherContentLayout;
    private TextView usernameTV;
    private TextView contentTV;
    private TextView unfoldTV;
    private LinearLayout contentImageLayout;

    public CommentTextContent(Context context) {
        this(context, null);
    }

    public CommentTextContent(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("InflateParams")
    public CommentTextContent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (view == null) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.custom_comment_text_cotent, null);

            layout = view.findViewById(R.id.relative);
            otherContentLayout = view.findViewById(R.id.other_content);
            usernameTV = view.findViewById(R.id.username);
            contentTV = view.findViewById(R.id.content);
            unfoldTV = view.findViewById(R.id.unfold);
            contentImageLayout = view.findViewById(R.id.content_image);


            unfoldTV.setOnClickListener(v -> {

                if (contentTV.getMaxLines() == 4) {
                    contentTV.setMaxLines(100);
                    unfoldTV.setText("收起");
                } else {
                    contentTV.setMaxLines(4);
                    unfoldTV.setText("显示全部");
                }
            });




            LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            addView(view, params);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        LogUtil.e("count: " + contentTV.getLineCount());
        // 会重复测量
        if (contentTV.getLineCount() > 4 && unfoldTV.getVisibility() == View.GONE) {
            contentTV.setMaxLines(4);
            unfoldTV.setVisibility(View.VISIBLE);
        }

    }

    public void setUsername(String s) {
        usernameTV.setText(s);
    }

    public void setContent(String s) {
        contentTV.setText(s);
    }

    public void setUploadImages(Context context, List<String> urls, String folder, String commentId) {
        for (String url: urls) {
            ImageView imageView = new ImageView(context);
            GlideUtil.loadImageWithCookie(context, "https://images.dmzj1.com/commentImg/" + folder + "/"
                    + url, imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(400, 400);
            params3.setMargins(0, 10, 10, 10);
            imageView.setLayoutParams(params3);


            imageView.setOnClickListener(v -> IntentUtil.goToLargeImageActivity(
                    context, "https://images.dmzj1.com/commentImg/" + folder + "/" + url));



            contentImageLayout.addView(imageView);
        }
    }

    public void addOtherContentView(View view, LinearLayout.LayoutParams params) {
        otherContentLayout.addView(view, params);
    }

    public void addOtherContentViewByIndex(View view, int index, LinearLayout.LayoutParams params) {
        otherContentLayout.addView(view, index, params);
    }

    public void removeOtherContentView(View view) {
        otherContentLayout.removeView(view);
    }

    public void setParentBg(int resourceId) {
        layout.setBackgroundResource(resourceId);
    }

    public void setParentPadding(int left, int top, int right, int bottom) {
        layout.setPadding(left, top, right, bottom);
    }

    public void setParentClickListener(View.OnClickListener listener) {
        layout.setOnClickListener(listener);
    }



}
