package com.sena.dmzjthird.custom.clipView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.sena.dmzjthird.databinding.ActivityClipBinding;
import com.sena.dmzjthird.utils.UriUtil;

import java.io.File;
import java.io.OutputStream;

public class ClipActivity extends AppCompatActivity {

    private ActivityClipBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClipBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initClick();

        Uri originalUri = getIntent().getParcelableExtra("clipUriData");
        if (originalUri == null) finish();
        File file = UriUtil.getFileByUri(this, originalUri);
        binding.imageView.setImageFile(file.getAbsolutePath(), 0, binding.frameView);

    }

    private void initClick() {

        binding.sure.setOnClickListener(v -> {
            Intent intent = new Intent();

            Bitmap bitmap = binding.imageView.getBitmap(binding.frameView);

            Uri uri = UriUtil.createFileInDownload(this, "customClipImage.jpg", "image/jpeg");
            try {
                OutputStream os = getContentResolver().openOutputStream(uri);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            } catch (Exception e) {
                e.printStackTrace();
            }
            intent.putExtra("clipUriData", uri);

            setResult(RESULT_OK, intent);
            finish();
        });


        binding.cancel.setOnClickListener(v -> finish());

    }
}