package com.sena.dmzjthird.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sena.dmzjthird.R;
import com.sena.dmzjthird.RetrofitService;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Created by Android Studio.
 * User: Sena
 * Date: 2021/9/2
 * Time: 16:51
 */
public class OnClickListenerHelper {


    public static void subscribeComic(Context context, String comicId, TextView view) {

        String uid = MyDataStore.getInstance(context).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, "");
        if ("".equals(uid)) {
            Toast.makeText(context, context.getString(R.string.not_login), Toast.LENGTH_SHORT).show();
            return;
        }
        RetrofitService service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);
        if (view.getContentDescription().equals("0")) {
            // 订阅
            service.subscribeComic(comicId, uid, "mh")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(bean -> {
                        if (bean.getCode() != 0) {
                            Toast.makeText(context, context.getString(R.string.subscribe_fail), Toast.LENGTH_SHORT).show();
                        } else {
                            view.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_subscribed, 0, 0);
                            view.setContentDescription("1");
                            Toast.makeText(context, context.getString(R.string.subscribe_success), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // 取消订阅
            service.cancelSubscribeComic(comicId, uid, "mh")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(bean -> {
                        if (bean.getCode() != 0) {
                            Toast.makeText(context, context.getString(R.string.cancel_subscribe_fail), Toast.LENGTH_SHORT).show();
                        } else {
                            view.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_subscribe, 0, 0);
                            view.setContentDescription("0");
                            Toast.makeText(context, context.getString(R.string.cancel_subscribe_success), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public static void subscribeComic(Context context, String comicId, ImageView view) {

        String uid = MyDataStore.getInstance(context).getValue(MyDataStore.DATA_STORE_USER, MyDataStore.USER_UID, "");
        if ("".equals(uid)) {
            Toast.makeText(context, context.getString(R.string.not_login), Toast.LENGTH_SHORT).show();
            return;
        }
        RetrofitService service = RetrofitHelper.getServer(RetrofitService.BASE_V3_URL);
        if (view.getContentDescription().equals("0")) {
            // 订阅
            service.subscribeComic(comicId, uid, "mh")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(bean -> {
                        if (bean.getCode() != 0) {
                            Toast.makeText(context, context.getString(R.string.subscribe_fail), Toast.LENGTH_SHORT).show();
                        } else {
                            view.setImageResource(R.drawable.ic_subscribed);
                            view.setContentDescription("1");
                            Toast.makeText(context, context.getString(R.string.subscribe_success), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // 取消订阅
            service.cancelSubscribeComic(comicId, uid, "mh")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(bean -> {
                        if (bean.getCode() != 0) {
                            Toast.makeText(context, context.getString(R.string.cancel_subscribe_fail), Toast.LENGTH_SHORT).show();
                        } else {
                            view.setImageResource(R.drawable.ic_subscribe);
                            view.setContentDescription("0");
                            Toast.makeText(context, context.getString(R.string.cancel_subscribe_success), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }

}
