package com.sena.dmzjthird.custom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.databinding.ActivityWebViewBinding;
import com.sena.dmzjthird.utils.IntentUtil;
import com.sena.dmzjthird.utils.LogUtil;

public class WebViewActivity extends AppCompatActivity {

    private ActivityWebViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ImmersionBar.with(this)
                .statusBarColor(R.color.theme_blue)
                .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                .titleBarMarginTop(binding.consLayout)
                .init();

        String pageUrl = IntentUtil.getNewsPageUrl(this);
        if (pageUrl == null) finish();
        String newsId = IntentUtil.getNewsId(this);
        String newsTitle = IntentUtil.getNewsTitle(this);
        String newsCover = IntentUtil.getNewsCover(this);

        binding.toolbar.setBackListener(v -> finish());
        binding.toolbar.setTitle(newsTitle);

        initWebView();

        loadUrl(pageUrl);
    }

    private void initWebView() {

        WebSettings settings = binding.webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);


        binding.webView.addJavascriptInterface(new JsCallJavaObj() {
            @JavascriptInterface
            @Override
            public void showBigImg(String url) {
                LogUtil.e("WebView图片路径: " + url);
                IntentUtil.goToLargeImageActivity(WebViewActivity.this, url);
            }
        }, "jsCallJavaObj");

        WebViewClient webViewClient = new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                setWebImageClick();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.e(url);
                String processedUrl = url.replace("dmzjimage://?src=", "");
                if (processedUrl.contains("images.dmzj.com")) {
                    IntentUtil.goToLargeImageActivity(WebViewActivity.this, processedUrl);
                } else {
                    view.loadUrl(processedUrl);
                }
                return true;
            }
        };
        binding.webView.setWebViewClient(webViewClient);
    }

    private void loadUrl(String url) {

        binding.webView.loadUrl(url);

    }

    private void setWebImageClick() {
        String jsCode="javascript:(function(){" +
                "var imgs=document.getElementsByTagName(\"img\");" +
                "for(var i=0;i<imgs.length;i++){" +
                "imgs[i].onclick=function(){" +
                "window.jsCallJavaObj.showBigImg(this.src);" +
                "}}})()";
        binding.webView.loadUrl(jsCode);
    }

    private interface JsCallJavaObj {
        void showBigImg(String url);
    }


}