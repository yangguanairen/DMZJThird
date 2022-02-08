package com.sena.dmzjthird.utils;

import android.content.Context;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.sena.dmzjthird.R;
import com.sena.dmzjthird.custom.CustomLoading;
import com.sena.dmzjthird.custom.CustomToast;

/**
 * FileName: XPopUpUtil
 * Author: JiaoCan
 * Date: 2022/1/29 14:31
 */

public class XPopUpUtil {


    public static void showCustomToast(Context context, int imageId, String title, String content) {
        new XPopup.Builder(context)
                .isDestroyOnDismiss(true)
                .asCustom(new CustomToast(context, imageId, title, content))
                .show();
    }

    public static void showCustomToast(Context context, int imageId, String content) {
        showCustomToast(context, imageId, "", content);
    }

    public static BasePopupView showLoadingView(Context context) {
        if (context == null) return null;
        return new XPopup.Builder(context)
                .dismissOnBackPressed(false)
                .isDestroyOnDismiss(true)
                .asCustom(new CustomLoading(context))
                .show();
    }

}
