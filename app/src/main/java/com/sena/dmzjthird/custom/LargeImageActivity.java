package com.sena.dmzjthird.custom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.sena.dmzjthird.databinding.ActivityLargeImageBinding;
import com.sena.dmzjthird.utils.GlideUtil;
import com.sena.dmzjthird.utils.IntentUtil;

public class LargeImageActivity extends AppCompatActivity {

    private ActivityLargeImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLargeImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String url = IntentUtil.getCoverUrl(this);

        binding.photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        GlideUtil.loadImageWithCookie(this, url, binding.photoView);

        binding.photoView.setOnClickListener(v -> {
            finish();
        });


    }
}