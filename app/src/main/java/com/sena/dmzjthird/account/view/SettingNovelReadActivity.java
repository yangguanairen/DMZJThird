package com.sena.dmzjthird.account.view;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.Toast;

import com.sena.dmzjthird.R;
import com.sena.dmzjthird.databinding.ActivitySettingNovelReadBinding;
import com.sena.dmzjthird.utils.MyDataStore;
import com.sena.dmzjthird.utils.ViewHelper;


public class SettingNovelReadActivity extends AppCompatActivity {

    private ActivitySettingNovelReadBinding binding;
    private MyDataStore dataStore;

    private float textSize;
    private float spaceLine;
    private int bgColorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingNovelReadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dataStore = MyDataStore.getInstance(this);

        initData();
        initView();
    }

    private void initView() {

        ViewHelper.immersiveStatus(this, binding.toolbar);

        binding.toolbar.setBackListener(v -> finish());

        binding.textSizeNum.setText(String.valueOf(textSize));
        binding.spaceLineNum.setText(String.valueOf(spaceLine));
        onBgChange(bgColorId, true);

        addListener();

    }

    private void addListener() {

        binding.increaseTextSize.setOnClickListener(v -> {
            if (textSize >= 70) {
                Toast.makeText(this, "字体无法继续变大!!", Toast.LENGTH_SHORT).show();
                return ;
            }
            textSize += 5;
            binding.textSizeNum.setText(String.valueOf(textSize));
        });
        binding.decreaseTextSize.setOnClickListener(v -> {
            if (textSize <= 30) {
                Toast.makeText(this, "字体无法继续变小!!", Toast.LENGTH_SHORT).show();
                return ;
            }
            textSize -= 5;
            binding.textSizeNum.setText(String.valueOf(textSize));
        });

        binding.increaseSpaceLine.setOnClickListener(v -> {
            if (spaceLine >= 40) {
                Toast.makeText(this, "行距无法继续变大!!", Toast.LENGTH_SHORT).show();
                return ;
            }
            spaceLine += 3.75;
            binding.spaceLineNum.setText(String.valueOf(spaceLine));
        });
        binding.decreaseSpaceLine.setOnClickListener(v -> {
            if (spaceLine <= 10) {
                Toast.makeText(this, "行距无法继续变小!!", Toast.LENGTH_SHORT).show();
                return ;
            }
            spaceLine -= 3.75;
            binding.spaceLineNum.setText(String.valueOf(spaceLine));
        });

        binding.bgYellow.setOnClickListener(v -> onBgChange(R.color.theme_yellow, false));
        binding.bgWhite.setOnClickListener(v -> onBgChange(R.color.white, false));
        binding.bgGreen.setOnClickListener(v -> onBgChange(R.color.theme_green, false));
        binding.bgBlack.setOnClickListener(v -> onBgChange(R.color.black, false));

    }

    private void onBgChange(int resId, boolean isFirst) {
        if (bgColorId == resId && !isFirst) return ;
        bgColorId = resId;

        GradientDrawable yellowDrawable = (GradientDrawable) binding.bgYellow.getBackground();
        yellowDrawable.setStroke(5, bgColorId == R.color.theme_yellow ? getColor(R.color.theme_blue) : getColor(R.color.gray));
        binding.bgYellow.setBackground(yellowDrawable);

        GradientDrawable whiteDrawable = (GradientDrawable) binding.bgWhite.getBackground();
        whiteDrawable.setStroke(5, bgColorId == R.color.white ? getColor(R.color.theme_blue) : getColor(R.color.gray));
        binding.bgWhite.setBackground(whiteDrawable);

        GradientDrawable greenDrawable = (GradientDrawable) binding.bgGreen.getBackground();
        greenDrawable.setStroke(5, bgColorId == R.color.theme_green ? getColor(R.color.theme_blue) : getColor(R.color.gray));
        binding.bgGreen.setBackground(greenDrawable);

        GradientDrawable blackDrawable = (GradientDrawable) binding.bgBlack.getBackground();
        blackDrawable.setStroke(5, bgColorId == R.color.black ? getColor(R.color.theme_blue) : getColor(R.color.gray));
        binding.bgBlack.setBackground(blackDrawable);

    }

    private void initData() {
        textSize = dataStore.getValue(MyDataStore.DATA_STORE_NOVEL_READ_SETTING, MyDataStore.SETTING_NOVEL_TEXT_SIZE, 50f);
        spaceLine = dataStore.getValue(MyDataStore.DATA_STORE_NOVEL_READ_SETTING, MyDataStore.SETTING_NOVEL_SPACE_LINE, 25f);
        bgColorId = dataStore.getValue(MyDataStore.DATA_STORE_NOVEL_READ_SETTING, MyDataStore.SETTING_NOVEL_BG, R.color.white);
    }

    private void saveData() {
        dataStore.saveValue(MyDataStore.DATA_STORE_NOVEL_READ_SETTING, MyDataStore.SETTING_NOVEL_TEXT_SIZE, textSize);
        dataStore.saveValue(MyDataStore.DATA_STORE_NOVEL_READ_SETTING, MyDataStore.SETTING_NOVEL_SPACE_LINE, spaceLine);
        dataStore.saveValue(MyDataStore.DATA_STORE_NOVEL_READ_SETTING, MyDataStore.SETTING_NOVEL_BG, bgColorId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }


}