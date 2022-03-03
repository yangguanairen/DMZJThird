package com.sena.dmzjthird.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sena.dmzjthird.R;
import com.sena.dmzjthird.account.MyRetrofitService;
import com.sena.dmzjthird.account.bean.ResultBean;

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

    public static void setSubscribeStatus(Context context, ImageView imageView, String comicId) {
        long uid = MyDataStore.getInstance(context).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, 0L);

        MyRetrofitService service = RetrofitHelper.getMyServer(MyRetrofitService.MY_BASE_URL);
        service.querySubscribe(uid, comicId)
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
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        // 请求出错，不处理
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    public static void setSubscribeStatus(Context context, TextView imageView, String comicId) {
        long uid = MyDataStore.getInstance(context).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, 0L);

        MyRetrofitService service = RetrofitHelper.getMyServer(MyRetrofitService.MY_BASE_URL);
        service.querySubscribe(uid, comicId)
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

                        imageView.setCompoundDrawablesWithIntrinsicBounds(0, "true".equals(resultBean.getContent()) ? R.drawable.ic_subscribed : R.drawable.ic_subscribe, 0, 0);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        // 请求出错，不处理
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public static void controlSubscribe(Context context, ImageView imageView, String comicId, String cover, String title, String author) {
        MyRetrofitService service = RetrofitHelper.getMyServer(MyRetrofitService.MY_BASE_URL);

        long uid = MyDataStore.getInstance(context).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, 0L);
        service.controlSubscribe(uid, comicId, cover, title, author)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(resultBean -> {
                    if (resultBean == null) {
                        XPopUpUtil.showCustomErrorToast(context, "请求失败，请稍后重试");
                        return ;
                    }
                    if (resultBean.getCode() == 100) {
                        XPopUpUtil.showCustomErrorToast(context, context.getString(R.string.not_login));
                        return ;
                    }
                    if ("true".equals(resultBean.getContent())) {
                        imageView.setImageResource(R.drawable.ic_subscribed);
                        Toast.makeText(context, "订阅成功!!", Toast.LENGTH_SHORT).show();
                    } else {
                        imageView.setImageResource(R.drawable.ic_subscribe);
                        Toast.makeText(context, "取消订阅成功", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static void controlSubscribe(Context context, TextView imageView, String comicId, String cover, String title, String author) {
        MyRetrofitService service = RetrofitHelper.getMyServer(MyRetrofitService.MY_BASE_URL);

        long uid = MyDataStore.getInstance(context).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, 0L);
        service.controlSubscribe(uid, comicId, cover, title, author)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(resultBean -> {
                    if (resultBean == null) {
                        XPopUpUtil.showCustomErrorToast(context, "请求失败，请稍后重试");
                        return ;
                    }
                    if (resultBean.getCode() == 100) {
                        XPopUpUtil.showCustomErrorToast(context, context.getString(R.string.not_login));
                        return ;
                    }
                    if ("true".equals(resultBean.getContent())) {
                        imageView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_subscribed, 0, 0);
                        Toast.makeText(context, "订阅成功!!", Toast.LENGTH_SHORT).show();
                    } else {
                        imageView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_subscribe, 0, 0);
                        Toast.makeText(context, "取消订阅成功", Toast.LENGTH_SHORT).show();
                    }
                });
    }



}
