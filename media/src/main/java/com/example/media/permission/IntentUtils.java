package com.example.media.permission;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * 项  目 :  YZBProject
 * 包  名 :  com.henji.yunyi.yizhibang.utils
 * 类  名 :  IntentUtils
 * 作  者 :  胡庆岭
 * 时  间 :  2018/1/9 0009 下午 4:03
 * 描  述 :  ${TODO}意图工具类
 *
 * @author ：
 */

public class IntentUtils {
    public static final int OPEN_APPLY_CENTER_CODE = 5501;

    /**
     * Activity打开应用中心
     *
     * @param activity 页面
     */
    public static void openActivityApplyCenter(@NonNull Activity activity) {
        Uri packageURI = Uri.parse("package:" + activity.getPackageName());
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
        activity.startActivityForResult(intent, OPEN_APPLY_CENTER_CODE);
    }

    /**
     * fragemnt打开应用中心
     *
     * @param fragment fragment
     * @param activity activity
     */
    public static void openFragmentApplyCenter(@NonNull Fragment fragment, @NonNull Activity activity) {
        Uri packageURI = Uri.parse("package:" + activity.getPackageName());
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
        fragment.startActivityForResult(intent, OPEN_APPLY_CENTER_CODE);
    }
}
