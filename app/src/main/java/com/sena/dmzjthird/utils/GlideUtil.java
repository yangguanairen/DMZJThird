package com.sena.dmzjthird.utils;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

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
}
