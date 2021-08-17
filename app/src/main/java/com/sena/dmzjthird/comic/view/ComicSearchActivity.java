package com.sena.dmzjthird.comic.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import com.sena.dmzjthird.R;
import com.sena.dmzjthird.comic.fragment.ComicSearchResultFragment;
import com.sena.dmzjthird.comic.fragment.SearchHotFragment;
import com.sena.dmzjthird.databinding.ActivityComicSearchBinding;

public class ComicSearchActivity extends AppCompatActivity {

    private ActivityComicSearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComicSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Fragment fragment = SearchHotFragment.newInstance(0);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, fragment).commit();

        binding.toolbar.setBackIVListener(v -> finish());
        binding.toolbar.setSearchIVListener(v -> {
            String query = binding.toolbar.getQueryETText();
            if (TextUtils.isEmpty(query)) {
                return;
            }
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentLayout, ComicSearchResultFragment.newInstance(query)).commit();
        });
        binding.toolbar.setQueryETEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.toolbar.setSearchClick();
            }

            return false;
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}