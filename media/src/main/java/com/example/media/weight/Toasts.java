package com.example.media.weight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.Toast;

public class Toasts {


    private Toasts() {

    }

    private static Toasts mToasts;

    public static Toasts with() {
        synchronized (Toasts.class) {
            if (mToasts == null) {
                synchronized (Toasts.class) {
                    mToasts = new Toasts();
                }
            }
        }
        return mToasts;
    }

    private Toast mToast;

    @SuppressLint("ShowToast")
    public void showToast(@NonNull Context context, @NonNull String text) {
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    @SuppressLint("ShowToast")
    public void showToast(@NonNull Context context, @StringRes int text, Object... object) {
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), context.getString(text, object), Toast.LENGTH_SHORT);
        } else {
            mToast.setText(context.getString(text, object));
        }
        mToast.show();
    }
}
