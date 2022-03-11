package com.sena.dmzjthird.custom.popup;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.BottomPopupView;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.utils.LogUtil;
import com.sena.dmzjthird.utils.MyDataStore;

/**
 * FileName: NovelBottomPopup
 * Author: JiaoCan
 * Date: 2022/3/10 16:14
 */

public class NovelBottomPopup extends BottomPopupView {

    private final Context mContext;
    private final Callbacks callbacks;
    private final MyDataStore dataStore;

    private TextView tvIncreaseTextSize;
    private TextView tvDecreaseTextSize;
    private TextView tvIncreaseSpaceLine;
    private TextView tvDecreaseSpaceLine;
    private View themeYellow;
    private View themeWhite;
    private View themeGreen;
    private View themeBlack;

    private float currentTextSize;
    private float currentSpaceLine;
    private int currentBgColorId;

    public NovelBottomPopup(@NonNull Context context) {
        super(context);
        mContext = context;
        callbacks = (Callbacks) context;
        dataStore = MyDataStore.getInstance(context);
        initData();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_novel_setting;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        // 只有在视图被创建时调用

        tvIncreaseTextSize = findViewById(R.id.increaseTextSize);
        tvDecreaseTextSize = findViewById(R.id.decreaseTextSize);
        tvIncreaseSpaceLine = findViewById(R.id.increaseSpaceLine);
        tvDecreaseSpaceLine = findViewById(R.id.decreaseSpaceLine);
        themeYellow = findViewById(R.id.bgYellow);
        themeWhite = findViewById(R.id.bgWhite);
        themeGreen = findViewById(R.id.bgGreen);
        themeBlack = findViewById(R.id.bgBlack);

        initClick();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dataStore.saveValue(MyDataStore.DATA_STORE_NOVEL_READ_SETTING, MyDataStore.SETTING_NOVEL_TEXT_SIZE, currentTextSize);
        dataStore.saveValue(MyDataStore.DATA_STORE_NOVEL_READ_SETTING, MyDataStore.SETTING_NOVEL_SPACE_LIN, currentSpaceLine);
        dataStore.saveValue(MyDataStore.DATA_STORE_NOVEL_READ_SETTING, MyDataStore.SETTING_NOVEL_BG, currentBgColorId);
    }

    private void initData() {
        currentTextSize = dataStore.getValue(MyDataStore.DATA_STORE_NOVEL_READ_SETTING, MyDataStore.SETTING_NOVEL_TEXT_SIZE, 50f);
        currentSpaceLine = dataStore.getValue(MyDataStore.DATA_STORE_NOVEL_READ_SETTING, MyDataStore.SETTING_NOVEL_TEXT_SIZE, 25f);
        currentBgColorId = dataStore.getValue(MyDataStore.DATA_STORE_NOVEL_READ_SETTING, MyDataStore.SETTING_NOVEL_BG, R.color.white);
    }

    private void initClick() {

        tvIncreaseTextSize.setOnClickListener(v -> {
            if (currentTextSize >= 70) {
                Toast.makeText(mContext, "字体无法继续变大!!", Toast.LENGTH_SHORT).show();
                return ;
            }
            currentTextSize += 5;
            callbacks.onTextSizeChange(currentTextSize);
        });
        tvDecreaseTextSize.setOnClickListener(v -> {
            if (currentTextSize <= 30) {
                Toast.makeText(mContext, "字体无法继续变小!!", Toast.LENGTH_SHORT).show();
                return ;
            }
            currentTextSize -= 5;
            callbacks.onTextSizeChange(currentTextSize);
        });

        tvIncreaseSpaceLine.setOnClickListener(v -> {
            if (currentSpaceLine >= 40) {
                Toast.makeText(mContext, "行距无法继续变大!!", Toast.LENGTH_SHORT).show();
                return ;
            }
            currentSpaceLine += 3.75;
            callbacks.onSpaceLineChange(currentSpaceLine);
        });
        tvDecreaseSpaceLine.setOnClickListener(v -> {
            if (currentSpaceLine <= 10) {
                Toast.makeText(mContext, "行距无法继续变小!!", Toast.LENGTH_SHORT).show();
                return ;
            }
            currentSpaceLine -= 3.75;
            callbacks.onSpaceLineChange(currentSpaceLine);
        });

        themeYellow.setOnClickListener(v -> {
            currentBgColorId = R.color.theme_yellow;
            callbacks.onBgChange(R.color.theme_yellow);
        });
        themeWhite.setOnClickListener(v -> {
            currentBgColorId = R.color.white;
            callbacks.onBgChange(R.color.white);
        });
        themeGreen.setOnClickListener(v -> {
            currentBgColorId = R.color.theme_green;
            callbacks.onBgChange(R.color.theme_green);
        });
        themeBlack.setOnClickListener(v -> {
            currentBgColorId = R.color.black;
            callbacks.onBgChange(R.color.black);
        });

    }

    public interface Callbacks {

        void onTextSizeChange(float textSize);

        void onSpaceLineChange(float spaceLine);

        void onBgChange(int colorResourcesId);

    }


}
