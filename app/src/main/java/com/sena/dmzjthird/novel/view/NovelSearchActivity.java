package com.sena.dmzjthird.novel.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.account.MyRetrofitService;
import com.sena.dmzjthird.comic.fragment.SearchHotFragment;
import com.sena.dmzjthird.databinding.ActivityNovelSearchBinding;
import com.sena.dmzjthird.novel.fragment.NovelSearchResultFragment;

public class NovelSearchActivity extends AppCompatActivity {

    private ActivityNovelSearchBinding binding;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNovelSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ImmersionBar.with(this)
                .statusBarColor(R.color.theme_blue)
                .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                .titleBarMarginTop(binding.toolbar)
                .init();

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentLayout, SearchHotFragment.newInstance(MyRetrofitService.TYPE_NOVEL)).commit();

        init();
    }


    private void init() {
        binding.toolbar.setBackIVListener(v -> finish());
        binding.toolbar.setSearchIVListener(v -> {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

            String query = binding.toolbar.getQueryETText();
            if (TextUtils.isEmpty(query)) {
                Toast.makeText(this, "输入不得为空!!", Toast.LENGTH_SHORT).show();
                return ;
            }
            fragmentManager.beginTransaction().replace(R.id.fragmentLayout, NovelSearchResultFragment.newInstance(query)).commit();
        });
        binding.toolbar.setQueryETEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.toolbar.setSearchClick();
            }
            return false;
        });
    }

}