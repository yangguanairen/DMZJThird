package com.sena.dmzjthird.utils;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.sena.dmzjthird.R;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/8/3
 * Time: 22:27
 */
public class GlideUtil {

    public static GlideUrl addCookie(String url) {
        GlideUrl cookieUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Host", "images.dmzj.com")
                .addHeader("Referer", "https://m.dmzj.com/")
                .addHeader("Sec-Fetch-Dest", "image")
                .build());
        return cookieUrl;
    }

    public static void loadImage(Context context, String url, ImageView imageView) {

        Glide.with(context).load(url)
                .transition(withCrossFade())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                .skipMemoryCache(false)
                .placeholder(R.drawable.selector_default_picture)
                .error(R.drawable.selector_default_picture)
                .into(imageView);
    }

    public static void loadImageWithCookie(Context context, String url, ImageView imageView) {

        Glide.with(context)
                .load(addCookie(url))
                .transition(withCrossFade(500))
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(10)))
                .skipMemoryCache(false)
                .placeholder(R.drawable.selector_default_picture)
                .error(R.drawable.selector_default_picture)
                .into(imageView);
    }
}
