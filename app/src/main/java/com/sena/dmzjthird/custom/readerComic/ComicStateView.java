package com.sena.dmzjthird.custom.readerComic;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.sena.dmzjthird.R;
import com.sena.dmzjthird.utils.BroadcastHelper;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2022/3/13
 * Time: 20:46
 */
public class ComicStateView extends LinearLayout {

    private final Context mContext;
    private final ComicViewVM vm;
    private View view;
    private TextView tvChapterName;
    private TextView tvPageNum;
    private TextView tvNetworkState;
    private TextView tvBatteryNum;

    private BroadcastReceiver receiver;

    public ComicStateView(Context context) {
        this(context, null);
    }

    public ComicStateView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ComicStateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        vm = new ViewModelProvider((AppCompatActivity) context).get(ComicViewVM.class);

        init();
        initVM();
    }

    @SuppressLint("InflateParams")
    private void init() {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.content_object_state, null);
        }
        tvChapterName = view.findViewById(R.id.chapterName);
        tvPageNum = view.findViewById(R.id.pageNum);
        tvNetworkState = view.findViewById(R.id.networkState);
        tvBatteryNum = view.findViewById(R.id.batteryNum);

        receiver = BroadcastHelper.getBatteryAndNetworkBroadcast(mContext, tvBatteryNum, tvNetworkState);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(view, layoutParams);
    }

    @SuppressLint("SetTextI18n")
    private void initVM() {
        vm.isShowState.observe((AppCompatActivity) mContext, isShow ->
                setVisibility(isShow ? VISIBLE : INVISIBLE));

        vm.totalPage.observe((AppCompatActivity) mContext, totalPage ->
                tvPageNum.setText(vm.currentPage.getValue() + "/" + totalPage));

        vm.currentPage.observe((AppCompatActivity) mContext, currentPage ->
                tvPageNum.setText(currentPage + "/" + vm.totalPage.getValue()));

        vm.currentChapterName.observe((AppCompatActivity) mContext, chapterName ->
                tvChapterName.setText(chapterName));
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        BroadcastHelper.unregisterBroadcast(mContext, receiver);
    }
}
