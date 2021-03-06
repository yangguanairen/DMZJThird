package com.sena.dmzjthird.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.application.ComicDetailRes;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.account.MyRetrofitService;
import com.sena.dmzjthird.account.bean.ResultBean;
import com.sena.dmzjthird.comic.adapter.ComicViewCatalogAdapter;
import com.sena.dmzjthird.databinding.ActivityComicViewBinding;
import com.sena.dmzjthird.utils.api.ComicApi;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * FileName: MyListener
 * Author: JiaoCan
 * Date: 2022/3/3 17:11
 */

public class ViewHelper {

    public static void setSubscribeStatus(Context context, String objectId, int type, ImageView imageView, TextView textView) {
        long uid = MyDataStore.getInstance(context).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, 0L);

        MyRetrofitService service = RetrofitHelper.getMyServer(MyRetrofitService.MY_BASE_URL);
        service.querySubscribe(uid, objectId, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResultBean resultBean) {
                        if (resultBean.getCode() == 100) {
                            return ;
                        }
                        imageView.setImageResource(
                                "true".equals(resultBean.getContent()) ? R.drawable.ic_subscribed : R.drawable.ic_subscribe
                        );
                        if (textView != null) {
                            textView.setText("true".equals(resultBean.getContent()) ? "?????????" : "??????");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        // ????????????????????????
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public static void controlSubscribe(Context context, String objectId, String cover, String title, String author, int type,
                                        ImageView imageView, TextView textView) {
        MyRetrofitService service = RetrofitHelper.getMyServer(MyRetrofitService.MY_BASE_URL);

        long uid = MyDataStore.getInstance(context).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, 0L);
        service.controlSubscribe(uid, objectId, cover, title, author, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(resultBean -> {
                    if (resultBean == null) {
                        XPopUpUtil.showCustomErrorToast(context, "??????????????????????????????");
                        return ;
                    }
                    if (resultBean.getCode() == 100) {
                        XPopUpUtil.showCustomErrorToast(context, context.getString(R.string.not_login));
                        return ;
                    }
                    if ("true".equals(resultBean.getContent())) {
                        imageView.setImageResource(R.drawable.ic_subscribed);
                        if (textView != null) textView.setText("?????????");
                        Toast.makeText(context, "????????????!!", Toast.LENGTH_SHORT).show();
                    } else {
                        imageView.setImageResource(R.drawable.ic_subscribe);
                        if (textView != null) textView.setText("??????");
                        Toast.makeText(context, "??????????????????", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public static void addHistory(Context context, String object, String cover, String title, int type,
                                  int volumeId, String volumeName, int chapterId, String chapterName) {

        long uid = MyDataStore.getInstance(context).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, 0L);
        if (uid == 0L) return ;
        MyRetrofitService service = RetrofitHelper.getMyServer(MyRetrofitService.MY_BASE_URL);
        service.addHistory(uid, object, cover, title, type, volumeId, volumeName, chapterId, chapterName)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ResultBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ResultBean resultBean) {
                        LogUtil.e("????????????????????????!!");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogUtil.e("????????????????????????!!");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * ?????????????????????????????????????????????
     * @param activity
     * @param toolbar
     */
    public static void immersiveStatus(Activity activity, View toolbar) {
        ImmersionBar.with(activity)
                .statusBarColor(R.color.theme_blue)
                .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                .titleBarMarginTop(toolbar)
                .init();
    }

}
