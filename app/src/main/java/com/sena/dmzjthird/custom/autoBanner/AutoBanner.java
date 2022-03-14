package com.sena.dmzjthird.custom.autoBanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.sena.dmzjthird.R;
import com.sena.dmzjthird.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/6
 * Time: 22:16
 */
public class AutoBanner extends ConstraintLayout {

    private View view;
    private ViewPager viewPager;
    private LinearLayout linear;
    private TextView titleTV;

    private final Context mContext;

    private final List<AutoBannerData> dataList = new ArrayList<>();

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.sendEmptyMessage(0);
        }
    };

    // 自动循环，每过5秒切换下一张ViewPager
    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    };

    public AutoBanner(@NonNull Context context) {
        this(context, null);
    }

    public AutoBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoBanner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    @SuppressLint("InflateParams")
    private void init() {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.view_pager_normal, null);
        }
        viewPager = view.findViewById(R.id.viewPager);
        linear = view.findViewById(R.id.linear);
        titleTV = view.findViewById(R.id.title);

        addListener();

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(view, params);
    }

    private void addListener() {
        viewPager.setOffscreenPageLimit(6);  // 缓存7个page页面
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                // 设置循环播放，在原本数据上在加上两条
                // 比如原来1，2，3，4，5；现在5，1，2，3，4，5，1
                // 跳到第一个5立刻跳向5，调到最后一个1立刻跳向1，达到伪循环
                if (position == dataList.size() - 1) {
                    position = 1;
                } else if (position == 0) {
                    position = dataList.size() - 2;
                }
                viewPager.setCurrentItem(position, false);
                // 改变title和point选中状态
                titleTV.setText(dataList.get(position).getTitle());
                selectedPoint(position - 1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    // 【状态：正在滑动】
                    // 用户拖拽，取消定时任务
                    handler.removeCallbacks(runnable);
//                        LogUtil.e("轮播图: " + "正在滑动");
                } else if (state == ViewPager.SCROLL_STATE_SETTLING) {
                    // 【状态：滑动结束】包括自动滑动也会满足此条件
                    // 添加定时任务
                    handler.postDelayed(runnable, 5000);
                }
            }
        });
    }


    public void setDataList(List<AutoBannerData> list) {
        if (list == null) {
            return;
        }
        dataList.add(list.get(list.size() - 1));
        dataList.addAll(list);
        dataList.add(list.get(0));

        initPoints();

        viewPager.setAdapter(new AutoBannerPagerAdapter(mContext, dataList));
        viewPager.setCurrentItem(1);

        for (AutoBannerData data: list) {
            LogUtil.e("title: " + data.getTitle());
        }

    }

    /**
     * 更新点的状态，当前位置为蓝色
     * @param position 当前位置
     */
    private void selectedPoint(int position) {
        for (int i = 0; i < linear.getChildCount(); i++) {
            View point = linear.getChildAt(i);
            point.setBackgroundResource(i == position ? R.drawable.shape_point_selected : R.drawable.shape_point_normal);
        }
    }

    /**
     * 初始点的图标，白色
     */
    private void initPoints() {
        for (int i = 0; i < dataList.size() - 2; i++) {
            View point = new View(mContext);
            point.setBackgroundResource(R.drawable.shape_point_normal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            params.leftMargin = 10;
            point.setLayoutParams(params);
            linear.addView(point);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 启动自动轮播
        handler.postDelayed(runnable, 5000);
    }

    // 减少资源消耗，同时防止空指针异常，handle调用了ViewPager对象，不是规范的写法
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacks(runnable);
    }
}
