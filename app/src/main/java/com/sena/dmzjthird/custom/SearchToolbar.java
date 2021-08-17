package com.sena.dmzjthird.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.sena.dmzjthird.R;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/17
 * Time: 18:49
 */
public class SearchToolbar extends Toolbar {

    private View view;
    private ImageView backIV;
    private EditText queryET;
    private ImageView searchIV;


    public SearchToolbar(@NonNull Context context) {
        this(context, null);
    }

    public SearchToolbar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchToolbar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);

    }

    private void init(Context context) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.toolbar_serach, null);

            backIV = view.findViewById(R.id.back);
            queryET = view.findViewById(R.id.query);
            searchIV = view.findViewById(R.id.search);

            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER_HORIZONTAL);
            addView(view, params);

        }
    }

    public void setBackIVListener(View.OnClickListener listener) {
        backIV.setOnClickListener(listener);
    }

    public String getQueryETText() {
        return queryET.getText().toString();
    }

    public void setSearchIVListener(View.OnClickListener listener) {
        searchIV.setOnClickListener(listener);
    }

    public void setQueryETEditorActionListener(TextView.OnEditorActionListener listener) {
        queryET.setOnEditorActionListener(listener);
    }

    public void setSearchClick() {
        searchIV.callOnClick();
    }
}
