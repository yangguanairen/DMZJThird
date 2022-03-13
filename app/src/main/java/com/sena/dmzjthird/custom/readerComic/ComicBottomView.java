package com.sena.dmzjthird.custom.readerComic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.sena.dmzjthird.R;

import java.util.List;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2022/3/13
 * Time: 18:10
 */
public class ComicBottomView extends ConstraintLayout {

    private final Context mContext;
    private final ComicViewVM vm;
    private View view;
    private TextView tvPreChapter;
    private TextView tvNextChapter;
    private SeekBar seekBar;
    private LinearLayout subscribeLayout;
    private ImageView ivSubscribeIcon;
    private TextView tvSubscribeText;
    private LinearLayout settingLayout;
    private LinearLayout chapterListLayout;

    private BasePopupView popupView;


    public ComicBottomView(@NonNull Context context) {
        this(context, null);
    }

    public ComicBottomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ComicBottomView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        vm = new ViewModelProvider((AppCompatActivity) context).get(ComicViewVM.class);
        init();
        initVM();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        popupView.onDestroy();
    }

    @SuppressLint("InflateParams")
    private void init() {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.content_object_bottom_tool_view, null);
        }
        tvPreChapter = view.findViewById(R.id.preChapter);
        tvNextChapter = view.findViewById(R.id.nextChapter);
        seekBar = view.findViewById(R.id.seekBar);
        subscribeLayout = view.findViewById(R.id.subscribeLayout);
        ivSubscribeIcon = view.findViewById(R.id.subscribeIcon);
        tvSubscribeText = view.findViewById(R.id.subscribeText);
        settingLayout = view.findViewById(R.id.settingLayout);
        chapterListLayout = view.findViewById(R.id.chapterListLayout);

        popupView = new XPopup.Builder(mContext).asCustom(new ComicSettingPopup(mContext));

        addClick();

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(view, layoutParams);
    }

    private void addClick() {
        tvPreChapter.setOnClickListener(v -> updateChapter(true));
        tvNextChapter.setOnClickListener(v -> updateChapter(false));


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                vm.isUserOperate.postValue(fromUser);
                vm.currentPage.postValue(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        settingLayout.setOnClickListener(v -> popupView.show());

    }

    private void initVM() {
        vm.currentPage.observe((AppCompatActivity) mContext, currentPage -> {

        });
        vm.totalPage.observe((AppCompatActivity) mContext, totalPage ->
                seekBar.setMax(totalPage));

        vm.isShowToolView.observe((AppCompatActivity) mContext, isShow ->
                setVisibility(isShow ? VISIBLE : INVISIBLE));


    }

    private void updateChapter(boolean isPreClick) {

        List<Integer> chapterIdList = vm.chapterIdList.getValue();
        if (chapterIdList == null) return ;
        List<String> chapterNameList = vm.chapterNameList.getValue();
        if (chapterNameList == null) return ;

        int index = -1;
        int currentChapterId = vm.currentChapterId.getValue();
        for (int i = 0; i < chapterIdList.size(); i++) {
            if (currentChapterId == chapterIdList.get(i)) {
                index = i;
                break;
            }
        }

        int cId; // 即将浏览的章节
        String cName;
        if (isPreClick) {
            if (index == -1 || index == chapterIdList.size() - 1) {
                Toast.makeText(mContext, "已经没有上一章了", Toast.LENGTH_SHORT).show();
                return ;
            } else {
                cId = chapterIdList.get(index + 1);
                cName = chapterNameList.get(index + 1);
            }
        } else {
            if (index == -1 || index == 0) {
                Toast.makeText(mContext, "已经没有下一章了", Toast.LENGTH_SHORT).show();
                return ;
            } else {
                cId = chapterIdList.get(index - 1);
                cName = chapterNameList.get(index - 1);
            }
        }
        vm.currentChapterId.postValue(cId);
        vm.currentChapterName.postValue(cName);
    }

    public void setSubscribeListener(View.OnClickListener listener) {
        subscribeLayout.setOnClickListener(listener);
    }

    public ImageView getSubscribeIconView() {
        return ivSubscribeIcon;
    }

    public TextView getSubscribeTextView() {
        return tvSubscribeText;
    }

    public void setChapterListListener(View.OnClickListener listener) {
        chapterListLayout.setOnClickListener(listener);
    }

    public void setSeekBarProgress(int progress) {
        seekBar.setProgress(progress);
    }

}
