package com.sena.dmzjthird.custom;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.comic.bean.ComicRecommendBean;
import com.sena.dmzjthird.utils.GlideUtil;
import com.sena.dmzjthird.utils.LogUtil;

import org.jetbrains.annotations.NotNull;

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

    private Context mContext;
    private Handler handler;
    private boolean isTouch = false;

    private List<ComicRecommendBean.Data> dataList = new ArrayList<>();

    private Runnable runnable;



    public AutoBanner(@NonNull @NotNull Context context) {
        super(context);

    }

    public AutoBanner(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        handler = new Handler();

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.view_pager_normal, null);

            viewPager = view.findViewById(R.id.viewPager);
            linear = view.findViewById(R.id.linear);
            titleTV = view.findViewById(R.id.title);

            // 自动循环，每过5秒切换下一张ViewPager
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (!isTouch) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                        handler.postDelayed(this, 5000);
                    }
                }
            };

            viewPager.setOffscreenPageLimit(6);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    // 设置循环播放，在原本数据上在加上两条
                    // 比如原来1，2，3，4，5；现在5，1，2，3，4，5，1
                    // 跳到第一个5立刻跳向5，调到最后一个1立刻跳向1，达到伪循环
//                    if (position == dataList.size()-1) {
//                        viewPager.setCurrentItem(1, false);
//                        return;
//                    }
//                    if (position == 0) {
//                        viewPager.setCurrentItem(dataList.size()-2, false);
//                        return;
//                    }
                    if (position == dataList.size()-1) {
                        position = 1;
                    } else if (position == 0) {
                        position = dataList.size()-2;
                    }
                    viewPager.setCurrentItem(position, false);
                    // 改变title和point选中状态
                    titleTV.setText(dataList.get(position).getTitle());
                    selectedPoint(position-1);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });


            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            addView(view, params);
        }

    }


    public void setDataList(List<ComicRecommendBean.Data> list) {
        if (list == null) {
            return;
        }
        this.dataList.add(list.get(list.size()-1));
        this.dataList.addAll(list);
        this.dataList.add(list.get(0));

        initPoints();

        viewPager.setAdapter(new MyAdapter());
        viewPager.setCurrentItem(1);

//        for (ComicRecommendBean.Data data: list) {
//            LogUtil.e("title: " + data.getTitle());
//        }

    }

    private void selectedPoint(int position) {
//        if (position == 0) {
//            position = dataList.size()-2;
//        } else if (position == dataList.size()-1) {
//            position = 1;
//        } else {
//            position -= 1;
//        }
        for (int i = 0; i < linear.getChildCount(); i++) {
            View point = linear.getChildAt(i);
            point.setBackgroundResource(i==position ? R.drawable.shape_point_selected : R.drawable.shape_point_normal);
        }
    }

    private void initPoints() {
        for (int i = 0; i < dataList.size()-2; i++) {
            View point = new View(mContext);
            point.setBackgroundResource(R.drawable.shape_point_normal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            params.leftMargin = 10;
            point.setLayoutParams(params);
            linear.addView(point);
        }
    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            if (dataList != null) {
                return dataList.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
            container.removeView((View) object);
        }

        @NonNull
        @NotNull
        @Override
        public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(mContext).load(GlideUtil.addCookie(dataList.get(position).getCover()))
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                    .into(imageView);
            imageView.setOnClickListener(v -> {
                // 跳转
                Toast.makeText(mContext, "暂时这样" + dataList.get(position).getObj_id(), Toast.LENGTH_SHORT).show();
            });
            container.addView(imageView);

            return imageView;

        }
    }

    // 减少资源消耗
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        handler.post(runnable);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeCallbacks(runnable);
    }
}
