package com.example.media.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

public class ScreenUtils {


    public static int screenWidth(@NonNull Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int screenHeight(@NonNull Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5F);
    }

    public static int px2dp(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5F);
    }

    public static void setDefaultRootViewSize(@NonNull Context context, @NonNull ViewGroup rootView) {
        ViewGroup.LayoutParams rootParams = rootView.getLayoutParams();
        rootParams.width = -1;
        rootParams.height = dp2px(context, 45.0F);
        rootView.setLayoutParams(rootParams);
    }

    public static int getStatuWindowsHeight(@NonNull Context context) {
        return context.getResources().getDimensionPixelSize(context.getResources().getIdentifier("status_bar_height", "dimen", "android"));
    }
}
